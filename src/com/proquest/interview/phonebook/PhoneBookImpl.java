package com.proquest.interview.phonebook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.proquest.interview.util.DatabaseUtil;

/**
 * @author jonengelbert
 *
 */
public class PhoneBookImpl implements PhoneBook {
	public List<Person> people = new ArrayList<Person>();
	

	/**
	 * initialize phonebook list from database records?
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
	
	public void addPersonToCollection(Person newPerson) {
		if (!people.contains(newPerson))
			people.add(newPerson);
	}
	
	@Override
	public void addPersonToDb(Person newPerson) {
		try (Connection cn = DatabaseUtil.getConnection(); Statement stmt = cn.createStatement()) {
			String queryString = "INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES('"
					+ newPerson.name
					+ "', '"
					+ newPerson.phoneNumber
					+ "', '"
					+ newPerson.address + "')";
			stmt.execute(queryString);
			cn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * find person using name (firstname + lastName) using Phonebook database
	 * Why not use phonebook list?
	 */
	@Override
	public Person findPerson(String firstName, String lastName)  {
		Person newPerson = new Person();
		try (Connection cn = DatabaseUtil.getConnection(); Statement stmt = cn.createStatement()) {
			String queryString = "SELECT NAME, PHONENUMBER, ADDRESS FROM PHONEBOOK WHERE NAME = '" + firstName + " " + lastName + "'";
			ResultSet rs = stmt.executeQuery(queryString);
			if (rs.next()) {
			    newPerson.name = rs.getString("NAME");
			    newPerson.phoneNumber = rs.getString("PHONENUMBER");
			    newPerson.address = rs.getString("ADDRESS");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newPerson;
	}

	
	/**
	 * generate Phonebook output string from database
	 * Why not use list?
	 */
	@Override
	public String toString() {
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
	

	public void PrintPeopleFromList() {
		System.out.println("People from list:");
		for (Person p : people) {
			System.out.println(p.toString());
		}
	}
	
	public static void main(String[] args) {
		DatabaseUtil.initDB();  //You should not remove this line, it creates the in-memory database
		PhoneBookImpl phoneBook = new PhoneBookImpl();

		/* Initialize list of people from database 
		*/
		phoneBook.InitializeList();
		phoneBook.PrintPeopleFromList();
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
		System.out.println("Phone book after adding John & Cynthia Smith");
		System.out.println(phoneBook.toString());

		// TODO: find Cynthia Smith and print out just her entry
		Person foundPerson = phoneBook.findPerson("Cynthia", "Smith");
		System.out.println("");
		System.out.println("Cynthia Smith query result:");
		System.out.println(foundPerson.toString());
		
		// TODO: insert the new person objects into the database
		p = new Person("John Harbaugh", "(800) MGO-BLUE", "1 N. Main St, Ann Arbor, MI");
		phoneBook.addPersonToDb(p);
		System.out.println("");
		System.out.println("Phone book after adding John Harbaugh: ");
		System.out.println(phoneBook.toString());
	}
}
