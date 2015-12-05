package utils;

import java.io.File;
import java.util.HashSet;

import models.Movie;
import models.Rating;
import models.User;

public class CSVLoader {
	public static HashSet<User> loadUsers(String filename) throws Exception {
		File file = new File(filename);
		In ins = new In(file);
		try {
			String delims = "[|]";
			HashSet<User> result = new HashSet<>();
			while (!ins.isEmpty()) {
				String details = ins.readLine();
				String[] tokens = details.split(delims);
				if (tokens.length == 7) {
					String id = tokens[0];
					String firstName = tokens[1];
					String lastName = tokens[2];
					int age = Integer.parseInt(tokens[3]);
					String gender = tokens[4];
					String occupation = tokens[5];
					String zip = tokens[6];
					result.add(new User(firstName, lastName, age, gender, occupation, zip));
				} else {
					throw new Exception("Invalid member length: " + tokens.length);
				}
			}
			return result;
		} finally {
			ins.close();
		}
	}

	public static HashSet<Movie> loadMovies(String filename) throws Exception {
		File file = new File(filename);
		In ins = new In(file);

		try {
			String delims = "[|]";
			HashSet<Movie> result = new HashSet<>();
			while (!ins.isEmpty()) {
				String details = ins.readLine();
				String[] tokens = details.split(delims);
				if (tokens.length == 23) {
					String id = tokens[0];
					String title = tokens[1];
					String releaseDate = tokens[2];
					String url = tokens[3];
					String genreCode = "";
					for (int i = 4; i < 23; i++)
						genreCode += tokens[i];
					result.add(new Movie(title, releaseDate, url, genreCode));
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

	public static HashSet<Rating> loadRatings(String filename) throws Exception {
		File file = new File(filename);
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
					result.add(new Rating(userId, movieId, rating, timestamp));
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
