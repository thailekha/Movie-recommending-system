package models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import utils.ToJsonString;

//TODO: NAMES IN LOWER CASE ? / OCCUPATION
public class User implements Comparable<User>, Query<User> {
	// Each user has rated at least 20 movies
	private static long counter = 1;
	private static HashSet<String> occupations = Sets.newHashSet("administrator", "artist", "doctor", "educator",
			"engineer", "entertainment", "executive", "healthcare", "homemaker", "lawyer", "librarian", "marketing",
			"none", "other", "programmer", "retired", "salesman", "scientist", "student", "technician", "writer");
	private long userId;
	private String firstName, lastName, gender, occupation, zip;
	private int age;

	// Map movie and rating point
	private HashMap<Long, Rating> ratings = new HashMap<>(); // quicker to check
																// duplicate
																// ratings
	// private ArrayList<Rating> ratings = new ArrayList<>();

	/**
	 * constructor
	 * 
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 * @param occupation
	 * @param zip
	 * @throws Exception
	 */
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
			throw new Exception("Invalid arguments to construct user object");
		}
	}

	/**
	 * query constructor (mainly for searching)
	 * 
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @throws Exception
	 */
	public User(String firstName, String lastName, int age) throws Exception {
		if (str(firstName, 200) && str(lastName, 200) && age > 0 && age < 100) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
		} else {
			throw new Exception("Invalid arguments to construct user query");
		}
	}

	/**
	 * get user id
	 * 
	 * @return user id
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * get first name
	 * 
	 * @return firstname
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * get last name
	 * 
	 * @return lastname
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * get age
	 * 
	 * @return age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * get gender
	 * 
	 * @return gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * get occupation
	 * 
	 * @return occupation
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * get zip code
	 * 
	 * @return zip code
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * get movie-rating map
	 * 
	 * @return map
	 */
	public HashMap<Long, Rating> getRatings() {
		return ratings;
	}

	// What to return if: new, duplicate. If duplicate, recommender need to find
	// and replace
	// public Rating addRating(long movieId, int rating) throws Exception {
	// Rating r = new Rating(userId,movieId,rating);
	// ratings.put(movieId, r);
	//
	// }

	// public void addRating(Rating r) {
	// if(r != null && !ratings.contains(r))
	// ratings.add(r);
	// }

	/**
	 * add a rating
	 * 
	 * @param rating
	 */
	public void addRating(Rating r) {
		if (r != null && Rating.checkRating(r.getRating()))
			ratings.put(r.getMovieId(), r);
	}

	// public void removeRating(Rating r) {
	// if(r != null) {
	// while(ratings.contains(r))
	// ratings.remove(r);
	// }
	// }

	/**
	 * get set of movie ids that are rated above 0
	 * 
	 * @return set if movies ids
	 */
	public HashSet<Long> getPositiveRatedMovieIds() {
		HashSet<Long> rateds = new HashSet<>();
		Iterator<Long> ite = ratings.keySet().iterator();
		while (ite.hasNext()) {
			long id = ite.next();
			if (ratings.get(id).getRating() > 0) {
				rateds.add(id);
			}
		}
		return rateds;
	}

	/**
	 * String representation in json
	 */
	public String toString() {
		return new ToJsonString(getClass(), this).toString();
	}

	/**
	 * string representation
	 * 
	 * @return
	 */
	public String info() {
		return firstName + " " + lastName + ", " + age + ", " + gender + ", " + occupation + ", " + zip + ", Rated "
				+ ratings.size() + " movies " + " $$ ID: " + userId;
	}

	/**
	 * ID is not included since it has affect on the Set.contains method hash
	 * code of object
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.lastName, this.firstName, this.gender, this.occupation, this.zip, this.age);
	}

	/**
	 * check equality with another user object
	 */
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

	/**
	 * set id counter to 1
	 */
	public static void resetCounter() {
		counter = 1;
	}

	/**
	 * get id counter
	 * 
	 * @return id counter
	 */
	public static long getCounter() {
		return counter;
	}

	/**
	 * make comparison
	 */
	@Override
	public int compareTo(User that) {
		// firstName, lastName, gender, occupation, zip
		int compareFirstName = firstName.toLowerCase().compareTo(that.firstName.toLowerCase());
		int compareLastName = lastName.toLowerCase().compareTo(that.lastName.toLowerCase());
		int compareGender = gender.toLowerCase().compareTo(that.gender.toLowerCase());
		int compareOccu = occupation.toLowerCase().compareTo(that.occupation.toLowerCase());
		int compareZip = zip.toLowerCase().compareTo(that.zip.toLowerCase());
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
		if (compareGender < 0)
			return -1;
		if (compareGender > 0)
			return 1;
		if (compareOccu < 0)
			return -1;
		if (compareOccu > 0)
			return 1;
		if (compareZip < 0)
			return -1;
		if (compareZip > 0)
			return 1;
		return 0;
	}

	/**
	 * Increment id counter by 1 (For testing)
	 */
	public static void incrementCounter() {
		counter++;
	}

	/**
	 * set id counter
	 * 
	 * @param new
	 *            counter
	 */
	public static void setCounter(long counter) {
		if (counter >= 1)
			User.counter = counter;
	}

	/**
	 * general string checker
	 * 
	 * @param toCheck
	 * @param allowed
	 *            length
	 * @return true if valid
	 */
	private static boolean str(String toCheck, int length) {
		if (toCheck == null)
			return false;
		String result = toCheck.trim();
		return result.length() > 0 && result.length() <= length;
	}

	/**
	 * compare user with another user in terms of name and age
	 */
	@Override
	public int queryCompareTo(User that) {
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

	/**
	 * check if user hasn't rated anything or all of the ratings are 0
	 * 
	 * @return boolean
	 */
	public boolean isNeutral() {
		boolean neutral = true;
		Iterator<Rating> ite = ratings.values().iterator();
		while (ite.hasNext()) {
			if (ite.next().getRating() != 0) {
				neutral = false;
				break;
			}
		}
		return neutral;
	}

	public HashMap<Long, Rating> getRatingsNoNeutral() {
		HashMap<Long, Rating> rates = new HashMap<>();
		Iterator<Long> ite = ratings.keySet().iterator();
		while (ite.hasNext()) {
			long nextId = ite.next();
			Rating r =  ratings.get(nextId);
			if (r.getRating() != 0) {
				rates.put(nextId, r);
			}
		}
		return rates;
	}
}
