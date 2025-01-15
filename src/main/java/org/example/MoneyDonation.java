package org.example;

public class MoneyDonation implements DonationBehavior {
    private final MoneyCreate moneyCreate;

    public MoneyDonation() {
        this.moneyCreate = new MoneyCreate();
    }

    @Override
    public void createDonation(String date, double amount, int elderId, int donatorId, String type) {
        // Delegate creation to the template method
        moneyCreate.createDonation(date, amount, elderId, donatorId, type);
    }

    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        return Donation.updateDonation(donationId, date, amount, elderId, type);
    }

    @Override
    public boolean cancelDonation(String donationId) {
        return Donation.cancelDonation(donationId);
    }
}
