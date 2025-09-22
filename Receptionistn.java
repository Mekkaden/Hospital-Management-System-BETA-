
import java.sql.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class Receptionistn { 
    private Connection conn;

    public Receptionistn() {
        connect();
    }

    private void connect() {
        try {
            conn = Dbconnection.getConnection(); 
            if (conn == null) {
                System.out.println("❌ Database connection failed!");
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

    // 1) registerPatient
    public void registerPatient(String name, String gender, String address, String dob, String phone) {
        try {
            String normalizedName = name.replaceAll("\\s+", "").toUpperCase();

            String checkQuery = "SELECT P_ID, P_NAME, PHONE_NO FROM PATIENT WHERE PHONE_NO = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkQuery);
            checkPst.setString(1, phone);
            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {
                String existingName = rs.getString("P_NAME").replaceAll("\\s+", "").toUpperCase();
                String existingPhone = rs.getString("PHONE_NO");

                if (existingPhone.equals(phone)) {
                    System.out.println("⚠ Patient already registered with this phone number! Name: " + rs.getString("P_NAME"));
                    return;
                }
            }

            String insertQuery = "INSERT INTO PATIENT (P_NAME, GENDER, ADDRESS, DOB, PHONE_NO, ADMITTED) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(insertQuery);
            pst.setString(1, name);
            pst.setString(2, gender);
            pst.setString(3, address);
            pst.setDate(4, java.sql.Date.valueOf(dob));
            pst.setString(5, phone);
            pst.setBoolean(6, false);
            pst.executeUpdate();

            System.out.println("✅ Patient Registered Successfully!");
        } catch (IllegalArgumentException ie) {
            System.out.println(" Date format must be YYYY-MM-DD");
        } catch (Exception e) {
            System.out.println("❌ Error registering patient: " + e.getMessage());
        }
    }

    // 2) bookAppointment
    public void bookAppointment(String patientName, String doctorName, String dateStr, String timeStr) {
        try {
            int pid = getPatientId(patientName);
            int did = getDoctorId(doctorName);

            String q = "INSERT INTO APPOINTMENT (P_ID, P_NAME, DOC_ID, DOC_NAME, APP_DATE, APP_TIME, APP_STATUS) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, pid);
            pst.setString(2, patientName);
            pst.setInt(3, did);
            pst.setString(4, doctorName);
            pst.setDate(5, java.sql.Date.valueOf(dateStr));
            pst.setTime(6, java.sql.Time.valueOf(timeStr));
            pst.setString(7, "BOOKED");
            pst.executeUpdate();

            System.out.println("✅ Appointment Booked!");
        } catch (IllegalArgumentException ie) {
            System.out.println("❌ Date must be YYYY-MM-DD and time HH:MM:SS");
        } catch (Exception e) {
            System.out.println("❌ Error booking appointment: " + e.getMessage());
        }
    }

    // 3) manageAppointments
    public DefaultTableModel getAppointmentsTableModel() {
        Vector<String> columns = new Vector<>(Arrays.asList(
            "APPOINTMENT_ID","P_ID","P_NAME","DOC_ID","DOC_NAME","APP_DATE","APP_TIME","APP_STATUS"
        ));
        Vector<Vector<Object>> data = new Vector<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT APPOINTMENT_ID, P_ID, P_NAME, DOC_ID, DOC_NAME, APP_DATE, APP_TIME, APP_STATUS FROM APPOINTMENT ORDER BY APP_DATE, APP_TIME");
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("APPOINTMENT_ID"));
                row.add(rs.getInt("P_ID"));
                row.add(rs.getString("P_NAME"));
                row.add(rs.getInt("DOC_ID"));
                row.add(rs.getString("DOC_NAME"));
                java.sql.Date d = rs.getDate("APP_DATE");
                java.sql.Time t = rs.getTime("APP_TIME");
                row.add(d != null ? d.toString() : "");
                row.add(t != null ? t.toString() : "");
                row.add(rs.getString("APP_STATUS"));
                data.add(row);
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching appointments: " + e.getMessage());
        }
        return new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    public boolean updateAppointment(int appointmentId, String newDateStr, String newTimeStr, String newStatus) {
        try {
            String q = "UPDATE APPOINTMENT SET APP_DATE=?, APP_TIME=?, APP_STATUS=? WHERE APPOINTMENT_ID=?";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setDate(1, java.sql.Date.valueOf(newDateStr));
            pst.setTime(2, java.sql.Time.valueOf(newTimeStr));
            pst.setString(3, newStatus);
            pst.setInt(4, appointmentId);
            int u = pst.executeUpdate();
            return u > 0;
        } catch (IllegalArgumentException ie) {
            System.out.println("❌ Date must be YYYY-MM-DD and time HH:MM:SS");
            return false;
        } catch (Exception e) {
            System.out.println("❌ Error updating appointment: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAppointment(int appointmentId) {
        try {
            String q = "DELETE FROM APPOINTMENT WHERE APPOINTMENT_ID=?";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, appointmentId);
            int d = pst.executeUpdate();
            return d > 0;
        } catch (Exception e) {
            System.out.println("❌ Error deleting appointment: " + e.getMessage());
            return false;
        }
    }

    // 4) viewDoctorSchedule
    public DefaultTableModel getDoctorScheduleModel(String doctorName) {
        Vector<String> columns = new Vector<>(Arrays.asList("APPOINTMENT_ID","P_NAME","APP_DATE","APP_TIME","APP_STATUS"));
        Vector<Vector<Object>> data = new Vector<>();
        try {
            String q = "SELECT APPOINTMENT_ID, P_NAME, APP_DATE, APP_TIME, APP_STATUS FROM APPOINTMENT WHERE DOC_NAME=? ORDER BY APP_DATE, APP_TIME";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setString(1, doctorName);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("APPOINTMENT_ID"));
                row.add(rs.getString("P_NAME"));
                java.sql.Date d = rs.getDate("APP_DATE");
                java.sql.Time t = rs.getTime("APP_TIME");
                row.add(d != null ? d.toString() : "");
                row.add(t != null ? t.toString() : "");
                row.add(rs.getString("APP_STATUS"));
                data.add(row);
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching doctor schedule: " + e.getMessage());
        }
        return new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    // 5) generateToken
    public int generateToken(String patientName, String doctorName) {
        try {
            int pid = getPatientId(patientName);
            int did = getDoctorId(doctorName);

            int nextToken = 1;
            String q1 = "SELECT MAX(TOKEN_NO) AS mx FROM DOCTOR_QUEUE WHERE DOC_ID=?";
            PreparedStatement pst1 = conn.prepareStatement(q1);
            pst1.setInt(1, did);
            ResultSet rs = pst1.executeQuery();
            if (rs.next()) {
                int mx = rs.getInt("mx");
                if (!rs.wasNull()) nextToken = mx + 1;
            }

            String q = "INSERT INTO DOCTOR_QUEUE (DOC_ID, P_ID, TOKEN_NO, QUEUE_STATUS) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setInt(1, did);
            pst.setInt(2, pid);
            pst.setInt(3, nextToken);
            pst.setString(4, "WAITING");
            pst.executeUpdate();

            System.out.println("🎫 Token generated: " + nextToken + " for " + patientName);
            return nextToken;
        } catch (Exception e) {
            System.out.println("❌ Error generating token: " + e.getMessage());
            return -1;
        }
    }

    // 6) transferToDoctorQueue
    public boolean transferToDoctorQueue() {
        try {
            String find = "SELECT QUEUE_ID FROM DOCTOR_QUEUE WHERE QUEUE_STATUS='WAITING' ORDER BY QUEUE_ID LIMIT 1";
            PreparedStatement pf = conn.prepareStatement(find);
            ResultSet rs = pf.executeQuery();
            if (rs.next()) {
                int qid = rs.getInt("QUEUE_ID");
                String upd = "UPDATE DOCTOR_QUEUE SET QUEUE_STATUS='IN_PROGRESS' WHERE QUEUE_ID=?";
                PreparedStatement pu = conn.prepareStatement(upd);
                pu.setInt(1, qid);
                int u = pu.executeUpdate();
                if (u > 0) {
                    System.out.println("➡️ Patient moved to IN_PROGRESS (QUEUE_ID=" + qid + ")");
                    return true;
                }
            } else {
                System.out.println("⚠ No patients in waiting queue.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error transferring to doctor queue: " + e.getMessage());
        }
        return false;
    }

    // Helpers --------------------
    public List<String> getAllPatientNames() {
        List<String> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT P_NAME FROM PATIENT ORDER BY P_NAME");
            while (rs.next()) list.add(rs.getString("P_NAME"));
        } catch (Exception ignored) {}
        return list;
    }

    public List<String> getAllDoctorNames() {
        List<String> list = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT DOC_NAME FROM DOCTOR ORDER BY DOC_NAME");
            while (rs.next()) list.add(rs.getString("DOC_NAME"));
        } catch (Exception ignored) {}
        return list;
    }

    private int getPatientId(String name) throws Exception {
        String q = "SELECT P_ID FROM PATIENT WHERE P_NAME=?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, name);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) return rs.getInt("P_ID");
        throw new Exception("Patient not found: " + name);
    }

    private int getDoctorId(String name) throws Exception {
        String q = "SELECT DOC_ID FROM DOCTOR WHERE DOC_NAME=?";
        PreparedStatement pst = conn.prepareStatement(q);
        pst.setString(1, name);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) return rs.getInt("DOC_ID");
        throw new Exception("Doctor not found: " + name);
    }
}
