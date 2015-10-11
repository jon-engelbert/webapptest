package com.proquest.interview.phonebook;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.proquest.interview.util.DatabaseUtil;

public class PhoneBookImpl implements PhoneBook {
	public List<Person> people = new ArrayList<Person>();
	

	/**
	 * synchronize phonebook list from database records
	 */
	private void InitializeList() {
		try (Connection cn = DatabaseUtil.getConnection(); Statement stmt = cn.createStatement()) {
			String queryString = "SELECT NAME, PHONENUMBER, ADDRESS FROM PHONEBOOK";
			ResultSet rs = stmt.executeQuery(queryString);
			while (rs.next()) {
				Person newPerson = new Person(rs.getString("NAME"), rs.getString("PHONENUMBER"), rs.getString("ADDRESS"));
			    addPersonToCollection(newPerson);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void addPerson(Person newPerson) {
		if (addPersonToDb(newPerson))
			addPersonToCollection(newPerson);
		
	}
	@Override
	public void addPersonToCollection(Person newPerson) {
		if (!people.contains(newPerson))
			people.add(newPerson);
	}
	
	@Override
	public boolean addPersonToDb(Person newPerson) {
		try (Connection cn = DatabaseUtil.getConnection()) {
			String queryString = "INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES(?, ?, ?)";
			PreparedStatement stmt = cn.prepareStatement(queryString);
			stmt.setString(1,  newPerson.getName());
			stmt.setString(2,  newPerson.getPhoneNumber());
			stmt.setString(3,  newPerson.getAddress());
			stmt.executeUpdate();
			cn.commit();
		} catch (SQLException e) {
			System.out.println("addPerson to database failed");
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e1) {
			System.out.println("addPerson to database failed");
			e1.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * find person using name (firstname + lastName) using Phonebook database
	 * perhaps use phonebook list?  
	 */
	@Override
	public Person findPerson(String firstName, String lastName)  {
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

	
	/**
	 * generate Phonebook output string from database
	 * Why not use list?
	 */
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
	public String toString() {
		String str = "";
		for (Person p : people) {
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
		phoneBook.InitializeList();
		System.out.println("Initial phone book (from List)");
		System.out.println(phoneBook.toString());
		/* TODO: create person objects and put them in the PhoneBook and database
		 * John Smith, (248) 123-4567, 1234 Sand Hill Dr, Royal Oak, MI
		 * Cynthia Smith, (824) 128-8758, 875 Main St, Ann Arbor, MI
		*/ 
		Person p = new Person("John Smith", "(248) 123-4567", "1234 Sand Hill Dr, Royal Oak, MI");
		phoneBook.addPersonToCollection(p);
		phoneBook.addPersonToDb(p);
		p = new Person("Cynthia Smith", "(824) 128-8758", "875 Main St, Ann Arbor, MI");
		phoneBook.addPersonToCollection(p);
		phoneBook.addPersonToDb(p);
		// TODO: print the phone book out to System.out
		System.out.println("\nPhone book (from database) after adding John & Cynthia Smith");
		System.out.println(phoneBook.toStringFromDb());

		// TODO: find Cynthia Smith and print out just her entry
		Person foundPerson = phoneBook.findPerson("Cynthia", "Smith");
		System.out.println("\nCynthia Smith query result:");
		System.out.println(foundPerson.toString());
		
		// TODO: insert the new person objects into the database
		p = new Person("John Harbaugh", "(800) MGO-BLUE", "1 N. Main St, Ann Arbor, MI");
		phoneBook.addPersonToDb(p);
		System.out.println("\nPhone book after adding John Harbaugh: ");
		System.out.println(phoneBook.toString());
	}
}
