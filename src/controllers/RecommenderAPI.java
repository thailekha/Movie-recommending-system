package controllers;
import models.Movie;
import models.Rating;

public interface RecommenderAPI {
	void addUser(String firstName, String lastName, int age, String gender, String occupation,String zip);

	void removeUser(int userID);

	void addMovie(String title, int year, String url);

	void addRating(int userID, int movieID, int rating);

	Movie getMovie(int movieID);

	Rating getUserRatings(int userID);

	void getUserRecommendations(int userID);

	void getTopTenMovies();

	void load();

	void write();
}
