package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MovieTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Movie.resetCounter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateMovie() {
		Movie a = new Movie("Toy Story (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)");
		assertEquals(a.getMovieId(),1);
		assertEquals(a.getTitle(),"Toy Story (1995)");
		assertEquals(a.getReleaseDate(),"01-Jan-1995");
		assertEquals(a.getUrl(),"http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)");
	}

	@Test
	public void testCreateManyMovie() {
		Movie a = new Movie("Toy Story (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)");
		assertEquals(a.getMovieId(),1);
		assertEquals(a.getTitle(),"Toy Story (1995)");
		assertEquals(a.getReleaseDate(),"01-Jan-1995");
		assertEquals(a.getUrl(),"http://us.imdb.com/M/title-exact?Toy%20Story%20(1995)");
		
		Movie b = new Movie("GoldenEye (1995)", "01-Jan-1995", "http://us.imdb.com/M/title-exact?GoldenEye%20(1995)");
		assertEquals(b.getMovieId(),2);
		assertEquals(b.getTitle(),"GoldenEye (1995)");
		assertEquals(b.getReleaseDate(),"01-Jan-1995");
		assertEquals(b.getUrl(),"http://us.imdb.com/M/title-exact?GoldenEye%20(1995)");		
	}
	
	@Test
	public void testCreateInvalidMovie() {
		String longStr = "                                           ";
		for(int i = 0; i < 300; i++)
			longStr += i;
		Movie a = new Movie(null, "", longStr);
		assertEquals(a.getMovieId(),1);
		assertEquals(a.getTitle(),"default title");
		assertEquals(a.getReleaseDate(),"default date");
		assertEquals(a.getUrl(),"default url");
	}
}
