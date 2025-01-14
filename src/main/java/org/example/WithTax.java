package asu.eng.models;

public class WithTax extends DonationReceipt{
    private DonationReceipt donationReceipt;

    public WithTax(DonationReceipt donationReceipt) {
        super(donationReceipt.getAmount());
        this.donationReceipt = donationReceipt;
    }

    @Override
    public int addValue() {
        return donationReceipt.getAmount() + super.getAmount() + 2;
    }
}
