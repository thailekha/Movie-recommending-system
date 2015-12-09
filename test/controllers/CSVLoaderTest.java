package controllers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;

import models.Fixtures;
import models.Movie;
import models.Rating;
import models.User;
import utils.CSVLoader;

public class CSVLoaderTest {

	CSVLoader c;

	@Before
	public void setUp() throws Exception {
		c = new CSVLoader("data_movieLens/users.dat", "data_movieLens/newItems.dat", "data_movieLens/ratings.dat",
				"data_movieLens/genre.dat");
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Right and cardinality
	 */
	@Test
	public void testLoadGenres() {
		try {
			HashSet<String> validGenres = Sets.newHashSet("unknown", "action", "adventure", "animation", "children's",
					"comedy", "crime", "documentary", "drama", "fantasy", "film-noir", "horror", "musical", "mystery",
					"romance", "sci-fi", "thriller", "war", "western");
			HashMap<Integer, String> genres = c.loadGenres();
			assertEquals(validGenres.size(), genres.size());

			for (int i = 0; i <= 18; i++) {
				assertTrue(validGenres.contains(genres.get(i)));
				genres.remove(i);
			}

			assertEquals(genres.size(), 0);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Right and cardinality
	 */
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

	/**
	 * Right
	 */
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

	/**
	 * Right and Cardinality
	 */
	@Test
	public void testLoadRatings() {
		try {
			c.loadUsers();
			c.loadMovies();
			HashSet<Rating> ratings = c.loadRatings();
			assertNotNull(ratings);
			assertEquals(ratings.size(), 99991);

			Rating[] sampleRatings = Fixtures.sampleBigDataRatings();
			for (int i = 0; i < sampleRatings.length; i++) {
				Rating r = sampleRatings[i];
				// Since ids are translated
				Rating realRating = new Rating(c.getUserIdTranlator().get(r.getUserId()),
						c.getMovieIdTranlator().get(r.getMovieId()), r.getRating(), r.getTime());
				//System.out.println("case: " + sampleRatings[i]);
				assertTrue(ratings.contains(realRating));
			}
		} catch (Exception e) {
			// e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
