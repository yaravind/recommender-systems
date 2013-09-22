package assignment1;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Resources;

public abstract class NonPersonalizedRecommender
{
	public abstract Multimap<Integer, ScoreHolder> recommend(Integer... movieIds);

	protected List<UserMovieRating> data = Lists.newArrayList();

	public final static Splitter SPLITTER = Splitter.on(",");

	public final static Joiner JOINER = Joiner.on(",");

	protected Multimap<Integer, UserMovieRating> ratingsByUser;

	protected Multimap<Integer, UserMovieRating> ratingsByMovie;

	protected void load(String ratingsFileName)
	{
		List<String> lines;
		try
		{
			lines = Resources.readLines(getClass().getResource(ratingsFileName), Charsets.UTF_8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		for (String line: lines)
		{
			String[] split = Iterables.toArray(SPLITTER.split(line), String.class);

			// user number, movie ID, and rating
			int userId = Integer.parseInt(split[0]);
			int movieId = Integer.parseInt(split[1]);
			double rating = Double.parseDouble(split[2]);

			UserMovieRating umr = new UserMovieRating(userId, movieId, rating);
			data.add(umr);
		}

		ratingsByUser = ArrayListMultimap.create();
		ratingsByMovie = ArrayListMultimap.create();

		for (UserMovieRating i: data)
		{
			ratingsByUser.put(i.userId, i);
			ratingsByMovie.put(i.movieId, i);
		}

		assert ratingsByUser.keySet().size() == 5564: "Total users should be 5564";
		assert ratingsByMovie.keySet().size() == 100: "Total movies should be 100";
	}

	public int getUserCount()
	{
		return ratingsByUser.keySet().size();
	}

	public int getMovieCount()
	{
		return ratingsByMovie.keySet().size();
	}

	protected void print(int movieId)
	{
		for (UserMovieRating entry: ratingsByMovie.get(movieId))
		{
			System.out.println(entry);
		}
	}

	public String format(Multimap<Integer, ScoreHolder> ratings, int limit, Integer X)
	{
		DecimalFormat df = new DecimalFormat("0.00");

		List<String> result = Lists.newArrayList();
		result.add(X.toString());

		int i = 1;
		for (ScoreHolder score: ratings.get(X))
		{
			String rounded = df.format((double) Math.round(score.score * 100) / 100);

			result.add(String.valueOf(score.movieId));
			result.add(rounded);

			if (i == limit)
			{
				break;
			}
			i++;
		}

		return JOINER.join(result);
	}
}