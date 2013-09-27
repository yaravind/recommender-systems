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

import static org.junit.Assert.assertEquals;

import static util.AssertUtil.assertOrder;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Part 1 of the assignment. Un-weighted scorer.
 * 
 * @author Aravind R. Yarram
 */
public class CBRUnweightedProfilesTest
{
	Logger logger = LoggerFactory.getLogger(getClass());

	LenskitConfiguration config;
	Recommender rec;
	ItemRecommender irec;

	@Before
	public void setUp() throws Exception
	{
		config = CBFMain.configureRecommender();
		rec = LenskitRecommender.build(config);
		irec = rec.getItemRecommender();
	}

	@Test
	public void unweighted4045()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(11l, 0.3596);
		expected.put(63l, 0.2612);
		expected.put(807l, 0.2363);
		expected.put(187l, 0.2059);
		expected.put(2164l, 0.1899);

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
		List<Long> order = newArrayList(11l, 63l, 807l, 187l, 2164l);
		assertOrder(recs, order);

		// Verify scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void unweighted144()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(11l, 0.3715);
		expected.put(585l, 0.2512);
		expected.put(38l, 0.1908);
		expected.put(141l, 0.1861);
		expected.put(807l, 0.1748);

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
		List<Long> order = newArrayList(11l, 585l, 38l, 141l, 807l);
		assertOrder(recs, order);

		// Verify scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void unweighted3855()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(1892l, 0.4303);
		expected.put(1894l, 0.2958);
		expected.put(63l, 0.2226);
		expected.put(2164l, 0.2119);
		expected.put(604l, 0.1941);

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
		List<Long> order = newArrayList(1892l, 1894l, 63l, 2164l, 604l);
		assertOrder(recs, order);

		// Verify scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void unweighted1637()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(2164l, 0.2272);
		expected.put(141l, 0.2225);
		expected.put(745l, 0.2067);
		expected.put(601l, 0.1995);
		expected.put(807l, 0.1846);

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
		List<Long> order = newArrayList(2164l, 141l, 745l, 601l, 807l);
		assertOrder(recs, order);

		// Verify scores
		for (ScoredId id: recs)
		{
			actual.put(id.getId(), id.getScore());
		}
		equalMaps(expected, actual);
	}

	@Test
	public void unweighted2919()
	{
		Map<Long, Double> expected = Maps.newHashMap();
		expected.put(11l, 0.3659);
		expected.put(1891l, 0.3278);
		expected.put(640l, 0.1958);
		expected.put(424l, 0.1840);
		expected.put(180l, 0.1527);

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
		List<Long> order = newArrayList(11l, 1891l, 640l, 424l, 180l);
		assertOrder(recs, order);

		// Verify scores
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

	private void equalMaps(Map<Long, Double> expected, Map<Long, Double> actual)
	{
		assertEquals(expected.size(), actual.size());

		for (Long key: expected.keySet())
		{
			assertEquals(expected.get(key), actual.get(key));
		}
	}
}