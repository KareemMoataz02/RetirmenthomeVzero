package org.example;

public class MoneyDonation implements DonationBehavior {
    private final MoneyCreate moneyCreate;

    public MoneyDonation() {
        this.moneyCreate = new MoneyCreate();
    }

    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        return moneyCreate.createDonation(date, amount, elderId, donatorId, type);
    }

    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        Donation updatedDonation = Donation.updateDonation(donationId, date, amount, elderId, type);
        if (updatedDonation != null) {
            moneyCreate.updateCurrencyTotals(amount, type);
        }
        return updatedDonation;
    }

    @Override
    public boolean cancelDonation(String donationId) {
        return Donation.cancelDonation(donationId);
    }
}
