package org.example;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class DonationAdmin {

    public void approveDonation(Donation donation) {
        System.out.println("Admin approves the donation with ID: " + donation.getDonationId());
        donation.nextState();
    }

    public void rejectDonation(Donation donation) {
        System.out.println("Admin rejects the donation with ID: " + donation.getDonationId());
        donation.setRejectedState();
    }

    public void processNextStep(Donation donation) {
        System.out.println("Admin processes the next step of the donation with ID: " + donation.getDonationId());
        donation.nextState();
    }

    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        System.out.println("Admin is creating a new donation.");
        return Donation.createDonation(date, amount, elderId, donatorId, type);
    }

    public Donation getDonation(String donationId) {
        System.out.println("Admin is retrieving donation with ID: " + donationId);
        return Donation.getDonation(donationId);
    }

    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        System.out.println("Admin is updating donation with ID: " + donationId);
        return Donation.updateDonation(donationId, date, amount, elderId, type);
    }

    public boolean deleteDonation(String donationId) {
        System.out.println("Admin is deleting donation with ID: " + donationId);
        return Donation.cancelDonation(donationId);
    }

    public List<Donation> listAllDonations() {
        System.out.println("Admin is listing all donations.");
        List<Donation> donations = new ArrayList<>();
        for (Document doc : Donation.getDonationCollection().find()) {
            donations.add(new Donation(
                    doc.getObjectId("_id").toString(),
                    doc.getString("date"),
                    doc.getDouble("amount"),
                    doc.getInteger("elderId"),
                    doc.getInteger("donatorId"),
                    doc.getString("type"),
                    doc.getString("status")
            ));
        }
        return donations;
    }

    public List<Donation> searchDonations(Document criteria) {
        System.out.println("Admin is searching for donations with criteria: " + criteria.toJson());
        List<Donation> donations = new ArrayList<>();
        for (Document doc : Donation.getDonationCollection().find(criteria)) {
            donations.add(new Donation(
                    doc.getObjectId("_id").toString(),
                    doc.getString("date"),
                    doc.getDouble("amount"),
                    doc.getInteger("elderId"),
                    doc.getInteger("donatorId"),
                    doc.getString("type"),
                    doc.getString("status")
            ));
        }
        return donations;
    }

    public boolean updateDonationStatus(String donationId, String status) {
        System.out.println("Admin is updating the status of donation with ID: " + donationId);
        return Donation.updateDonation(donationId, null, 0, 0, null) != null;
    }
}