package asu.eng.models;

public class DonationReceipt {
    private int amount;

    public DonationReceipt(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int addValue() {
        return amount + 1;
    }
}
