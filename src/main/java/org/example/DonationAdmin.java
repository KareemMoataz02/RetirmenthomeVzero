package org.example;

import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.ArrayList;

public class DonationAdmin {

    public void approveDonation(Donation donation) {
        System.out.println("Admin approves the donation with ID: " + donation.getDonationId());
        donation.nextState(); // Move to the next state
    }

    public void rejectDonation(Donation donation) {
        System.out.println("Admin rejects the donation with ID: " + donation.getDonationId());
        donation.setRejectedState(); // Transition to Rejected state
    }

    public void processNextStep(Donation donation) {
        System.out.println("Admin processes the next step of the donation with ID: " + donation.getDonationId());
        donation.nextState(); // Process the next state
    }

    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        System.out.println("Admin is creating a new donation.");
        Donation donation = Donation.createDonation(date, amount, elderId, donatorId, type);
        if (donation != null) {
            System.out.println("Donation created successfully with ID: " + donation.getDonationId());
        } else {
            System.err.println("Failed to create donation.");
        }
        return donation;
    }

    public Donation getDonation(String donationId) {
        System.out.println("Admin is retrieving donation with ID: " + donationId);
        Donation donation = Donation.getDonation(donationId);
        if (donation != null) {
            System.out.println("Donation retrieved: " + donation);
        } else {
            System.err.println("Donation with ID " + donationId + " not found.");
        }
        return donation;
    }

    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        System.out.println("Admin is updating donation with ID: " + donationId);
        Donation donation = Donation.updateDonation(donationId, date, amount, elderId, type);
        if (donation != null) {
            System.out.println("Donation updated successfully: " + donation);
        } else {
            System.err.println("Failed to update donation with ID: " + donationId);
        }
        return donation;
    }

    public boolean deleteDonation(String donationId) {
        System.out.println("Admin is deleting donation with ID: " + donationId);
        boolean isDeleted = Donation.cancelDonation(donationId);
        if (isDeleted) {
            System.out.println("Donation with ID " + donationId + " deleted successfully.");
        } else {
            System.err.println("Failed to delete donation with ID: " + donationId);
        }
        return isDeleted;
    }

    public List<Donation> listAllDonations() {
        System.out.println("Admin is listing all donations.");
        List<Donation> donations = new ArrayList<>();
        try {
            for (Document doc : Donation.getDonationCollection().find()) {
                Donation donation = new Donation(
                        doc.getObjectId("_id").toString(),
                        doc.getString("date"),
                        doc.getDouble("amount"),
                        doc.getInteger("elderId"),
                        doc.getInteger("donatorId"),
                        doc.getString("type"),
                        doc.getString("status")
                );
                donations.add(donation);
            }
            System.out.println("Total donations found: " + donations.size());
        } catch (Exception e) {
            System.err.println("Error listing donations: " + e.getMessage());
            e.printStackTrace();
        }
        return donations;
    }

    public List<Donation> searchDonations(Document criteria) {
        System.out.println("Admin is searching for donations with criteria: " + criteria.toJson());
        List<Donation> donations = new ArrayList<>();
        try {
            for (Document doc : Donation.getDonationCollection().find(criteria)) {
                Donation donation = new Donation(
                        doc.getObjectId("_id").toString(),
                        doc.getString("date"),
                        doc.getDouble("amount"),
                        doc.getInteger("elderId"),
                        doc.getInteger("donatorId"),
                        doc.getString("type"),
                        doc.getString("status")
                );
                donations.add(donation);
            }
            System.out.println("Donations found: " + donations.size());
        } catch (Exception e) {
            System.err.println("Error searching donations: " + e.getMessage());
            e.printStackTrace();
        }
        return donations;
    }

    public boolean updateDonationStatus(String donationId, String status) {
        System.out.println("Admin is updating status of donation with ID: " + donationId + " to " + status);
        try {
            Document updateDoc = new Document("$set", new Document("status", status));
            com.mongodb.client.result.UpdateResult result = Donation.getDonationCollection().updateOne(Filters.eq("_id", new ObjectId(donationId)), updateDoc);
            if (result.getMatchedCount() > 0) {
                if (result.getModifiedCount() > 0) {
                    System.out.println("Donation status updated successfully.");
                } else {
                    System.out.println("Donation status was already: " + status);
                }
                return true;
            } else {
                System.err.println("No donation found with ID: " + donationId);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error updating donation status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
