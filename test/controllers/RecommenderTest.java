package controllers;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.princeton.cs.introcs.Stopwatch;
import models.Fixtures;
import models.Movie;
import models.Rating;
import models.User;
import utils.CSVLoader;
import utils.JSONSerializer;

public class RecommenderTest {

	Recommender r;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		User.resetCounter();
		Movie.resetCounter();
		r = new Recommender();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddUser() {
		try {
			r.addUser("Monkey D", "Luffy", 17, "M", "student", "123456");
			assertEquals(r.getUsers().size(), 1);
			User mirror = r.getUsers().values().iterator().next();
			assertEquals(mirror.getUserId(), 1);
			assertEquals(mirror.getFirstName(), "Monkey D");
			assertEquals(mirror.getLastName(), "Luffy");
			assertEquals(mirror.getAge(), 17);
			assertEquals(mirror.getGender(), "M");
			assertEquals(mirror.getOccupation(), "student");
			assertEquals(mirror.getZip(), "123456");
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testAddManyUser() {
		try {
			r.addUser("Monkey D", "Luffy", 17, "M", "student", "123456");
			r.addUser("Zorro", "Swordman", 19, "M", "student", "78990");
			assertEquals(r.getUsers().size(), 2);

			long mirrorId1 = 1;
			User mirror1 = r.getUsers().get(mirrorId1);
			assertEquals(mirror1.getUserId(), 1);
			assertEquals(mirror1.getFirstName(), "Monkey D");
			assertEquals(mirror1.getLastName(), "Luffy");
			assertEquals(mirror1.getAge(), 17);
			assertEquals(mirror1.getGender(), "M");
			assertEquals(mirror1.getOccupation(), "student");
			assertEquals(mirror1.getZip(), "123456");

			long mirrorId2 = 2;
			User mirror2 = r.getUsers().get(mirrorId2);
			assertEquals(mirror2.getUserId(), 2);
			assertEquals(mirror2.getFirstName(), "Zorro");
			assertEquals(mirror2.getLastName(), "Swordman");
			assertEquals(mirror2.getAge(), 19);
			assertEquals(mirror2.getGender(), "M");
			assertEquals(mirror2.getOccupation(), "student");
			assertEquals(mirror2.getZip(), "78990");
		} catch (Exception e) {
			fail("Exception thrown");
		}

	}

	// TODO: not finished
	@Test
	public void testSortUsers() {
		try {
			User[] users = Fixtures.getUsersForSort();
			for (int i = 0; i < users.length; i++) {
				if (users[i].getFirstName().compareTo("asd") == 0 && users[i].getLastName().compareTo("dsa") == 0
						&& users[i].getAge() == 19) {
					System.out.println();
				}
				r.addUser(users[i]);
			}
			ArrayList<Long> ids = r.getUserIdList();
			assertEquals(ids.size(), r.getUsersSize());

			for (int i = 0; i < ids.size(); i++) {
				System.out.println(r.getUsers().get(ids.get(i)).info());
			}
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testSortUsersVsQuickSort() {
		Recommender r2 = new Recommender(null, new CSVLoader("data_movieLens/users.dat", "data_movieLens/newItems.dat",
				"data_movieLens/ratings.dat", "data_movieLens/genre.dat"));
		try {
			r2.prime();
			final HashMap<Long, User> users = r2.getUsers();
			final HashMap<Long, Movie> movies = r2.getMovies();
			ArrayList<Long> userIds = r2.getUserIdList();
			ArrayList<Long> movieIds = r2.getMovieIdList();

			ArrayList<Long> quickUserIds = new ArrayList<>();
			quickUserIds.addAll(userIds);
			ArrayList<Long> quickMovieIds = new ArrayList<>();
			quickMovieIds.addAll(movieIds);

			quickUserIds.add((long) 10000); // Make sure whole new values, not
											// references
			assertNotEquals(userIds.size(), quickUserIds.size());
			quickUserIds.remove(quickUserIds.size() - 1);

			Collections.sort(quickUserIds, new Comparator<Long>() {
				@Override
				public int compare(Long id1, Long id2) {
					User user1 = users.get(id1);
					User that = users.get(id2);
					int compareFirstName = user1.getFirstName().toLowerCase()
							.compareTo(that.getFirstName().toLowerCase());
					int compareLastName = user1.getLastName().toLowerCase().compareTo(that.getLastName().toLowerCase());
					if (compareFirstName < 0)
						return -1;
					if (compareFirstName > 0)
						return 1;
					if (compareLastName < 0)
						return -1;
					if (compareLastName > 0)
						return 1;
					if (user1.getAge() < that.getAge())
						return -1;
					if (user1.getAge() > that.getAge())
						return 1;
					return 0;
				}
			});

			Collections.sort(quickMovieIds, new Comparator<Long>() {
				@Override
				public int compare(Long id1, Long id2) {
					Movie movie = movies.get(id1);
					Movie that = movies.get(id2);
					int compareTitle = movie.getTitle().toLowerCase().compareTo(that.getTitle().toLowerCase());
					if (compareTitle < 0)
						return -1;
					if (compareTitle > 0)
						return 1;
					return 0;
				}
			});

			for (int i = 0; i < userIds.size(); i++) {
				assertEquals(userIds.get(i), quickUserIds.get(i));
			}
			for (int i = 0; i < movieIds.size(); i++) {
				assertEquals(movieIds.get(i), quickMovieIds.get(i));
			}

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRemoveUser() {
		try {
			User[] users = Fixtures.getUsers();
			for (int i = 0; i < users.length; i++)
				r.addUser(users[i]);
			long id = User.getCounter() - 1;
			int size = r.getUsersSize();
			for (int i = r.getUsersSize(); i > 0; i--) {
				User removedUser = r.removeUser(id);
				assertEquals(--size, r.getUsersSize());
				assertEquals(removedUser, users[(int) id - 1]);
				id--;
			}
			assertEquals(r.getUsersSize(), 0);
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testGetMovie() {
		Movie[] movies;
		try {
			movies = Fixtures.getMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}
			Random rand = new Random();
			for (int i = 0; i < 10000; i++) {
				int index = rand.nextInt(movies.length);
				long id = (long) index + 1;
				assertEquals(movies[index], r.getMovie(id));
			}
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testAddRating() {
		try {
			User[] users = Fixtures.getUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			Movie[] movies = Fixtures.getMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			r.addRating(1, 2, 5);
			long mirrorUserId = r.getUsers().keySet().iterator().next();
			long mirrorMovieId = r.getMovies().keySet().iterator().next();
			Rating fromDB = r.getRating(1, 2);
			User rater = r.getUser((long) 1);
			Movie rated = r.getMovie((long) 2);

			assertEquals(rater.getRatings().size(), 1);
			assertEquals(rated.getRatings().size(), 1);
			// HashMap<Long,Integer> ratingsFromUser = rater.getRatings();
			int fromUser = rater.getRatings().get(mirrorMovieId).getRating();
			int fromMovie = rated.getRatings().get(mirrorUserId);
			assertEquals(fromUser, 5);
			assertEquals(fromMovie, 5);

			assertEquals(mirrorUserId, 1);
			assertEquals(mirrorMovieId, 2);
			//assertEquals(r.getRatingsDB().size(), 1);
			assertEquals(r.getRatings().size(), 1);
			assertNotNull(fromDB);
			// assertNotNull(fromUser);

			assertEquals(fromDB.getUserId(), rater.getUserId());
			assertEquals(fromDB.getMovieId(), rated.getMovieId());
			assertEquals(fromDB.getRating(), 5);

			// assertEquals(fromUser.getUserId(), rater.getUserId());
			// assertEquals(fromUser.getMovieId(), rated.getMovieId());
			// assertEquals(fromUser.getRating(), 5);
			//
			// assertEquals(fromDB, fromUser);

		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testAddManyRating() {
		try {
			User[] users = Fixtures.getUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			Movie[] movies = Fixtures.getMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			r.addRating(1, 2, 5); // A
			r.addRating(1, 3, -3); // B
			r.addRating(2, 3, 1); // C
			//assertEquals(r.getRatingsDB().size(), 3);
			assertEquals(r.getRatings().size(), 3);

			Rating fromDBA = r.getRating(1, 2);
			Rating fromDBB = r.getRating(1, 3);
			Rating fromDBC = r.getRating(2, 3);
			assertEquals(fromDBA.getMovieId(), 2);
			assertEquals(fromDBB.getMovieId(), 3);
			assertEquals(fromDBC.getMovieId(), 3);

			User user1 = r.getUser((long) 1);
			User user2 = r.getUser((long) 2);
			HashMap<Long, Rating> ratingsFromUser1 = user1.getRatings();
			HashMap<Long, Rating> ratingsFromUser2 = user2.getRatings();
			assertEquals(ratingsFromUser1.size(), 2);
			assertEquals(ratingsFromUser2.size(), 1);

			int mirrorA = ratingsFromUser1.get((long) 2).getRating();
			int mirrorB = ratingsFromUser1.get((long) 3).getRating();
			int mirrorC = ratingsFromUser2.get((long) 3).getRating();
			assertEquals(mirrorA, 5);
			assertEquals(mirrorB, -3);
			assertEquals(mirrorC, 1);
			assertEquals(fromDBA.getRating(), mirrorA);
			assertEquals(fromDBB.getRating(), mirrorB);
			assertEquals(fromDBC.getRating(), mirrorC);

		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testAddRatingUserNotExisted() {
		try {
			Movie[] movies = Fixtures.getMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}
			r.addRating(50, 1, 5);
			assertEquals(r.getRatings().size(), 0);
			//assertEquals(r.getRatingsDB().size(), 0);
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testAddRatingMovieNotExisted() {
		try {
			User[] users = Fixtures.getUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			r.addRating(1, 100, 5);
			assertEquals(r.getRatings().size(), 0);
			//assertEquals(r.getRatingsDB().size(), 0);
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testAddRatingInvalid() {
		try {
			User[] users = Fixtures.getUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			Movie[] movies = Fixtures.getMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			r.addRating(1, 2, 100);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}

	}
}
