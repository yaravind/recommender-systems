package util;

import java.util.List;
import java.util.Map;

import org.grouplens.lenskit.scored.ScoredId;

import static org.junit.Assert.assertEquals;

public class AssertUtil
{
	public static void equalMaps(Map<Long, Double> expected, Map<Long, Double> actual)
	{
		assertEquals(expected.size(), actual.size());

		for (Long key: expected.keySet())
		{
			assertEquals(expected.get(key), actual.get(key));
		}
	}

	public static void assertOrder(List<ScoredId> recs, List<Long> order)
	{
		int i = 0;
		for (ScoredId id: recs)
		{
			assertEquals(order.get(i++).longValue(), id.getId());
		}
	}
}
