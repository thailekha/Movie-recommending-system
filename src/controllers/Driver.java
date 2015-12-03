package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import edu.princeton.cs.introcs.In;
import models.Fixtures;
import models.Movie;
import models.User;

public class Driver {

	private Recommender recommender;

	public Driver() throws Exception {
		recommender = new Recommender();
		User[] users = Fixtures.getSimilarUsers();

		for (int i = 0; i < users.length; i++) {
			recommender.addUser(users[i]);
		}

		Movie[] movies = Fixtures.getMovies();
		for (int i = 0; i < movies.length; i++) {
			recommender.addMovie(movies[i]);
		}
	}

	public static void main(String[] agrs) throws Exception {
		// new Driver().read(1);
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
			System.out.println(movies.next().toString());
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
			System.out.println(m);
	}

	@Command(description = "Get all users details")
	public void getUsers() {
		// Iterator<User> users = recommender.getUsers().values().iterator();
		// while (users.hasNext()) {
		// System.out.println(users.next().toString());
		// }
		ArrayList<Long> ids = recommender.getUserIdList();
		for (Long id : ids) {
			System.out.println(recommender.getUser(id));
		}
	}

	@Command(description = "Look up user(s)")
	public void userLookup(@Param(name = "first name") String firstName, @Param(name = "last name") String lastName,
			@Param(name = "age") int age) throws Exception {
		ArrayList<Comparable> found = recommender.searchUser(firstName, lastName,age);
		if (found.size() == 0) {
			System.out.println("Not found");
		} else {
			for (Comparable item : found) {
				System.out.println(item);
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
				System.out.println(item);
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

	// public void read(int tokenLength) throws Exception {
	// File usersFile = new File("small_data/genre.dat");
	// In inUsers = new In(usersFile);
	// // each field is separated(delimited) by a '|'
	// String delims = "[|]";
	// while (!inUsers.isEmpty()) {
	// // get user and rating from data source
	// String userDetails = inUsers.readLine();
	// System.out.println(userDetails);
	//
	// // parse user details string
	// String[] userTokens = userDetails.split(delims);
	//
	// // output user data to console.
	// if (userTokens.length == tokenLength) {
	// // System.out.println("UserID: "+userTokens[0]+",First Name:"+
	// // userTokens[1]+",Surname:" + userTokens[2]+",Age:"+
	// //
	// Integer.parseInt(userTokens[3])+",Gender:"+userTokens[4]+",Occupation:"+
	// // userTokens[5]);
	// for (int i = 0; i < tokenLength; i++)
	// System.out.println(userTokens[i]);
	//
	// } else {
	// throw new Exception("Invalid member length: " + userTokens.length);
	// }
	// }
	// }

	// Object castcader(String x,String type){
	// switch(type) {
	// case "int": return (int) x;
	// }
	// return null;
	// }
}
