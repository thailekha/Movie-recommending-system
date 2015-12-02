package controllers;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import models.Fixtures;
import models.User;

public class RecommenderTest {
	
	Recommender r;
	//User uA,uB,uC,uD,uE;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		User.resetCounter();
		r = new Recommender();		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddUser() {
		r.addUser("Monkey D", "Luffy", "17", "M", "student", "123456");
		assertEquals(r.getUsers().size(),1);
		User mirror = r.getUsers().values().iterator().next();
		assertEquals(mirror.getUserId(), 1);
		assertEquals(mirror.getFirstName(), "Monkey D");
		assertEquals(mirror.getLastName(), "Luffy");
		assertEquals(mirror.getAge(), "17");
		assertEquals(mirror.getGender(), "M");
		assertEquals(mirror.getOccupation(), "student");
		assertEquals(mirror.getZip(), "123456");
	}
	
	@Test
	public void testAddManyUser() {
		r.addUser("Monkey D", "Luffy", "17", "M", "student", "123456");
		r.addUser("Zorro", "Swordman", "19", "M", "student", "78990");
		assertEquals(r.getUsers().size(),2);
		
		long mirrorId1 = 1;
		User mirror1 = r.getUsers().get(mirrorId1);
		assertEquals(mirror1.getUserId(), 1);
		assertEquals(mirror1.getFirstName(), "Monkey D");
		assertEquals(mirror1.getLastName(), "Luffy");
		assertEquals(mirror1.getAge(), "17");
		assertEquals(mirror1.getGender(), "M");
		assertEquals(mirror1.getOccupation(), "student");
		assertEquals(mirror1.getZip(), "123456");
		
		long mirrorId2 = 2;
		User mirror2 = r.getUsers().get(mirrorId2);
		assertEquals(mirror2.getUserId(), 2);
		assertEquals(mirror2.getFirstName(), "Zorro");
		assertEquals(mirror2.getLastName(), "Swordman");
		assertEquals(mirror2.getAge(), "19");
		assertEquals(mirror2.getGender(), "M");
		assertEquals(mirror2.getOccupation(), "student");
		assertEquals(mirror2.getZip(), "78990");
	}

	//TODO: not finished
	@Test
	public void testSortUsers() {
		User[] users = Fixtures.getUsersForSort();
		
		for(int i = 0; i < users.length; i++) {
			r.addUser(users[i]);
		}
		ArrayList<Long> ids = r.getUserIdList();
		assertEquals(ids.size(),r.getUsersSize());
		
		for(int i = 0; i < ids.size(); i++) {
			System.out.println(r.getUsers().get(ids.get(i)));
		}
	}
	
	@Test
	public void testRemoveUser() {
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
	}
}
