package com.proquest.interview.phonebook;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.proquest.interview.util.DatabaseUtil;

public class PhoneBookImplTest {
	PhoneBook phoneBook = null;
    @Before
    public  void initialize() {
		DatabaseUtil.initDB(); 
		phoneBook = new PhoneBookImpl();
		phoneBook.InitializeList();
	}
    @Test
	public void shouldAddFindPerson() {
		Person p = new Person("John Smith", "(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
		phoneBook.addPerson(p);
		Person pFound = phoneBook.findPerson("John", "Smith");
		assertEquals(p, pFound);
	}
    
    @Test
	public void shouldAddFindPersonFromDb() {
		Person p = new Person("John Smith", "(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
		phoneBook.addPerson(p);
		Person pFound = phoneBook.findPersonFromDb("John", "Smith");
		assertEquals(p, pFound);
	}
    
    @Test
	public void shouldFindInitialPeople() {
		Person pFound = phoneBook.findPerson("Chris", "Johnson");
		assertNotNull(pFound);
		assertEquals(2, phoneBook.getPeople().size());
	}
    
    @Test
	public void ExpectToStringToGenerateListingOfPhonebook() {
		Assert.assertThat(phoneBook.toStringFromDb(), CoreMatchers.containsString("name=Chris Johnson, phoneNumber=(321) 231-7876, address=452 Freeman Drive, Algonac, MI"));
		Assert.assertThat(phoneBook.toStringFromDb(), CoreMatchers.containsString("name=Dave Williams, phoneNumber=(231) 502-1236, address=285 Huron St, Port Austin, MI"));
		Assert.assertThat(phoneBook.toString(), CoreMatchers.containsString("name=Chris Johnson, phoneNumber=(321) 231-7876, address=452 Freeman Drive, Algonac, MI"));
		Assert.assertThat(phoneBook.toString(), CoreMatchers.containsString("name=Dave Williams, phoneNumber=(231) 502-1236, address=285 Huron St, Port Austin, MI"));
}
    
//    Just for fun... test prepared statements to see how they work with sql injection attempt
	@Test
	public void shouldPreventSqlInjectionFind() {
		Person p = new Person("jimmy connors", "(248) 123-3210", "1234 blue bay Dr, detroit, MI");
		phoneBook.addPerson(p);
		Person pFound = phoneBook.findPerson("' or", " '1'='1");
		assertNull(pFound);
	}
	
//  Just for fun... test prepared statements to see how they work with sql injection attempt
	@Test
	public void shouldPreventSqlInjectionInsert() {
		Person p = new Person("johnny mac", "(BIG) MC-ENROE" , "'); DROP TABLE customer; --");
		phoneBook.addPerson(p);
		Person pFound = phoneBook.findPerson("johnny", "mac");
		assertEquals(p,pFound);
	}
	
}
