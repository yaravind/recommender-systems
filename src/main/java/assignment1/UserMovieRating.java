package assignment1;

public class UserMovieRating
{
	public final int userId;
	public final int movieId;
	public final double rating;

	public UserMovieRating(int userId, int movieId, double rating)
	{
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
	}

	@Override
	public String toString()
	{
		return "UserMovieRating [userId=" + userId + ", movieId=" + movieId + ", rating=" + rating + "]";
	}
}