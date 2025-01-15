package org.example;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        DonationBehavior moneyDonationBehavior = new MoneyDonation();

        Donation donation = moneyDonationBehavior.createDonation("2025-01-15", 50.0, 101, 1001, "USD");

        // Base receipt generator
        IReceiptGenerator baseReceipt = new MoneyDonationReceiptAdapter();

        // Decorate with VAT
        IReceiptGenerator receiptWithVAT = new WithVAT(baseReceipt, 0.20); // Adding 20% VAT

        // Decorate with Tax
        IReceiptGenerator receiptWithTax = new WithTax(baseReceipt, 0.10); // Adding 10% Tax

        // Decorate with both VAT and Tax
        IReceiptGenerator receiptWithBoth = new WithVAT(new WithTax(baseReceipt, 0.10), 0.20);

        System.out.println("Receipt with VAT:");
        System.out.println(receiptWithVAT.generateReceipt(donation));

        System.out.println("\nReceipt with Tax:");
        System.out.println(receiptWithTax.generateReceipt(donation));

        System.out.println("\nReceipt with Both VAT and Tax:");
        System.out.println(receiptWithBoth.generateReceipt(donation));
    }
}

