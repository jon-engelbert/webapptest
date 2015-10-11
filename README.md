#webapptest
-----------
###Test for interviews

This test is designed to give an overview of your capabilities as a java developer.  The ultimate goal of the test is spelled out in the main method of src/com/proquest/interview/phonebook/PhoneBookImpl.

Implement the TODO comments to the best of your ability.  In addition, you should be looking at how to improve this program.  The DatabaseUtil class should not need to be significantly refactored, although you may do so as long as you do not destroy it's functionality.

You can download this project, make your changes, zip/tar it up and send it along with your resume, or if you want to impress us, fork it, put your changes in, and send along the url.

This test should take less than an hour.  You WILL be given another (quite different) test if you interview here, so please don't bother cheating.

Thank you,
Rob Conklin

-------------

##Jon Engelbert's additions
###Questions:
Ideally, I'd run these by the product owner (Rob Conklin) before starting.
* What is the purpose of this product?  
* How big might this phonebook get?
* Is the product going to be used by multiple clients ?
* How much load do you expect, i.e. how many clients at a time?

###Assumptions 
* Eventually, the phonebook will be stored in a database on one or more servers... and there will be infrastructure to keep the database consistent so that the phonebook application can act as if the database is a single consistent entity.
* Eventually, multiple instances of this phonebook executable will run at the same time on one or more servers.
* The variable "people" in the PhoneBook class should always have the same content as the database table named PhoneBook.
* For quick response to common queries, especially when the very latest data is not critical, a heap copy of the phonebook (e.g., a List) could be used.  .... this heap copy of the phonebook, or cache, will occassionally need to be updated from the database.  That could perhaps be done through messaging (e.g. RabbitMQ) or by reloading the phonebook from the database at regular intervals.  
* However, we must be aware that different instances of this phonebook executable will have different heaps, and adding a member to one instance's heap copy of the phonebook won't update the local phonebook in the heap of other running instances.

###The original instructions:
* "create person objects and put them in the PhoneBook and database..."
* "print the phone book out to System.out"
* "find Cynthia Smith and print out just her entry"
* "insert the new person objects into the database"

###Modified instructions:
* synchronize the local phonebook with the database copy
* "create new person objects and put them in the PhoneBook List and PhoneBook database table..."
* "print the phone book out to System.out"
* "find Cynthia Smith and print out just her entry"
* "insert another new person object into the database" 

###User stories
* As a phonebook admin
, I'd like to add a new person to the phonebook
, In order to build a comprehensive phonebook

* Given an existing phonebook and a new person
, When the user adds a new person
, Then the phonebook is modified to contain the new user

* Given the phonebook  
, When the program is beginning
, Then the local phonebook List is synchronized from the database

* As a user
, I'd like to see a person's address/phone
, In order to contact that person.

* Given the phonebook  and the person's name
, When the user requests contact info for a person
, Then the person's contact information is displayed

* As a user
, I'd like to a listing of the address/phone of the entire phonebook
, In order to print out a directory.

* Given the phonebook 
, When the user requests a print-out
, Then a phonebook listing is generated of all users.

