package controllers;

import java.util.ArrayList;
import java.util.List;

import models.User;

public class Recommender {

	private List<User> users = new ArrayList<>();

	public void addUser(String firstName, String lastName, String age, String gender, String occupation, String zip) {
		// TODO Auto-generated method stub
		users.add(new User(firstName, lastName, age, gender, occupation, zip));
	}
	
	public List<User> getUsers() {
		return users;
	}
}
