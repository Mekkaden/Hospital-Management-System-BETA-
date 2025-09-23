import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        //  Get details from user at runtime
        System.out.print("Enter Patient ID: ");
        int id = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter DOB (yyyy-mm-dd): ");
        String dob = sc.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();

        System.out.print("Enter Diagnosis: ");
        String diagnosis = sc.nextLine();

        // Create Patient object
        Patient p = new Patient(id, name, gender, dob, phone, diagnosis);

        //  Save to DB
        p.save();
        System.out.println("✅ Patient inserted successfully.");

        //  Admit patient
        p.admit();
        System.out.println("✅ Patient admitted.");

        //  Add one medical record
        System.out.print("Enter treatment: ");
        String treatment = sc.nextLine();
        System.out.print("Enter prescription: ");
        String prescription = sc.nextLine();
        System.out.print("Enter visit date (yyyy-mm-dd): ");
        String visitDate = sc.nextLine();

        p.addRecord(diagnosis, treatment, prescription, visitDate);

        //  Show history
        System.out.println("\nMedical History:");
        for (String record : p.getHistory()) {
            System.out.println(record);
        }

        sc.close();
    }
}
