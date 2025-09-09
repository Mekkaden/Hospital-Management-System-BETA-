package oops;

public class Doctor {

    private int doctorId;
    private String doctorName;
    private String specialization;
    private String phoneNo;
    private String email;

    public Doctor(int doctorId, String doctorName, String specialization, String phoneNo, String email) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.phoneNo = phoneNo;
        this.email = email;
    }

    public void viewTodaysAppointments() {
        System.out.println("--- Appointments for Dr. " + this.doctorName + " Today ---");
        System.out.println("09:00 AM - John Doe");
        System.out.println("09:30 AM - Jane Smith");
        System.out.println("---------------------------------------");
    }

    public void viewPatientMedicalHistory(int patientId) {
        System.out.println("Fetching medical history for patient ID: " + patientId);
    }

    public void updateAppointmentStatus(int appointmentId, String newStatus) {
        System.out.println("Updating appointment " + appointmentId + " to status: '" + newStatus + "'");
    }

    public void diagnosePatient(int patientId) {
        System.out.println("Dr. " + this.doctorName + " is diagnosing patient ID: " + patientId);
    }

    public void prescribeMedicine(int patientId, String medicine) {
        System.out.println("Dr. " + this.doctorName + " prescribed " + medicine + " to patient ID: " + patientId);
    }

    public void requestLabTest(int patientId, String testName) {
        System.out.println("Dr. " + this.doctorName + " requested test '" + testName + "' for patient ID: " + patientId);
    }

    public void referToSpecialist(int patientId, String specialistName) {
        System.out.println("Dr. " + this.doctorName + " referred patient ID: " + patientId + " to " + specialistName);
    }
}
