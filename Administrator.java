import java.sql.*;
import java.time.LocalDate;
class Admin{

    public void login(String username,String password){
        try{

            if(username==DBMS.USER && password==DBMS.PASSWORD){
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
                pstmt.setString(1,username);
                pstmt.setString(2,password);
                pstmt.executeQuery();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

//---------------------------------------------- DOCTOR ------------------------------------------
class aDoctor{

    public void addDoctor(int doc_ID,String doc_name,String qualification,String specialization,String phone_no,boolean on_leave){
        String query="Insert into doctor(doc_ID,doc_name,qualification,specialization,phone_no,on_leave) values (?,?,?,?,?,?)";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt = conn.prepareStatement(query)){
            pstmt.setInt(1,doc_ID);
            pstmt.setString(2,doc_name);
            pstmt.setString(3,qualification);
            pstmt.setString(4,specialization);
            pstmt.setString(5,phone_no);
            pstmt.setBoolean(6,on_leave);
            pstmt.executeUpdate();
            System.out.println("Doctor field added successfully ");
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public void updateDoctor(int doc_ID,String doc_name,String qualification,String specialization,String phone_no,boolean on_leave){
        String query="UPDATE doctor SET doc_name=?, qualification=?, specialization=?, phone_no=?, on_leave=? where doc_ID=?";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setInt(1,doc_ID);
            pstmt.setString(2,doc_name);
            pstmt.setString(3,qualification);
            pstmt.setString(4,specialization);
            pstmt.setString(5,phone_no);
            pstmt.setBoolean(6,on_leave);
            int rows=pstmt.executeUpdate();
            if(rows>0)
                System.out.println("Doctor info updated successfully");
            else
                System.out.println("No records of doctor found with doc_ID "+doc_ID);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void removeDoctor(int doc_ID){
        String query="DELETE from doctor where doc_ID=?";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setInt(1,doc_ID);
            int rows=pstmt.executeUpdate();
            if(rows>0)
                System.out.println("Doctor info deleted successfully");
            else    
                System.out.println("Doctor info not found for doc_ID "+doc_ID);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void checkLeave(int doc_ID,String doc_name){
        //To check whether a doctor is on leave
        String query="SELECT doc_ID,doc_name,on_leave from doctor where doc_ID=? AND doc_name=?";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setInt(1,doc_ID);
            pstmt.setString(2,doc_name);
            ResultSet rs=pstmt.executeQuery();

            if(rs.next()){
                    int id=rs.getInt("doc_ID");
                    String name=rs.getString("doc_name");
                    Boolean on_leave=rs.getBoolean("on_leave");

                    System.out.println("Doctor ID: "+id);
                    System.out.println("Name: "+name);
                    System.out.println("On leave : "+on_leave);
            }
            else{
                System.out.println("Doctor not found with ID "+doc_ID);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

//---------------------------------------------- PATIENT ------------------------------------------
class aPatient{

    public void addPatient(int p_ID,String p_name,String gender,LocalDate DOB,String phone_no,String diagnosis,String history,boolean admitted){
        String query="INSERT into patient(p_ID,p_name,gender,DOB,phone_no,diagnosis,history,admitted) values(?,?,?,?,?,?,?,?)";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setInt(1,p_ID);
            pstmt.setString(2,p_name);
            pstmt.setString(3,gender);
            pstmt.setDate(4,java.sql.Date.valueOf(DOB));
            pstmt.setString(5,phone_no);
            pstmt.setString(6,diagnosis);
            pstmt.setString(7,history);
            pstmt.setBoolean(8,admitted);

            pstmt.executeUpdate();
            System.out.println("Patient details added successfully");

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updatePatient(int  p_ID,String p_name,String gender,LocalDate DOB,String phone_no,String diagnosis,String history,boolean admitted){
        String query="UPDATE patient SET p_name=?,gender=?,DOB=?,phone_no=?,diagnosis=?,history=?,admitted=? where p_ID=?";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setString(1,p_name);
            pstmt.setString(2,gender);
            pstmt.setDate(3,java.sql.Date.valueOf(DOB));
            pstmt.setString(4,phone_no);
            pstmt.setString(5,diagnosis);
            pstmt.setString(6,history);
            pstmt.setBoolean(7,admitted);
            pstmt.setInt(8,p_ID);

            int rows=pstmt.executeUpdate();
            if(rows>0){
                System.out.println("Updated info: ");
                String query2="SELECT * from patient where p_ID=?";
                PreparedStatement pstmt2=conn.prepareStatement(query2);
                pstmt2.setInt(1,p_ID);
            }
            else{
                System.out.println("Record not found for patient with ID "+p_ID);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public void removePatient(int p_ID){
        String query="DELETE from patient where p_ID=?";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setInt(1,p_ID);
            int rows=pstmt.executeUpdate();
            if(rows>0){
                System.out.println("Patient record deleted successfully ");
            }
            else{
                System.out.println("Record not found for patient with ID "+p_ID);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public void bill(int p_ID,String p_name){

    }
    public void generateReport(int p_ID,String p_name){

    }
}

//---------------------------------------------- PHARMACY ------------------------------------------
class aPharmacy{
    public void addDrug(int drug_ID,String drug_name,double price,String concentration,String supplier,
    LocalDate mfg_date,LocalDate expiry_date,int stock_level,boolean stock_available){
        String query="INSERT into pharmacy(drug_ID,drug_name,price,concentration,supplier,mfg_date,expiry_date,stock_level,stock_available) values(?,?,?,?,?,?,?,?,?)";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setInt(1,drug_ID);
            pstmt.setString(2,drug_name);
            pstmt.setDouble(3,price);
            pstmt.setString(4,concentration);
            pstmt.setString(5,supplier);
            pstmt.setDate(6,java.sql.Date.valueOf(mfg_date));
            pstmt.setDate(7,java.sql.Date.valueOf(expiry_date));
            pstmt.setInt(8,stock_level);
            pstmt.setBoolean(9,stock_available);

            pstmt.executeUpdate();
            System.out.println("Drug details added successfully");

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
        

    public void checkStock(int drug_ID){
        String query="Select drug_ID,drug_name,stock_available,stock_level where drug_ID=?";
        try(Connection conn=DBMS.getConnection();PreparedStatement pstmt=conn.prepareStatement(query)){
            pstmt.setInt(1,drug_ID);
            ResultSet rs=pstmt.executeQuery();
            if(rs.next()){
                int id=rs.getInt("drug_ID");
                String name=rs.getString("drug_name");
                Boolean available=rs.getBoolean("stock_available");
                int level=rs.getInt("stock_level");

                System.out.println("Drug ID: "+id);
                System.out.println("Drug name: "+name);
                System.out.println("Whether stock is available: "+available);
                if(available){
                    System.out.println("Stock level: "+level);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void billing(int p_ID){
        
    }
}

public class Administrator extends Admin{
    public static void main(String[] args){
        Administrator admin = new Administrator();
        aDoctor doctorOps = new aDoctor();
        aPatient patientOps = new aPatient();
        aPharmacy pharmacyOps = new aPharmacy();

        System.out.println("=== Hospital Management System ===");
        System.out.println("Backend loaded successfully. Waiting for UI...");

        // Example of where your Swing entry point will be:
        // -------------------------------------------------
        // javax.swing.SwingUtilities.invokeLater(() -> {
        //     new AdminLoginUI(admin, doctorOps, patientOps, pharmacyOps);
        // });
        //
        // The AdminLoginUI class (you’ll build later) will handle:
        // - login form
        // - doctor/patient/pharmacy CRUD forms
        // - event listeners that call these backend methods
        // -------------------------------------------------

        // For now, just confirm backend readiness
        System.out.println("System ready. Connect Swing UI to these objects.");
    }
}
