package org.example;

import java.util.HashMap;
import java.util.Map;

public class MoneyCreate extends DonationTemplate {
    private static final Map<String, Double> currencyTotals = new HashMap<>(); // Static map to track totals

    @Override
    protected void performPostCreationTasks(double amount, String type) {
        // Update the total for the given currency
        currencyTotals.put(type, currencyTotals.getOrDefault(type, 0.0) + amount);
        System.out.println("Updated total for currency " + type + ": " + currencyTotals.get(type));
    }

    // Getter for the currency totals (optional)
    public static Map<String, Double> getCurrencyTotals() {
        return currencyTotals;
    }
}
