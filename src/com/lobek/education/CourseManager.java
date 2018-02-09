package com.lobek.education;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * 
 * @author Andrew Oseghale
 *
 */
public class CourseManager implements DataTypeManager {
	
	private Connection conn;

	public CourseManager(Connection conn) {
		this.conn = conn;
	}
	
	public int getRecordCount() {
		int rtn = 0;
		
		try {
			Statement stmt = this.conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM course");
			rs.next();
			int rowCount = rs.getInt(1);
			
			rtn = rowCount;
		} catch (Exception e) {
			;
		}
		
		return rtn;
	
	}
	
	public void process() {
		boolean done = false;
		
		printInstructions();
		
		char c; 
		Scanner console = new Scanner(System.in);
		
		do {
			String consoleLine = console.nextLine();
			c = consoleLine.charAt(0);
			
			switch(c) {
				case 'l':
					listAll();
					System.out.format("%n%n");
					printInstructions();
					break;
				case 'a':
					addNewCourse();
					System.out.format("%n%n");
					printInstructions();
					break;
				case 'd':
					System.out.print("Please enter the course id to be deleted:\t ");
					Scanner scn = new Scanner(System.in);
					int id = scn.nextInt();
					deleteCourse(id);
					System.out.format("%n%n");
					printInstructions();
					break;
				case 'm':
				try {
					modifyCourse();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					System.out.format("%n%n");
					printInstructions();
					break;
										
				case 'b': // Go Back
					break;
					
				default:
					System.out.println("You entered an unsupported command (" + c + ")");
					
			}
		} while (c != 'b');
	console.close();
	}
	
	private void printInstructions() {
		System.out.format("%n%n");
		System.out.println("=======================================================================");
		System.out.format ("| COURSES MANAGER (TOTAL RECORDS: %3d)                                |%n", getRecordCount());
		System.out.println("=======================================================================");
		System.out.println("USAGE: enter a command below to manage the courses");
		System.out.println("l                           List all courses");
		System.out.println("a                           Add a new course");
		System.out.println("d                           Delete a course");
		System.out.println("m                           Modify a course");
		System.out.println("b                           Go Back");
		System.out.print("Please enter your command to continue: ");
	}
	
	public void listAll() {
				
			try {
			Statement stmt = this.conn.createStatement();
			String descriptionOut = "";
			ResultSet rs = stmt.executeQuery("SELECT * FROM course");
			while (rs.next()) {
				int id = rs.getInt("id");
				String department = rs.getString("dept_id");
				String courseCode = rs.getString("code");
				String courseName = rs.getString("name");
				String description = rs.getString("description");
				if(description.length() > 20) {
				   descriptionOut = description.substring(0,20) + "...";
				}
				float courseCredits = rs.getFloat("credits");
				
				System.out.format("\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\"\n", id, department, courseCode, courseName, descriptionOut, courseCredits);
			}
			stmt.close();
			} catch (Exception e) {
				System.err.println("Got an exception!");
				System.err.println(e.getMessage());
		}
		
		
	}
	public void addNewCourse() {
		
		try {
			Scanner scn = new Scanner(System.in);
			System.out.print("Please enter the course details below:\n ");
			System.out.print("Dept ID: ");
			String dept_id = scn.nextLine();
			System.out.print("Code: ");
			String code = scn.nextLine();
			System.out.print("Name: ");
			String name = scn.nextLine();
			System.out.print("Description: ");
			String description = scn.nextLine();
			System.out.print("Credits: ");
			float credits = scn.nextFloat();
			
			scn.close();			
			
			String sql = ("INSERT INTO course(dept_id, code, name, description, credits) VALUES(?,?,?,?,?)");
			PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
			preparedStatement.setString(1, dept_id);
			preparedStatement.setString(2, code);
			preparedStatement.setString(3, name);
			preparedStatement.setString(4, description);
			preparedStatement.setFloat(5, credits);
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
			conn.close();
			System.out.print("Thank you for adding a new course!\n ");
			} catch (SQLException e) {
            System.out.println(e.getMessage());
			}
            catch (Exception e) {
				System.err.println("Got an exception!");
				System.err.println(e.getMessage());
		}
		   
        }
		
	public void deleteCourse(int id) {
		
		String sql = "DELETE FROM course WHERE id = ?";
		
		try {
			PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		System.out.print("The course has been deleted!\n ");
	}
	public void modifyCourse() throws SQLException {
		
		Scanner scn = new Scanner(System.in);
		System.out.print("Please enter the course id to be modified: ");
		int idModify = scn.nextInt();
			try {
			
			Statement stmt = this.conn.createStatement();
			System.out.println(idModify);
		ResultSet rs = stmt.executeQuery("SELECT * FROM course WHERE id = '" + idModify + "';");
		
			int id = rs.getInt("id");
			String department = rs.getString("dept_id");
			String courseCode = rs.getString("code");
			String courseName = rs.getString("name");
			String description = rs.getString("description");
					
			System.out.format(" ID: %s\n Department ID: %s\n Code: %s\n Name: %s\n Description: %s\n", id, department, courseCode, courseName, description);
		
		stmt.close();
		} catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
	} 
		System.out.println("Enter a command below to edit this course");
		System.out.println("d           Change course department id");
		System.out.println("c           Change course code");
		System.out.println("n           Change course name");
		System.out.println("t           Change course description");
		System.out.println("b           Go Back");
		System.out.print("Enter the field you want to modify: ");
		
				
		char u;
				
		do {
			
			String consoleLine = scn.next();
			u = consoleLine.charAt(0);
			
			switch(u) {
				case 'd':
					System.out.print("Dept ID: ");
					String dept_id = scn.next();
					String sql1 = ("UPDATE course SET dept_id = ? WHERE id = ? ");
					PreparedStatement preparedStatement = this.conn.prepareStatement(sql1);
					preparedStatement.setString(1, dept_id);
					preparedStatement.setInt(2, idModify);
					preparedStatement.executeUpdate();
					break;
				case 'c':
					System.out.print("Course Code: ");
					String courseCode = scn.next();
					String sql2 = ("UPDATE course SET code = ? WHERE id = ? ");
					PreparedStatement preparedStatement2 = this.conn.prepareStatement(sql2);
					preparedStatement2.setString(1, courseCode);
					preparedStatement2.setInt(2, idModify);
					preparedStatement2.executeUpdate();
					break;
				case 'n':
					System.out.print("Name: ");
					String courseName = scn.next();
					String sql3 = ("UPDATE course SET name = ? WHERE id = ? ");
					PreparedStatement preparedStatement3 = this.conn.prepareStatement(sql3);
					preparedStatement3.setString(1, courseName);
					preparedStatement3.setInt(2, idModify);
					preparedStatement3.executeUpdate();
					break;
				case 't':
					System.out.print("Description: ");
					String courseDescription = scn.next();
					String sql4 = ("UPDATE course SET description = ? WHERE id = ? ");
					PreparedStatement preparedStatement4 = this.conn.prepareStatement(sql4);
					preparedStatement4.setString(1, courseDescription);
					preparedStatement4.setInt(2, idModify);
					preparedStatement4.executeUpdate();
					break;
					
				case 'b': 
					break;
					
				default:
					System.out.println("You entered an unsupported command (" + u + ")");
										
			}
		} while (u != 'b');
			
		scn.close();
		
	}
}
