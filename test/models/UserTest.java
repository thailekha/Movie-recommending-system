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
		User a = new User("Monkey D", "Luffy", "17", "M", "student", "123456");

		assertEquals(a.getUserId(), 1);
		assertEquals(a.getFirstName(), "Monkey D");
		assertEquals(a.getLastName(), "Luffy");
		assertEquals(a.getAge(), "17");
		assertEquals(a.getGender(), "M");
		assertEquals(a.getOccupation(), "student");
		assertEquals(a.getZip(), "123456");
	}

	@Test
	public void testCreateManyUser() {
		User a = new User("Monkey D", "Luffy", "17", "M", "student", "123456");
		User b = new User("Zorro", "Swordman", "19", "M", "student", "78990");

		assertEquals(a.getUserId(), 1);
		assertEquals(a.getFirstName(), "Monkey D");
		assertEquals(a.getLastName(), "Luffy");
		assertEquals(a.getAge(), "17");
		assertEquals(a.getGender(), "M");
		assertEquals(a.getOccupation(), "student");
		assertEquals(a.getZip(), "123456");

		assertEquals(b.getUserId(), 2);
		assertEquals(b.getFirstName(), "Zorro");
		assertEquals(b.getLastName(), "Swordman");
		assertEquals(b.getAge(), "19");
		assertEquals(b.getGender(), "M");
		assertEquals(b.getOccupation(), "student");
		assertEquals(b.getZip(), "78990");
	}

	@Test
	public void testCreateInvalidUser() {
		String longStr = "";
		for(int i = 0; i < 300;i++) {
			longStr += i;
		}
		User a = new User(null, "", null, "       ", longStr, "      123456789      ");
		
		assertEquals(a.getUserId(), 1);
		assertEquals(a.getFirstName(), "default first name");
		assertEquals(a.getLastName(), "default last name");
		assertEquals(a.getAge(), "default age");
		assertEquals(a.getGender(), "F");
		assertEquals(a.getOccupation(), "default occupation");
		assertEquals(a.getZip(), "123456789");
	}
	
	@Test
	public void testEqual() {
		User a = new User("Monkey D", "Luffy", "17", "M", "student", "123456");
		User b = new User("Zorro", "Swordman", "19", "M", "student", "78990");
		
		assertEquals(a,a);
		assertNotEquals(a,b);
	}
}
