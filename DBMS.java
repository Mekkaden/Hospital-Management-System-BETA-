import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Dbconnection {
    public static final String URL="jdbc:mysql://localhost:3306/hospital_ms";
    public static final String USER="root";
    public static final String PASSWORD="cornetto";

    public static Connection DBConnection(){
        Connection conn=null;
        try{
            conn=DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("Connection successful ");
        }
        catch(SQLException e){
            System.out.println("Connection failed: "+e.getMessage());
        }
        return conn;
    }
    public static void main(String[] args){
        DBConnection();
    }
}

