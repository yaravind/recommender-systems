package assignment2;

import java.util.List;
import java.util.Map;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.scored.ScoredId;
import org.grouplens.mooc.cbf.CBFMain;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import static util.AssertUtil.assertOrder;
import static util.AssertUtil.equalMaps;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Part 2 of the assignment 2.
 * 
 * @author Aravind R. Yarram
 */
public class CBRWeightedProfilesTest
{
	Logger logger = LoggerFactory.getLogger(getClass());

	LenskitConfiguration config;
	Recommender rec;
	ItemRecommender irec;

	@Before
	public void setUp() throws Exception
	{
		// configured the weighted recommender
		config = CBFMain.configureWeightedRecommender();
		rec = LenskitRecommender.build(config);
		irec = rec.getItemRecommender();
	}

	@Test
	public void weighted4045()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(807l, 0.1932);
		expected.put(63l, 0.1438);
		expected.put(187l, 0.0947);
		expected.put(11l, 0.0900);
		expected.put(641l, 0.0471);

		Map<Long, Double> actual = Maps.newHashMap();

		long user = 4045;

		logger.info("Searching for recommendations for user ", user);
		List<ScoredId> recs = irec.recommend(user, 5);

		if (recs.isEmpty())
		{
			logger.warn("no recommendations for user , do they exist?", user);
		}
		logger.info("Recommendations for user {} is {}", user, recs);

		// Verify the order
		List<Long> order = newArrayList(807l, 63l, 187l, 11l, 641l);
		assertOrder(recs, order);

		// Verify the scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void weighted144()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(11l, 0.1394);
		expected.put(585l, 0.1229);
		expected.put(671l, 0.1130);
		expected.put(672l, 0.0878);
		expected.put(141l, 0.0436);

		Map<Long, Double> actual = Maps.newHashMap();

		long user = 144;

		logger.info("Searching for recommendations for user ", user);
		List<ScoredId> recs = irec.recommend(user, 5);

		if (recs.isEmpty())
		{
			logger.warn("no recommendations for user , do they exist?", user);
		}
		logger.info("Recommendations for user {} is {}", user, recs);

		// Verify the order
		List<Long> order = newArrayList(11l, 585l, 671l, 672l, 141l);
		assertOrder(recs, order);

		// Verify the scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void weighted3855()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(1892l, 0.2243);
		expected.put(1894l, 0.1465);
		expected.put(604l, 0.1258);
		expected.put(462l, 0.1050);
		expected.put(10020l, 0.0898);

		Map<Long, Double> actual = Maps.newHashMap();

		long user = 3855;

		logger.info("Searching for recommendations for user ", user);
		List<ScoredId> recs = irec.recommend(user, 5);

		if (recs.isEmpty())
		{
			logger.warn("no recommendations for user , do they exist?", user);
		}
		logger.info("Recommendations for user {} is {}", user, recs);

		// Verify the order
		List<Long> order = newArrayList(1892l, 1894l, 604l, 462l, 10020l);
		assertOrder(recs, order);

		// Verify the scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void weighted1637()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(393l, 0.1976);
		expected.put(24l, 0.1900);
		expected.put(2164l, 0.1522);
		expected.put(601l, 0.1334);
		expected.put(5503l, 0.0992);

		Map<Long, Double> actual = Maps.newHashMap();

		long user = 1637;

		logger.info("Searching for recommendations for user ", user);
		List<ScoredId> recs = irec.recommend(user, 5);

		if (recs.isEmpty())
		{
			logger.warn("no recommendations for user , do they exist?", user);
		}
		logger.info("Recommendations for user {} is {}", user, recs);

		// Verify the order
		List<Long> order = newArrayList(393l, 24l, 2164l, 601l, 5503l);
		assertOrder(recs, order);

		// Verify the scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void weighted2919()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(180l, 0.1454);
		expected.put(11l, 0.1238);
		expected.put(1891l, 0.1172);
		expected.put(424l, 0.1074);
		expected.put(2501l, 0.0973);

		Map<Long, Double> actual = Maps.newHashMap();

		long user = 2919;

		logger.info("Searching for recommendations for user ", user);
		List<ScoredId> recs = irec.recommend(user, 5);

		if (recs.isEmpty())
		{
			logger.warn("no recommendations for user , do they exist?", user);
		}
		logger.info("Recommendations for user {} is {}", user, recs);

		// Verify the order
		List<Long> order = newArrayList(180l, 11l, 1891l, 424l, 2501l);
		assertOrder(recs, order);

		// Verify the scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void assignment()
	{
		long[] users = { 35, 1285, 5542, 4292, 1275};

		for (long user: users)
		{
			List<ScoredId> recs = irec.recommend(user, 5);

			if (recs.isEmpty())
			{
				System.err.println("No recommendations for user " + user + ", do they exist?");
			}
			System.out.format("recommendations for user %d:\n", user);

			for (ScoredId id: recs)
			{
				System.out.format("  %d: %.4f\n", id.getId(), id.getScore());
			}
		}
	}
}