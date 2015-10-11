package com.proquest.interview.phonebook;

import java.util.Map;

import com.proquest.interview.exceptions.AddPersonException;

public interface PhoneBook {
	public Person findPerson(String firstName, String lastName);
	public Person findPersonFromDb(String firstName, String lastName);
	public void addPerson(Person newPerson);
	public void addPersonToDb(Person newPerson) throws AddPersonException;
	public void addPersonToCollection(Person newPerson);
	public String toStringFromDb();
	public String toString();
	public Map<String, Person> getPeople();
	public void InitializeList();
}
