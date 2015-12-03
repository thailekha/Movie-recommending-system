package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import models.Movie;
import models.User;

public class Recommender {

	private HashMap<Long, User> users = new HashMap<>();
	private HashMap<Long, Movie> movies = new HashMap<>();
	private ArrayList<Long> userIdList = new ArrayList<>();

	public void addUser(String firstName, String lastName, String age, String gender, String occupation, String zip) {
		// TODO Auto-generated method stub
		User u = new User(firstName, lastName, age, gender, occupation, zip);
		if (!users.containsValue(u)) {
			users.put(u.getUserId(), u);
			userIdList.add(u.getUserId());
			addSort(userIdList);
		}
	}

	public void addUser(User user) {
		if (!(users.containsKey(user.getUserId()) && users.containsValue(user))) {
			users.put(user.getUserId(), user);
			userIdList.add(user.getUserId());
			addSort(userIdList);
		}
	}

	public HashMap<Long, User> getUsers() {
		return users;
	}

	public ArrayList<Long> getUserIdList() {
		return userIdList;
	}

	public int getUsersSize() {
		return users.size();
	}

	public void addMovie(String title, String releaseDate, String url, String genreCode) {
		// TODO Auto-generated method stub
		Movie m = new Movie(title, releaseDate, url, genreCode);
		if (!movies.containsValue(m)) {
			movies.put(m.getMovieId(), m);
		}
	}

	public void addMovie(Movie movie) {
		if (!(movies.containsKey(movie.getMovieId()) && movies.containsValue(movie))) {
			movies.put(movie.getMovieId(), movie);
		}
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

	public ArrayList<User> searchUser(String firstName, String lastName) {
		ArrayList<User> results = new ArrayList<>();

		if (users.size() > 0) {
			// long id = -1;
			boolean found = false;
			int foundPos = -1;
			int lo = 0;
			int hi = userIdList.size();
			while (lo <= hi) {
				int mid = (lo + hi) / 2;
				long midId = userIdList.get(mid);
				User midUser = users.get(midId);
				int result = midUser.compareTo(firstName, lastName);
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

			System.out.println("Got here");

			// ripple
			if (found) {
				User thisU = users.get(userIdList.get(foundPos));
				results.add(thisU);
				// left
				for (int i = foundPos - 1; i >= 0; i--) {
					long userId = userIdList.get(i);
					User thatU = users.get(userId);
					if (thisU.compareTo(thatU) == 0)
						results.add(thatU);
					else
						break;
				}

				// right
				for (int i = foundPos + 1; i < userIdList.size(); i++) {
					long userId = userIdList.get(i);
					User thatU = users.get(userId);
					if (thisU.compareTo(thatU) == 0)
						results.add(thatU);
					else
						break;
				}
			}
		}
		return results;
	}

	public Movie getMovie(Long id) {
		return movies.get(id);
	}
	
	public HashMap<Long, Movie> getMovies() {
		return movies;
	}
	
	void addSort(ArrayList<Long> ids) {
		for (int i = userIdList.size() - 1; i > 0; i--) {
			long curId = userIdList.get(i);
			long prevId = userIdList.get(i - 1);
			Comparable cur = users.get(curId);
			Comparable prev = users.get(prevId);
			if (less(cur, prev)) {
				Collections.swap(userIdList, i, i - 1);
				// System.out.println(userIdList);
			} else
				break;
		}
	}

	void sort(ArrayList<Long> ids) {
		for (int i = 1; i < userIdList.size(); i++) {
			for (int j = i; j > 0; j--) {
				long curId = userIdList.get(j);
				long prevId = userIdList.get(j - 1);
				Comparable cur = users.get(curId);
				Comparable prev = users.get(prevId);
				if (less(cur, prev)) {
					Collections.swap(userIdList, j, j - 1);
					// System.out.println(userIdList);
				} else
					break;
			}
		}
	}

	boolean less(Comparable p, Comparable q) {
		return p.compareTo(q) < 0;
	}
}
