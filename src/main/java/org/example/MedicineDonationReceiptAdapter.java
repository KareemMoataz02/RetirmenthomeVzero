package org.example;

public class MedicineDonationReceiptAdapter implements IReceiptGenerator {
    private final MoneyDonationReceiptAdapter moneyAdapter;

    public MedicineDonationReceiptAdapter() {
        this.moneyAdapter = new MoneyDonationReceiptAdapter();
    }

    @Override
    public String generateReceipt(Donation donation) {
        // Generate the base receipt using the money donation adapter
        String baseReceipt = moneyAdapter.generateReceipt(donation);

        // Modify the receipt for medicine donation specifics
        StringBuilder medicineReceipt = new StringBuilder();

        // Replace "Money Donation" with "Medicine Donation"
        medicineReceipt.append(baseReceipt.replace("Money Donation", "Medicine Donation"));

        // Replace the amount with units if necessary
        // Assuming that for medicine donations, 'type' might represent the unit type
        medicineReceipt = new StringBuilder(
                medicineReceipt.toString().replace(
                        donation.getAmount() + " " + donation.getType(),
                        donation.getAmount() + " units of " + donation.getType()
                )
        );

        // Add medicine-specific details if any
        medicineReceipt.append("Medicine Details: ").append(donation.getType()).append("\n");

        return medicineReceipt.toString();
    }
}
