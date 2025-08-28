public class Pharmacist {
    String pharmacistId;
    String name;

    // Constructor
    public Pharmacist(String pharmacistId, String name) {
        this.pharmacistId = pharmacistId;
        this.name = name;
    }

    // Methods
    public void viewPrescriptions() {
        System.out.println("Viewing prescriptions...");
    }

    public void issueMedication() {
        System.out.println("Issuing medication...");
    }

    public void checkInventory() {
        System.out.println("Checking inventory...");
    }

    public void updateInventory() {
        System.out.println("Updating inventory...");
    }

    public void getAlerts() {
        System.out.println("Getting alerts for low/expired stock...");
    }

    // Main to test
    public static void main(String[] args) {
        Pharmacist p = new Pharmacist("P001", "John Doe");
        System.out.println("Pharmacist: " + p.name);

        p.viewPrescriptions();
        p.issueMedication();
        p.checkInventory();
        p.updateInventory();
        p.getAlerts();
    }
}
