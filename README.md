Haulmont Test Task
==================

This repository contains a source code of the Web application written using Java, Vaadin, JDBC and HSQLDB database.
This Web application is a system of input and display information about the students of the Institute.
The appearance of the application user interface is shown below.

![screenshots](ui/screenshots.gif)

Separately screenshots: [main](ui/screen-1.png)   [groups](ui/screen-2.png)   [students](ui/screen-3.png)   [add group](ui/screen-4.png)   [add student](ui/screen-5.png)

Prerequisites   
-------------

* [Java Development Kit (JDK) 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3](https://maven.apache.org/download.cgi)

Build and Run
-------------

1. Run in the command line:
	```
	mvn package
	mvn jetty:run
	```
	Note: to successful execution of the unit tests at step "mvn package" it is necessary that Jetty server was stopped before this step.

2. Open `http://localhost:8080` in a web browser.
