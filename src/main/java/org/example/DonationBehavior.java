package org.example;

public interface DonationBehavior {
    void createDonation(String date, double amount, int elderId, int donatorId, String type);

    Donation updateDonation(String donationId, String date, double amount, int elderId, String type);

    boolean cancelDonation(String donationId);
}
