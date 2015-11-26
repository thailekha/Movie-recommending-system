package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import models.Movie;
import models.User;

public class Recommender {

	private HashMap<Long,User> users = new HashMap<>();
	private HashMap<Long,Movie> movies = new HashMap<>();

	public void addUser(String firstName, String lastName, String age, String gender, String occupation, String zip) {
		// TODO Auto-generated method stub
		User u = new User(firstName, lastName, age, gender, occupation, zip);
		users.put(u.getUserId(),u);
	}
	
	public HashMap<Long,User> getUsers() {
		return users;
	}

	public void addMovie(String title, String releaseDate, String url, String genreCode) {
		// TODO Auto-generated method stub
		Movie m = new Movie(title,releaseDate,url,genreCode);
		movies.put(m.getMovieId(), m);
	}
}
