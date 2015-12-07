package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.google.common.collect.HashBasedTable;

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
	private HashBasedTable<Long, Long, Rating> ratingsDB = HashBasedTable.create(); // userid,movieid,rating
	private CSVLoader primer;

	private Serializer serializer;

	public Recommender() {

	}

	public Recommender(Serializer s, CSVLoader primer) {
		this.primer = primer;
		serializer = s;
	}

	public void addUser(String firstName, String lastName, int age, String gender, String occupation, String zip)
			throws Exception {
		// TODO Auto-generated method stub
		User u = new User(firstName, lastName, age, gender, occupation, zip);
		if (!users.containsValue(u)) {
			putUser(u);
		}
	}

	public void addUser(User user) throws Exception {
		if (!users.containsKey(user.getUserId()) && !users.containsValue(user)) {
			putUser(user);
		}
	}

	private void putUser(User user) throws Exception {

		// userIdList.add(user.getUserId());
		// addSort(userIdList, users);

		int pos = newPos(users, userIdList, user);
		if (pos > userIdList.size())
			userIdList.add(user.getUserId());
		else
			userIdList.add(pos, user.getUserId());
		users.put(user.getUserId(), user);
		if (users.size() != userIdList.size())
			throw new Exception();
	}

	public HashMap<Long, User> getUsers() {
		return users;
	}

	public ArrayList<Long> getUserIdList() {
		return userIdList;
	}

	public ArrayList<Long> getMovieIdList() {
		return movieIdList;
	}

	public boolean ratingsSorted() {
		return ratingsSorted;
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	public int getUsersSize() {
		return users.size();
	}

	public void addMovie(String title, String releaseDate, String url, String genreCode) throws Exception {
		// TODO Auto-generated method stub
		Movie m = new Movie(title, releaseDate, url, genreCode);
		if (!movies.containsValue(m)) {
			putMovie(m);
		}
	}

	public void addMovie(Movie movie) throws Exception {
		if (!movies.containsKey(movie.getMovieId()) && !movies.containsValue(movie)) {
			putMovie(movie);
		}
	}

	private void putMovie(Movie movie) throws Exception {
		movies.put(movie.getMovieId(), movie);
		// movieIdList.add(movie.getMovieId());
		// addSort(movieIdList, movies);

		int pos = newPos(movies, movieIdList, movie);
		if (pos > movieIdList.size())
			movieIdList.add(movie.getMovieId());
		else
			movieIdList.add(pos, movie.getMovieId());

		if (movies.size() != movieIdList.size())
			throw new Exception();
	}

	public User getUser(Long id) {
		return users.get(id);
	}

	public User removeUser(long id) {
		if (users.containsKey(id)) {
			System.out.println(users.size() == userIdList.size());
			userIdList.remove(id);
			return users.remove(id);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Comparable> searchUser(String firstName, String lastName, int age) throws Exception {
		User query =  new User(firstName, lastName, age);
		int position =  search(users, userIdList,query);
		return rippleSearch(users, userIdList, query,position);
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Comparable> searchMovie(String title) throws Exception {
		Movie query = new Movie(title);
		int position  = search(movies,movieIdList,query);
		return rippleSearch(movies, movieIdList, query,position);
	}

	private int newPos(HashMap<Long, ? extends Comparable> map, ArrayList<Long> ids, Comparable query) throws Exception {
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
//			if(lo != hi)
//				throw new Exception();
			pos = lo;
		}
		return pos;
	}

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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	public void addRating(long userId, long movieId, int rating) throws Exception {
		if (users.containsKey(userId) && movies.containsKey(movieId)) {
			// User user = users.get(userId);
			Rating r = new Rating(userId, movieId, rating);
			// Rating put = ratingsDB.put(userId, movieId, r); // the previous
			// // value, if
			// // replaced
			// if (put != null) {
			// while (ratings.contains(put))
			// ratings.remove(put);
			// user.removeRating(put);
			// }
			//
			// ratings.add(r);
			// user.addRating(r);
			// ratingsSorted = false;
			putRating(r);
		}
	}

	public void addRating(Rating r) {
		long uId = r.getUserId();
		long mId = r.getMovieId();
		if (users.containsKey(uId) && movies.containsKey(mId) && r.getTime() >= 0
				&& Rating.checkRating(r.getRating())) {
			putRating(r);
		}
	}

	private void putRating(Rating r) {
		long uId = r.getUserId();
		long mId = r.getMovieId();
		ratings.add(r);
		ratingsDB.put(uId, mId, r);
		users.get(uId).addRating(r);
		movies.get(mId).addRating(r);
		ratingsSorted = false;
	}

	public Rating getRating(long userId, long movieId) {
		return ratingsDB.get(userId, movieId);
	}

	public HashBasedTable<Long, Long, Rating> getRatingsDB() {
		return ratingsDB;
	}

	public Movie getMovie(Long id) {
		return movies.get(id);
	}

	public HashMap<Long, Movie> getMovies() {
		return movies;
	}

	public List<Movie> getTopTenMovies() {
		List<Movie> topten = new ArrayList<>();
		if (movies.size() > 10) {
			// if (!ratingsSorted) {
			// sortRatings();
			// }
			// for (int i = ratings.size() - 1; i >= ratings.size() - 10; i--) {
			// Rating r = ratings.get(i);
			// if (r.getRating() > 0)
			// topten.add(movies.get(r.getMovieId()));
			// }

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
			// if(this.movies.containsKey(nextR.getMovieId())){
			// System.out.println("flaw");
			// }
			addRating(nextR);
		}

		System.out.println("Completed, " + this.users.size() + " users, " + this.movies.size() + " movies, "
				+ this.ratings.size() + "/" + this.ratingsDB.size() + " ratings");
	}

	private void sortRatings() {
		Collections.sort(ratings);
		ratingsSorted = true;
	}

	@SuppressWarnings("rawtypes")
	private void addSort(ArrayList<Long> ids, HashMap<Long, ? extends Comparable> map) {
		for (int i = ids.size() - 1; i > 0; i--) {
			long curId = ids.get(i);
			long prevId = ids.get(i - 1);
			Comparable cur = map.get(curId);
			Comparable prev = map.get(prevId);
			if (less(cur, prev)) {
				Collections.swap(ids, i, i - 1);
				// System.out.println(userIdList);
			} else
				break;
		}
	}

	// private void sort(ArrayList<Long> ids, HashMap<Long, ? extends
	// Comparable> map) {
	// for (int i = 1; i < ids.size(); i++) {
	// for (int j = i; j > 0; j--) {
	// long curId = ids.get(j);
	// long prevId = ids.get(j - 1);
	// Comparable cur = map.get(curId);
	// Comparable prev = map.get(prevId);
	// if (less(cur, prev)) {
	// Collections.swap(ids, j, j - 1);
	// // System.out.println(userIdList);
	// } else
	// break;
	// }
	// }
	// }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean less(Comparable p, Comparable q) {
		return p.compareTo(q) < 0;
	}

	public ArrayList<Movie> recommend(long userId) {
		ArrayList<Movie> recommendedMovies = new ArrayList<>();

		HashSet<Long> similarUsersId = new HashSet<>();
		User user = users.get(userId);
		HashSet<String> favoriteGenres = Matrix.getAllContents(movies, user.getPositiveRatedMovieIds());

		// Find users that also like the same movies
		HashMap<Long, Integer> rated = user.getRatings();
		Iterator<Long> ite = rated.keySet().iterator();
		while (ite.hasNext()) {
			long movieId = ite.next();
			int point = rated.get(movieId);
			if (point > 0) {
				HashMap<Integer, HashSet<Long>> raters = movies.get(movieId).getPositiveRaters();
				HashSet<Long> similarRaters = raters.get(point);
				similarUsersId.addAll(similarRaters);
			}
			similarUsersId.remove(userId); // make sure the user looking for
											// recommendation isn't in this set
		}

		if (similarUsersId.size() > 0) {
			User mostSimilar = null;
			double min = Double.MAX_VALUE * (-1);
			Iterator<Long> iteSim = similarUsersId.iterator();
			while (iteSim.hasNext()) {
				User other = users.get(iteSim.next());
				double similarity = Matrix.similarityInRadian(movieIdList, rated, other.getRatings());
				if (similarity > min) {
					min = similarity;
					mostSimilar = other;
				}
			}
			System.out.println("Recommendation is retrieved from " + mostSimilar.getFirstName() + " "
					+ mostSimilar.getLastName() + " (User ID: " + mostSimilar.getUserId() + "), similarity: " + min);

			Iterator<Long> ids = mostSimilar.getPositiveRatedMovieIds().iterator();
			while (ids.hasNext()) {
				long nextId = ids.next();
				if (!rated.containsKey(nextId)) { // only take movies that user
													// hasn't seen yet into
													// account
					Movie m = movies.get(nextId);
					// Content-based filter
					HashSet<String> movieGenre = m.getIndivGenre();
					boolean flag = true;
					Iterator<String> genreIte = movieGenre.iterator();
					while (genreIte.hasNext()) {
						if (!favoriteGenres.contains(genreIte.next())) {
							flag = false;
							break;
						}
					}
					if (flag) {
						recommendedMovies.add(movies.get(nextId));
					}
				}
			}
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
		ratingsDB = (HashBasedTable<Long, Long, Rating>) serializer.pop();
		System.out.println("Completed, " + watch.elapsedTime() + " seconds");
	}

	public void store() throws Exception {
		Stopwatch watch = new Stopwatch();
		System.out.println("Saving to datastore...");
		serializer.push(ratingsDB);
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

	public String info() {
		return "Users: " + users.size() + "$ Movies: " + movies.size() + "$ Ratings: " + ratings.size();
	}
}
