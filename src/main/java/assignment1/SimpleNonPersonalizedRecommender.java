package assignment1;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newTreeSet;

import static com.google.common.collect.ArrayListMultimap.create;

public class SimpleNonPersonalizedRecommender extends NonPersonalizedRecommender
{
	@Override
	public Multimap<Integer, ScoreHolder> recommend(Integer... movieIds)
	{
		Multimap<Integer, ScoreHolder> result = create();
		load("recsys-data-ratings.csv");

		for (Integer id: movieIds)
		{
			System.err.println(String.format("Totoal ratings for movie %d is %d", id, ratingsByMovie.get(id).size()));

			// find all users who rated the current movie X
			Set<Integer> usersWhoRatedX = newTreeSet();
			for (UserMovieRating umr: ratingsByMovie.get(id))
			{
				usersWhoRatedX.add(umr.userId);
			}
			int X = usersWhoRatedX.size();// Total users who rated movie X
			System.err.println(String.format("Unique users who rated movie %d is %d", id, X));

			Set<ScoreHolder> scores = newTreeSet(Collections.reverseOrder());

			// find common users from X who also rated other movies
			for (Integer otherMovieId: ratingsByMovie.keySet())
			{
				/*
				 * do not calculate for X - (remember, your movie is x, and you are looking for the other movies y that
				 * maximize the formula values)
				 */
				if (!otherMovieId.equals(id))
				{
					Set<Integer> usersWhoRatedY = newTreeSet();
					for (UserMovieRating umr: ratingsByMovie.get(otherMovieId))
					{
						usersWhoRatedY.add(umr.userId);
					}
					Set<Integer> usersWhoAlsoRatedY = intersection(usersWhoRatedX, usersWhoRatedY);
					int Y = usersWhoAlsoRatedY.size();
					System.err.println(String.format("Users who rated both %d (X) and %d (Y) is %d", id, otherMovieId, Y));

					// // (x and y) / x
					scores.add(new ScoreHolder(otherMovieId, ((double) Y / X)));
				}
			}
			result.putAll(id, Lists.newArrayList(scores));
		}
		return result;
	}
}