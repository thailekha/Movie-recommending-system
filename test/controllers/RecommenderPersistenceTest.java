/**
 * 
 */
package controllers;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.HashBasedTable;

import models.Movie;
import models.Rating;
import models.User;
import utils.CSVLoader;
import utils.JSONSerializer;
import utils.Serializer;

/**
 * @author HP
 *
 */
public class RecommenderPersistenceTest {

	Recommender r;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		User.resetCounter();
		Movie.resetCounter();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		User.resetCounter();
		Movie.resetCounter();
	}

	/**
	 * Right
	 * @throws Exception
	 */
	@Test
	public void testJSONSerializer() throws Exception {
		String datastoreFile = "testdatastore.json";
		deleteFile(datastoreFile);

		Serializer serializer = new JSONSerializer(new File(datastoreFile));

		r = new Recommender(serializer, new CSVLoader("small_data/users5.dat", "small_data/items5.dat",
				"small_data/ratings5.dat", "small_data/genre.dat"));
		r.prime();
		r.store();
		long uCount = User.getCounter();
		long mCount = Movie.getCounter();
		
		User.resetCounter();
		Movie.resetCounter();

		Recommender r2 = new Recommender(serializer, new CSVLoader("small_data/users5.dat", "small_data/items5.dat",
				"small_data/ratings5.dat", "small_data/genre.dat"));
		r2.load();

		assertEquals(r.getUsersSize(), r2.getUsersSize());
		assertEquals(r.getMovies().size(), r2.getMovies().size());

		// Test static fields
		assertEquals(User.getCounter(), uCount);
		assertEquals(Movie.getCounter(), mCount);

		// Test maps
		Iterator<Long> users = r.getUsers().keySet().iterator();
		while (users.hasNext()) {
			long id = users.next();
			assertTrue(r2.getUsers().containsKey(id));
			assertEquals(r.getUsers().get(id),r2.getUsers().get(id));
		}
		Iterator<Long> movies = r.getMovies().keySet().iterator();
		while (movies.hasNext()) {
			long id = movies.next();
			assertTrue(r2.getMovies().containsKey(id));
			assertEquals(r.getMovies().get(id),r2.getMovies().get(id));
		}

		// Test other fields
		assertEquals(r.getUserIdList().size(), r2.getUserIdList().size());
		for (long id : r.getUserIdList()) {
			assertTrue(r2.getUserIdList().contains(id));
		}
		assertEquals(r.getMovieIdList().size(), r2.getMovieIdList().size());
		for (long id : r.getMovieIdList()) {
			assertTrue(r2.getMovieIdList().contains(id));
		}
		assertEquals(r.getRatings().size(), r2.getRatings().size());
		for (Rating rating : r.getRatings()) {
			assertTrue(r2.getRatings().contains(rating));
		}
		//assertEquals(r.ratingsSorted(), r2.ratingsSorted());

		deleteFile("testdatastore.json");
	}

	void deleteFile(String fileName) {
		File datastore = new File(fileName);
		if (datastore.exists()) {
			datastore.delete();
		}
	}
}
