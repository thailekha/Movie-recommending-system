package utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import com.google.common.collect.HashBiMap;

import models.Movie;
import models.Rating;
import models.User;

public class CSVLoader {
	private HashMap<Long,Long> userIdTranlator = new HashMap<>();
	private HashMap<Long,Long> movieIdTranlator = new HashMap<>();
	private String users, movies, ratings, genres;

	public CSVLoader(String users, String movies, String ratings, String genres) {
		this.users = users;
		this.movies = movies;
		this.ratings = ratings;
		this.genres = genres;
	}

	public HashMap<Long,Long> getUserIdTranlator() {
		return userIdTranlator;
	}
	
	public HashMap<Long,Long> getMovieIdTranlator() {
		return movieIdTranlator;
	}
	
	public HashMap<Integer,String> loadGenres() throws Exception {
		File file = new File(genres);
		In ins = new In(file);
		try {
			String delims = "[|]";
			HashMap<Integer,String> result = new HashMap<>();
			while (!ins.isEmpty()) {
				String details = ins.readLine();
				String[] tokens = details.split(delims);
				if (tokens.length == 2) {
					String genre = tokens[0].toLowerCase();
					int id = Integer.parseInt(tokens[1]);
					result.put(id, genre);
				} else {
					throw new Exception("Invalid member length: " + tokens.length);
				}
			}
			return result;
		} finally {
			ins.close();
		}
	}
	
	public HashSet<User> loadUsers() throws Exception {
		File file = new File(users);
		In ins = new In(file);
		try {
			String delims = "[|]";
			HashSet<User> result = new HashSet<>();
			while (!ins.isEmpty()) {
				String details = ins.readLine();
				String[] tokens = details.split(delims);
				if (tokens.length == 7) {
					long id = Long.parseLong(tokens[0]);
					String firstName = tokens[1];
					String lastName = tokens[2];
					int age = Integer.parseInt(tokens[3]);
					String gender = tokens[4];
					String occupation = tokens[5];
					String zip = tokens[6];
					User toAdd = new User(firstName, lastName, age, gender, occupation, zip);
					Long translation = userIdTranlator.put(id, toAdd.getUserId());
					if(translation != null)
						throw new Exception("Duplicate ids detected in <" + users + ">");
					result.add(toAdd);
				} else {
					throw new Exception("Invalid member length: " + tokens.length);
				}
			}
			return result;
		} finally {
			ins.close();
		}
	}

	public HashSet<Movie> loadMovies() throws Exception {
		File file = new File(movies);
		In ins = new In(file);

		try {
			String delims = "[|]";
			HashSet<Movie> result = new HashSet<>();
			while (!ins.isEmpty()) {
				String details = ins.readLine();
				String[] tokens = details.split(delims);
				if (tokens.length == 23) {
					long id = Long.parseLong(tokens[0]);
					String title = tokens[1];
					String releaseDate = tokens[2];
					String url = tokens[3];
					String genreCode = "";
					for (int i = 4; i < 23; i++)
						genreCode += tokens[i];
					Movie toAdd = new Movie(title, releaseDate, url, genreCode);
					Long translation = movieIdTranlator.put(id, toAdd.getMovieId());
					if(translation != null)
						throw new Exception("Duplicate ids detected in <" + movies + ">");
					result.add(toAdd);
				} else {
					throw new Exception("Invalid member length: " + tokens.length);
				}
			}

			ins.close();
			return result;
		} finally {
			ins.close();
		}
	}

	public HashSet<Rating> loadRatings() throws Exception {
		File file = new File(ratings);
		In ins = new In(file);
		try {
			String delims = "[|]";
			HashSet<Rating> result = new HashSet<>();
			while (!ins.isEmpty()) {
				String details = ins.readLine();
				String[] tokens = details.split(delims);
				if (tokens.length == 4) {
					long userId = Long.parseLong(tokens[0]);
					long movieId = Long.parseLong(tokens[1]);
					int rating = Integer.parseInt(tokens[2]);
					long timestamp = Long.parseLong(tokens[3]);
					
					Long translatedUserId = userIdTranlator.get(userId);
					Long translatedMovieId = movieIdTranlator.get(movieId);
					if(translatedUserId == null || translatedMovieId == null) {
						System.out.println("null");
					}
					result.add(new Rating(translatedUserId, translatedMovieId, rating, timestamp));
				} else {
					throw new Exception("Invalid member length: " + tokens.length);
				}
			}
			return result;
		} finally {
			ins.close();
		}
	}
}
