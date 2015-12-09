package controllers;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.introcs.Stopwatch;
import models.Movie;
import models.Rating;
import models.User;
import utils.CSVLoader;
import utils.Matrix;
import utils.Serializer;

public class Recommender {

	private HashMap<Long, User> users = new HashMap<>();
	private HashMap<Long, Movie> movies = new HashMap<>();
	private ArrayList<Long> userIdList = new ArrayList<>();
	private ArrayList<Long> movieIdList = new ArrayList<>(); // sequence of
																// movies in
																// order of the
																// titles
	private ArrayList<Rating> ratings = new ArrayList<>();
	private boolean ratingsSorted = false;
	private CSVLoader primer;

	private Serializer serializer;

	/**
	 * default constructor
	 */
	public Recommender() {

	}

	/**
	 * full constructor
	 * 
	 * @param serializer
	 * @param primer
	 *            (representing CSV file manipulator)
	 */
	public Recommender(Serializer s, CSVLoader primer) {
		this.primer = primer;
		serializer = s;
	}

	/**
	 * get id-user map
	 * 
	 * @return map
	 */
	public HashMap<Long, User> getUsers() {
		return users;
	}

	/**
	 * get user ids sorted by user's name and age
	 * 
	 * @return user ids
	 */
	public ArrayList<Long> getUserIdList() {
		return userIdList;
	}

	/**
	 * get movie ids sorted by movie's title
	 * 
	 * @return movie ids
	 */
	public ArrayList<Long> getMovieIdList() {
		return movieIdList;
	}

	// public boolean ratingsSorted() {
	// return ratingsSorted;
	// }

	/**
	 * get ratings made by a user
	 * 
	 * @param userId
	 * @return
	 */
	public ArrayList<Rating> getUserRatings(long userId) {
		ArrayList<Rating> ratings = new ArrayList<>();
		if (users.containsKey(userId)) {
			User user = users.get(userId);
			ratings.addAll(user.getRatings().values());
			Collections.sort(ratings, new Comparator<Rating>() {
				@Override
				public int compare(Rating o1, Rating o2) {
					if (o1.getRating() < o2.getRating())
						return 1; //descending order
					if (o1.getRating() > o2.getRating())
						return -1;
					return 0;
				}
			});
		}
		return ratings;
	}

	/**
	 * get ratings made by a user in string
	 * 
	 * @param userId
	 * @return string
	 */
	public String printUserRatings(long userId) {
		String toPrint = "";
		toPrint += users.get(userId).info() + "\n";
		ArrayList<Rating> ratings = getUserRatings(userId);
		if (ratings.size() > 0) {
			for (Rating r : ratings) {
				toPrint += r.getRating() + " => " + "<Movie ID - " + r.getMovieId() + "> " + movies.get(r.getMovieId()).info()
						+ "\n";
			}
		} else {
			toPrint += "Not available";
		}
		return toPrint;
	}

	/**
	 * get ratings list
	 * 
	 * @return ratings list
	 */
	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	/**
	 * get user size
	 * 
	 * @return user size
	 */
	public int getUsersSize() {
		return users.size();
	}

	/**
	 * add a user by fields
	 * 
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 * @param occupation
	 * @param zip
	 * @throws Exception
	 */
	public void addUser(String firstName, String lastName, int age, String gender, String occupation, String zip)
			throws Exception {
		User u = new User(firstName, lastName, age, gender, occupation, zip);
		if (!users.containsValue(u)) {
			putUser(u);
		}
	}

	/**
	 * add a user object
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void addUser(User user) throws Exception {
		if (!users.containsKey(user.getUserId()) && !users.containsValue(user)) {
			putUser(user);
		}
	}

	/**
	 * put the new user object onto the global fields
	 * 
	 * @param user
	 * @throws Exception
	 */
	private void putUser(User user) throws Exception {
		int pos = newPos(users, userIdList, user);
		if (pos > userIdList.size())
			userIdList.add(user.getUserId());
		else
			userIdList.add(pos, user.getUserId());
		users.put(user.getUserId(), user);
		if (users.size() != userIdList.size())
			throw new Exception();
	}

