package util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import static com.google.common.collect.Iterables.toArray;

public class CsvUtility
{
	public final static Splitter SPLITTER = Splitter.on(",");
	public final static Splitter COLON_SPLITTER = Splitter.on(":");
	private static final List<String> toWrite = Lists.newArrayList();
	public static final Map<Integer, Integer> indexToMovieId = Maps.newHashMap();

	public static void main(String[] args)
	{
		List<String> lines;
		try
		{
			lines = Resources.readLines(CsvUtility.class.getResource("recsys-user-ratings-written-assignment-1.csv"),
					Charsets.UTF_8);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		int index = 1;
		for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++)
		{
			String[] split = toArray(SPLITTER.split(lines.get(lineIndex)), String.class);

			for (int columnIdx = 1; columnIdx < split.length; columnIdx++)
			{
				if (lineIndex == 0)
				{
					String[] temp = toArray(COLON_SPLITTER.split(split[columnIdx]), String.class);
					if (temp.length > 1)
					{
						// ignore column 1 of the header
						// System.out.println(temp[0] + "=" + temp[1]);
						indexToMovieId.put(index++, Integer.valueOf(temp[0]));
					}
				}
				else
				{
					if (!split[columnIdx].equals(""))
					{
						String s = split[0] + "," + indexToMovieId.get(columnIdx) + "," + split[columnIdx];
						toWrite.add(s);
						System.err.println(s);
					}
				}
			}
		}
	}
}
