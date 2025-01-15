package org.example;

public class Main {
    public static void main(String[] args) {
        // Initialize the donation types
        MedicineDonation medicineDonation = new MedicineDonation();
        MoneyDonation moneyDonation = new MoneyDonation();

        // Test Medicine Donation
        System.out.println("Creating Medicine Donation...");
        medicineDonation.createDonation("2025-01-15", 50, 1, 101, "Painkillers");

        System.out.println("\nUpdating Medicine Donation...");
        Donation updatedMedicineDonation = medicineDonation.updateDonation(
                "donationId", "2025-01-16", 60, 1, "Antibiotics");
        System.out.println("Updated Medicine Donation: " + updatedMedicineDonation);

        System.out.println("\nCancelling Medicine Donation...");
        boolean isMedicineCancelled = medicineDonation.cancelDonation("donationId");
        System.out.println("Medicine Donation Cancelled: " + isMedicineCancelled);

        // Test Money Donation
        System.out.println("\nCreating Money Donation...");
        moneyDonation.createDonation("2025-01-15", 100, 1, 102, "USD");

        System.out.println("\nUpdating Money Donation...");
        Donation updatedMoneyDonation = moneyDonation.updateDonation(
                "donationId", "2025-01-17", 150, 1, "EUR");
        System.out.println("Updated Money Donation: " + updatedMoneyDonation);

        System.out.println("\nCancelling Money Donation...");
        boolean isMoneyCancelled = moneyDonation.cancelDonation("donationId");
        System.out.println("Money Donation Cancelled: " + isMoneyCancelled);
    }
}
