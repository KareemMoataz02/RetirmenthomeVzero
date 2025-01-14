package org.example;

public class MoneyDonation implements DonationBehavior {

    // Constructor to initialize the currency type
    public MoneyDonation() {
    }

    // Create a new donation by delegating to the Donation class
    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        // The 'type' parameter can be overridden by currency if needed
        return Donation.createDonation(date, amount, elderId, donatorId, type);
    }

    // Update an existing donation by delegating to the Donation class
    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        // The 'type' parameter can be overridden by currency if needed
        return Donation.updateDonation(donationId, date, amount, elderId, type);
    }

    // Cancel a donation by delegating to the Donation class
    @Override
    public boolean cancelDonation(String donationId) {
        return Donation.cancelDonation(donationId);
    }
}
