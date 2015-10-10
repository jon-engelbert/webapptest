package com.proquest.interview.phonebook;

public interface PhoneBook {
	public Person findPerson(String firstName, String lastName);
	public void addPersonToDb(Person newPerson);
	public void addPersonToCollection(Person newPerson);
}
