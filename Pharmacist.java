import java.sql.*;
import java.util.Scanner;

public class Pharmacist {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_ms";
    private static final String DB_USER = "root"; // change if your MySQL user is different
    private static final String DB_PASSWORD = "1234"; // <- change this

    private Connection conn;
    private Scanner sc;

    public Pharmacist() {
        sc = new Scanner(System.in);
        connectToDatabase();
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Connected to hospital_ms database successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
            System.exit(0);
        }
    }

    // --------------------- PHARMACY OPERATIONS ---------------------

    private void viewAllDrugs() {
        System.out.println("\n📦 Available Drugs in Pharmacy:");
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
            System.out.println("❌ Error viewing drugs: " + e.getMessage());
        }
    }

    private void addNewDrug() {
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
            System.out.println("✅ Drug added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding drug: " + e.getMessage());
        }
    }

    private void updateDrugStock() {
        System.out.print("\nEnter Drug ID to update: ");
        int id = sc.nextInt();
        System.out.print("Enter new stock level: ");
        int stock = sc.nextInt();

        String query = "UPDATE PHARMACY SET STOCK_LEVEL = ?, STOCK_AVAILABLE = ? WHERE DRUG_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, stock);
            ps.setBoolean(2, stock > 0);
            ps.setInt(3, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Stock updated successfully!");
            else
                System.out.println("⚠️ Drug ID not found.");
        } catch (SQLException e) {
            System.out.println("❌ Error updating stock: " + e.getMessage());
        }
    }

    private void deleteDrug() {
        System.out.print("\nEnter Drug ID to delete: ");
        int id = sc.nextInt();

        String query = "DELETE FROM PHARMACY WHERE DRUG_ID = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Drug deleted successfully!");
            else
                System.out.println("⚠️ Drug ID not found.");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting drug: " + e.getMessage());
        }
    }

    // --------------------- PRESCRIPTION / DISPENSE ---------------------

    private void viewPendingPrescriptions() {
        System.out.println("\n💊 Pending Prescriptions:");
        String query = "SELECT P.PRESCRIPTION_ID, P.P_ID, PAT.P_NAME, D.DRUG_NAME, P.DOSAGE, P.DURATION_DAYS " +
                "FROM PRESCRIPTION P " +
                "JOIN PHARMACY D ON P.DRUG_ID = D.DRUG_ID " +
                "JOIN PATIENT PAT ON P.P_ID = PAT.P_ID " +
                "WHERE P.PRESCRIPTION_ID NOT IN (SELECT PRESCRIPTION_ID FROM DISPENSED_MEDICINE WHERE DISPENSE_STATUS = 'DISPENSED')";
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
            System.out.println("❌ Error viewing prescriptions: " + e.getMessage());
        }
    }

    private void markAsDispensed() {
        System.out.print("\nEnter Prescription ID to mark as dispensed: ");
        int prescId = sc.nextInt();
        System.out.print("Enter Drug ID: ");
        int drugId = sc.nextInt();
        System.out.print("Enter Patient ID: ");
        int pId = sc.nextInt();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        System.out.print("Enter Price per unit: ");
        float price = sc.nextFloat();

        float total = qty * price;
        String query = "INSERT INTO DISPENSED_MEDICINE (PRESCRIPTION_ID, DRUG_ID, P_ID, QUANTITY, PRICE, TOTAL_PRICE, DISPENSE_STATUS) VALUES (?, ?, ?, ?, ?, ?, 'DISPENSED')";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, prescId);
            ps.setInt(2, drugId);
            ps.setInt(3, pId);
            ps.setInt(4, qty);
            ps.setFloat(5, price);
            ps.setFloat(6, total);
            ps.executeUpdate();
            System.out.println("✅ Prescription marked as dispensed!");
        } catch (SQLException e) {
            System.out.println("❌ Error dispensing medicine: " + e.getMessage());
        }
    }

    // --------------------- MENU ---------------------

    public void menu() {
        while (true) {
            System.out.println("\n===== PHARMACIST MENU =====");
            System.out.println("1. View All Drugs");
            System.out.println("2. Add New Drug");
            System.out.println("3. Update Drug Stock");
            System.out.println("4. Delete Drug");
            System.out.println("5. View Pending Prescriptions");
            System.out.println("6. Mark Prescription as Dispensed");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewAllDrugs();
                case 2 -> addNewDrug();
                case 3 -> updateDrugStock();
                case 4 -> deleteDrug();
                case 5 -> viewPendingPrescriptions();
                case 6 -> markAsDispensed();
                case 7 -> {
                    System.out.println("👋 Logging out...");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.menu();
    }
}
