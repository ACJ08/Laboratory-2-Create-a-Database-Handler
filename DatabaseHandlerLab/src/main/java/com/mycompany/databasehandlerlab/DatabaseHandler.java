
package com.mycompany.databasehandlerlab;

/**
 *
 * @author Anne Carol G. Jonson 2CSD
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;



public class DatabaseHandler {
    private Connection conn;

     // Constructor to initialize the connection
    public DatabaseHandler() {
        this.conn = connect(); // Ensure the connection is established here
    }

    // Static method to establish the connection
    static Connection connect() {
        try {
            return DriverManager.getConnection("jdbc:sqlite:C:\\Users\\acj32\\Documents\\NetBeansProjects\\DatabaseHandlerLab\\resources\\students");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage());
            return null;
        }
    }
    
   // Modified method to reflect new table structure specification on 
    public void initializeStudents() {
        
        if (conn==null){
           System.out.println("Null. Unable to initialize the table Students");
           return;
       }
       
       String dropTableSql = "DROP TABLE IF EXISTS Students;";
       try (PreparedStatement dropStmt = conn.prepareStatement(dropTableSql)) {
           dropStmt.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
          
        String sql = 
                     "CREATE TABLE Students (" +
                     "student_id TEXT NOT NULL PRIMARY KEY CHECK (student_id LIKE '____010____'), " + // student_id as PRIMARY KEY
                     "student_fname TEXT NOT NULL, " + 
                     "student_mname TEXT NOT NULL, " +
                     "student_lname TEXT NOT NULL, " +
                     "student_sex TEXT NOT NULL CHECK (student_sex IN ('M', 'F')), " + // M or F values only
                     "student_birth TEXT NOT NULL CHECK (student_birth LIKE '____-__-__'), " + // yyyy-mm-dd format
                     "student_start INTEGER NOT NULL CHECK (student_start > 1900), " + // valid year
                     "student_department TEXT NOT NULL, " + 
                     "student_units INTEGER NOT NULL DEFAULT 0, " +
                     "student_address TEXT);"; // nullable address

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//initializedModifiedStudents
    
    
      // Utility method to map ResultSet to Student object
    private Student mapToStudent(ResultSet rs) throws SQLException {
        return new Student(
            rs.getString("student_id"),
            rs.getString("student_fname"),
            rs.getString("student_mname"),
            rs.getString("student_lname"),
            rs.getString("student_sex"),
            rs.getString("student_birth"),
            rs.getInt("student_start"),
            rs.getString("student_department"),
            rs.getInt("student_units"),
            rs.getString("student_address")
        );
    }
     
     // 1. Get a student by student number
     Student getStudent(String student_id) {
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
     // 2. Get a student by first name, middle name, and last name
     Student getStudent(String studentFname, String studentMname, String studentLname) {
        String sql = "SELECT * FROM Students WHERE student_fname = ? AND student_mname = ? AND student_lname = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentFname);
            stmt.setString(2, studentMname);
            stmt.setString(3, studentLname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 3. Get all students
     List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Students";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(mapToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    // 4. Remove a student by student number
    public boolean removeStudent(String student_id) {
        String sql = "DELETE FROM Students WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student_id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 5. Get students by year (based on student_number format)
     List<Student> getStudentsByYear(int year) {

        List<Student> students = new ArrayList<>();
    String sql = "SELECT * FROM Students WHERE student_id LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "____010" + year);  // Using LIKE to match the pattern
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = mapToStudent(rs);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
     // 6. Update student information
     boolean updateStudentInfo(String student_id, Student studentInfo) {
        String sql = "UPDATE Students SET student_fname = ?, student_mname = ?, student_lname = ?, student_department = ?, student_address = ? WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentInfo.getFirstName());
            stmt.setString(2, studentInfo.getMiddleName());
            stmt.setString(3, studentInfo.getLastName());
            stmt.setString(4, studentInfo.getDepartment());
            stmt.setString(5, studentInfo.getAddress());
            stmt.setString(6, student_id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 7. Update student units
    public boolean updateStudentUnits(String student_id, int subtractedUnits) {
        String sql = "UPDATE Students SET student_units = student_units - ? WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subtractedUnits);
            stmt.setString(2, student_id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 8. Insert a new student
     boolean insertStudent(Student newStudent) {
        String sql = "INSERT INTO Students (student_id, student_fname, student_mname, student_lname, student_sex, student_birth, student_start, student_department, student_units, student_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStudent.getStudentNumber());
            stmt.setString(2, newStudent.getFirstName());
            stmt.setString(3, newStudent.getMiddleName());
            stmt.setString(4, newStudent.getLastName());
            stmt.setString(5, newStudent.getSex());
            stmt.setString(6, newStudent.getBirthDate());
            stmt.setInt(7, newStudent.getStartYear());
            stmt.setString(8, newStudent.getDepartment());
            stmt.setInt(9, newStudent.getUnits());
            stmt.setString(10, newStudent.getAddress());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
  
    // Method to close the connection
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }//closeConnection
    
}//databaseHandler Class

class Student {
    private String student_id; 
    private String firstName;
    private String middleName;
    private String lastName;
    private String sex;
    private String birthDate;
    private int startYear;
    private String department;
    private int units;
    private String address;

    // Constructor
    public Student(String student_id, String firstName, String middleName, String lastName, 
                   String sex, String birthDate, int startYear, String department, 
                   int units, String address) {
        this.student_id = student_id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.sex = sex;
        this.birthDate = birthDate;
        this.startYear = startYear;
        this.department = department;
        this.units = units;
        this.address = address;
    }

    // Getters
    public String getStudentNumber() {
        return student_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public int getStartYear() {
        return startYear;
    }

    public String getDepartment() {
        return department;
    }

    public int getUnits() {
        return units;
    }

    public String getAddress() {
        return address;
    }
}//Student