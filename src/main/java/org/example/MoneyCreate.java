package org.example;

public class MoneyCreate extends DonationTemplate {
    private static final java.util.Map<String, Double> currencyTotals = new java.util.HashMap<>();

    @Override
    protected Donation saveToDatabase(String date, double amount, int elderId, int donatorId, String type) {
        Donation donation = Donation.createDonation(date, amount, elderId, donatorId, type);
        if (donation == null) {
            throw new RuntimeException("Failed to save money donation to the database.");
        }
        return donation;
    }

    @Override
    protected void performPostCreationTasks(double amount, String type) {
        updateCurrencyTotals(amount, type);
    }

    public void updateCurrencyTotals(double amount, String type) {
        currencyTotals.put(type, currencyTotals.getOrDefault(type, 0.0) + amount);
        System.out.println("Updated total for currency " + type + ": " + currencyTotals.get(type));
    }

    public static java.util.Map<String, Double> getCurrencyTotals() {
        return currencyTotals;
    }
}
