package controllers;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Fixtures;
import models.Movie;
import models.Rating;
import models.User;
import utils.CSVLoader;

public class CSVLoaderTest {

	CSVLoader c;

	@Before
	public void setUp() throws Exception {
		c = new CSVLoader("data_movieLens/users.dat", "data_movieLens/newItems.dat", "data_movieLens/ratings.dat");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadUsers() {
		try {
			// long preCounter = User.getCounter();
			User[] samples = Fixtures.sampleDataUsers();
			HashSet<User> users = c.loadUsers();
			assertEquals(users.size(), 943);
			// assertEquals(User.getCounter(),users.size()+preCounter);
			assertNotNull(users);
			for (int i = 0; i < samples.length; i++) {
				assertTrue(users.contains(samples[i]));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testLoadMovies() {
		try {
			Movie[] samples = Fixtures.getMovies();
			HashSet<Movie> movies = c.loadMovies();
			assertNotNull(movies);
			for (int i = 0; i < samples.length; i++) {
				assertTrue(movies.contains(samples[i]));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testLoadRatings() {
		try {
			// Fixtures.sampleDataUsers();
			// Fixtures.getMovies();
			c.loadUsers();
			c.loadMovies();
			Rating[] samples = Fixtures.sampleBigDataRatings();
			HashSet<Rating> ratings = c.loadRatings();
			assertNotNull(ratings);
			assertEquals(ratings.size(), 100000);
			for (int i = 0; i < samples.length; i++) {
				assertTrue(ratings.contains(samples[i]));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}