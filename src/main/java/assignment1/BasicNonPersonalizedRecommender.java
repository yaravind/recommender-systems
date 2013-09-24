package assignment1;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import static com.google.common.collect.Lists.newArrayList;

import static com.google.common.collect.Sets.intersection;
import static com.google.common.collect.Sets.newHashSet;

public class BasicNonPersonalizedRecommender extends NonPersonalizedRecommender
{
	public BasicNonPersonalizedRecommender(String f)
	{
		super(f);
	}

	@Override
	public void score(Integer... movieIds)
	{
		System.out.println("----------------------------------------------------------------------------------------------------");
		load(fileName);
		System.err.println("Loaded data set");

		for (Integer movieX: movieIds)
		{
			System.out.println("Simple recommender for movie X: " + movieX);
			System.err.println(String.format("Totoal ratings for movie %d is %d", movieX, ratingsByMovie.get(movieX).size()));

			// find all users who rated the current movie X
			Set<Integer> usersWhoRatedX = getUsersWhoRatedMovieX(movieX);

			List<ScoreHolder> simple = newArrayList();// Collections.reverseOrder()
			List<ScoreHolder> advanced = newArrayList();

			// find common users from X who also rated other movies
			for (Integer movieY: ratingsByMovie.keySet())
			{
				/*
				 * do not calculate for X - (remember, your movie is x, and you are looking for the other movies y that
				 * maximize the formula values)
				 */
				if (!movieY.equals(movieX))
				{
					Set<Integer> usersWhoRatedY = getUsersWhoRatedMovieX(movieY);

					Set<Integer> usersWhoRatedBothXAndY = intersection(usersWhoRatedX, usersWhoRatedY);

					int XandY = usersWhoRatedBothXAndY.size();
					int X = usersWhoRatedX.size();
					// (x and y) / x
					double simpleFormula = (double) XandY / X;
					simple.add(new ScoreHolder(movieY, simpleFormula));
					System.err.println(String.format("Users who rated movie %d is %d", movieX, X));
					System.err.println(String.format("Users who rated both %d (X) and %d (Y) is %d", movieX, movieY, XandY));

					// find all users who DIDN'T rate the current movie X
					Set<Integer> usersWhoDidNotRateX = Sets.difference(getAllUsers(), usersWhoRatedX);

					int notX = this.getUserCount() - X;
					System.err.println(String.format("Users who DID NOT rate %d (X) is %d", movieX, notX));
					assert notX == usersWhoDidNotRateX.size();

					Set<Integer> usersWhoRatedYButNotX = intersection(usersWhoDidNotRateX, usersWhoRatedY);
					int notXButY = usersWhoRatedYButNotX.size();// !X and Y
					System.err.println(String.format("Users who did not rate (X) %d but rated (Y) %d  is %d", movieX, movieY,
							notXButY));

					// ((X and Y) / X) / ((!X and Y) / !X)
					double advancedFormula = simpleFormula / ((double) notXButY / notX);
					advanced.add(new ScoreHolder(movieY, advancedFormula));
				}
			}

			Collections.sort(simple, ScoreHolder.SCORE_COMPARATOR);
			simpleScores.putAll(movieX, simple);

			Collections.sort(advanced, ScoreHolder.SCORE_COMPARATOR);
			advancedScores.putAll(movieX, advanced);
		}
		System.out.println("----------------------------------------------------------------------------------------------------");
	}

	private Set<Integer> getUsersWhoRatedMovieX(Integer movieX)
	{
		Set<Integer> usersWhoRatedX = newHashSet();

		for (UserMovieRating umr: ratingsByMovie.get(movieX))
		{
			usersWhoRatedX.add(umr.userId);
		}
		return usersWhoRatedX;
	}
}