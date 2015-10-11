package com.proquest.interview.phonebook;

import com.proquest.interview.exceptions.AddPersonException;

public interface PhoneBook {
	public Person findPerson(String firstName, String lastName);
	public void addPerson(Person newPerson);
	public void addPersonToDb(Person newPerson) throws AddPersonException;
	public void addPersonToCollection(Person newPerson);
	public void InitializeList();
}
