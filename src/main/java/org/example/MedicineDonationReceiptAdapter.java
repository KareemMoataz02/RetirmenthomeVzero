package org.example;

public class MedicineDonationReceiptAdapter implements IReceiptGenerator {
    @Override
    public String generateReceipt(Donation donation) {
        return "Receipt for Medicine Donation\n" +
                "Donation ID: " + donation.getDonationId() + "\n" +
                "Medicine Type: " + donation.getType() + "\n" +
                "Amount: " + donation.getAmount() + " units\n" +
                "Donator ID: " + donation.getDonatorId() + "\n" +
                "Date: " + donation.getDate() + "\n";
    }
}
