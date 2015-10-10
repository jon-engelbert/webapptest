package com.proquest.interview.phonebook;

public class Person {
	public String name;
	public String phoneNumber;
	public String address;

	public Person() {
		super();
	}
	public Person(String name, String phoneNumber, String address) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	@Override
	public String toString() {
		return "name=" + name + ", phoneNumber=" + phoneNumber
				+ ", address=" + address;
	}
}
