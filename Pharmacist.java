import java.sql.*;
import java.util.Scanner;

public class Pharmacist {
    private Connection conn;
    private Scanner sc;

    public Pharmacist() {
        sc = new Scanner(System.in);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC driver
            conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/hospital?useSSL=false&serverTimezone=UTC",
    "root",      // MySQL username
    "1234"       // MySQL root password
);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }

    public boolean login() {
        System.out.print("Enter username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        try {
            String query = "SELECT * FROM pharmacist WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, user);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void menu() {
        System.out.println("Welcome to Pharmacist Menu!");
        System.out.println("1. View Prescriptions");
        System.out.println("2. View Medicine Stock");
        System.out.println("3. Exit");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline
        switch (choice) {
            case 1: viewPrescriptions(); break;
            case 2: viewStock(); break;
            case 3: System.exit(0);
            default: System.out.println("Invalid choice!"); menu();
        }
    }

    public void viewPrescriptions() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM prescription");
            System.out.println("ID | Patient | Medicine | Quantity");
            while(rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                                   rs.getString("patient_name") + " | " +
                                   rs.getString("medicine") + " | " +
                                   rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewStock() {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM medicine_stock");
            System.out.println("ID | Medicine | Quantity");
            while(rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                                   rs.getString("medicine_name") + " | " +
                                   rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Pharmacist p = new Pharmacist();
        if(p.login()) {
            p.menu();
        } else {
            System.out.println("Login failed!");
        }
    }
}
