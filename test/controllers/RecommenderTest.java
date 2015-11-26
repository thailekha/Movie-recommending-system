package controllers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		
		User mirror1 = r.getUsers().values().iterator().next();
		assertEquals(mirror1.getUserId(), 1);
		assertEquals(mirror1.getFirstName(), "Monkey D");
		assertEquals(mirror1.getLastName(), "Luffy");
		assertEquals(mirror1.getAge(), "17");
		assertEquals(mirror1.getGender(), "M");
		assertEquals(mirror1.getOccupation(), "student");
		assertEquals(mirror1.getZip(), "123456");
	}

}
