package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.google.common.base.Objects;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Stopwatch;
import models.Fixtures;
import models.Movie;
import models.Query;
import models.Rating;
import models.User;
import utils.CSVLoader;
import utils.JSONSerializer;

public class Driver {

	private Recommender recommender;
	private boolean primeAble = true;

	public Driver() throws Exception {
		String bigU = "data_movieLens/users.dat";
		String bigM = "data_movieLens/newItems.dat";
		String bigR = "data_movieLens/ratings.dat";
		String genre = "data_movieLens/genre.dat";
		String smallU = "small_data/users5.dat";
		String smallM = "small_data/items5.dat";
		String smallR = "small_data/ratings5.dat";
		File datastore = new File("datastore/store.json");
		int choice = choose();
		recommender = choice == 1
				? new Recommender(new JSONSerializer(datastore), new CSVLoader(bigU, bigM, bigR, genre))
				: new Recommender(new JSONSerializer(datastore), new CSVLoader(smallU, smallM, smallR, genre));
	}

	private int choose() {
		Scanner s = new Scanner(System.in);
		System.out.println("Big or small CSV data?\n1)Big\n2)Small\n==>");
		int choice = s.nextInt();
		while (choice != 1 && choice != 2) {
			System.out.print("Not available, try again ==>");
			choice = s.nextInt();
		}
		return choice;
	}

	public static void main(String[] agrs) throws Exception {
		Driver main = new Driver();
		Shell shell = ShellFactory.createConsoleShell("pm", "Welcome - ?help for instructions", main);
		shell.commandLoop();
	}

