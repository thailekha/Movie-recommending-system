package models;

import com.google.common.base.Objects;

import utils.ToJsonString;
import utils.Valid;

public class User {
	// Each user has rated at least 20 movies
	private static long counter = 1;
	private long userId;
	private String firstName, lastName, age, gender, occupation, zip;
	//private int age;

	public User(String firstName, String lastName, String age, String gender, String occupation, String zip) {
		String[] strs = new String[] { firstName, lastName, gender, occupation, zip };
		this.userId = counter++;
		this.firstName = Valid.str(firstName, 200,"default first name");
		this.lastName = Valid.str(lastName, 200,"default last name");
		//this.age = Valid.integer(age, 1, 99, -1);
		this.age = Valid.str(age, 3,"default age");
		this.gender = Valid.str(gender, 1 ,"F");
		this.occupation = Valid.str(occupation, 200,"default occupation");
		this.zip = Valid.str(zip, 200,"default zip");
		System.out.println(this);
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
	
	public String getAge() {
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

	public String toString() {
		return new ToJsonString(getClass(), this).toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.lastName, this.firstName, this.gender, this.occupation, this.zip, this.age);
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
}
