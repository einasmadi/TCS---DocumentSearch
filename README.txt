It's recommended to use Eclipse in order to view the TargetSearch project.
To use Eclipse:
1- Open the Eclipse IDE and click on the file menu and select import
2- In the import window, select existing projects into workspace
3- Select the TargetSearch folder and press continue

In the Eclipse IDE, you can either run the Driver class or the TestSearchFiles class
The Driver class contains the Search program that prompts the user for a search term and search method
The TestSearchFiles class contains various tests and the 2M searches with random search terms, however, the variable 2M is editted to be 20000 for speed purposes.

The command prompt can also be used to run the Driver class through the jar file.
In order to run the jar file, 
	The files french_armed_forces.txt, hitchhikers.txt and warp_drive.txt must be in the same folder
	run the command: java -jar TargetSearch.java
	OR
	Enter the file names you wish to have a search performed on as arguments
	run the command: java -jar TargetSearch.java fileName1 fileName2 ...

Feel free to email me at einasmadi@yahoo.com for any questions :)