	@Command(description = "Add a new User")
	public void createUser(@Param(name = "first name") String firstName, @Param(name = "last name") String lastName,
			@Param(name = "age") int age, @Param(name = "gender") String gender,
			@Param(name = "occupation") String occupation, @Param(name = "zip") String zip) {
		try {
			recommender.addUser(firstName, lastName, age, gender, occupation, zip);
		} catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e.getMessage());
		}
	}

	@Command(description = "Add a new Movie")
	public void createMovie(@Param(name = "Movie title") String title,
			@Param(name = "Movie release date") String releaseDate, @Param(name = "Movie url") String url,
			@Param(name = "Genre code") String genreCode) {
		try {
			recommender.addMovie(title, releaseDate, url, genreCode);
		} catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e.getMessage());
		}
	}

	@Command(description = "Get all movies details")
	public void getMovies() {
		HashMap<Long, Movie> movies = recommender.getMovies();
		ArrayList<Long> sequence = recommender.getMovieIdList();
		for (long id : sequence) {
			System.out.println(movies.get(id).info());
		}
	}

	@Command(description = "Get a movie detals")
	public void getMovie(@Param(name = "Movie ID") Long id) {
		Movie m = recommender.getMovie(id);
		if (m == null)
			System.out.println("Movie not found");
		else
			System.out.println(m.info());
	}

	@Command(description = "Get all users details")
	public void getUsers() {
		ArrayList<Long> ids = recommender.getUserIdList();
		for (Long id : ids) {
			System.out.println(recommender.getUser(id).info());
		}
	}

	@Command(description = "Get user ratings")
	public void printUserRatings(@Param(name = "User ID") long userId) {
		System.out.println(recommender.printUserRatings(userId));
	}

	@Command(description = "Get user details")
	public void getUserDetails(@Param(name = "User ID") long userId) {
		System.out.println(recommender.getUser(userId).info());
	}

	@Command(description = "Look up user(s)")
	public void userLookup(@Param(name = "first name") String firstName, @Param(name = "last name") String lastName,
			@Param(name = "age") int age) {
		try {
			ArrayList<Query> found = recommender.searchUser(firstName, lastName, age);
			if (found == null || found.size() == 0) {
				System.out.println("Not found");
			} else {
				System.out.println("Found user(s):");
				for (Query item : found) {
					User u = (User) item;
					System.out.println(u.info());
				}
			}
		} catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e.getMessage());
		}

	}

	@Command(description = "Look up movie(s)")
	public void movieLookup(@Param(name = "movive title") String title) {
		try {
			ArrayList<Query> found = recommender.searchMovie(title);
			if (found == null || found.size() == 0) {
				System.out.println("Not found");
			} else {
				System.out.println("Found movie(s):");
				for (Query item : found) {
					Movie m = (Movie) item;
					System.out.println(m.info());
				}
			}
		} catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e.getMessage());
		}

	}

	@Command(description = "Remove a user")
	public void removeUser(@Param(name = "User ID") Long id) {
		User removed = recommender.removeUser(id);
		if (removed == null)
			System.out.println("User not existed");
		else
			System.out.println("Deleted --> " + removed.info());
	}

	@Command(description = "Rate a movie")
	public void rateMovie(@Param(name = "User ID") Long userId, @Param(name = "Movie ID") Long movieId,
			@Param(name = "Rating") int rating) {
		try {
			recommender.addRating(userId, movieId, rating);
		} catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e.getMessage());
		}
	}

	// @Command(description = "Get ratings")
	// public void getRatings() {
	// ArrayList<Rating> ratings = recommender.getRatings();
	// for (Rating r : ratings)
	// System.out.println(r);
	// }

	@Command(description = "Get top ten movies")
	public void getTopten() {
		List<Movie> topten = recommender.getTopTenMovies();
		if (topten.size() == 0) {
			System.out.println("Unavailable");
			return;
		}

		if (topten.size() < 10)
			System.out.println("Top movies:");
		else
			System.out.println("Top ten movies:");

		for (Movie m : topten)
			System.out.println("~~> " + m.info());
		System.out.println(topten.size());
	}

	@Command(description = "prime")
	public void prime() {
		try {
			if (primeAble) {
				Stopwatch watch = new Stopwatch();
				recommender.prime();
				System.out.println(watch.elapsedTime() + " seconds");
				primeAble = false;
			} else {
				System.out.println("Error, you can only prime once");
			}
		} catch (Exception e) {
			System.out.println("Error.");
			System.out.println(e.getMessage());
		}
	}

	@Command(description = "Load")
	public void load() throws Exception {
		recommender.load();
	}

	@Command(description = "Store")
	public void store() throws Exception {
		recommender.store();
	}

	@Command(description = "System Info")
	public void systemInfo() {
		System.out.println(recommender.info());
	}

	@Command(description = "Get recommendations for a user")
	public void getUserRecommendations(@Param(name = "User ID") long userID) {
		ArrayList<Movie> movies = recommender.recommend(userID, false);
		if (movies == null)
			System.out.println("User not found");
		else {
			if (movies.size() == 0) {
				getTopten(); //hasn't rated anything
			} else {
				System.out.println(movies.size() + " recommended movies");
				for (Movie movie : movies) {
					double roundedPoint = ((int) movie.getAveragePoint() * 10) / 10;
					System.out.println("~> " + movie.getTitle() + ", genres: " + movie.printGenre()
							+ ", average point: " + roundedPoint + ", Movie ID: " + movie.getMovieId());
				}
			}
		}
	}

	@Command(description = "Get recommendations for a user basing on his/her most recently "
			+ "positive rating, implying that the user is in the mood to watch such genres from that rating")
	public void quickRecommendations(@Param(name = "User ID") long userID) {
		ArrayList<Movie> movies = recommender.recommend(userID, true);
		if (movies == null)
			System.out.println("User not found");
		else {
			if (movies.size() == 0) {
				getTopten(); //hasn't rated anything
			} else {
				System.out.println(movies.size() + " recommended movies");
				for (Movie movie : movies) {
					double roundedPoint = ((int) movie.getAveragePoint() * 10) / 10;
					System.out.println("~> " + movie.getTitle() + ", genres: " + movie.printGenre()
							+ ", average point: " + roundedPoint + ", Movie ID: " + movie.getMovieId());
				}
			}
		}
	}
}
