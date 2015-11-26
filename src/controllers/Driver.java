package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import edu.princeton.cs.introcs.In;
import models.User;

public class Driver {

	private Recommender recommender;

	public Driver() throws Exception {
		recommender = new Recommender();
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
			@Param(name = "age") String age, @Param(name = "gender") String gender,
			@Param(name = "occupation") String occupation, @Param(name = "zip") String zip) {
		recommender.addUser(firstName, lastName, age, gender, occupation, zip);
	}

	@Command(description = "Add a new Movie")
	public void createMovie(@Param(name = "Movie title") String title, @Param(name = "Movie release date") String releaseDate,
			@Param(name = "Movie url") String url, @Param(name = "Genre code") String genreCode) {
		recommender.addMovie(title,releaseDate,url,genreCode);
	}

	@Command(description = "Get all users details")
	public void getUsers() {
		Iterator<User> users = recommender.getUsers().values().iterator();
		while(users.hasNext()) {
			System.out.println(users.next().toString());
		}
	}

//	public void read(int tokenLength) throws Exception {
//		File usersFile = new File("small_data/genre.dat");
//		In inUsers = new In(usersFile);
//		// each field is separated(delimited) by a '|'
//		String delims = "[|]";
//		while (!inUsers.isEmpty()) {
//			// get user and rating from data source
//			String userDetails = inUsers.readLine();
//			System.out.println(userDetails);
//
//			// parse user details string
//			String[] userTokens = userDetails.split(delims);
//
//			// output user data to console.
//			if (userTokens.length == tokenLength) {
//				// System.out.println("UserID: "+userTokens[0]+",First Name:"+
//				// userTokens[1]+",Surname:" + userTokens[2]+",Age:"+
//				// Integer.parseInt(userTokens[3])+",Gender:"+userTokens[4]+",Occupation:"+
//				// userTokens[5]);
//				for (int i = 0; i < tokenLength; i++)
//					System.out.println(userTokens[i]);
//
//			} else {
//				throw new Exception("Invalid member length: " + userTokens.length);
//			}
//		}
//	}

	// Object castcader(String x,String type){
	// switch(type) {
	// case "int": return (int) x;
	// }
	// return null;
	// }
}
