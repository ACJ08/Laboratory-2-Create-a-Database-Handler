
package com.mycompany.databasehandlerlab;

import java.util.List;

/**
 *
 * @author Anne Carol G. Jonson 2CSD
 */
public class DatabaseHandlerLab {
    public static void main(String[] args) {
        DatabaseHandler dbHandler = new DatabaseHandler();       
       // Call  method 
        dbHandler.initializeStudents();

        // Example of inserting a new student
        Student newInsertStudent = new Student("20230101234", "Anne", "G.", "Jonson", "F", "2004-11-08", 2023, "CICS", 15, "Manila City");
        
        Student newInsertStudent2 = new Student("20240103344", "Juana", "G.", "Reyes", "F", "2004-11-08", 2024, "CICS", 15, "Manila City");
        boolean resultInsert = dbHandler.insertStudent(newInsertStudent);
        
        boolean resultInsert2 = dbHandler.insertStudent(newInsertStudent2);
        
        System.out.println("Insert Student: " + (resultInsert ? "Success" : "Failed"));
        
        System.out.println("Insert Student: " + (resultInsert2 ? "Success" : "Failed"));
        
        
        // Example of getting a student by first name, middle name, and last name
        Student retrievedStudentByName = dbHandler.getStudent("Anne", "G.", "Jonson"); // Pass the first, middle, and last names
        if (retrievedStudentByName != null) {
            System.out.println("Retrieved Student by Name: " + retrievedStudentByName.getFirstName() + " " + retrievedStudentByName.getMiddleName() + " " + retrievedStudentByName.getLastName());
        } else {
            System.out.println("Student not found by name.");
        }
    
        // Example of getting students by year (e.g., year 2023)
        List<Student> studentsByYear = dbHandler.getStudentsByYear(2023); // Modify the year as needed
        System.out.println("Students from the year 2023:");
        for (Student student : studentsByYear) {
            System.out.println(student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName());
        }

        //Example of getting a student by student number
        Student retrievedStudent = dbHandler.getStudent("20230101234");
            if (retrievedStudent != null) {
                System.out.println("Retrieved Student by Student Number: " + retrievedStudent.getFirstName() + " " + retrievedStudent.getMiddleName() + " " + retrievedStudent.getLastName());
            }

        //Get all students
        List<Student> allStudents = dbHandler.getStudents();
            System.out.println("All Students:");
            for (Student student : allStudents) {
                System.out.println(student.getFirstName() + " " + student.getMiddleName() + " "+ student.getLastName());
            }
            
        
        //  Update student information
        Student updatedStudent = new Student("20230101234", "Anne Carol", "G.", "Jonson", "F", "2000-01-01", 2023, "CICS", 15, "456 Main St.");
        boolean updateSuccess = dbHandler.updateStudentInfo("20230101234", updatedStudent);
        System.out.println("Update success: " + updateSuccess);
        
        //  Update student units (subtract 3 units)
        boolean updateUnitsSuccess = dbHandler.updateStudentUnits("20230101234", 3);
        System.out.println("Update units success: " + updateUnitsSuccess);
    
     //  Remove a student
        boolean removeSuccess = dbHandler.removeStudent("20230101234");
        System.out.println("Remove student success: " + removeSuccess);
    

        // After performing other actions, close the connection
        dbHandler.closeConnection();
    }
 
}
