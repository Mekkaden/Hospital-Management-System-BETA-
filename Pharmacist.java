import java.sql.*;
import java.util.Scanner;

public class Pharmacist {

    private Connection conn;

    public Pharmacist() {
        connect();
    }

    private void connect() {
        try {
            conn = Dbconnection.getConnection(); 
            if (conn == null) {
                System.out.println(" Database connection failed!");
            }
        } catch (Exception e) {
            System.out.println("DB Connection Error: " + e.getMessage());
        }
    }

    public void close() {
        try { 
            if (conn != null && !conn.isClosed()) conn.close(); 
        } catch (Exception ignored) {}
    }



    // ------------------ 1. VIEW ALL DRUGS ------------------
    private void viewAllDrugs() {
        System.out.println("\n--- Available Drugs in Pharmacy ---");
        String query = "SELECT * FROM PHARMACY";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println("Drug ID: " + rs.getInt("DRUG_ID")
                        + " | Name: " + rs.getString("DRUG_NAME")
                        + " | Price: " + rs.getFloat("PRICE")
                        + " | Stock: " + rs.getInt("STOCK_LEVEL")
                        + " | Available: " + rs.getBoolean("STOCK_AVAILABLE"));
            }

        } catch (SQLException e) {
            System.out.println("Error viewing drugs: " + e.getMessage());
        }
    }

    // ------------------ 2. ADD NEW DRUG ------------------
    private void addNewDrug() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Drug Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Price: ");
        float price = sc.nextFloat();
        sc.nextLine();

        System.out.print("Enter Concentration: ");
        String conc = sc.nextLine();

        System.out.print("Enter Supplier: ");
        String supplier = sc.nextLine();

        System.out.print("Enter MFG Date (YYYY-MM-DD): ");
        String mfg = sc.nextLine();

        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        String exp = sc.nextLine();

        System.out.print("Enter Stock Level: ");
        int stock = sc.nextInt();
        sc.nextLine();

        String query = "INSERT INTO PHARMACY (DRUG_NAME, PRICE, CONCENTRATION, SUPPLIER, MFG_DATE, EXPIRY_DATE, STOCK_LEVEL) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setFloat(2, price);
            ps.setString(3, conc);
            ps.setString(4, supplier);
            ps.setDate(5, Date.valueOf(mfg));
            ps.setDate(6, Date.valueOf(exp));
            ps.setInt(7, stock);
            ps.executeUpdate();
            System.out.println("Drug added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding drug: " + e.getMessage());
        }
    }

   private void viewPrescriptions() {
    System.out.println("\nAll Prescriptions:");
    String query = "SELECT P.PRESCRIPTION_ID, P.P_ID, PAT.P_NAME, D.DRUG_NAME, P.DOSAGE, P.DURATION_DAYS " +
                   "FROM PRESCRIPTION P " +
                   "JOIN PHARMACY D ON P.DRUG_ID = D.DRUG_ID " +
                   "JOIN PATIENT PAT ON P.P_ID = PAT.P_ID";
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            System.out.println("Prescription ID: " + rs.getInt("PRESCRIPTION_ID") +
                               " | Patient: " + rs.getString("P_NAME") +
                               " | Drug: " + rs.getString("DRUG_NAME") +
                               " | Dosage: " + rs.getString("DOSAGE") +
                               " | Duration: " + rs.getInt("DURATION_DAYS") + " days");
        }
    } catch (SQLException e) {
        System.out.println("Error viewing prescriptions: " + e.getMessage());
    }
}


    public void menu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== PHARMACIST MENU =====");
            System.out.println("1. View All Drugs");
            System.out.println("2. Add New Drug");
            System.out.println("3. View Prescriptions");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewAllDrugs();
                case 2 -> addNewDrug();
                case 3 -> viewPrescriptions();
                case 4 -> {
                    System.out.println("Exiting Pharmacist module...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }


}
