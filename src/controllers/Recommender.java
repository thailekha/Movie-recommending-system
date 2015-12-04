package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.HashBasedTable;

import models.Movie;
import models.Rating;
import models.User;

public class Recommender {

	private HashMap<Long, User> users = new HashMap<>();
	private HashMap<Long, Movie> movies = new HashMap<>();
	private ArrayList<Long> userIdList = new ArrayList<>();
	private ArrayList<Long> movieIdList = new ArrayList<>();
	private ArrayList<Rating> ratings = new ArrayList<>();
	private boolean ratingsSorted = false;
	private HashBasedTable<Long, Long, Rating> ratingsDB = HashBasedTable.create(); // userid,movieid,rating

	public void addUser(String firstName, String lastName, int age, String gender, String occupation, String zip)
			throws Exception {
		// TODO Auto-generated method stub
		User u = new User(firstName, lastName, age, gender, occupation, zip);
		if (!users.containsValue(u)) {
			putUser(u);
		}
	}

	public void addUser(User user) {
		if (!(users.containsKey(user.getUserId()) && users.containsValue(user))) {
			putUser(user);
		}
	}

	private void putUser(User user) {
		users.put(user.getUserId(), user);
		userIdList.add(user.getUserId());
		addSort(userIdList, users);
	}

	public HashMap<Long, User> getUsers() {
		return users;
	}

	public ArrayList<Long> getUserIdList() {
		return userIdList;
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

	public void addMovie(Movie movie) {
		if (!(movies.containsKey(movie.getMovieId()) && movies.containsValue(movie))) {
			putMovie(movie);
		}
	}

	private void putMovie(Movie movie) {
		movies.put(movie.getMovieId(), movie);
		movieIdList.add(movie.getMovieId());
		addSort(movieIdList, movies);
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
		return search(users, userIdList, new User(firstName, lastName, age));
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<Comparable> searchMovie(String title) throws Exception {
		return search(movies, movieIdList, new Movie(title));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList<Comparable> search(HashMap<Long, ? extends Comparable> map, ArrayList<Long> ids,
			Comparable query) {
		ArrayList<Comparable> results = new ArrayList<>();

		if (map.size() > 0) {
			// long id = -1;
			boolean found = false;
			int foundPos = -1;
			int lo = 0;
			int hi = ids.size() - 1;
			while (lo <= hi) {
				int mid = (lo + hi) / 2;
				long midId = ids.get(mid);
				Comparable midObject = map.get(midId);
				int result = midObject.compareTo(query);
				if (result > 0)
					hi = mid - 1;
				else if (result < 0)
					lo = mid + 1;
				else {
					foundPos = mid;
					found = true;
					break;
				}
			}

			// System.out.println("Got here");

			// ripple
			if (found) {
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
		}
		return results;
	}

	public void addRating(long userId, long movieId, int rating) throws Exception {
		if (users.containsKey(userId) && movies.containsKey(movieId)) {
			User user = users.get(userId);
			Rating r = new Rating(userId, movieId, rating);
			Rating put = ratingsDB.put(userId, movieId, r); // the previous
															// value, if
															// replaced
			if (put != null) {
				while (ratings.contains(put))
					ratings.remove(put);
				user.removeRating(put);
			}
			ratings.add(r);
			user.addRating(r);
			ratingsSorted = false;
		}
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

	public HashSet<Movie> getTopTenMovies() {
		HashSet<Movie> topten = new HashSet<>();
		if (movies.size() < 10) {
			if (!ratingsSorted) {
				sortRatings();
			}
			for(int i = ratings.size() - 1; i >= 0; i--) {
				Rating r = ratings.get(i);
				if(r.getRating() > 0)
					topten.add(movies.get(r.getMovieId()));
			}
		}
		return topten;
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

//	private void sort(ArrayList<Long> ids, HashMap<Long, ? extends Comparable> map) {
//		for (int i = 1; i < ids.size(); i++) {
//			for (int j = i; j > 0; j--) {
//				long curId = ids.get(j);
//				long prevId = ids.get(j - 1);
//				Comparable cur = map.get(curId);
//				Comparable prev = map.get(prevId);
//				if (less(cur, prev)) {
//					Collections.swap(ids, j, j - 1);
//					// System.out.println(userIdList);
//				} else
//					break;
//			}
//		}
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean less(Comparable p, Comparable q) {
		return p.compareTo(q) < 0;
	}
}
