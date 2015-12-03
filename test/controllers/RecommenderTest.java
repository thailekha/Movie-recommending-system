package controllers;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.Fixtures;
import models.Movie;
import models.User;

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
			assertEquals(r.getUsers().size(),1);
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
			assertEquals(r.getUsers().size(),2);
			
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

	//TODO: not finished
	@Test
	public void testSortUsers() {
		try {
			User[] users = Fixtures.getUsersForSort();
			for(int i = 0; i < users.length; i++) {
				r.addUser(users[i]);
			}
			ArrayList<Long> ids = r.getUserIdList();
			assertEquals(ids.size(),r.getUsersSize());
			
			for(int i = 0; i < ids.size(); i++) {
				System.out.println(r.getUsers().get(ids.get(i)));
			}
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}
	
	@Test
	public void testRemoveUser() {
		try {
			User[] users = Fixtures.getUsers();
			for(int i = 0; i < users.length; i++)
				r.addUser(users[i]);
			long id = User.getCounter() - 1;
			int size = r.getUsersSize();
			for(int i =  r.getUsersSize(); i > 0; i--) {
				User removedUser = r.removeUser(id);
				assertEquals(--size,r.getUsersSize());
				assertEquals(removedUser,users[(int) id - 1]);
				id--;
			}
			assertEquals(r.getUsersSize(),0);
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}
	
	@Test
	public void testGetMovie() {
		Movie[] movies;
		try {
			movies = Fixtures.getMovies();
			for(int i = 0; i < movies.length; i++) {
				r.addMovie(movies[i]);
			}
			Random rand = new Random();
			for(int i = 0; i < 10000; i++){
				int index = rand.nextInt(movies.length);
				long id = (long) index + 1;
				assertEquals(movies[index],r.getMovie(id));
			}
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}
}
