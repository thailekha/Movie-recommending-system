package models;

import utils.Valid;

public class Movie {
	private static long counter = 1;
	private long movieId;
	private String title, releaseDate, url;
	//String genreCode;
	
	public Movie(String title,String releaseDate,String url) {
		movieId = counter++;
		this.title = Valid.str(title,"default title");
		this.releaseDate = Valid.str(releaseDate, "default date"); 
		this.url = Valid.str(url,"default url");
	}
	
	public long getMovieId() {
		return movieId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getReleaseDate() {
		return releaseDate;
	}
	
	public String getUrl() {
		return url;
	}
	
	public static void resetCounter() {
		counter = 1;
	}
}
