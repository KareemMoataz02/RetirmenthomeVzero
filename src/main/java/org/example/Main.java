package org.example;

import org.bson.Document;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize the DonationAdmin instance
        DonationAdmin admin = new DonationAdmin();

        // 1. Create a Money Donation
        System.out.println("=== Creating a Money Donation ===");
        Donation moneyDonation = admin.createDonation("2024-12-01", 100.0, 1, 101, "USD");
        if (moneyDonation != null) {
            System.out.println("Money Donation Created: " + moneyDonation);
        } else {
            System.err.println("Failed to create Money Donation.");
        }

        // 2. Create a Medicine Donation
        System.out.println("\n=== Creating a Medicine Donation ===");
        Donation medicineDonation = admin.createDonation("2024-12-02", 50.0, 2, 102, "Aspirin");
        if (medicineDonation != null) {
            System.out.println("Medicine Donation Created: " + medicineDonation);
        } else {
            System.err.println("Failed to create Medicine Donation.");
        }

        // 3. Retrieve Donations by ID (Optional)
        if (moneyDonation != null) {
            System.out.println("\n=== Retrieving the Money Donation ===");
            Donation retrievedDonation = admin.getDonation(moneyDonation.getDonationId());
            if (retrievedDonation != null) {
                System.out.println("Retrieved Donation: " + retrievedDonation);
            } else {
                System.err.println("Donation not found.");
            }
        }

        // 4. Approve the Money Donation (Pending Approval -> Processing)
        if (moneyDonation != null) {
            System.out.println("\n=== Approving the Money Donation ===");
            admin.approveDonation(moneyDonation);
            Donation updatedDonation = admin.getDonation(moneyDonation.getDonationId());
            System.out.println("After Approval: " + updatedDonation);
        }

        // 5. Process the Money Donation to Completion (Processing -> Completed)
        if (moneyDonation != null) {
            System.out.println("\n=== Processing the Money Donation to Completion ===");
            admin.processNextStep(moneyDonation);
            Donation completedDonation = admin.getDonation(moneyDonation.getDonationId());
            System.out.println("After Processing to Completion: " + completedDonation);
        }

        // 6. Reject the Medicine Donation
        if (medicineDonation != null) {
            System.out.println("\n=== Rejecting the Medicine Donation ===");
            admin.rejectDonation(medicineDonation);
            Donation rejectedDonation = admin.getDonation(medicineDonation.getDonationId());
            System.out.println("After Rejection: " + rejectedDonation);
        }

        // 7. Generate Receipt for Money Donation
        if (moneyDonation != null) {
            System.out.println("\n=== Generating Receipt for Money Donation ===");
            IReceiptGenerator moneyReceiptGenerator = new MoneyDonationReceiptAdapter();
            String moneyReceipt = moneyReceiptGenerator.generateReceipt(moneyDonation);
            System.out.println(moneyReceipt);
        }

        // 8. Generate Receipt for Medicine Donation
        if (medicineDonation != null) {
            System.out.println("\n=== Generating Receipt for Medicine Donation ===");
            IReceiptGenerator medicineReceiptGenerator = new MedicineDonationReceiptAdapter();
            String medicineReceipt = medicineReceiptGenerator.generateReceipt(medicineDonation);
            System.out.println(medicineReceipt);
        }

        // 9. List All Donations
        System.out.println("\n=== Listing All Donations ===");
        List<Donation> allDonations = admin.listAllDonations();
        for (Donation donation : allDonations) {
            System.out.println(donation);
        }

        // 10. Search Donations by Type (Optional)
        System.out.println("\n=== Searching for Donations with Type 'USD' ===");
        Document searchCriteria = new Document("type", "USD");
        List<Donation> searchedDonations = admin.searchDonations(searchCriteria);
        for (Donation donation : searchedDonations) {
            System.out.println(donation);
        }

        // 11. Final Listing of All Donations
        System.out.println("\n=== Final Listing of All Donations ===");
        allDonations = admin.listAllDonations();
        for (Donation donation : allDonations) {
            System.out.println(donation);
        }

        System.out.println("\n=== Donation Management Testing Completed ===");
    }
}
