package org.example;

public class MedicineDonation implements DonationBehavior {
    private final MedicineCreate medicineCreate;

    public MedicineDonation() {
        this.medicineCreate = new MedicineCreate();
    }

    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        // Delegate to the `MedicineCreate` class
        return medicineCreate.createDonation(date, amount, elderId, donatorId, type);
    }

    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        return Donation.updateDonation(donationId, date, amount, elderId, type);
    }

    @Override
    public boolean cancelDonation(String donationId) {
        return Donation.cancelDonation(donationId);
    }
}
