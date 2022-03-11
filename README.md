# VechicleInventoryMVC

Vehicle Inventory Management System Project - Andy Szeto

// —————————————————————————————————————————————————————————————————————- >
Background: After graduating college, I started to relearn Python and Java. This project started as a Python app that ran through the IDE console and stored data in an excel spreadsheet. My desire to learn backend development and proficiency in Java led me to changing the language this app was written in. After learning about the MVC design pattern, I was able to produce the model and view components in Java, but my grasp on Controllers (HttpSerlvet specicially) wasn't so great, so I decided to learn Spring in order to make my application more robust and modular. This is by far the largest software project I've worked on as a programmer, but I hope to grow and eventually use this knowledge in the industry.

Abstract: To create a program that manages vehicle inventory data for a car dealership. This project teaches me about Java development at the enterprise level by implementing technologies offered by Spring. I am focusing on the Model-View-Controller design pattern with interactions between three main entities (Vehicle, CustomerAccount, FinanceRecords). 

// —————————————————————————————————————————————————————————————————————- >
Tools/Technologies:
IDE: Eclipse 
Build tools: Maven
Frameworks: Spring Core, Hibernate
Database: MySQL + MySQLWorkbench, JDBC
Server: Apache Tomcat 
View layer: HTML/CSS + JSP, JSTL

// —————————————————————————————————————————————————————————————————————- >
Phase 1: Creating a vehicle object.

The fields of the object contain data that is relevant to the consumer. These fields are to be stored in the database. Here, basic principles of OOP such as encapsulation with the get and set methods are employed. Because the object has so many fields, I found that using the creational builder design pattern would help simply and organize the instantiation of the vehicle object. 3/8/22 update: I've found that because Spring's dependency injection offers inversion of control, the components of the builder pattern within the Vehicle class may be unecessary. 

Vehicle fields:

year (int)
mileage (int)

vehicleIdNumber(String)
make (String)
model(String)
condition(String)
interiorColor(String)
exteriorColor(String)
titleStatus (String)
drivetrainType (String)
transmissionType (String)
fuelType (String)

price (double)

12/30/21 update: 
A user class is created. This may be refactored to 'Customer' later. This entity is used to keep track of customers who have purchased/financed a vehicle.
3/8/22 update: This has been refactored to 'CustomerAccounts'.

CustomerAccount Fields

customerId (int)
Username (String)
Password (String)
FirstName (String)
LastName (String)
emailAddress (String)
mailingAddress (String)
phoneNumber (int)

// —————————————————————————————————————————————————————————————————————- >
Phase 2: 

Using jdbc as the driver connector for mySQL, it becomes possible to permanentize and update the states of the vehicle object. 

Here, I learned how to set up the mySQL server and instances of databases which I can manage through mySQLworkbench. I had familiarized myself with SQL about two years ago so half of this was re-learned. Within databases, I dove into schemas and learned how to populate tables within the schemas. The class VehicleDbComm.java is design to setup a connection to the database from the Eclipse. From here specific queries will be writing to do things such as display data for a specific vehicle(s) and also be able to update the data whenever the user wishes.

At the moment of writing this, I have thought about splitting the table I’ve created in mySQL into several smaller tables; I’ll have to see if there are any advantages to this. 

12/30/21 update: 
DAO’s are written for each the user and the vehicle object with CRUD operations. 

// —————————————————————————————————————————————————————————————————————- >
Phase 3: connect logic to front end.

One idea is to separate the methods by search and update/edit.

Search function ideas:
-Print all vehicles in DB
-Look up by VIN
-Look up by category
-Combining more than one category
-Use some kind of data structure to store each search category and use a function to convert it into a query

Update function ideas:
-Use VIN to update a specific vehicle

12/30/21 update: 
Integrating code into the view layer using JSP displays all data from both tables.


// —————————————————————————————————————————————————————————————————————- >
Furthering the project (ideas):

-Use (scraping) to utilize a function to grab the (first) image of the car when description is entered (i.e. “black 1991 Honda Civic”)
-Embed to a website
-Create graphics for application; find out how to incorporate images (with javaFX?)

I’m realizing the redundancy of creating a vehicle object and having a database. Perhaps I can store the vehicle objects in a data structure and use them to somehow make my program more efficient? Maybe having “two copies” of the same data can provide some insurance incase the database server goes down/is offline.

// —————————————————————————————————————————————————————————————————————- >
Changelogs

11/10/21:
Up until now, I haven’t been documenting the changes and ideas chronologically. I will be doing so moving forward to track my progress in detail. 

I have been refining the Vehicle class this week to ensure type compatibility when writing to the database and the current structure/complexity of the object is satisfactory for now. 

I have established that there will be a separate client file that will call the methods from the database communication file. There are many similarities between methods written for the query methods (especially the search methods). Adding features such as ordering data adds a need for another method so I will have to figure out how to refactor parts of the code as I did with the establishConnection() method. 


12/30/21:
This project has been revamped once again to include user accounts that can be associated with vehicles in the lot. Using JSP and the MVC model, html/css can be used to display data in a web browser so I have writing object models for both the vehicles and the users, which I have created database access layers for each. Another table in the database was created for the users. Launching the now web app on the Tomcat Server lists all user and vehicle details from the database. Its important to note that each time the database reads a user/vehicle, an object is usually returned. Each DAO has CRUD functionality. I am looking to add options and create JSP forms that will enable the client to edit details about the users/vehicles. After this, the controller component is the in line to be created.

The next major goal is to create another table that lists any offers or currently financed vehicle. Also another table for cars that have been sold.

3/8/22:
After spending time learning Spring and how to better use Maven, I am able to turn this into a dynamic web MVC application with annotation based configuration. I have started to overhaul a lot of the code in the entity and DAO layers. I have implemented the basic CRUD functions and I am working on connecting the User Account and Finance Record entities to the Vehicle object. More to come...

3/9/22:
I have refactored much of the FinanceRecord class so that the loan calculations and fields work the way I desired. For simplicity's sake, the customer will be limited to making monthly payments of exact amounts, this is calculated by the amortization formula A = principal * ((apr * ((1 + apr) ^ termLength)/(((1 + apr) ^ termLength) - 1))). 
