package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import utils.ToJsonString;
import utils.Valid;

//TODO: NAMES IN LOWER CASE ? / OCCUPATION
public class User implements Comparable<User> {
	// Each user has rated at least 20 movies
	private static long counter = 1;
	private static HashSet<String> occupations = Sets.newHashSet("administrator", "artist", "doctor", "educator",
			"engineer", "entertainment", "executive", "healthcare", "homemaker", "lawyer", "librarian", "marketing",
			"none", "other", "programmer", "retired", "salesman", "scientist", "student", "technician", "writer");
	private long userId;
	private String firstName, lastName, gender, occupation, zip;
	private int age;
	
	//Map movie and rating point
	private HashMap<Long,Integer> ratings = new HashMap<>(); //quicker to check duplicate ratings
	//private ArrayList<Rating> ratings = new ArrayList<>(); 

	public User(String firstName, String lastName, int age, String gender, String occupation, String zip)
			throws Exception {
		if (str(firstName, 200) && str(lastName, 200) && str(gender, 1) && occupations.contains(occupation)
				&& str(zip, 200) && age > 0 && age < 100) {
			this.userId = counter++;
			this.firstName = firstName.trim();
			this.lastName = lastName.trim();
			this.age = age;
			this.gender = gender.trim();
			this.occupation = occupation.trim();
			this.zip = zip.trim();
			// System.out.println(this);
		} else {
			throw new Exception();
		}
	}

	//For searching query
	public User(String firstName, String lastName, int age) throws Exception {
		if (str(firstName, 200) && str(lastName, 200) && age > 0 && age < 100) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
		}
		else {
			throw new Exception();
		}
	}

	public long getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getAge() {
		return age;
	}

	public String getGender() {
		return gender;
	}

	public String getOccupation() {
		return occupation;
	}

	public String getZip() {
		return zip;
	}

//	public ArrayList<Rating> getRatings() {
//		return ratings;
//	}
	
	public HashMap<Long,Integer> getRatings() {
		return ratings;
	}
	
	//What to return if: new, duplicate. If duplicate, recommender need to find and replace
//	public Rating addRating(long movieId, int rating) throws Exception {
//		Rating r = new Rating(userId,movieId,rating);
//		ratings.put(movieId, r);
//		
//	}
	
//	public void addRating(Rating r) {
//		if(r != null && !ratings.contains(r))
//			ratings.add(r);
//	}
	
	public void addRating(Rating r) {
		if(r != null && Rating.checkRating(r.getRating()))
			ratings.put(r.getMovieId(), r.getRating());
	}
	
//	public void removeRating(Rating r) {
//		if(r != null) {
//			while(ratings.contains(r))
//				ratings.remove(r);
//		}
//	}
	
	public HashSet<Long> getPositiveRatedMovieIds() {
		HashSet<Long> rateds = new HashSet<>();
		Iterator<Long> ite = ratings.keySet().iterator();
		while (ite.hasNext()) {
			long id = ite.next();
			if(ratings.get(id) > 0) {
				rateds.add(id);
			}
		}
		return rateds;
	}
	
	public String toString() {
		return new ToJsonString(getClass(), this).toString();
	}

	public String info() {
		return firstName + " " + lastName + ", " + age + ", " + gender + ", " + occupation + ", "
				+ zip + ", Rated " + ratings.size() + " movies " + " $$ ID: " + userId;
	}

	//ID has affect on the Set contains method
	@Override
	public int hashCode() {
		return Objects.hashCode(this.lastName, this.firstName, this.gender, this.occupation, this.zip,
				this.age);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof User) {
			final User other = (User) obj;
			return Objects.equal(firstName, other.firstName) && Objects.equal(lastName, other.lastName)
					&& Objects.equal(gender, other.gender) && Objects.equal(occupation, other.occupation)
					&& Objects.equal(zip, other.zip) && Objects.equal(age, other.age);
		} else {
			return false;
		}
	}

	public static void resetCounter() {
		counter = 1;
	}

	public static long getCounter() {
		return counter;
	}

	// firstName, lastName, age, gender, occupation, zip
	@Override
	public int compareTo(User that) {
		int compareFirstName = firstName.toLowerCase().compareTo(that.firstName.toLowerCase());
		int compareLastName = lastName.toLowerCase().compareTo(that.lastName.toLowerCase());
		if (compareFirstName < 0)
			return -1;
		if (compareFirstName > 0)
			return 1;
		if (compareLastName < 0)
			return -1;
		if (compareLastName > 0)
			return 1;
		if (age < that.age)
			return -1;
		if (age > that.age)
			return 1;
		return 0;
	}

	// public int compareTo(String firstName, String lastName, int age) {
	// int compareFirstName =
	// this.firstName.toLowerCase().compareTo(firstName.toLowerCase());
	// int compareLastName =
	// this.lastName.toLowerCase().compareTo(lastName.toLowerCase());
	// if (compareFirstName < 0)
	// return -1;
	// if (compareFirstName > 0)
	// return 1;
	// if (compareLastName < 0)
	// return -1;
	// if (compareLastName > 0)
	// return 1;
	// if(this.age < age)
	// return -1;
	// if(this.age > age)
	// return 1;
	// return 0;
	// }

	// For testing
	public static void incrementCounter() {
		counter++;
	}

	public static void setCounter(long counter) {
		if(counter >= 1)
			User.counter = counter;
	}
	
	private static boolean str(String toCheck, int length) {
		if (toCheck == null)
			return false;
		String result = toCheck.trim();
		return result.length() > 0 && result.length() <= length;
	}

	// private String identifyOccupation(String oc) {
	// switch(oc.toLowerCase()) {
	//
	// }
	// }
}
