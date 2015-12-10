package controllers;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.princeton.cs.introcs.Stopwatch;
import models.Fixtures;
import models.Movie;
import models.Query;
import models.Rating;
import models.User;
import utils.CSVLoader;
import utils.JSONSerializer;

public class RecommenderTest {
	// try {} catch(Exception e) {fail(e.getMessage());}
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
		r = null;
	}

	/**
	 * right and cardinality
	 */
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

	/**
	 * right and cardinality
	 */
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

	/**
	 * right and cardinality
	 */
	@Test
	public void testAddMovie() {
		try {
			Movie add = new Movie("Toy Story (1995)", "01-Jan-1995",
					"http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)", "0001110000000000000");
			r.addMovie(add);
			Movie added = r.getMovie((long) 1);

			assertEquals(r.getMovies().size(), 1);
			assertEquals(r.getMovieIdList().size(), 1);
			assertTrue(r.getMovieIdList().get(0) == (long) 1);
			assertEquals(add, added);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right and cardinality
	 */
	@Test
	public void testAddManyMovies() {
		try {
			Movie[] movies = Fixtures.getMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i].getTitle(), movies[i].getReleaseDate(), movies[i].getUrl(),
						movies[i].getGenreCode());
			}

			HashMap<Long, Movie> moviesDB = r.getMovies();
			assertEquals(moviesDB.size(), movies.length);
			assertEquals(r.getMovieIdList().size(), movies.length);

			for (int i = 0; i < movies.length; i++) {
				assertTrue(moviesDB.containsValue(movies[i]));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSystemInfo() {
		try {
			User[] users = Fixtures.sampleToptenUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			Movie[] movies = Fixtures.getToptenMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			r.addRating(1, ((Movie) r.searchMovie("a").get(0)).getMovieId(), 5);
			r.addRating(10, ((Movie) r.searchMovie("j").get(0)).getMovieId(), 5);
			r.addRating(14, ((Movie) r.searchMovie("n").get(0)).getMovieId(), 5);
			r.addRating(15, ((Movie) r.searchMovie("o").get(0)).getMovieId(), 5);
			r.addRating(2, ((Movie) r.searchMovie("b").get(0)).getMovieId(), 3);
			r.addRating(3, ((Movie) r.searchMovie("c").get(0)).getMovieId(), 3);
			r.addRating(9, ((Movie) r.searchMovie("i").get(0)).getMovieId(), 3);
			r.addRating(11, ((Movie) r.searchMovie("k").get(0)).getMovieId(), 3);
			r.addRating(12, ((Movie) r.searchMovie("l").get(0)).getMovieId(), 3);
			r.addRating(13, ((Movie) r.searchMovie("m").get(0)).getMovieId(), 3);

			assertEquals(r.info(), "Users: 15 $ Movies: 15 $ Ratings: 10");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right
	 */
	@Test
	public void testSortUsers() {
		try {
			User[] users = Fixtures.getUsersForSort2();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			ArrayList<Long> ids = r.getUserIdList();
			assertEquals(ids.size(), r.getUsersSize());
			assertEquals(ids.size(), users.length);

			assertEquals(r.getUser(ids.get(0)).getFirstName(), "a");
			assertEquals(r.getUser(ids.get(1)).getFirstName(), "b");
			assertEquals(r.getUser(ids.get(2)).getFirstName(), "c");
			assertEquals(r.getUser(ids.get(3)).getFirstName(), "d");
			assertEquals(r.getUser(ids.get(4)).getFirstName(), "x");
			assertEquals(r.getUser(ids.get(5)).getFirstName(), "y");
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	/**
	 * right
	 */
	@Test
	public void testSortMovies() {
		try {
			Movie[] movies = Fixtures.getSimilarMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			ArrayList<Long> ids = r.getMovieIdList();
			assertEquals(ids.size(), r.getMovies().size());
			assertEquals(ids.size(), movies.length);

			assertEquals(r.getMovie(ids.get(0)).getUrl(), "http://us.imdb.com/M/");
			assertEquals(r.getMovie(ids.get(1)).getUrl(), "http://us.imdb.com/M/title-exact?GoldenEye%20(1995)");
			assertEquals(r.getMovie(ids.get(2)).getUrl(), "us.imdb.com/M/title-exact?GoldenEye%20(1995)");
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	/**
	 * right and conformance
	 */
	@Test
	public void testSearch() {
		try {
			User[] users = Fixtures.getUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			ArrayList<Query> searchU = r.searchUser("tom", "cat", 99);
			assertNotNull(searchU);
			assertEquals(searchU.size(), 1);
			assertTrue(searchU.get(0) instanceof User);
			assertEquals((User) searchU.get(0), users[2]);

			Movie[] movies = Fixtures.getMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}
			ArrayList<Query> searchM = r.searchMovie("Get Shorty (1995)");
			assertNotNull(searchM);
			assertEquals(searchM.size(), 1);
			assertTrue(searchM.get(0) instanceof Movie);
			assertEquals((Movie) searchM.get(0), movies[3]);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right and cardinality
	 */
	@Test
	public void testRippleSearch() {
		try {
			User[] users = Fixtures.getSimilarUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			ArrayList<Query> searchU = r.searchUser("tom", "cat", 99);
			assertNotNull(searchU);
			assertEquals(searchU.size(), 4);

			Movie[] movies = Fixtures.getSimilarMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}
			ArrayList<Query> searchM = r.searchMovie("GoldenEye (1995)");
			assertNotNull(searchM);
			assertEquals(searchM.size(), 3);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Cross-check Using Other Means
	 */
	@Test
	public void testSortUsersVsQuickSort() {
		Recommender r2 = new Recommender(null, new CSVLoader("data_movieLens/users.dat", "data_movieLens/newItems.dat",
				"data_movieLens/ratings.dat", "data_movieLens/genre.dat"));
		try {
			r2.prime();
			r2.addUser("Noel", "Schwartz", 22, "F", "student", "000000");

			// retrieve database
			final HashMap<Long, User> users = r2.getUsers();
			final HashMap<Long, Movie> movies = r2.getMovies();

			// retrieve id lists
			ArrayList<Long> userIds = r2.getUserIdList();
			ArrayList<Long> movieIds = r2.getMovieIdList();

			// clone id lists
			ArrayList<Long> quickUserIds = new ArrayList<>();
			quickUserIds.addAll(userIds);
			ArrayList<Long> quickMovieIds = new ArrayList<>();
			quickMovieIds.addAll(movieIds);

			// Make sure they are whole new lists, not references
			quickUserIds.add((long) 10000);
			assertNotEquals(userIds.size(), quickUserIds.size());
			quickUserIds.remove(quickUserIds.size() - 1);

			Collections.sort(quickUserIds, new Comparator<Long>() {
				@Override
				public int compare(Long id1, Long id2) {
					User user1 = users.get(id1);
					User that = users.get(id2);
					return user1.compareTo(that);
				}
			});

			Collections.sort(quickMovieIds, new Comparator<Long>() {
				@Override
				public int compare(Long id1, Long id2) {
					Movie movie = movies.get(id1);
					Movie that = movies.get(id2);
					return movie.compareTo(that);
				}
			});

			// Test
			for (int i = 0; i < userIds.size(); i++) {
				assertEquals(userIds.get(i), quickUserIds.get(i));
				assertEquals(users.get(userIds.get(i)), users.get(quickUserIds.get(i)));
			}
			for (int i = 0; i < movieIds.size(); i++) {
				assertEquals(movieIds.get(i), quickMovieIds.get(i));
				assertEquals(movies.get(movieIds.get(i)), movies.get(quickMovieIds.get(i)));
			}

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right and cardinality
	 */
	@Test
	public void testRemoveUser() {
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
			r.addRating(1, 3, 1);
			r.addRating(1, 1, -3);

			Movie ratedMovie1 = r.getMovie((long) 1);
			Movie ratedMovie2 = r.getMovie((long) 2);
			Movie ratedMovie3 = r.getMovie((long) 3);

			assertEquals(ratedMovie1.getRatings().size(), 1);
			assertEquals(ratedMovie2.getRatings().size(), 1);
			assertEquals(ratedMovie3.getRatings().size(), 1);

			r.removeUser(1);
			assertEquals(r.getUsersSize(), users.length - 1);

			// check that ratings made by user are gone
			assertEquals(ratedMovie1.getRatings().size(), 0);
			assertEquals(ratedMovie2.getRatings().size(), 0);
			assertEquals(ratedMovie3.getRatings().size(), 0);

		} catch (Exception e) {
			fail("Exception thrown");
		}
	}
	
	/**
	 * right
	 */
	@Test
	public void testRemoveAllUser() {
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
			r.addRating(1, 3, 1);
			r.addRating(1, 1, -3);

			Movie ratedMovie1 = r.getMovie((long) 1);
			Movie ratedMovie2 = r.getMovie((long) 2);
			Movie ratedMovie3 = r.getMovie((long) 3);

			assertEquals(ratedMovie1.getRatings().size(), 1);
			assertEquals(ratedMovie2.getRatings().size(), 1);
			assertEquals(ratedMovie3.getRatings().size(), 1);

			long id = User.getCounter() - 1;
			int size = r.getUsersSize();
			for (int i = r.getUsersSize(); i > 0; i--) {
				User removedUser = r.removeUser(id);
				assertEquals(--size, r.getUsersSize());
				assertEquals(removedUser, users[(int) id - 1]);
				id--;
			}
			assertEquals(r.getUsersSize(), 0);

			// check that ratings made by user are gone
			assertEquals(ratedMovie1.getRatings().size(), 0);
			assertEquals(ratedMovie2.getRatings().size(), 0);
			assertEquals(ratedMovie3.getRatings().size(), 0);

		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	/**
	 * right
	 */
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

	/**
	 * right and cardinality
	 */
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
			Rating fromBigClass = r.getRating((long) 1, (long) 2);
			User rater = r.getUser((long) 1);
			Rating fromUser = rater.getRatings().get((long) 2);
			Movie rated = r.getMovie((long) 2);
			int ratingPointFromMovie = rated.getRatings().get((long) 1);

			assertEquals(r.getRatings().size(), 1);
			assertEquals(rater.getRatings().size(), 1);
			assertEquals(rated.getRatings().size(), 1);
			assertEquals(fromUser, fromBigClass);
			assertEquals(fromUser.getRating(), ratingPointFromMovie);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right and cardinality
	 */
	@Test
	public void testAddDuplicateRating() {
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
			r.addRating(1, 2, 3);
			Rating fromBigClass = r.getRating((long) 1, (long) 2);
			User rater = r.getUser((long) 1);
			Rating fromUser = rater.getRatings().get((long) 2);
			Movie rated = r.getMovie((long) 2);
			int ratingPointFromMovie = rated.getRatings().get((long) 1);

			assertEquals(fromBigClass.getRating(), 3);
			assertEquals(r.getRatings().size(), 1);
			assertEquals(rater.getRatings().size(), 1);
			assertEquals(rated.getRatings().size(), 1);
			assertEquals(fromUser, fromBigClass);
			assertEquals(fromUser.getRating(), ratingPointFromMovie);

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right and cardinality
	 */
	@Test
	public void testToptenMovies() {
		try {
			User[] users = Fixtures.sampleToptenUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			Movie[] movies = Fixtures.getToptenMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			r.addRating(1, ((Movie) r.searchMovie("a").get(0)).getMovieId(), 5);
			r.addRating(10, ((Movie) r.searchMovie("j").get(0)).getMovieId(), 5);
			r.addRating(14, ((Movie) r.searchMovie("n").get(0)).getMovieId(), 5);
			r.addRating(15, ((Movie) r.searchMovie("o").get(0)).getMovieId(), 5);
			r.addRating(2, ((Movie) r.searchMovie("b").get(0)).getMovieId(), 3);
			r.addRating(3, ((Movie) r.searchMovie("c").get(0)).getMovieId(), 3);
			r.addRating(9, ((Movie) r.searchMovie("i").get(0)).getMovieId(), 3);
			r.addRating(11, ((Movie) r.searchMovie("k").get(0)).getMovieId(), 3);
			r.addRating(12, ((Movie) r.searchMovie("l").get(0)).getMovieId(), 3);
			r.addRating(13, ((Movie) r.searchMovie("m").get(0)).getMovieId(), 3);
			r.addRating(8, ((Movie) r.searchMovie("h").get(0)).getMovieId(), 1);
			r.addRating(4, ((Movie) r.searchMovie("d").get(0)).getMovieId(), 0);
			r.addRating(5, ((Movie) r.searchMovie("e").get(0)).getMovieId(), -1);
			r.addRating(6, ((Movie) r.searchMovie("f").get(0)).getMovieId(), -3);
			r.addRating(7, ((Movie) r.searchMovie("g").get(0)).getMovieId(), -5);

			String[] result = new String[] { "a", "j", "n", "o", "b", "c", "i", "k", "l", "m" };

			// List is arranged by rating point, so title order is not
			// guaranteed
			List<Movie> topten = r.getTopTenMovies();
			assertEquals(topten.size(), 10);

			boolean flag = false;
			for (int i = 0; i < result.length; i++) {
				// System.out.println(result[i]);
				for (int j = 0; j < topten.size(); j++) {
					if (topten.get(j).getTitle().compareTo(result[i]) == 0) {
						flag = true;
						topten.remove(topten.get(j));
						break;
					}
				}
				assertTrue(flag);
				flag = false;
			}
			assertEquals(topten.size(), 0);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right
	 */
	@Test
	public void testGetUserRatings() {
		try {
			r.addUser("Tom", "Cat", 99, "M", "other", "234567");
			Movie[] movies = Fixtures.getToptenMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			r.addRating(1, 1, 1);
			r.addRating(1, 2, 0);
			r.addRating(1, 3, -5);
			r.addRating(1, 4, 3);
			r.addRating(1, 5, 5);
			r.addRating(1, 6, -1);
			r.addRating(1, 7, -3);

			List<Rating> ratings = r.getUserRatings(1);
			assertEquals(ratings.size(), 7);
			assertEquals(ratings.get(0).getRating(), 5);
			assertEquals(ratings.get(1).getRating(), 3);
			assertEquals(ratings.get(2).getRating(), 1);
			assertEquals(ratings.get(3).getRating(), 0);
			assertEquals(ratings.get(4).getRating(), -1);
			assertEquals(ratings.get(5).getRating(), -3);
			assertEquals(ratings.get(6).getRating(), -5);

			r.addUser("T", "C", 9, "M", "other", "234567");
			assertEquals(r.printUserRatings((long) 2),
					"T C, 9, M, other, 234567, Rated 0 movies  $$ ID: 2\nNot available");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right and cardinality
	 */
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
			// assertEquals(r.getRatingsDB().size(), 3);
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

	/**
	 * test error
	 */
	@Test
	public void testAddRatingUserNotExisted() {
		try {
			Movie[] movies = Fixtures.getMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}
			r.addRating(50, 1, 5);
			assertEquals(r.getRatings().size(), 0);
			// assertEquals(r.getRatingsDB().size(), 0);
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	/**
	 * test error
	 */
	@Test
	public void testAddRatingMovieNotExisted() {
		try {
			User[] users = Fixtures.getUsers();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			r.addRating(1, 100, 5);
			assertEquals(r.getRatings().size(), 0);
			// assertEquals(r.getRatingsDB().size(), 0);
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	/**
	 * test error
	 */
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

	/**
	 * right
	 */
	@Test
	public void testGetRecommendation() {
		try {
			User[] users = Fixtures.getUsersForSort2();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}

			Movie[] movies = Fixtures.getToptenMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			// Note:from here, user and movie is identified by id, eg. user 1 =
			// user has
			// ID 1.

			// user 1, 3 and 5 rate movie 1 the same point (5); since user 1
			// likes movie 1 and 2 his/her favorite genres are [comedy,
			// animation, children's] and [adventure, thriller, action] (Note:
			// user 7 hasn't rated anything/neutral)
			r.addRating(1, 1, 5);
			r.addRating(5, 1, 5);
			r.addRating(3, 1, 5);
			r.addRating(2, 1, 5);
			r.addRating(4, 1, 5);
			r.addRating(6, 1, 5);

			// user 1, 5 likes movie 2 while other users hate it or neutral
			// (user 6 and 7)
			r.addRating(1, 2, 3);
			r.addRating(5, 2, 1);
			r.addRating(3, 2, -5);
			r.addRating(2, 2, -3);
			r.addRating(4, 2, -1);

			// user 5 also likes movie 3, 5 and 12
			r.addRating(5, 3, 3);
			r.addRating(5, 5, 5);
			r.addRating(5, 12, 1);

			// so user 1 should get recommendation from user 5, particularly
			// movies 3,5,12 since user 1 hasn't rated them. However, the genres
			// of movie 3 is [thriller] and movie 12 are [adventure, thriller,
			// action], those are among of user 1's favorite
			// genres, while movie 4's are [drama]. So only movie 3 and 12 will
			// be recommended
			ArrayList<Movie> rMovies = r.recommend((long) 1, false);
			assertEquals(rMovies.size(), 2);

			// test using only title here because movies in movies list aren't
			// rated yet (note that rMovies are in order of the average rating
			// point)
			assertEquals(rMovies.get(0).getTitle(), movies[2].getTitle());
			assertEquals(rMovies.get(1).getTitle(), movies[11].getTitle());

		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * right
	 */
	@Test
	public void testQuickRecommendation() {
		try {
			User[] users = Fixtures.getUsersForSort2();
			for (int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}

			Movie[] movies = Fixtures.getToptenMovies();
			for (int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}

			// same sequence of rating as testGetRecommendation()
			Rating[] ratings = Fixtures.getRatingsQuickRecommend();
			for (int i = 0; i < ratings.length; i++)
				r.addRating(ratings[i]);

			// note that the timestamp of the above ratings are given statically
			// because if they're added too fast, a few of them can happen
			// within the same millisecond (timestamp in Rating class can be
			// setup by System.currentTimeMillis() or a given argument). This
			// issue has caused the test to sometimes give freaking red and
			// sometimes give freaking green !!!!!!!!!

			// now user likes movie 9, so he/she is in the mood of watching
			// [adventure] (this rating will be collected as the most recently
			// and highest rated rating)
			r.addRating(1, 9, 5);

			// so user 1 should get recommendation from user 5, particularly
			// movies 3,5,12 since user 1 hasn't rated them. However, the genres
			// of movie 3 is [thriller], movie 12 are [adventure, thriller,
			// action], and movie 4's are [drama]. So only movie 12 will
			// be recommended because it has [adventure]
			ArrayList<Movie> rMovies = r.recommend((long) 1, true);
			assertEquals(rMovies.size(), 1);

			assertEquals(rMovies.get(0).getTitle(), movies[11].getTitle());

		} catch (Exception e) {
			fail(e.getMessage());
		}

	}
}
