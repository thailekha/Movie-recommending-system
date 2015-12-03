package models;

public class Fixtures {

	// If have the fixtures as fields, id counter in User is hard to control
	public static User[] getUsers() throws Exception {
		return new User[] { new User("Monkey D", "Luffy", 17, "M", "student", "123456"),
				new User("Zorro", "Swordman", 19, "M", "student", "78990"),
				new User("Tom", "Cat", 99, "M", "other", "234567"),
				new User("Jerry", "Mouse", 99, "M", "other", "345456"),
				new User("Spike", "Dog", 99, "M", "other", "456567") };
	}

	public static User[] getUsersForSort() throws Exception {
		return new User[] { new User("x", "y", 17, "M", "student", "123456"),
				new User("asd", "dsa", 19, "M", "student", "78990"), 
				new User("Pink", "Panther", 99, "M", "other", "890111"),
				new User("0123456", "0", 99, "M", "other", "345456"),
				new User("Tom", "Jerry", 99, "M", "other", "456567"), };
	}

	public static User[] getUsersForSort2() throws Exception {
		return new User[] { new User("x", "y", 17, "M", "student", "123456"),
				new User("y", "dsa", 19, "M", "student", "78990"), 
				new User("d", "bla", 99, "M", "other", "234567"),
				new User("c", "Panther", 99, "M", "other", "890111"),
				new User("b", "0", 99, "M", "other", "345456"),
				new User("a", "Jerry", 99, "M", "other", "456567"), };
	}
	
	public static User[] getSimilarUsers() throws Exception {
		return new User[]{new User("Tom", "Cat", 99, "M", "other", "234567"),
				new User("Tom ", "SuperCat", 88, "M", "other", "234567"),
				new User("Tom", "Cat", 99, "M", "other", "234567"),
				new User("Tom", "Cat", 99, "F", "artist", "234678"),
				new User("Tom", "Cat", 99, "F", "executive", "234890"),
				new User("Tom", "Cat", 99, "M", "writer", "234546")};
	}

	public static Movie[] getMovies() throws Exception {
		return new Movie[] {
				new Movie("Toy Story (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)",
						"0001100000000000000"),
				new Movie("GoldenEye (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?GoldenEye%20(1995)",
						"0001100000000000000"),
				new Movie("Get Shorty (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Get%20Shorty%20(1995)",
						"010001001000000000") };
	}
}
