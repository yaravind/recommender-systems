package assignment1;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Multimap;

import static org.junit.Assert.assertEquals;

public class BasicNonPersonalizedRecommenderTest
{
	NonPersonalizedRecommender recommender;
	Double[] type;

	@Before
	public void setUp() throws Exception
	{
		recommender = new BasicNonPersonalizedRecommender("recsys-data-ratings.csv");
	}

	@Test
	public void simple()
	{
		recommender.score(11, 121, 8587);
		Multimap<Integer, ScoreHolder> result = recommender.getSimpleScores();

		assertEquals(5564, recommender.getUserCount());
		assertEquals(100, recommender.getMovieCount());

		String expected11 = "11,603,0.96,1892,0.94,1891,0.94,120,0.93,1894,0.93";
		String expected121 = "121,120,0.95,122,0.95,603,0.94,597,0.89,604,0.88";
		String expected8587 = "8587,603,0.92,597,0.90,607,0.87,120,0.86,13,0.86";

		String actual11 = recommender.format(result, 5, 11);
		assertEquals(expected11, actual11);

		String actual121 = recommender.format(result, 5, 121);
		assertEquals(expected121, actual121);

		String actual8587 = recommender.format(result, 5, 8587);
		assertEquals(expected8587, actual8587);

		// Only run if the tests pass
		recommender.score(8467, 601, 1891);
		result = recommender.getSimpleScores();
		String recommendations = recommender.format(result, 5, Integer.valueOf(8467));
		System.out.println(recommendations);
		recommendations = recommender.format(result, 5, Integer.valueOf(601));
		System.out.println(recommendations);
		recommendations = recommender.format(result, 5, Integer.valueOf(1891));
		System.out.println(recommendations);
	}

	@Test
	public void advanced()
	{
		recommender.score(11, 121, 8587);
		Multimap<Integer, ScoreHolder> result = recommender.getAdvancedScores();

		assertEquals(5564, recommender.getUserCount());
		assertEquals(100, recommender.getMovieCount());

		String expected11 = "11,1891,5.69,1892,5.65,243,5.00,1894,4.72,2164,4.11";
		String expected121 = "121,122,4.74,120,3.82,2164,3.40,243,3.26,1894,3.22";
		String expected8587 = "8587,10020,4.18,812,4.03,7443,2.63,9331,2.46,786,2.39";

		String actual11 = recommender.format(result, 5, 11);
		assertEquals(expected11, actual11);

		String actual121 = recommender.format(result, 5, 121);
		assertEquals(expected121, actual121);

		String actual8587 = recommender.format(result, 5, 8587);
		assertEquals(expected8587, actual8587);

		// Only run if the tests pass
		recommender.score(8467, 601, 1891);
		result = recommender.getAdvancedScores();
		String recommendations = recommender.format(result, 5, Integer.valueOf(8467));
		System.out.println(recommendations);
		recommendations = recommender.format(result, 5, Integer.valueOf(601));
		System.out.println(recommendations);
		recommendations = recommender.format(result, 5, Integer.valueOf(1891));
		System.out.println(recommendations);
	}
}
