package org.example;

public class WithVAT extends ReceiptDecorator {
    private double vatRate;

    public WithVAT(IReceiptGenerator receipt, double vatRate) {
        super(receipt);
        this.vatRate = vatRate;
    }

    @Override
    public String generateReceipt(Donation donation) {
        String receipt = decoratedReceipt.generateReceipt(donation);
        double vatAmount = donation.getAmount() * vatRate;
        double newTotal = donation.getAmount() + vatAmount;
        return receipt + String.format("\nIncluding VAT (%.0f%%): %.2f (New Total: %.2f)", vatRate * 100, vatAmount, newTotal);
    }
}
