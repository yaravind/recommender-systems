package assignment1;

import java.util.Comparator;

public class ScoreHolder // implements Comparator<ScoreHolder>// , Comparable<ScoreHolder>
{
	public final double score;
	public final int movieId;

	public static final Comparator<ScoreHolder> SCORE_COMPARATOR = new Comparator<ScoreHolder>()
	{
		public int compare(ScoreHolder o1, ScoreHolder o2)
		{
			Double o1Score = Double.valueOf(o1.score);
			Double o2Score = Double.valueOf(o2.score);

			// comparing o2 with o1 to make it DESCENDING order
			return o2Score.compareTo(o1Score);
		}
	};

	public ScoreHolder(int movieId, double score)
	{
		this.score = score;
		this.movieId = movieId;
	}

	// @Override
	// public int hashCode()
	// {
	// final int prime = 31;
	// int result = 1;
	// long temp;
	// temp = Double.doubleToLongBits(score);
	// result = prime * result + (int) (temp ^ (temp >>> 32));
	// return result;
	// }
	//
	// @Override
	// public boolean equals(Object obj)
	// {
	// if (this == obj)
	// {
	// return true;
	// }
	// if (obj == null)
	// {
	// return false;
	// }
	// if (getClass() != obj.getClass())
	// {
	// return false;
	// }
	// ScoreHolder other = (ScoreHolder) obj;
	// if (Double.doubleToLongBits(score) != Double.doubleToLongBits(other.score))
	// {
	// return false;
	// }
	// return true;
	// }

	@Override
	public String toString()
	{
		return "ScoreHolder [score=" + score + ", movieId=" + movieId + "]";
	}
}