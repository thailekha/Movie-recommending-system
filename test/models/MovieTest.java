package models;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MovieTest {

	@Before
	public void setUp() throws Exception {
		Movie.resetCounter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateMovie() {
		try {
			Movie a = new Movie("Toy Story (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)","0001100000000000000");
			assertEquals(a.getMovieId(),1);
			assertEquals(a.getTitle(),"Toy Story (1995)");
			assertEquals(a.getReleaseDate(),"01-Jan-1995");
			assertEquals(a.getUrl(),"http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)");
			assertEquals(a.getGenreCode(),"0001100000000000000");
			
			Movie mirrorA = new Movie("Toy Story (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)","0001100000000000000");
			assertEquals(mirrorA.getMovieId(),2);
			assertEquals(mirrorA.getTitle(),"Toy Story (1995)");
			assertEquals(mirrorA.getReleaseDate(),"01-Jan-1995");
			assertEquals(mirrorA.getUrl(),"http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)");
			assertEquals(mirrorA.getGenreCode(),"0001100000000000000");
			
			assertEquals(a,mirrorA);
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testCreateManyMovie() {
	
		try {
			Movie a = new Movie("Toy Story (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)","0001100000000000000");
			assertEquals(a.getMovieId(),1);
			assertEquals(a.getTitle(),"Toy Story (1995)");
			assertEquals(a.getReleaseDate(),"01-Jan-1995");
			assertEquals(a.getUrl(),"http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)");
			assertEquals(a.getGenreCode(),"0001100000000000000");
			
			Movie b = new Movie("GoldenEye (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?GoldenEye%20(1995)","0001100000000000000");
			assertEquals(b.getMovieId(),2);
			assertEquals(b.getTitle(),"GoldenEye (1995)");
			assertEquals(b.getReleaseDate(),"01-Jan-1995");
			assertEquals(b.getUrl(),"http://us.imdb.com/M/title-exact?GoldenEye%20(1995)");
			assertEquals(b.getGenreCode(),"0001100000000000000");
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}
	
	@Test
	public void testCreateInvalidMovie() {
		try {
			String longStr = autoStr(' ', 10) + autoStr('a', 500) + autoStr(' ', 10);
			Movie a = new Movie(null, "", longStr,autoStr('1', 20));
			
			fail("Should have thrown an Exception");
		} catch (Exception e) {
			assertTrue(true);
		}
		
	}
	
	private static String autoStr(char agent, int length) {
		String result = "";
		for(int i  = 0; i < length; i ++) {
			result += agent;
		}
		return result;
	}
}
