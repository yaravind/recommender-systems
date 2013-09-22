package assignment1;

import java.util.Comparator;

import com.google.common.primitives.Doubles;

public class ScoreHolder implements Comparator<ScoreHolder>, Comparable<ScoreHolder>
{
	public final double score;
	public final int movieId;

	public ScoreHolder(int movieId, double score)
	{
		this.score = score;
		this.movieId = movieId;
	}

	public int compareTo(ScoreHolder o)
	{
		return Doubles.compare(score, o.score);
	}

	public int compare(ScoreHolder o1, ScoreHolder o2)
	{
		// returning a negative integer, 0, or a positive integer depending on whether the first argument is less than,
		// equal to, or greater than the second
		Double o1Score = Double.valueOf(o1.score);
		Double o2Score = Double.valueOf(o2.score);

		return o1Score.compareTo(o2Score);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		ScoreHolder other = (ScoreHolder) obj;
		if (Double.doubleToLongBits(score) != Double.doubleToLongBits(other.score))
		{
			return false;
		}
		return true;
	}
}