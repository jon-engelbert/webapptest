package com.proquest.interview.phonebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.proquest.interview.util.DatabaseUtil;
import com.proquest.interview.exceptions.AddPersonException;

public class PhoneBookImpl implements PhoneBook {
	private Map<String, Person> people = new HashMap<String, Person>();
	
	@Override
	public Map<String, Person> getPeopleFromCache() {
		return people;
	}


	public void setPeople(HashMap<String, Person> people) {
		this.people = people;
	}
	
	/**
	 * synchronize phonebook list from database records
	 */
	public Map<String, Person> getPeopleFromDb() {
		try (Connection cn = DatabaseUtil.getConnection(); Statement stmt = cn.createStatement()) {
			String queryString = "SELECT NAME, PHONENUMBER, ADDRESS FROM PHONEBOOK";
			ResultSet rs = stmt.executeQuery(queryString);
			while (rs.next()) {
				Person newPerson = new Person(rs.getString("NAME"), rs.getString("PHONENUMBER"), rs.getString("ADDRESS"));
			    addPersonToCache(newPerson);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return people;
	}
	

	@Override
	public void addPerson(Person newPerson) {
		try {
			addPersonToDb(newPerson);
		} catch (AddPersonException e) {
			System.out.println("addPerson failed: " + newPerson.getName());
			e.printStackTrace();
		}
		addPersonToCache(newPerson);
		
	}
	@Override
	public void addPersonToCache(Person newPerson) {
		if (!people.containsKey(newPerson.getName()))
			people.put(newPerson.getName(), newPerson);
	}
	
	@Override
	public void addPersonToDb(Person newPerson) throws AddPersonException  {
		try (Connection cn = DatabaseUtil.getConnection()) {
			String queryString = "INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES(?, ?, ?)";
			PreparedStatement stmt = cn.prepareStatement(queryString);
			stmt.setString(1,  newPerson.getName());
			stmt.setString(2,  newPerson.getPhoneNumber());
			stmt.setString(3,  newPerson.getAddress());
			stmt.executeUpdate();
			cn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AddPersonException("sql Exception");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			throw new AddPersonException("sql Exception");
		}
	}
	
	/**
	 * find person using name (firstname + lastName) using Phonebook database
	 * perhaps use phonebook list?  
	 */
	@Override
	public Person findPersonFromDb(String firstName, String lastName)  {
		Person newPerson = new Person();
		try (Connection cn = DatabaseUtil.getConnection()) {
			String queryString = "SELECT NAME, PHONENUMBER, ADDRESS FROM PHONEBOOK WHERE NAME = ?";
			PreparedStatement stmt = cn.prepareStatement(queryString);
			stmt.setString(1, firstName + " " + lastName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
			    newPerson.setName(rs.getString("NAME"));
			    newPerson.setPhoneNumber(rs.getString("PHONENUMBER"));
			    newPerson.setAddress(rs.getString("ADDRESS"));
			} else
				newPerson = null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("findPerson failed");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("findPerson failed");
			e.printStackTrace();
		}
		return newPerson;
	}
	
	@Override
	public Person findPersonFromCache(String firstName, String lastName) {
		Person newPerson = null;
		newPerson = people.get(firstName + " " + lastName);
		return newPerson;
	}

	
	/**
	 * generate Phonebook output string from database
	 * Why not use list?
	 */
	@Override
	public String toStringFromDb() {
		String str = "";
		try (Connection cn = DatabaseUtil.getConnection(); Statement stmt = cn.createStatement()) {
			String queryString = "SELECT NAME, PHONENUMBER, ADDRESS FROM PHONEBOOK";
			ResultSet rs = stmt.executeQuery(queryString);
			while (rs.next()) {
				Person newPerson = new Person(rs.getString("NAME"), 
						rs.getString("PHONENUMBER"),
						rs.getString("ADDRESS"));
			    if (!str.isEmpty())
			    	str += "\n";
			    str += newPerson.toString();
			}
//					cn.close(); handled by try with catch
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	

	@Override
	public String toStringFromCache() {
		String str = "";
		for (Person p : people.values()) {
		    if (!str.isEmpty())
		    	str += "\n";
			str += (p.toString());
		}
		return str;
	}
	
	public static void main(String[] args) {
		DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database
		PhoneBookImpl phoneBook = new PhoneBookImpl();

		/* Initialize list of people from database 
		*/
		phoneBook.getPeopleFromDb();
		System.out.println("Initial phone book (from List)");
		System.out.println(phoneBook.toString());
		/* TODO: create person objects and put them in the PhoneBook and database
		 * John Smith, (248) 123-4567, 1234 Sand Hill Dr, Royal Oak, MI
		 * Cynthia Smith, (824) 128-8758, 875 Main St, Ann Arbor, MI
		*/ 
		Person p = new Person("John Smith", "(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
		phoneBook.addPerson(p);
		p = new Person("Cynthia Smith", "(824) 128-8758", "875 Main St, Ann Arbor, MI");
		phoneBook.addPerson(p);
		// TODO: print the phone book out to System.out
		System.out.println("\nPhone book (from database) after adding John & Cynthia Smith");
		System.out.println(phoneBook.toStringFromDb());

		// TODO: find Cynthia Smith and print out just her entry
		Person foundPerson = phoneBook.findPersonFromCache("Cynthia", "Smith");
		System.out.println("\nCynthia Smith query result:");
		System.out.println(foundPerson.toString());
		
		// TODO: insert the new person objects into the database (and the phonebook member / list!)
		p = new Person("Jim Harbaugh", "(800) MGO-BLUE", "1 N. Main St, Ann Arbor, MI");
		phoneBook.addPerson(p);
		System.out.println("\nPhone book after adding Jim Harbaugh: ");
		System.out.println(phoneBook.toStringFromCache());
	}



}
