package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.google.common.base.Objects;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import edu.princeton.cs.introcs.In;
import models.Fixtures;
import models.Movie;
import models.Rating;
import models.User;
import utils.CSVLoader;
import utils.JSONSerializer;

public class Driver {

	private Recommender recommender;

	public Driver() throws Exception {
//		new CSVLoader("data_movieLens/users.dat", "data_movieLens/newItems.dat",
//				"data_movieLens/ratings.dat")
//		new CSVLoader("small_data/users5.dat", "small_data/items5.dat",
//				"small_data/ratings5.dat")
		File datastore = new File("datastore/store.json");
		recommender = new Recommender(new JSONSerializer(datastore),new CSVLoader("data_movieLens/users.dat", "data_movieLens/newItems.dat",
				"data_movieLens/ratings.dat"));
	}

	public static void main(String[] agrs) throws Exception {
		Driver main = new Driver();
		Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to pacemaker-console - ?help for instructions",
				main);
		shell.commandLoop();
	}

	@Command(description = "Add a new User")
	public void createUser(@Param(name = "first name") String firstName, @Param(name = "last name") String lastName,
			@Param(name = "age") int age, @Param(name = "gender") String gender,
			@Param(name = "occupation") String occupation, @Param(name = "zip") String zip) throws Exception {
		recommender.addUser(firstName, lastName, age, gender, occupation, zip);
	}

	@Command(description = "Add a new Movie")
	public void createMovie(@Param(name = "Movie title") String title,
			@Param(name = "Movie release date") String releaseDate, @Param(name = "Movie url") String url,
			@Param(name = "Genre code") String genreCode) throws Exception {
		recommender.addMovie(title, releaseDate, url, genreCode);
	}

	@Command(description = "Get all movies details")
	public void getMovies() {
		Iterator<Movie> movies = recommender.getMovies().values().iterator();
		while (movies.hasNext()) {
			System.out.println(movies.next().info());
		}
		// ArrayList<Long> ids = recommender.getUserIdList();
		// for(Long id: ids) {
		// System.out.println(recommender.getUser(id));
		// }
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
		// Iterator<User> users = recommender.getUsers().values().iterator();
		// while (users.hasNext()) {
		// System.out.println(users.next().toString());
		// }
		ArrayList<Long> ids = recommender.getUserIdList();
		for (Long id : ids) {
			System.out.println(recommender.getUser(id).info());
		}
	}

	@Command(description = "Look up user(s)")
	public void userLookup(@Param(name = "first name") String firstName, @Param(name = "last name") String lastName,
			@Param(name = "age") int age) throws Exception {
		ArrayList<Comparable> found = recommender.searchUser(firstName, lastName, age);
		if (found.size() == 0) {
			System.out.println("Not found");
		} else {
			for (Comparable item : found) {
				User u = (User) item;
				System.out.println(u.info());
			}
		}
	}

	@Command(description = "Look up movie(s)")
	public void movieLookup(@Param(name = "movive title") String title) throws Exception {
		ArrayList<Comparable> found = recommender.searchMovie(title);
		if (found.size() == 0) {
			System.out.println("Not found");
		} else {
			for (Comparable item : found) {
				Movie m = (Movie) item;
				System.out.println(m.info());
				//System.out.println(item.hashCode());
			}
		}
	}

	@Command(description = "Remove a user")
	public void removeUser(@Param(name = "User ID") Long id) {
		User removed = recommender.removeUser(id);
		if (removed == null)
			System.out.println("User not existed");
		else
			System.out.println("Deleted --> " + removed);
	}

	@Command(description = "Rate a movie")
	public void rateMovie(@Param(name = "User ID") Long userId, @Param(name = "Movie ID") Long movieId,
			@Param(name = "Rating") int rating) throws Exception {
		recommender.addRating(userId, movieId, rating);
	}

	@Command(description = "Get ratings")
	public void getRatings() {
		ArrayList<Rating> ratings = recommender.getRatings();
		for (Rating r : ratings)
			System.out.println(r);
	}

	@Command(description = "Get top ten movies")
	public void getTopten() {
		List<Movie> topten = recommender.getTopTenMovies();
		if (topten.size() == 0) {
			System.out.println("Unavailable");
			return;
		}

		if (topten.size() < 10)
			System.out.println("There are less than 10 movies that are top\nTop movies:");
		else
			System.out.println("Top ten movies:");

		for(Movie m: topten)
			System.out.println("~~> " + m.info());
		System.out.println(topten.size());
	}

	@Command(description = "prime")
	public void prime() throws Exception {
		recommender.prime();
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
	public void systemInfo() throws Exception {
		System.out.println(recommender.info());
	}
}
