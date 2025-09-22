package oops;

import java.sql.*;
import java.util.Scanner;

public class Doctor {

    private Connection conn; //Here COnnection is like uk int 
    private int doctorId;
    private String doctorName;
    private String specialization;

    public Doctor(int doctorId, String doctorName, String specialization) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        connect(); //AS teh objetc is created nammal connect 
    }

    private void connect() {
        try {
            conn = Dbconnection.getConnection();  //Getting conneciton
            if (conn == null) {
                System.out.println(" Database connection failed for Doctor module!");
            }
        } catch (Exception e) {
            System.out.println("DB Connection Error: " + e.getMessage());
        }
    }

    public void close() {
        try { 
            if (conn != null && !conn.isClosed()) { 
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    public void viewTodaysAppointments() {
        System.out.println("\n--- Appointments for Dr. " + this.doctorName + " Today ---");
        String query = "SELECT P_NAME, APP_TIME FROM APPOINTMENT WHERE DOC_ID = ? AND APP_DATE = CURDATE() ORDER BY APP_TIME";
        
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, this.doctorId); //Replace 1st ? by this.doctoerid
            
            ResultSet rs = pst.executeQuery();
            boolean hasAppointments = false;
            while (rs.next()) {
                hasAppointments = true;
                System.out.println(rs.getTime("APP_TIME") + " - " + rs.getString("P_NAME"));
            }
            if (!hasAppointments) {
                System.out.println("No appointments scheduled for today.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching appointments: " + e.getMessage());
        }
        System.out.println("------------------------------------------");
    }

    public void viewPatientMedicalHistory(int patientId) {
        System.out.println("\n--- Medical History for Patient ID: " + patientId + " ---");
        String query = "SELECT visit_date, diagnosis, treatment, prescription FROM MedicalHistory WHERE P_ID = ? ORDER BY visit_date DESC";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, patientId);
            ResultSet rs = pst.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No medical history found for this patient.");
            } else {
                while (rs.next()) {
                    System.out.println("Date: " + rs.getDate("visit_date"));
                    System.out.println("  Diagnosis: " + rs.getString("diagnosis"));
                    System.out.println("  Treatment: " + rs.getString("treatment"));
                    System.out.println("  Prescription: " + rs.getString("prescription"));
                    System.out.println("--------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching medical history: " + e.getMessage());
        }
    }

    public void updateAppointmentStatus(int appointmentId, String newStatus) {
        System.out.println("\nUpdating appointment " + appointmentId + " to status: '" + newStatus + "'...");
        String query = "UPDATE APPOINTMENT SET APP_STATUS = ? WHERE APPOINTMENT_ID = ? AND DOC_ID = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, newStatus);
            pst.setInt(2, appointmentId);
            pst.setInt(3, this.doctorId);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Appointment status updated successfully!");
            } else {
                System.out.println("⚠ Appointment not found or you do not have permission to update it.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error updating appointment status: " + e.getMessage());
        }
    }

    public void diagnosePatient(int patientId, String diagnosis, String treatment) {
        System.out.println("\nAdding new medical record for Patient ID: " + patientId);
        String query = "INSERT INTO MedicalHistory (P_ID, diagnosis, treatment, visit_date) VALUES (?, ?, ?, CURDATE())";
        
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, patientId);
            pst.setString(2, diagnosis);
            pst.setString(3, treatment);
            
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Diagnosis and treatment recorded successfully.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error recording diagnosis: " + e.getMessage());
        }
    }

    public void prescribeMedicine(int patientId, int drugId, String dosage, int durationDays) {
        System.out.println("\nPrescribing medicine for Patient ID: " + patientId);
        String query = "INSERT INTO PRESCRIPTION (P_ID, DOC_ID, DRUG_ID, PRESCRIPTION_DATE, DOSAGE, DURATION_DAYS) VALUES (?, ?, ?, CURDATE(), ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, patientId);
            pst.setInt(2, this.doctorId);
            pst.setInt(3, drugId);
            pst.setString(4, dosage);
            pst.setInt(5, durationDays);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ Medicine prescribed successfully!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error prescribing medicine: " + e.getMessage());
        }
    }

    public void requestLabTest(int patientId, String testName) {
        System.out.println("\n--- Lab Test Request ---");
        System.out.println("Dr. " + this.doctorName + " requested test '" + testName + "' for patient ID: " + patientId);
        System.out.println("This would typically be sent to a Laboratory module or table.");
        System.out.println("------------------------");
    }

    public void referToSpecialist(int patientId, String specialistName, String reason) {
        System.out.println("\n--- Specialist Referral ---");
        System.out.println("Patient ID " + patientId + " referred to " + specialistName);
        System.out.println("Reason: " + reason);
        System.out.println("-------------------------");
    }
}