	/**
	 * add a movie by fields
	 * 
	 * @param title
	 * @param releaseDate
	 * @param url
	 * @param genreCode
	 * @throws Exception
	 */
	public void addMovie(String title, String releaseDate, String url, String genreCode) throws Exception {
		Movie m = new Movie(title, releaseDate, url, genreCode);
		if (!movies.containsValue(m)) {
			putMovie(m);
		}
	}

	/**
	 * add a movie by movie object
	 * 
	 * @param movie
	 * @throws Exception
	 */
	public void addMovie(Movie movie) throws Exception {
		if (!movies.containsKey(movie.getMovieId()) && !movies.containsValue(movie)) {
			putMovie(movie);
		}
	}

	/**
	 * put the new movie object onto the global fields
	 * 
	 * @param movie
	 * @throws Exception
	 */
	private void putMovie(Movie movie) throws Exception {
		int pos = newPos(movies, movieIdList, movie);
		if (pos > movieIdList.size())
			movieIdList.add(movie.getMovieId());
		else
			movieIdList.add(pos, movie.getMovieId());
		movies.put(movie.getMovieId(), movie);
		if (movies.size() != movieIdList.size())
			throw new Exception();
	}

	/**
	 * get user
	 * 
	 * @param user
	 *            id
	 * @return user
	 */
	public User getUser(Long id) {
		return users.get(id);
	}

	/**
	 * remove a user
	 * 
	 * @param user
	 *            id
	 * @return removed user
	 */
	public User removeUser(long id) {
		if (users.containsKey(id)) {
			// System.out.println(users.size() == userIdList.size());
			User user = users.get(id);
			HashMap<Long, Rating> madeRatings = user.getRatings();
			Iterator<Long> ite = user.getRatings().keySet().iterator();
			while (ite.hasNext()) {
				long movieId = ite.next();
				Movie ratedMovie = movies.get(movieId);
				Rating rate = madeRatings.get(movieId);

				ratedMovie.removeRating(id);
				while (this.ratings.contains(rate)) {
					this.ratings.remove(rate);
				}
			}
			userIdList.remove(id);
			return users.remove(id);
		}
		return null;
	}

	/**
	 * add rating by fields
	 * 
	 * @param userId
	 * @param movieId
	 * @param rating
	 *            point
	 * @throws Exception
	 */
	public void addRating(long userId, long movieId, int rating) throws Exception {
		if (users.containsKey(userId) && movies.containsKey(movieId)) {
			// User user = users.get(userId);
			Rating r = new Rating(userId, movieId, rating);
			putRating(r);
		}
	}

	/**
	 * add rating by rating object
	 * 
	 * @param rating
	 *            object
	 */
	public void addRating(Rating r) {
		long uId = r.getUserId();
		long mId = r.getMovieId();
		if (users.containsKey(uId) && movies.containsKey(mId) && r.getTime() >= 0
				&& Rating.checkRating(r.getRating())) {
			putRating(r);
		}
	}

	/**
	 * add rating to system
	 * 
	 * @param rating
	 */
	private void putRating(Rating r) {
		long uId = r.getUserId();
		long mId = r.getMovieId();
		ratings.add(r);
		// ratingsDB.put(uId, mId, r);
		users.get(uId).addRating(r);
		movies.get(mId).addRating(r);
		ratingsSorted = false;
	}

	/**
	 * get a rating
	 * 
	 * @param userId
	 * @param movieId
	 * @return rating
	 */
	public Rating getRating(long userId, long movieId) {
		if (users.containsKey(userId) && movies.containsKey(movieId))
			return users.get(userId).getRatings().get(movieId);
		return null;
	}

	/**
	 * get a movie
	 * 
	 * @param movie
	 *            id
	 * @return movie
	 */
	public Movie getMovie(Long id) {
		return movies.get(id);
	}

	/**
	 * get id-movie map
	 * 
	 * @return map
	 */
	public HashMap<Long, Movie> getMovies() {
		return movies;
	}

