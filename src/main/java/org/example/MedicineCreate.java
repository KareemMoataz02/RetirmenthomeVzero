package org.example;

public class MedicineCreate extends DonationTemplate {
    private static double totalMedicineInventory = 0; // Static variable to track total inventory

    @Override
    protected Donation saveToDatabase(String date, double amount, int elderId, int donatorId, String type) {
        // Save the donation to the database using the existing `Donation.createDonation` method
        Donation donation = Donation.createDonation(date, amount, elderId, donatorId, type);

        if (donation == null) {
            throw new RuntimeException("Failed to save medicine donation to the database.");
        }

        return donation;
    }

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
