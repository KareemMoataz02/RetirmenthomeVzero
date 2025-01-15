package org.example;

public abstract class DonationTemplate {
    public final Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        // Perform common steps before saving
        validateInputs(date, amount, elderId, donatorId, type);

        // Save donation to the database and get the created Donation object
        Donation donation = saveToDatabase(date, amount, elderId, donatorId, type);

        // Perform post-creation tasks specific to the donation type
        performPostCreationTasks(amount, type);

        return donation; // Return the created Donation
    }

    protected void validateInputs(String date, double amount, int elderId, int donatorId, String type) {
        if (date == null || date.isEmpty() || amount <= 0 || type == null || type.isEmpty()) {
            throw new IllegalArgumentException("Invalid donation inputs.");
        }
    }

    // Abstract method for saving the donation
    protected abstract Donation saveToDatabase(String date, double amount, int elderId, int donatorId, String type);

    // Abstract method for post-creation tasks
    protected abstract void performPostCreationTasks(double amount, String type);
}
