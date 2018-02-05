/**
 * 
 */
package com.lobek.education;

import java.util.*;
import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Andrew Oseghale
 *
 */
public class CampusManagement {
	
	private static Logger logger = LogManager.getLogger(CampusManagement.class);
	private static Properties prop = new Properties();
	private static Connection conn = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadProperties("app.xml");
		
		printDashboard();
		printInstructions();
		
		processApp();
		
		closeConnection();
	}
	
	/**
	 * Load the properties from the properties file.
	 * 
	 * @param propertiesFileName
	 */
	protected static void loadProperties(String propertiesFileName) 
	{
		try {
			FileInputStream fis = new FileInputStream(propertiesFileName);
			prop.loadFromXML(fis);
//			prop.list(System.out);
			
			
		} catch (Exception e) {
			;
		}
		
	}
	
	/**
	 * Process the application
	 */
	protected static void processApp() {
		char c; 
		Scanner console = new Scanner(System.in);
		
		do {
			String consoleLine = console.nextLine();
			c = consoleLine.charAt(0);
			
			switch(c) {
				case 'd':
					DepartmentManager dm = new DepartmentManager(getConnection());
					dm.process();
					printDashboard();
					printInstructions();
					break;
					
				case 'c':
					CourseManager cm = new CourseManager(getConnection());
					cm.process();
					printDashboard();
					printInstructions();
					break;
					
				case 'f':
					FacultyManager fm = new FacultyManager(getConnection());
					fm.process();
					printDashboard();
					printInstructions();
					break;
					
				case 's':
					StudentManager sm = new StudentManager(getConnection());
					sm.process();
					printDashboard();
					printInstructions();
					break;
					
				case 'q': // Quit
					System.out.format("%n");
					System.out.println("Thank you for using the Campus Management Application");
					System.out.println("Please come back soon");
					break;
					
				default:
					System.out.println("You entered an unsupported command (" + c + ")");
					
			}
		} while (c != 'q');
		
		console.close();
	}
	
	/**
	 * Prints the dashboard
	 */
	protected static void printDashboard() {
		int deptCount = 0;
		int coursesCount = 0;
		int facultyCount = 0;
		int studentCount = 0;
		
		// get department count
		DepartmentManager dm = new DepartmentManager(getConnection());
		deptCount = dm.getRecordCount();
		
		// get courses count
		CourseManager cm = new CourseManager(getConnection());
		coursesCount = cm.getRecordCount();
		
		// get faculties count
		FacultyManager fm = new FacultyManager(getConnection());
		facultyCount = fm.getRecordCount();
		
		// get students count
		StudentManager sm = new StudentManager(getConnection());
		studentCount = sm.getRecordCount();
		
		System.out.println("===========================================================================");
		System.out.println("|                                                                         |");
		System.out.println("| Welcome to the Campus Management System                                 |");
		System.out.println("|                                                                         |");
		System.out.println("| DASHBOARD                                                               |");
		System.out.println("| +++++++++                                                               |");
		System.out.println("|                                                                         |");
		System.out.println("| +++++++++++++++++++++++                         +++++++++++++++++++++++ |");
		System.out.println("| +                     +                         +                     + |");
		System.out.format ("| +  DEPARTMENTS:  %3d  +                         +  COURSES:      %3d  + |%n", deptCount, coursesCount); 
		System.out.println("| +                     +                         +                     + |");
		System.out.println("| +++++++++++++++++++++++                         +++++++++++++++++++++++ |");
		System.out.println("|                                                                         |");
		System.out.println("| +++++++++++++++++++++++                         +++++++++++++++++++++++ |");
		System.out.println("| +                     +                         +                     + |");
		System.out.format ("| +  FACULTY:      %3d  +                         +  STUDENTS:     %3d  + |%n", facultyCount, studentCount); 
		System.out.println("| +                     +                         +                     + |");
		System.out.println("| +++++++++++++++++++++++                         +++++++++++++++++++++++ |");
		System.out.println("|                                                                         |");
		System.out.println("===========================================================================");
	}
	
	/**
	 * Prints Instructions for the Campus Management Application.
	 */
	private static void printInstructions() {
		System.out.format("%n%n");
		System.out.println("USAGE: enter a command below to perform an operation in the campus database");
		System.out.println("d                           Go to Departments Manager");
		System.out.println("c                           Go to Courses Manager");
		System.out.println("s                           Go to Student Manager");
		System.out.println("f                           Go to Faculty Manager");
		System.out.println("q                           Quit");
		System.out.print("Please enter your command to continue: ");
	}
	
	
	/**
	 * Returns the database connection
	 * 
	 * @return The database connection
	 * 
	 */
	private static Connection getConnection() 
	{
		if (conn == null) {			
	        String url = prop.getProperty("db.connection_string");
	        
	        try {
	            conn = DriverManager.getConnection(url);
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		}
        
        return conn;
	}
	
	/**
	 * Close the database connection
	 */
	private static void closeConnection() 
	{
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
