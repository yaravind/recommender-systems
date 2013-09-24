package assignment1;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Resources;

import static com.google.common.collect.ArrayListMultimap.create;

public abstract class NonPersonalizedRecommender
{
	protected List<UserMovieRating> data = Lists.newArrayList();

	public final static Splitter SPLITTER = Splitter.on(",");

	public final static Joiner JOINER = Joiner.on(",");

	protected Multimap<Integer, UserMovieRating> ratingsByUser;

	protected Multimap<Integer, UserMovieRating> ratingsByMovie;

	protected Multimap<Integer, ScoreHolder> simpleScores = create();

	protected Multimap<Integer, ScoreHolder> advancedScores = create();

	public abstract void score(Integer... movieIds);

	protected final String fileName;

	public NonPersonalizedRecommender(String f)
	{
		fileName = f;
	}

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

	public int getUserCount()
	{
		return ratingsByUser.keySet().size();
	}

	public int getMovieCount()
	{
		return ratingsByMovie.keySet().size();
	}

	public Set<Integer> getAllUsers()
	{
		return ratingsByUser.keySet();
	}

	/**
	 * (X and Y)/X
	 * <p>
	 * Eg. We want to find the correlation between Star Wars and others, and let we start with The Matrix. If - Star
	 * Wars was rated by 16 people out of 20 and out of those 16 people, 8 also rated for Matrix. Then the correlation
	 * is 8/16 = 50%. Now take Inception and try to find the correlation of the Star Wars and Inception. Now from the
	 * above, we know that 16 people rated Star Wars, assume that 12 out of these 16 rated Inception. (We don't care
	 * about what ratings they gave to the movie.) Then the correlation is 12/16 = 75%
	 * </p>
	 */
	public Multimap<Integer, ScoreHolder> getSimpleScores()
	{
		return simpleScores;
	}

	/**
	 * ((X and Y) / X) / ((!X and Y) / !X)
	 * <p>
	 * Eg. Star Wars and Matrix If - Star Wars was rated by 16 people out of 20 and out of those 16 people, 8 also rated
	 * for Matrix. Now, there are 4 people who did not rate Star wars (20 - 16) , and out of these 4 people, 1 person
	 * did rate Matrix, then ((8)/16) / ((1)/4) 0.5 / 0.25 = 2 is the correlation. (tip: Advanced corr can be more than
	 * 1) Numerator - same as the simple correlation. Denominator - (How many rated for matrix but did not vote SW/
	 * number of people who did not rate SW)
	 * </p>
	 */
	public Multimap<Integer, ScoreHolder> getAdvancedScores()
	{
		return advancedScores;
	}
}