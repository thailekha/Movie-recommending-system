package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.google.common.math.DoubleMath;

import utils.ToJsonString;

public class Movie implements Comparable<Movie> {

	private static HashMap<Integer, String> genres = new HashMap<>();
	private static long counter = 1;
	private long movieId;
	private String title, releaseDate, url, genreCode;
	private HashMap<Long, Integer> ratings = new HashMap<>(); // Map user and
																// rating point

	/**
	 * Contructor
	 * 
	 * @param movie
	 *            title
	 * @param movie
	 *            releaseDate
	 * @param movie
	 *            url
	 * @param movie's
	 *            genres ins string code
	 * @throws Exception
	 */
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

	/**
	 * Query constructor, used when searching a movie
	 * 
	 * @param movie
	 *            title
	 * @throws Exception
	 */
	public Movie(String title) throws Exception {
		if (str(title, 200))
			this.title = title;
		else
			throw new Exception();
	}

	/**
	 * movie id getter
	 * 
	 * @return movie id
	 */
	public long getMovieId() {
		return movieId;
	}

	/**
	 * movie title getter
	 * 
	 * @return movie title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * release date getter
	 * 
	 * @return movie release date
	 */
	public String getReleaseDate() {
		return releaseDate;
	}

	/**
	 * movie url getter
	 * 
	 * @return movie url (internet link)
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * movie genres
	 * 
	 * @return movies genres in string code
	 */
	public String getGenreCode() {
		return genreCode;
	}

	/**
	 * get all ratings made for the movie
	 * 
	 * @return ratings map (user -> rating)
	 */
	public HashMap<Long, Integer> getRatings() {
		return ratings;
	}

	/**
	 * rate the movie
	 * 
	 * @param rating
	 *            object
	 */
	public void addRating(Rating r) {
		if (r != null && Rating.checkRating(r.getRating()))
			ratings.put(r.getUserId(), r.getRating());
	}

	/**
	 * get average rating point from all the ratings made for the movie
	 * 
	 * @return average rating point
	 */
	public double getAveragePoint() {
		return DoubleMath.mean(ratings.values());
	}

	/**
	 * get set of genres of the movie
	 * 
	 * @return set of genres
	 */
	public HashSet<String> getIndivGenre() {
		HashMap<Integer, String> toLookup = getGenres();
		HashSet<String> genres = new HashSet<>();
		for (int i = 0; i < genreCode.length(); i++) {
			char bit = genreCode.charAt(i);
			if (bit == '1') {
				genres.add(toLookup.get(i));
			}
		}
		return genres;
	}

	/**
	 * toString in Json style
	 */
	public String toString() {
		return new ToJsonString(getClass(), this).toString();
	}

	/**
	 * proper display of the movie's details
	 * 
	 * @return string of the movie's details
	 */
	public String info() {
		double roundedPoint = ((int) getAveragePoint() * 10) / 10;
		return title + ", " + releaseDate + ", " + url + ", " + "\nGenre(s): " + printGenre() + "\nAverage point: "
				+ roundedPoint;
	}

	/**
	 * String display of a movie's genres
	 */
	public String printGenre() {
		String genre = "[";
		Iterator<String> ite = getIndivGenre().iterator();
		while (ite.hasNext()) {
			genre += ite.next();
			if (ite.hasNext())
				genre += ",";
		}
		genre += "]";
		return genre;
	}

	/**
	 * hash code used for hash-based collection types
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.title, this.releaseDate, this.url, this.genreCode);
	}

	/**
	 * check equality with another movie object
	 */
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

	/**
	 * Make comparison with another movie object (mainly for sorting by movie
	 * title)
	 */
	@Override
	public int compareTo(Movie that) {
		int compareTitle = title.toLowerCase().compareTo(that.title.toLowerCase());
		if (compareTitle < 0)
			return -1;
		if (compareTitle > 0)
			return 1;
		return 0;
	}

	/**
	 * reset ID counter of Movie class
	 */
	public static void resetCounter() {
		counter = 1;
	}

	/**
	 * get ID counter of Movie class
	 * 
	 * @return ID counter
	 */
	public static long getCounter() {
		return counter;
	}

	/**
	 * increment ID counter of Movie class by 1, for testing purposes
	 */
	public static void incrementCounter() {
		counter++;
	}

	/**
	 * set ID counter of Movie class
	 * 
	 * @param new
	 *            ID counter value
	 */
	public static void setCounter(long counter) {
		if (counter >= 1)
			Movie.counter = counter;
	}

	/**
	 * get a map view of all the users that rated the movie above 0, each rating
	 * point is associated with a group of users
	 * 
	 * @return
	 */
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

	/**
	 * check if string genre code is valid (within allowed length and contains
	 * only 0s and 1s)
	 * 
	 * @param genre
	 *            code
	 * @return true if valid
	 */
	private boolean checkGenreCode(String code) {
		String c = code.trim();
		for (int i = 0; i < c.length(); i++) {
			if (!(c.charAt(i) == '0' || c.charAt(i) == '1'))
				return false;
		}
		return true;
	}

	/**
	 * general checking String object if it's not null and within a defined
	 * length
	 * 
	 * @param string
	 *            to Check
	 * @param allowed
	 *            length
	 * @return true if valid
	 */
	private static boolean str(String toCheck, int length) {
		if (toCheck == null)
			return false;
		String result = toCheck.trim();
		return result.length() > 0 && result.length() <= length;
	}

	/**
	 * Set the valid genres that a movie object can be of
	 * 
	 * @param genres
	 *            map
	 */
	public static void setGenres(HashMap<Integer, String> genres) {
		Movie.genres = genres;
	}

	/**
	 * Set of the valid genres that a movie object can be of
	 * 
	 * @return genres map
	 */
	public static HashMap<Integer, String> getGenres() {
		if (Movie.genres.size() == 0) {
			HashMap<Integer, String> map = new HashMap<>();
			String[] g = new String[] { "unknown", "action", "adventure", "animation", "children's", "comedy", "crime",
					"documentary", "drama", "fantasy", "film-noir", "horror", "musical", "mystery", "romance", "sci-fi",
					"thriller", "war", "western" };
			for (int i = 0; i < g.length; i++)
				map.put(i, g[i]);
			return map;
		} else {
			return genres;
		}
	}

	/**
	 * get set of total genres of a movie set
	 * 
	 * @param movies
	 *            database
	 * @param movie
	 *            ids
	 * @return set of genres
	 */
	public static HashSet<String> getGenresFromMoviesGroup(HashMap<Long, Movie> movies, HashSet<Long> ids) {
		HashSet<String> genres = new HashSet<>();
		Iterator<Long> ite = ids.iterator();
		while (ite.hasNext()) {
			Movie m = movies.get(ite.next());
			genres.addAll(m.getIndivGenre());
		}
		return genres;
	}
	
	public void removeRating(long userId) {
		if(ratings.containsKey(userId)) {
			ratings.remove(userId);
		}
	}
}
