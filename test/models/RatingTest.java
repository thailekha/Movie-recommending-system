package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RatingTest {

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
		
		for(int i = 30; i > 0; i--) {
			User.incrementCounter();
			Movie.incrementCounter();
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * right
	 * @throws Exception
	 */
	@Test
	public void testCreateRating() throws Exception {
		Rating r = new Rating(1,2,0);
		Rating mirror = new Rating(1,2,0);
		
		assertEquals(r.getUserId(),1);
		assertEquals(r.getMovieId(),2);
		assertEquals(r.getRating(),0);
		assertEquals(r,mirror);
	}
	
	/**
	 * test error
	 */
	@Test
	public void testCreateRatingInvalidUserId() {
		try {
			Rating r = new Rating(User.getCounter()+1,1,3);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * test error
	 */
	@Test
	public void testCreateRatingInvalidMovieId() {
		try {
			Rating r = new Rating(2,Movie.getCounter() + 1,3);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	/**
	 * test error
	 */
	@Test
	public void testCreateRatingInvalidRating() {
		try {
			Rating r = new Rating(2,1,2);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	/**
	 * test error
	 */
	@Test
	public void testCreateRatingInvalidAll() {
		try {
			Rating r = new Rating(User.getCounter()+1,Movie.getCounter() + 1,100);
			fail("Should have thrown an exception");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}
