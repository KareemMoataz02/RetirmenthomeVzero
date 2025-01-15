package org.example;

public class Main {
    public static void main(String[] args) {
        MedicineDonation medicineDonation = new MedicineDonation();

        // Create a new Medicine Donation
        System.out.println("Creating Medicine Donation...");
        Donation createdMedicineDonation = medicineDonation.createDonation("2025-01-15", 50, 1, 101, "Painkillers");
        System.out.println("Medicine Donation created: " + createdMedicineDonation);

        // Check total inventory
        System.out.println("Total Medicine Inventory: " + MedicineCreate.getTotalMedicineInventory());
    }
}
