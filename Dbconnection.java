// Dbconnection.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Dbconnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hospital_ms"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "rockey2005"; 

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connection successful!");
            } catch (SQLException e) {
                System.out.println("Connection Error: " + e.getMessage());
            }
        }
        return conn;
    }
}

