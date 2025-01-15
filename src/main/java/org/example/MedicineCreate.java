package org.example;

public class MedicineCreate extends DonationTemplate {
    private static double totalMedicineInventory = 0; // Static variable to track total inventory

    @Override
    protected void performPostCreationTasks(double amount, String type) {
        // Update the total inventory with the donated amount
        totalMedicineInventory += amount;
        System.out.println("Updated total medicine inventory: " + totalMedicineInventory + " units.");
    }

    // Getter for the total inventory (optional)
    public static double getTotalMedicineInventory() {
        return totalMedicineInventory;
    }
}
