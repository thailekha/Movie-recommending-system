package models;

public class Fixtures {

	// If have the fixtures as fields, id counter in User is hard to control
	public static User[] getUsers() {
		return new User[] { new User("Monkey D", "Luffy", "17", "M", "student", "123456"),
				new User("Zorro", "Swordman", "19", "M", "student", "78990"),
				new User("Tom", "Cat", "99", "M", "unknown", "234567"),
				new User("Jerry", "Mouse", "99", "M", "unknown", "345456"),
				new User("Spike", "Dog", "99", "M", "unknown", "456567") };
	}

	public static User[] getUsersForSort() {
		return new User[] { new User("x", "y", "17", "M", "student", "123456"),
				new User("asd", "dsa", "19", "M", "student", "78990"), new User("", "", "99", "M", "unknown", "234567"),
				new User("Pink", "Panther", "99", "M", "unknown", "890111"),
				new User("0123456", "0", "99", "M", "unknown", "345456"),
				new User("Tom", "Jerry", "99", "M", "unknown", "456567"), };
	}

	public static User[] getUsersForSort2() {
		return new User[] { new User("x", "y", "17", "M", "student", "123456"),
				new User("y", "dsa", "19", "M", "student", "78990"), 
				new User("d", "", "99", "M", "unknown", "234567"),
				new User("c", "Panther", "99", "M", "unknown", "890111"),
				new User("b", "0", "99", "M", "unknown", "345456"),
				new User("a", "Jerry", "99", "M", "unknown", "456567"), };
	}
	
	public static User[] sorted = new User[] {

	};
}
