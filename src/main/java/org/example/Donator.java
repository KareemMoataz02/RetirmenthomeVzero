package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Donator implements DonationBehavior {
    private String name;
    private DonationBehavior donationBehavior;
    private MongoCollection<Document> donatorCollection;

    // Constructor for Donator
    public Donator(String name) {
        this.name = name;

        // Use Singleton to get the MongoDatabase instance
        MongoDatabase database = Singleton.getInstance().getDatabase();
        this.donatorCollection = database.getCollection("donators"); // Collection name
    }

    // Method to create a new donation in MongoDB and link it to a Donator
    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        try {
            // Create the donation using the StandardDonation behavior
            Donation newDonation = donationBehavior.createDonation(date, amount, elderId, donatorId, type);

            if (newDonation == null) {
                System.err.println("Failed to create donation.");
                return null;
            }

            // Store Donator information in MongoDB
            Document donatorDoc = new Document("name", this.name)
                    .append("donationId", newDonation.getDonationId())
                    .append("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("donatorId", donatorId)
                    .append("type", type);

            // Insert Donator into the MongoDB donators collection
            donatorCollection.insertOne(donatorDoc);

            return newDonation;
        } catch (Exception e) {
            System.err.println("Error creating donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Method to update an existing donation in MongoDB and update Donator info
    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        try {
            // Update the donation using the StandardDonation behavior
            Donation updatedDonation = donationBehavior.updateDonation(donationId, date, amount, elderId, type);

            if (updatedDonation == null) {
                System.err.println("Failed to update donation.");
                return null;
            }

            // Update Donator's donation information in MongoDB
            donatorCollection.updateOne(
                    new Document("donationId", donationId),
                    new Document("$set", new Document("date", date)
                            .append("amount", amount)
                            .append("elderId", elderId)
                            .append("type", type))
            );

            return updatedDonation;
        } catch (Exception e) {
            System.err.println("Error updating donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Method to cancel a donation and remove Donator from MongoDB
    @Override
    public boolean cancelDonation(String donationId) {
        try {
            // Cancel the donation using the StandardDonation behavior
            boolean result = donationBehavior.cancelDonation(donationId);

            if (!result) {
                System.err.println("Failed to cancel donation.");
                return false;
            }

            // Remove Donator from the MongoDB collection if donation is cancelled
            donatorCollection.deleteOne(new Document("donationId", donationId));

            return true;
        } catch (Exception e) {
            System.err.println("Error canceling donation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Getter for the Donator's name
    public String getName() {
        return name;
    }
}
