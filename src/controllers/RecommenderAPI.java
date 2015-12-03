package controllers;
import models.Movie;
import models.Rating;

public interface RecommenderAPI {
	void addUser(String firstName, String lastName, int age, String gender, String occupation,String zip);

	void removeUser(long userID);

	void addMovie(String title, int year, String url);

	void addRating(long userID, long movieID, int rating);

	Movie getMovie(long movieID);

	Rating getUserRatings(int userID);

	void getUserRecommendations(int userID);

	void getTopTenMovies();

	void load();

	void write();
}
