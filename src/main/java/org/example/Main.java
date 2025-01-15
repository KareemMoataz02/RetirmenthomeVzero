package org.example;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize MoneyDonation
            MoneyDonation moneyDonation = new MoneyDonation();

            // Create a donation in USD
            System.out.println("Creating USD Donation...");
            Donation usdDonation = moneyDonation.createDonation("2025-01-15", 100.0, 101, 102, "USD");
            System.out.println("Created USD Donation: " + usdDonation);

            // Create a donation in EGP
            System.out.println("\nCreating EGP Donation...");
            Donation egpDonation = moneyDonation.createDonation("2025-01-16", 500.0, 103, 104, "EGP");
            System.out.println("Created EGP Donation: " + egpDonation);

            // Update the USD donation
            System.out.println("\nUpdating USD Donation...");
            if (usdDonation != null) {
                Donation updatedUsdDonation = moneyDonation.updateDonation(
                        usdDonation.getDonationId(), "2025-01-20", 150.0, 101, "USD"
                );
                System.out.println("Updated USD Donation: " + updatedUsdDonation);
            }

            // Update the EGP donation
            System.out.println("\nUpdating EGP Donation...");
            if (egpDonation != null) {
                Donation updatedEgpDonation = moneyDonation.updateDonation(
                        egpDonation.getDonationId(), "2025-01-21", 300.0, 103, "EGP"
                );
                System.out.println("Updated EGP Donation: " + updatedEgpDonation);
            }

            // Display the updated totals
            System.out.println("\nCurrency Totals: " + MoneyCreate.getCurrencyTotals());

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
