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
			HashSet<Rating> ratings = c.loadRatings();
			assertNotNull(ratings);
			assertEquals(ratings.size(), 99991);
			
			HashBasedTable<Long, Long, Rating> temporaryDB = HashBasedTable.create();
			Iterator<Rating> ite = ratings.iterator();
			while(ite.hasNext()) {
				Rating r = ite.next();
				Rating put = temporaryDB.put(r.getUserId(), r.getMovieId(), r);
				if(put != null)
					fail("Duplicate detected");
			}
			
			BiMap<Long,Long> uTrans = c.getUserIdTranlator().inverse();
			BiMap<Long,Long> mTrans = c.getMovieIdTranlator().inverse();
			
			Rating test1 = temporaryDB.get(uTrans.get(196), mTrans.get(242));
			assertNotNull(test1);
			assertEquals(test1.getRating(),1);
			assertEquals(test1.getTime(),881250949);
			
			Rating test2 = temporaryDB.get(uTrans.get(624), mTrans.get(333));
			assertNotNull(test2);
			assertEquals(test2.getRating(),3);
			assertEquals(test2.getTime(),879791884);
			
			Rating test3 = temporaryDB.get(uTrans.get(12), mTrans.get(203));
			assertNotNull(test3);
			assertEquals(test3.getRating(),1);
			assertEquals(test3.getTime(),879959583);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
