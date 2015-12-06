package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.math.DoubleMath;

import utils.ToJsonString;

public class Movie implements Comparable<Movie> {
	private static long counter = 1;
	private long movieId;
	private String title, releaseDate, url, genreCode;
	private HashMap<Long, Integer> ratings = new HashMap<>(); // Map user and
																// rating point

	public Movie(String title, String releaseDate, String url, String genreCode) throws Exception {
		if (str(title, 200) && str(releaseDate, 200) && str(url, 200) && str(genreCode, 19)
				&& checkGenreCode(genreCode)) {
			movieId = counter++;
			this.title = title.trim();
			this.releaseDate = releaseDate.trim();
			this.url = url.trim();
			this.genreCode = genreCode.trim();
		} else {
			throw new Exception();
		}
	}

	public Movie(String title) throws Exception {
		if (str(title, 200))
			this.title = title;
		else
			throw new Exception();
	}

	public long getMovieId() {
		return movieId;
	}

	public String getTitle() {
		return title;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public String getUrl() {
		return url;
	}

	public String getGenreCode() {
		return genreCode;
	}

	public HashMap<Long, Integer> getRatings() {
		return ratings;
	}

	public void addRating(Rating r) {
		if (r != null && Rating.checkRating(r.getRating()))
			ratings.put(r.getUserId(), r.getRating());
	}

	public double getAveragePoint() {
		return DoubleMath.mean(ratings.values());
	}

	public String toString() {
		return new ToJsonString(getClass(), this).toString();
	}

	public String info() {
		return title + ", " + releaseDate + ", " + url + ", " + genreCode + ", " + getAveragePoint();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.title, this.releaseDate, this.url, this.genreCode);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Movie) {
			final Movie other = (Movie) obj;
			return Objects.equal(title, other.getTitle()) && Objects.equal(releaseDate, other.getReleaseDate())
					&& Objects.equal(url, other.getUrl()) && Objects.equal(genreCode, other.getGenreCode());
		} else {
			return false;
		}
	}

	@Override
	public int compareTo(Movie that) {
		int compareTitle = title.toLowerCase().compareTo(that.title.toLowerCase());
		if (compareTitle < 0)
			return -1;
		if (compareTitle > 0)
			return 1;
		return 0;
	}

	public static void resetCounter() {
		counter = 1;
	}

	public static long getCounter() {
		return counter;
	}

	// For testing
	public static void incrementCounter() {
		counter++;
	}

	public static void setCounter(long counter) {
		if (counter >= 1)
			Movie.counter = counter;
	}

	public HashMap<Integer, HashSet<Long>> getPositiveRaters() {
		HashMap<Integer, HashSet<Long>> map = new HashMap<>();
		HashSet<Long> one = new HashSet<>();
		HashSet<Long> three = new HashSet<>();
		HashSet<Long> five = new HashSet<>();
		Iterator<Long> ite = ratings.keySet().iterator();
		while (ite.hasNext()) {
			long rater = ite.next();
			int point = ratings.get(rater);
			switch (point) {
			case 1:
				one.add(rater);
				break;
			case 3:
				three.add(rater);
				break;
			case 5:
				five.add(rater);
				break;
			default:
				;
				break;
			}
		}
		map.put(1, one);
		map.put(3, three);
		map.put(5, five);
		return map;
	}

	private boolean checkGenreCode(String code) {
		String c = code.trim();
		for (int i = 0; i < c.length(); i++) {
			if (!(c.charAt(i) == '0' || c.charAt(i) == '1'))
				return false;
		}
		return true;
	}

	private static boolean str(String toCheck, int length) {
		if (toCheck == null)
			return false;
		String result = toCheck.trim();
		return result.length() > 0 && result.length() <= length;
	}
}
