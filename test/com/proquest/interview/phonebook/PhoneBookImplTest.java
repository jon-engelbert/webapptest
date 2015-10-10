package com.proquest.interview.phonebook;

import org.junit.Test;

import com.proquest.interview.util.DatabaseUtil;

public class PhoneBookImplTest {
	@Test
	public void shouldAddFindPerson() {
		DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database
		PhoneBookImpl phoneBook = new PhoneBookImpl();
		Person p = new Person("John Smith", "1234 Sand Hill Dr, Royal Oak, MI", "(248) 123-4567");
		phoneBook.addPersonToCollection(p);
		phoneBook.addPersonToDb(p);
		Person pFound = phoneBook.findPerson("John", "Smith");
		assert(p == pFound);
	}
}