	/**
	 * search for a user
	 * 
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @return user
	 * @throws Exception
	 */
	public ArrayList<Comparable> searchUser(String firstName, String lastName, int age) throws Exception {
		User query = new User(firstName, lastName, age);
		int position = search(users, userIdList, query);
		return rippleSearch(users, userIdList, query, position);
	}

	/**
	 * search for a movie
	 * 
	 * @param title
	 * @return movie
	 * @throws Exception
	 */
	public ArrayList<Comparable> searchMovie(String title) throws Exception {
		Movie query = new Movie(title);
		int position = search(movies, movieIdList, query);
		return rippleSearch(movies, movieIdList, query, position);
	}

	/**
	 * get top ten movies
	 * 
	 * @return list of top ten movies
	 */
	public List<Movie> getTopTenMovies() {
		List<Movie> topten = new ArrayList<>();
		if (movies.size() > 10) {
			List<Movie> movieByAvrRatingPoint = new ArrayList<Movie>(movies.values());
			Collections.sort(movieByAvrRatingPoint, new Comparator<Movie>() {
				@Override // descending order
				public int compare(Movie m1, Movie m2) {
					if (m1.getAveragePoint() < m2.getAveragePoint())
						return 1;
					if (m1.getAveragePoint() > m2.getAveragePoint())
						return -1;
					return 0;
				}
			});
			for (int i = 0; i < movieByAvrRatingPoint.size(); i++) {
				if (i > 9)
					break;
				Movie mov = movieByAvrRatingPoint.get(i);
				if (mov.getAveragePoint() > 0)
					topten.add(mov);
			}
		}
		return topten;
	}

	/**
	 * load CSV file
	 * 
	 * @throws Exception
	 */
	public void prime() throws Exception {
		System.out.println("Loading...");
		HashMap<Integer, String> genres = primer.loadGenres();
		Movie.setGenres(genres);
		Iterator<User> users = primer.loadUsers().iterator();
		while (users.hasNext()) {
			addUser(users.next());
		}

		Iterator<Movie> movies = primer.loadMovies().iterator();
		while (movies.hasNext()) {
			addMovie(movies.next());
		}

		Iterator<Rating> ratings = primer.loadRatings().iterator();
		while (ratings.hasNext()) {
			Rating nextR = ratings.next();
			addRating(nextR);
		}

		System.out.println("Completed, " + this.users.size() + " users, " + this.movies.size() + " movies, "
				+ this.ratings.size() + "/" + this.ratings.size() + " ratings");
	}

	/**
	 * get a list of recommended movies for a user
	 * 
	 * @param userId
	 * @return list of movies
	 */
	public ArrayList<Movie> recommend(long userId) {
		ArrayList<Movie> recommendedMovies = new ArrayList<>();
		User user = null;
		if (users.containsKey(userId))
			user = users.get(userId);
		else
			return null;
		// Find users that also like the same movies
		HashSet<Long> similarUsersId = findSimilarUsers(user);
		System.out.println("Similar users: " + similarUsersId.size());
		if (similarUsersId.size() > 0) {
			User mostSimilar = findMostSimilarUSer(user, similarUsersId);
			if (mostSimilar == null)
				return recommendedMovies;
			appendRecommendedMovies(user, mostSimilar, recommendedMovies);
			Collections.sort(recommendedMovies, new Comparator<Movie>() {
				@Override // descending order
				public int compare(Movie m1, Movie m2) {
					if (m1.getAveragePoint() < m2.getAveragePoint())
						return 1;
					if (m1.getAveragePoint() > m2.getAveragePoint())
						return -1;
					return 0;
				}
			});
		}
		return recommendedMovies;
	}

