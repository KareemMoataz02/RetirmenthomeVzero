package org.example;

import com.mongodb.client.*;
import org.bson.Document;

public class Donator implements DonationBehavior {
    private String name;
    private DonationBehavior donationBehavior;
    private MongoCollection<Document> donatorCollection;

    // Constructor for Donator
    public Donator(String name) {
        this.name = name;
        this.donationBehavior = new StandardDonation(); // Donator uses StandardDonation behavior

        // Use Singleton to get the MongoDatabase instance
        MongoDatabase database = Singleton.getInstance().getDatabase();
        this.donatorCollection = database.getCollection("donators"); // Collection name
    }

    // Method to create a new donation in MongoDB and link it to a Donator
    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId) {
        // Create the donation using the StandardDonation behavior
        Donation newDonation = donationBehavior.createDonation(date, amount, elderId, donatorId);

        // Store Donator information in MongoDB
        Document donatorDoc = new Document("name", this.name)
                .append("donationId", newDonation.getDonationId())
                .append("date", date)
                .append("amount", amount)
                .append("elderId", elderId)
                .append("donatorId", donatorId);

        // Insert Donator into the MongoDB donators collection
        donatorCollection.insertOne(donatorDoc);

        // Return the created Donation object
        return newDonation;
    }

    // Method to update an existing donation in MongoDB and update Donator info
    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId) {
        // Update the donation using the StandardDonation behavior
        Donation updatedDonation = donationBehavior.updateDonation(donationId, date, amount, elderId);

        // Update Donator's donation information in MongoDB
        donatorCollection.updateOne(
                new Document("donationId", donationId),
                new Document("$set", new Document("date", date)
                        .append("amount", amount)
                        .append("elderId", elderId))
        );

        return updatedDonation;
    }

    // Method to cancel a donation and remove Donator from MongoDB
    @Override
    public boolean cancelDonation(String donationId) {
        // Cancel the donation using the StandardDonation behavior
        boolean result = donationBehavior.cancelDonation(donationId);

        // Remove Donator from the MongoDB collection if donation is cancelled
        donatorCollection.deleteOne(new Document("donationId", donationId));

        return result;
    }

    // Getter for the Donator's name
    public String getName() {
        return name;
    }

    // Generate a Donator ID (placeholder logic)
    private int generateDonatorId() {
        // Implement your own logic to generate a unique ID for Donator
        return (int) (Math.random() * 10000); // Example: generate random ID
    }
}
