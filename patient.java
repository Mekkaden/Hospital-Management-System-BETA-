import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int id;
    private String name, gender, dob, phone, diagnosis;
    private boolean admitted;

    // DB connection settings
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_ms";
    private static final String USER = "root";
    private static final String PASSWORD = "rashmi123456"; // change this

    // Constructor
    public Patient(int id, String name, String gender, String dob, String phone, String diagnosis) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
        this.phone = phone;
        this.diagnosis = diagnosis;
        this.admitted = false;
    }

    // Insert new patient
    public void save() throws SQLException {
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql = "INSERT INTO PATIENT (P_ID, P_NAME, GENDER, DOB, PHONE_NO, DIAGNOSIS, ADMITTED) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, gender);
        ps.setString(4, dob);
        ps.setString(5, phone);
        ps.setString(6, diagnosis);
        ps.setBoolean(7, admitted);
        ps.executeUpdate();
        con.close();
    }

    // Update patient info
    public void update(String field, String value) throws SQLException {
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql = "UPDATE PATIENT SET " + field + "=? WHERE P_ID=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, value);
        ps.setInt(2, id);
        ps.executeUpdate();
        con.close();
    }

    // Admit / Discharge
    public void admit() throws SQLException { changeStatus(true); }
    public void discharge() throws SQLException { changeStatus(false); }

    private void changeStatus(boolean status) throws SQLException {
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql = "UPDATE PATIENT SET ADMITTED=? WHERE P_ID=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setBoolean(1, status);
        ps.setInt(2, id);
        ps.executeUpdate();
        admitted = status;
        con.close();
    }

    // Add medical record
    public void addRecord(String diag, String treat, String presc, String date) throws SQLException {
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql = "INSERT INTO MedicalHistory (P_ID, diagnosis, treatment, prescription, visit_date) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, diag);
        ps.setString(3, treat);
        ps.setString(4, presc);
        ps.setString(5, date);
        ps.executeUpdate();
        con.close();
    }

    // Get medical history
    public List<String> getHistory() throws SQLException {
        List<String> history = new ArrayList<>();
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql = "SELECT diagnosis, treatment, prescription, visit_date FROM MedicalHistory WHERE P_ID=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            history.add(rs.getString("diagnosis") + " | " +
                        rs.getString("treatment") + " | " +
                        rs.getString("prescription") + " | " +
                        rs.getDate("visit_date"));
        }
        con.close();
        return history;
    }
}