	/**
	 * find users that have similar behavior (rating) as a user
	 * 
	 * @param user
	 * @return set of users
	 */
	private HashSet<Long> findSimilarUsers(User user) {
		HashSet<Long> similarUsersId = new HashSet<>();
		HashMap<Long, Rating> rated = user.getRatings(); // get all movies
															// rated by user
		Iterator<Long> ite = rated.keySet().iterator();
		while (ite.hasNext()) {
			long movieId = ite.next();
			int point = rated.get(movieId).getRating();
			if (point > 0) // if user rated it positively (likes it)
			{
				// this maps a particular rating point (either 1,3, or 5) with a
				// group of users
				HashMap<Integer, HashSet<Long>> raters = movies.get(movieId).getPositiveRaters(); // get
																									// users
																									// that
																									// also
																									// rated
																									// the
																									// movie
																									// either
																									// (1,3,5)

				HashSet<Long> similarRaters = raters.get(point); // get users
																	// that also
																	// rated the
																	// movie
																	// with the
																	// same
																	// point
				similarUsersId.addAll(similarRaters);
			}
			similarUsersId.remove(user.getUserId()); // make sure the user
														// looking for
			// recommendation isn't in this set
		}
		return similarUsersId;
	}

	/**
	 * find the user (from a list of users) that has the most similar behavior
	 * (rating) as the current user
	 * 
	 * @param current
	 *            user
	 * @param list
	 *            of similar users' ids
	 * @return most similar behavior user
	 */
	private User findMostSimilarUSer(User user, HashSet<Long> similarUsersId) {
		User mostSimilar = null;
		HashMap<Long, Rating> rated = user.getRatings();
		double min = Double.MAX_VALUE;
		Iterator<Long> iteSim = similarUsersId.iterator();
		while (iteSim.hasNext()) {
			User other = users.get(iteSim.next());
			double similarity = Matrix.similarityInRadian(movieIdList, rated, other.getRatings());
			if (similarity < min) {
				min = similarity;
				mostSimilar = other;
			}
		}
		if (mostSimilar == null || min >= Math.PI / 2)
			return null;
		else {
			System.out.println("Recommendation is retrieved from " + mostSimilar.getFirstName() + " "
					+ mostSimilar.getLastName() + " (User ID: " + mostSimilar.getUserId() + "), similarity: " + min);
		}
		return mostSimilar;
	}

