# bakerypackingalgorithm
This project follows a bin packing algorithm to pack list of given bakery order in an optimized way.

# Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

# Prerequisites
JDK 1.8, Maven

# Setup

Install jdk 1.8
Install Maven
run mvn clean package

bakery-0.0.1-SNAPSHOT.jar file will be created under the target folder of the project

Run the application with the following command
java -jar bakery-0.0.1-SNAPSHOT.jar

# Running the tests
Once the application is called, it will prompt for number of structures, number of rooms in each structures and capacity of seniors and juniors.

Output will be printed after the inputs

java -jar bakery-0.0.1-SNAPSHOT.jar
Enter the number of Vegemite Scroll:
10
Enter the Blueberry Muffin:
14
Enter the Croissant:
13

Below is the output of the program
VS5 list is : Pack Of : 5 * 2 : valued at : 17.98 == Full Price : 17.98
MB11 list is : Pack Of : 8 * 1 : valued at : 24.95 and Pack Of : 2 * 3 : valued at : 29.849999999999998 == Full Price : 54.8
CF list is : Pack Of : 5 * 2 : valued at : 19.9 and Pack Of : 3 * 1 : valued at : 5.95 == Full Price : 25.849999999999998

MB11 list can be either 8*1 and 2*3  or 5*2 and 2*2. But right now as per the requirement it is designed as above. If required it can be modified

# Test
unit test code have detailed coverage of sample test cases
BakeryMainTest.java

# Authors
Jeyaprakash Ganesan
