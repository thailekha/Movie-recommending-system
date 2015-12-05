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

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadUsers() {
		try {
			long preIDcounter = User.getCounter();
			User[] samples = Fixtures.sampleDataUsers();
			HashSet<User> users = CSVLoader.loadUsers("small_data/users5.dat");
			
			assertNotNull(users);
			assertEquals(samples.length,users.size());
			//assertEquals(User.getCounter(), preIDcounter + users.size());
			Iterator<User> ite = users.iterator();
			while (ite.hasNext()) {
				User next = ite.next();
				assertTrue(next instanceof User);
				//System.out.println(next);
			}
			for (int i = 0; i < samples.length; i++) {
				//System.out.println("Case: " + samples[i]);
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
			HashSet<Movie> movies = CSVLoader.loadMovies("small_data/items5.dat");
			
			assertNotNull(movies);
			assertEquals(10,movies.size());
			//assertEquals(User.getCounter(), preIDcounter + users.size());
			Iterator<Movie> ite = movies.iterator();
			while (ite.hasNext()) {
				Movie next = ite.next();
				assertTrue(next instanceof Movie);
				//System.out.println(next);
			}
			for (int i = 0; i < samples.length; i++) {
				System.out.println("Case: " + samples[i]);
				assertTrue(movies.contains(samples[i]));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		} 
	}
	
	@Test
	public void testLoadRatings() {
		try {
			Fixtures.sampleDataUsers();
			Fixtures.getMovies();
			Rating[] samples = Fixtures.sampleDataRatings();
			HashSet<Rating> ratings = CSVLoader.loadRatings("small_data/ratings5.dat");
			assertNotNull(ratings);
			for(int i = 0; i < samples.length;i++) {
				assertTrue(ratings.contains(samples[i]));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		} 
	}
	
}
