package org.example;

public class WithTax extends ReceiptDecorator {
    private double taxRate;

    public WithTax(IReceiptGenerator receipt, double taxRate) {
        super(receipt);
        this.taxRate = taxRate;
    }

    @Override
    public String generateReceipt(Donation donation) {
        String receipt = decoratedReceipt.generateReceipt(donation);
        double taxAmount = donation.getAmount() * taxRate;
        double newTotal = donation.getAmount() + taxAmount;
        return receipt + String.format("\nIncluding Tax (%.0f%%): %.2f (New Total: %.2f)", taxRate * 100, taxAmount, newTotal);
    }
}
