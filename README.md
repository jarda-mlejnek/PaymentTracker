PaymentTracker-1.0-SNAPSHOT
---------------------------

General informations:

PaymentTracker is a simple Java SE apllication that reads currency payments from the command line
and/or the file on the input, registers them and prints the net amount for each currency to
the console repeatedly.

How to build "PaymentTracker":
------------------------------
The application can be build by Maven by. Steps:
	
	1. Go to the application root dir (where pom.xml is located)
	2. Run Maven build command:
	
		$MAVEN_HOME/mvn install
	
	3. The builded application is located in "target" directory

How to build "PaymentTracker":
------------------------------

	java -jar "PaymentTracker-1.0-SNAPSHOT.jar"
	java -jar "PaymentTracker-1.0-SNAPSHOT.jar" /path/to/input/file


This project is developed under Apache license (version 2.0).
