package org.example;

public class MoneyDonationReceiptAdapter implements IReceiptGenerator {
    @Override
    public String generateReceipt(Donation donation) {
        return "Receipt for Money Donation\n" +
                "Donation ID: " + donation.getDonationId() + "\n" +
                "Amount: " + donation.getAmount() + " USD\n" +
                "Donator ID: " + donation.getDonatorId() + "\n" +
                "Date: " + donation.getDate() + "\n";
    }
}
