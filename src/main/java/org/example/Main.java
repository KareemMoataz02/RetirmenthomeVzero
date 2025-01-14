package org.example;

import org.bson.Document;

public class Main {
    public static void main(String[] args) {
        try {
            // Step 1: Initialize MongoDB Users
            System.out.println("Initializing Users...");

            // Clean existing users to avoid duplicates (Optional)
            User.deleteUser(1);
            User.deleteUser(2);

            // Create new users
            User.createUser(1, "Dr. Alice");
            User.createUser(2, "Bob");

            // Step 2: Initialize Donation Behaviors
            DonationBehavior medicineDonationBehavior = new MedicineDonation("Pain Killers");
            DonationBehavior moneyDonationBehavior = new MoneyDonation("Sterling Pound");

            // Step 3: Initialize Receipt Generators
            IReceiptGenerator medicineReceiptGenerator = new MedicineDonationReceiptAdapter();
            IReceiptGenerator moneyReceiptGenerator = new MoneyDonationReceiptAdapter();

            // Step 4: Create Donations
            System.out.println("Creating Donations...");
            Donation medDonation1 = medicineDonationBehavior.createDonation("2025-01-15", 50.0, 101, 1001, "Pain Killers");
            Donation moneyDonation1 = moneyDonationBehavior.createDonation("2025-01-16", 200.0, 102, 1002, "Sterling Pound");

            if (medDonation1 != null && moneyDonation1 != null) {
                System.out.println("\nGenerating Receipts...");
                System.out.println(medicineReceiptGenerator.generateReceipt(medDonation1));
                System.out.println(moneyReceiptGenerator.generateReceipt(moneyDonation1));

                // Step 5: Update Donations
                System.out.println("\nUpdating Donations...");
                Donation updatedMedDonation = medicineDonationBehavior.updateDonation(
                        medDonation1.getDonationId(), "2025-01-20", 75.0, 101, "Updated Pain Killers"
                );
                Donation updatedMoneyDonation = moneyDonationBehavior.updateDonation(
                        moneyDonation1.getDonationId(), "2025-01-21", 250.0, 102, "Updated Sterling Pound"
                );

                if (updatedMedDonation != null && updatedMoneyDonation != null) {
                    System.out.println("\nGenerating Updated Receipts...");
                    System.out.println(medicineReceiptGenerator.generateReceipt(updatedMedDonation));
                    System.out.println(moneyReceiptGenerator.generateReceipt(updatedMoneyDonation));
                }
            } else {
                System.err.println("Failed to create initial donations.");
            }

            // Step 6: Cancel Donations
            System.out.println("\nCancelling Donations...");
            System.out.println("Medicine Donation Cancelled: " +
                    medicineDonationBehavior.cancelDonation(medDonation1 != null ? medDonation1.getDonationId() : null));
            System.out.println("Money Donation Cancelled: " +
                    moneyDonationBehavior.cancelDonation(moneyDonation1 != null ? moneyDonation1.getDonationId() : null));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
