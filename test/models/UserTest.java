package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserTest {
	// String firstName, String lastName, int age, String gender, String
	// occupation, String zip

	@Before
	public void setUp() throws Exception {
		User.resetCounter();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateUser() {
		try {
			User a = new User("Monkey D", "Luffy", 17, "M", "student", "123456");
			assertEquals(a.getUserId(), 1);
			assertEquals(a.getFirstName(), "Monkey D");
			assertEquals(a.getLastName(), "Luffy");
			assertEquals(a.getAge(), 17);
			assertEquals(a.getGender(), "M");
			assertEquals(a.getOccupation(), "student");
			assertEquals(a.getZip(), "123456");
			
			User mirror =  new User("Monkey D", "Luffy", 17, "M", "student", "123456");
			User b = new User("Zorro", "Swordman", 19, "M", "student", "78990");
			
			assertEquals(a,mirror);
			assertNotEquals(a,b);
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testCreateManyUser() {
		try {
			User a = new User("Monkey D", "Luffy", 17, "M", "student", "123456");
			User b = new User("Zorro", "Swordman", 19, "M", "student", "78990");
			assertEquals(a.getUserId(), 1);
			assertEquals(a.getFirstName(), "Monkey D");
			assertEquals(a.getLastName(), "Luffy");
			assertEquals(a.getAge(), 17);
			assertEquals(a.getGender(), "M");
			assertEquals(a.getOccupation(), "student");
			assertEquals(a.getZip(), "123456");

			assertEquals(b.getUserId(), 2);
			assertEquals(b.getFirstName(), "Zorro");
			assertEquals(b.getLastName(), "Swordman");
			assertEquals(b.getAge(), 19);
			assertEquals(b.getGender(), "M");
			assertEquals(b.getOccupation(), "student");
			assertEquals(b.getZip(), "78990");
		} catch (Exception e) {
			fail("Exception thrown");
		}
	}

	@Test
	public void testCreateInvalidUser() {
		
		User a;
		try {
			String longStr = "";
			for(int i = 0; i < 300;i++) {
				longStr += i;
			}
			a = new User(null, "", 999, "       ", longStr, "      123456789      ");
			fail("Should have thrown an Exception");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
}
