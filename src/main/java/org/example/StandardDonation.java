//package org.example;
//
//public class StandardDonation implements DonationBehavior {
//    private String donationType;
//
//    // Constructor to initialize the donation type if needed
//    public StandardDonation(String donationType) {
//        this.donationType = donationType;
//    }
//
//    // Default constructor with a predefined donation type
//    public StandardDonation() {
//        this.donationType = "Standard";
//    }
//
//    @Override
//    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
//        // Optionally override the type with a predefined donation type
//        String donationType = (this.donationType != null && !this.donationType.isEmpty()) ? this.donationType : type;
//        return Donation.createDonation(date, amount, elderId, donatorId, donationType);
//    }
//
//    @Override
//    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
//        // Optionally override the type with a predefined donation type
//        String donationType = (this.donationType != null && !this.donationType.isEmpty()) ? this.donationType : type;
//        return Donation.updateDonation(donationId, date, amount, elderId, donationType);
//    }
//
//    @Override
//    public boolean cancelDonation(String donationId) {
//        return Donation.cancelDonation(donationId);
//    }
//}
