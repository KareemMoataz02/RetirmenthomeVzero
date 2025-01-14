package asu.eng.models;

public class WithVAT extends DonationReceipt {
    private DonationReceipt donationReceipt;
    private static final double VAT_RATE = 0.15; // Example VAT rate of 15%

    public WithVAT(DonationReceipt donationReceipt) {
        super(donationReceipt.getAmount());
        this.donationReceipt = donationReceipt;
    }

    @Override
    public int addValue() {
        // Add VAT to the donation amount
        int vat = (int) (donationReceipt.getAmount() * VAT_RATE);
        return donationReceipt.addValue() + vat;
    }
}