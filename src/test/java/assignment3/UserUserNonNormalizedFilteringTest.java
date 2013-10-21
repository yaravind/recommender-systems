package assignment3;

import java.util.Map;

import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import useruser.UUMain;

import com.google.common.collect.Maps;

import static org.junit.Assert.assertEquals;

/**
 * Part 2 of the assignment 2.
 * 
 * @author Aravind R. Yarram
 */
public class UserUserNonNormalizedFilteringTest
{
	Logger logger = LoggerFactory.getLogger(getClass());

	LenskitConfiguration config;
	Recommender rec;
	ItemRecommender irec;

	@Before
	public void setUp() throws Exception
	{
		config = UUMain.configureRecommender();
		rec = LenskitRecommender.build(config);
		irec = rec.getItemRecommender();
	}

	@Test
	public void weighted1024()
	{
		Map<Long, String> expected = Maps.newHashMap();
		expected.put(462l, "3.1082");
		expected.put(393l, "3.8722");
		expected.put(36955l, "2.3524");
		expected.put(77l, "4.3848");
		expected.put(268l, "2.8646");

		String[] input = { "1024:77", "1024:268", "1024:462", "1024:393", "1024:36955"};
		Map<Long, String> actual = UUMain.predict(input);

		equalMaps(expected, actual);
	}

	@Test
	public void weighted2048()
	{
		Map<Long, String> expected = Maps.newHashMap();
		expected.put(788l, "3.8509");
		expected.put(36955l, "3.9698");
		expected.put(77l, "4.8493");

		String[] input = { "2048:77", "2048:36955", "2048:788"};

		Map<Long, String> actual = UUMain.predict(input);

		equalMaps(expected, actual);
	}

	@Test
	public void assignment()
	{
		String[] input = { "3787:7443", "3787:146", "3787:272", "3787:862", "3787:857", "3344:114", "3344:664", "3344:1637",
				"3344:862", "3344:85", "4889:462", "4889:1892", "4889:114", "4889:38", "4889:272", "1990:597", "1990:105",
				"1990:393", "1990:155", "1990:1572", "5461:641", "5461:1892", "5461:7443", "5461:187", "5461:8587"};

		UUMain.predict(input);
	}

	public static void equalMaps(Map<Long, String> expected, Map<Long, String> actual)
	{
		assertEquals(expected.size(), actual.size());

		for (Long key: expected.keySet())
		{
			assertEquals(expected.get(key), actual.get(key));
		}
	}
}