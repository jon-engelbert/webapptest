package com.proquest.interview.phonebook;

import java.util.Map;

import com.proquest.interview.exceptions.AddPersonException;

public interface PhoneBook {
	public Person findPersonFromCache(String firstName, String lastName);
	public Person findPersonFromDb(String firstName, String lastName);
	public void addPerson(Person newPerson);
	public void addPersonToDb(Person newPerson) throws AddPersonException;
	public void addPersonToCache(Person newPerson);
	public String toStringFromDb();
	public String toStringFromCache();
	public Map<String, Person> getPeopleFromCache();
	public Map<String, Person> getPeopleFromDb();
}
