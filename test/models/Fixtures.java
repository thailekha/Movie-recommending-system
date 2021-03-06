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

	public static User[] getUsers2() throws Exception {
		return new User[] { new User("x", "y", 17, "M", "student", "123456"),
				new User("asd", "dsa", 19, "M", "student", "78990"), new User("asd", "ds", 19, "M", "student", "78990"),
				new User("asd", "dsa", 18, "M", "student", "78990"),
				new User("asd", "dsa", 17, "M", "student", "78990"),
				new User("Pink", "Panther", 99, "M", "other", "890111"),
				new User("Pink", "Panther", 90, "M", "other", "890111"),
				new User("0123456", "0", 99, "M", "other", "345456"),
				new User("Tom", "Jerry", 99, "M", "other", "456567"), };
	}

	public static User[] getUsersForSort2() throws Exception {
		return new User[] { new User("x", "y", 17, "M", "student", "123456"),
				new User("y", "dsa", 19, "M", "student", "78990"), new User("d", "bla", 99, "M", "other", "234567"),
				new User("c", "Panther", 99, "M", "other", "890111"), new User("b", "0", 99, "M", "other", "345456"),
				new User("a", "Jerry", 99, "M", "other", "456567"), };
	}

	public static User[] getSimilarUsers() throws Exception {
		// Note the first one is later on duplicated
		return new User[] { new User("Tom", "Cat", 99, "M", "other", "234567"),
				new User("Tom ", "SuperCat", 88, "M", "other", "234567"),
				new User("Tom", "Cat", 99, "M", "other", "234567"), new User("Tom", "Cat", 99, "F", "artist", "234678"),
				new User("Tom", "Cat", 99, "F", "executive", "234890"),
				new User("Tom", "Cat", 99, "M", "writer", "234546") };
	}

	public static Movie[] getMovies() throws Exception {
		return new Movie[] {
				new Movie("Toy Story (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)",
						"0001110000000000000"),
				new Movie("GoldenEye (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?GoldenEye%20(1995)",
						"0110000000000000100"),
				new Movie("Four Rooms (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Four%20Rooms%20(1995)",
						"0000000000000000100"),
				new Movie("Get Shorty (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Get%20Shorty%20(1995)",
						"0100010010000000000"),
				new Movie("Shanghai Triad (Yao a yao yao dao waipo qiao) (1995)", "01-Jan-1995",
						"http://us.imdb.com/Title?Yao+a+yao+yao+dao+waipo+qiao+(1995)", "0000000010000000000") };
	}

	public static Movie[] getSimilarMovies() throws Exception {
		return new Movie[] {
				new Movie("Toy Story (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)",
						"0001110000000000000"),

				// 3
				new Movie("GoldenEye (1995)", "01-Jan-1995", "us.imdb.com/M/title-exact?GoldenEye%20(1995)",
						"0110000000000000100"),

				// 1
				new Movie("GoldenEye (1995)", "01-Jan-1995", "http://us.imdb.com/M/", "0110000000000000100"),

				// 2
				new Movie("GoldenEye (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?GoldenEye%20(1995)",
						"0110000000000000100"),
				new Movie("Shanghai Triad (Yao a yao yao dao waipo qiao) (1995)", "01-Jan-1995",
						"http://us.imdb.com/Title?Yao+a+yao+yao+dao+waipo+qiao+(1995)", "0000000010000000000") };
	}

	public static User[] sampleDataUsers() throws Exception {
		return new User[] { new User("Leonard", "Hernandez", 24, "M", "technician", "85711"),
				new User("Melody", "Roberson", 53, "F", "other", "94043"),
				new User("Gregory", "Newton", 23, "M", "writer", "32067"),
				new User("Oliver", "George", 24, "M", "technician", "43537"),
				new User("Jenna", "Parker", 33, "F", "other", "15213") };
	}

	public static Rating[] sampleSmallDataRatings() throws Exception {
		return new Rating[] { new Rating(5, 10, -5, 875636493), new Rating(1, 1, 3, 878542420),
				new Rating(1, 1, 5, 889751711), new Rating(2, 1, 5, 888552084), new Rating(2, 1, 1, 888980240),
				new Rating(3, 1, 3, 889237455) };
	}

	public static Rating[] sampleBigDataRatings() throws Exception {
		return new Rating[] { new Rating(196, 242, 1, 881250949), new Rating(186, 302, 1, 891717742),
				new Rating(712, 662, 5, 874730320), new Rating(380, 684, 1, 885478886),
				new Rating(13, 225, -3, 882399156), new Rating(12, 203, 1, 879959583) };
	}

	public static User[] sampleToptenUsers() throws Exception {
		return new User[] { new User("Leonard", "Hernandez", 24, "M", "technician", "85711"),
				new User("Melody", "Roberson", 53, "F", "other", "94043"),
				new User("Gregory", "Newton", 23, "M", "writer", "32067"),
				new User("Oliver", "George", 24, "M", "technician", "43537"),
				new User("Jenna", "Parker", 33, "F", "other", "15213"),
				new User("Leo", "Hernandez", 24, "M", "technician", "85711"),
				new User("Mel", "Roberson", 53, "F", "other", "94043"),
				new User("Greg", "Newton", 23, "M", "writer", "32067"),
				new User("Oli", "George", 24, "M", "technician", "43537"),
				new User("Jen", "Parker", 33, "F", "other", "15213"),
				new User("Leonard", "H", 24, "M", "technician", "85711"),
				new User("Melody", "R", 53, "F", "other", "94043"),
				new User("Gregory", "N", 23, "M", "writer", "32067"),
				new User("Oliver", "G", 24, "M", "technician", "43537"),
				new User("Jenna", "P", 33, "F", "other", "15213") };
	}

	public static Movie[] getToptenMovies() throws Exception {
		return new Movie[] { new Movie("a", "01-Jan-1995", "http://", "0001110000000000000"),
				new Movie("b", "01-Jan-1995", "http://", "0110000000000000100"),
				new Movie("c", "01-Jan-1995", "http://", "0000000000000000100"),

				new Movie("d", "01-Jan-1995", "http://", "0100010010000000000"),
				new Movie("e", "01-Jan-1995", "http://", "0000000010000000000"),
				new Movie("f", "01-Jan-1995", "http://", "0001110000000000000"),
				new Movie("g", "01-Jan-1995", "http://", "0110000000000000100"),
				new Movie("h", "01-Jan-1995", "http://", "0000000000000000100"),
				new Movie("i", "01-Jan-1995", "http://", "0010000000000000000"),
				new Movie("j", "01-Jan-1995", "http://", "0000000010000000000"),
				new Movie("k", "01-Jan-1995", "http://", "0001110000000000000"),
				new Movie("l", "01-Jan-1995", "http://", "0110000000000000100"),
				new Movie("m", "01-Jan-1995", "http://", "0000000000000000100"),
				new Movie("n", "01-Jan-1995", "http://", "0100010010000000000"),
				new Movie("o", "01-Jan-1995", "http://", "0000000010000000000"), };
	}

	public static Rating[] getRatingsQuickRecommend() throws Exception {
		return new Rating[] { new Rating(1, 1, 5, 100), new Rating(5, 1, 5, 200), new Rating(3, 1, 5, 300),
				new Rating(2, 1, 5, 400), new Rating(4, 1, 5, 500), new Rating(6, 1, 5, 600), new Rating(1, 2, 3, 700),
				new Rating(5, 2, 1, 800), new Rating(3, 2, -5, 900), new Rating(2, 2, -3, 1000),
				new Rating(4, 2, -1, 1100), new Rating(5, 3, 3, 1200), new Rating(5, 5, 5, 1300),
				new Rating(5, 12, 1, 1400) };
	}
}
