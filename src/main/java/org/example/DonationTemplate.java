package org.example;

public abstract class DonationTemplate {
    // Template method
    public final void createDonation(String date, double amount, int elderId, int donatorId, String type) {
        validateDonation(date, amount, elderId, type);
        saveDonationToDatabase(date, amount, elderId, donatorId, type);
        performPostCreationTasks(amount, type); // Type-specific behavior
        logDonationCreation(date, amount, elderId, type);
    }

    // Common functions
    protected void validateDonation(String date, double amount, int elderId, String type) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date cannot be null or empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (elderId <= 0) {
            throw new IllegalArgumentException("Elder ID must be valid.");
        }
    }

    protected void saveDonationToDatabase(String date, double amount, int elderId, int donatorId, String type) {
        Donation.createDonation(date, amount, elderId, donatorId, type);
    }

    protected void logDonationCreation(String date, double amount, int elderId, String type) {
        System.out.println("Donation created: [Date: " + date + ", Amount: " + amount + ", Elder ID: " + elderId + ", Type: " + type + "]");
    }

    // Abstract method for type-specific tasks
    protected abstract void performPostCreationTasks(double amount, String type);
}
