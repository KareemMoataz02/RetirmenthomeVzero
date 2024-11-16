package org.example;

public interface DonationBehavior {

    // Method to create a donation
    Donation createDonation(String date, double amount, int elderId, int donatorId);

    // Method to update a donation
    Donation updateDonation(String donationId, String date, double amount, int elderId);

    // Method to cancel a donation
    boolean cancelDonation(String donationId);
}