	/**
	 * append movies from the most similar behavior user
	 * 
	 * @param current
	 *            user
	 * @param most
	 *            similar user
	 * @param list
	 *            to append recommended movies
	 */
	private void appendRecommendedMovies(User user, User mostSimilar, ArrayList<Movie> recommendedMovies) {
		HashSet<String> favoriteGenres = Movie.getGenresFromMoviesGroup(movies, user.getPositiveRatedMovieIds());
		// String genres = "[";
		// Iterator<String> ite = favoriteGenres.iterator();
		// while (ite.hasNext()) {
		// genres += ite.next();
		// if (ite.hasNext())
		// genres += ",";
		// }
		// genres += "]";
		// System.out.println("Your favorite genres (" + favoriteGenres.size() +
		// "): " + genres);
		HashMap<Long, Rating> rated = user.getRatings();
		Iterator<Long> ids = mostSimilar.getPositiveRatedMovieIds().iterator();
		while (ids.hasNext()) {
			long nextId = ids.next();
			if (!rated.containsKey(nextId)) { // only take movies that user
												// hasn't seen yet into
												// account
				Movie m = movies.get(nextId);
				// Content-based filter on genres
				HashSet<String> movieGenre = m.getIndivGenre();
				boolean flag = false;
				Iterator<String> genreIte = movieGenre.iterator();
				while (genreIte.hasNext()) {
					if (favoriteGenres.contains(genreIte.next())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					recommendedMovies.add(movies.get(nextId));
				}
			}
		}
	}

	/**
	 * Current status of system
	 * 
	 * @return string
	 */
	public String info() {
		return "Users: " + users.size() + "$ Movies: " + movies.size() + "$ Ratings: " + ratings.size();
	}

	/**
	 * load data from the serializer
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
		Stopwatch watch = new Stopwatch();
		System.out.println("Loading from datastore...");
		serializer.read();
		User.setCounter((long) serializer.pop());
		Movie.setCounter((long) serializer.pop());
		users = (HashMap<Long, User>) serializer.pop();
		movies = (HashMap<Long, Movie>) serializer.pop();
		userIdList = (ArrayList<Long>) serializer.pop();
		movieIdList = (ArrayList<Long>) serializer.pop();
		ratings = (ArrayList<Rating>) serializer.pop();
		ratingsSorted = (boolean) serializer.pop();
		// ratingsDB = (HashBasedTable<Long, Long, Rating>) serializer.pop();
		System.out.println("Completed, " + watch.elapsedTime() + " seconds");
	}

	/**
	 * store data into the serializer
	 * 
	 * @throws Exception
	 */
	public void store() throws Exception {
		Stopwatch watch = new Stopwatch();
		System.out.println("Saving to datastore...");
		// serializer.push(ratingsDB);
		serializer.push(ratingsSorted);
		serializer.push(ratings);
		serializer.push(movieIdList);
		serializer.push(userIdList);
		serializer.push(movies);
		serializer.push(users);
		serializer.push(Movie.getCounter());
		serializer.push(User.getCounter());
		serializer.write();
		System.out.println("Completed, " + watch.elapsedTime() + " seconds");
	}

	/**
	 * find the proper position to add a new item to the list
	 * 
	 * @param map
	 * @param ids
	 * @param query
	 * @return position in ids list
	 * @throws Exception
	 */
	private int newPos(HashMap<Long, ? extends Comparable> map, ArrayList<Long> ids, Comparable query)
			throws Exception {
		int pos = 0;
		if (ids.size() == 0 && map.size() == 0)
			return pos;
		if (ids.size() == 1 && map.size() == 1) {
			Comparable presentObject = map.values().iterator().next();
			int result = query.compareTo(presentObject);
			// if the new one is "smaller" than currently present object, add to
			// left of the current object
			// else add to right (normally add)
			if (result > 0)
				pos = 1;
		} else {
			int lo = 0;
			int hi = ids.size() - 1;
			while (lo <= hi) {
				int mid = (lo + hi) / 2;
				long midId = ids.get(mid);
				Comparable midObject = map.get(midId);
				int result = midObject.compareTo(query);
				if (result > 0)
					hi = mid - 1;
				else if (result < 0) {
					lo = mid + 1;
				}
			}
			// if(lo != hi)
			// throw new Exception();
			pos = lo;
		}
		return pos;
	}

	/**
	 * retrieve a searching query's index (if available) in ids list
	 * 
	 * @param map
	 * @param ids
	 * @param query
	 * @return found position
	 */
	private int search(HashMap<Long, ? extends Comparable> map, ArrayList<Long> ids, Comparable query) {
		int foundPos = -1;
		if (map.size() > 0) {
			int lo = 0;
			int hi = ids.size() - 1;
			while (lo <= hi) {
				int mid = (lo + hi) / 2;
				long midId = ids.get(mid);
				Comparable midObject = map.get(midId);
				int result = midObject.compareTo(query);
				if (result > 0)
					hi = mid - 1;
				else if (result < 0) {
					lo = mid + 1;
				} else {
					foundPos = mid;
					break;
				}
			}
		}
		return foundPos;
	}

	/**
	 * from a found position in a list, traverse left and right to collect
	 * similar items
	 * 
	 * @param map
	 * @param ids
	 * @param query
	 * @param foundPos
	 * @return list of similar items
	 */
	private ArrayList<Comparable> rippleSearch(HashMap<Long, ? extends Comparable> map, ArrayList<Long> ids,
			Comparable query, int foundPos) {
		ArrayList<Comparable> results = new ArrayList<>();

		if (map.size() > 0 && foundPos >= 0 && foundPos < map.size()) {
			// ripple
			Comparable thisObject = map.get(ids.get(foundPos));
			results.add(thisObject);
			// left
			for (int i = foundPos - 1; i >= 0; i--) {
				long objID = ids.get(i);
				Comparable that = map.get(objID);
				if (thisObject.compareTo(that) == 0)
					results.add(that);
				else
					break;
			}

			// right
			for (int i = foundPos + 1; i < ids.size(); i++) {
				long objID = ids.get(i);
				Comparable that = map.get(objID);
				if (thisObject.compareTo(that) == 0)
					results.add(that);
				else
					break;
			}

		}
		return results;
	}
}
