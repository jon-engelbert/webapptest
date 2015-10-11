package com.proquest.interview.phonebook;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.proquest.interview.util.DatabaseUtil;

public class PhoneBookImplTest {
    @BeforeClass
    public static void initialize() {
		DatabaseUtil.initDB();  
	}
    @Test
	public void shouldAddFindPerson() {
		PhoneBookImpl phoneBook = new PhoneBookImpl();
		Person p = new Person("John Smith", "(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
		phoneBook.addPerson(p);
		Person pFound = phoneBook.findPerson("John", "Smith");
		assertEquals(p, pFound);
	}
//    Just for fun... test prepared statements to see how they work with sql injection attempt
	@Test
	public void shouldPreventSqlInjectionFind() {
		PhoneBookImpl phoneBook = new PhoneBookImpl();
		Person p = new Person("jimmy connors", "(248) 123-3210", "1234 blue bay Dr, detroit, MI");
		phoneBook.addPersonToDb(p);
		Person pFound = phoneBook.findPerson("' or", " '1'='1");
		assertNull(pFound);
	}
	
//  Just for fun... test prepared statements to see how they work with sql injection attempt
	@Test
	public void shouldPreventSqlInjectionInsert() {
		PhoneBookImpl phoneBook = new PhoneBookImpl();
		Person p = new Person("johnny mac", "(BIG) MC-ENROE" , "'); DROP TABLE customer; --");
		phoneBook.addPersonToDb(p);
		Person pFound = phoneBook.findPerson("johnny", "mac");
		assertEquals(p,pFound);
	}
	
}
