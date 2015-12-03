package models;

import java.util.Comparator;
import java.util.HashSet;

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

	public String toJsonString() {
		return new ToJsonString(getClass(), this).toString();
	}

	public String toString() {
		return userId + " - " + firstName + " " + lastName + ", " + age + ", " + gender + ", " + occupation + ", "
				+ zip;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.userId, this.lastName, this.firstName, this.gender, this.occupation, this.zip,
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
		if(age < that.age)
			return -1;
		if(age > that.age)
			return 1;
		return 0;
	}

	public int compareTo(String firstName, String lastName, int age) {
		int compareFirstName = this.firstName.toLowerCase().compareTo(firstName.toLowerCase());
		int compareLastName = this.lastName.toLowerCase().compareTo(lastName.toLowerCase());
		if (compareFirstName < 0)
			return -1;
		if (compareFirstName > 0)
			return 1;
		if (compareLastName < 0)
			return -1;
		if (compareLastName > 0)
			return 1;
		if(this.age < age)
			return -1;
		if(this.age > age)
			return 1;
		return 0;
	}

	// For testing
	public static void incrementCounter() {
		counter++;
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
