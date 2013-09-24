package assignment1;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Multimap;

import static org.junit.Assert.assertEquals;

public class WrittenAssignment1
{
	NonPersonalizedRecommender recommender;
	Double[] type;

	@Before
	public void setUp() throws Exception
	{
		recommender = new BasicNonPersonalizedRecommender("recsys-data-written-assignment-1.csv");
	}

	@Test
	public void test()
	{
		recommender.score(260);
		Multimap<Integer, ScoreHolder> result = recommender.getSimpleScores();

		assertEquals(20, recommender.getUserCount());
		assertEquals(20, recommender.getMovieCount());

		System.out.println(recommender.format(result, 19, 260));

		ScoreHolder raidersOfTheLostArkScore = null;
		for (ScoreHolder s: result.values())
		{
			if (s.movieId == 1198)
			{
				raidersOfTheLostArkScore = s;
			}
		}
		assertEquals(50.00, raidersOfTheLostArkScore.score * 100, 0.00);
		System.out.println(result.values());
	}
}