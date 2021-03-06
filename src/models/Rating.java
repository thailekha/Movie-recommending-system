package models;

import java.util.Calendar;
import java.util.HashSet;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import utils.ToJsonString;

public class Rating implements Comparable<Rating> {

	private static final HashSet<Integer> validRatings = Sets.newHashSet(-5, -3, -1, 0, 1, 3, 5);
	private long userId, movieId;
	private int rating;
	private long timestamp;

	public Rating(long userId, long movieId, int rating) throws Exception {
		if (userId < User.getCounter() && movieId < Movie.getCounter() && validRatings.contains(rating)) {
			this.userId = userId;
			this.movieId = movieId;
			this.rating = rating;
			this.timestamp = System.currentTimeMillis() * 1000;
		} else
			throw new Exception("Invalid arguments to construct rating object");
	}

	public Rating(long userId, long movieId, int rating, long timestamp) throws Exception {
		boolean uCond = userId < User.getCounter();
		boolean mCond = movieId < Movie.getCounter();
		boolean rCond = checkRating(rating);
		boolean tCond = timestamp >= 0;
		if (uCond && mCond && rCond && tCond) {
			this.userId = userId;
			this.movieId = movieId;
			this.rating = rating;
			this.timestamp = timestamp;
		} else
			throw new Exception("Invalid arguments to construct rating object");
	}

	public long getUserId() {
		return userId;
	}

	public long getMovieId() {
		return movieId;
	}

	public int getRating() {
		return rating;
	}

	public long getTime() {
		return timestamp;
	}

	@Override
	public int compareTo(Rating that) {
		if (rating < that.rating)
			return -1;
		if (rating > that.rating)
			return 1;
		return 0;
	}

	public String info() {
		return "User ID: " + userId + ", " + "Movie ID: " + movieId + ", " + "Rating: " + rating + ", " + "Timestamp: "
				+ timestamp;
	}
	
	public String toString() {
		return new ToJsonString(getClass(), this).toString();
	}

	public boolean equals(final Object obj) {
		if (obj instanceof Rating) {
			Rating that = (Rating) obj;
			return Objects.equal(userId, that.userId) && Objects.equal(movieId, that.movieId)
					&& Objects.equal(rating, that.rating) && Objects.equal(timestamp, that.timestamp);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.userId, this.movieId, this.rating, this.timestamp);
	}

	public static boolean checkRating(int rating) {
		return validRatings.contains(rating);
	}
}
