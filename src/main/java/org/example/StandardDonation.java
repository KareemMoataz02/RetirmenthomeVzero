package org.example;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

public class StandardDonation implements DonationBehavior {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> donationCollection;


    // Constructor to initialize MongoDB connection
    public StandardDonation() {
        {
            // Establish MongoDB connection
            MongoDatabase database = Singleton.getInstance().getDatabase(); // Get the database using Singleton
            this.donationCollection = database.getCollection("donations"); // Collection name for medical visits
        }
    }

    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId) {
        Document donationDocument = new Document("date", date)
                .append("amount", amount)
                .append("elderId", elderId)
                .append("donatorId", donatorId); // Added donatorId here

        // Insert the donation into MongoDB
        donationCollection.insertOne(donationDocument);

        // Return a new Donation object with data from MongoDB
        return new Donation(
                donationDocument.getObjectId("_id").toString(),
                date,
                amount,
                elderId,
                donatorId // Pass donatorId here as well
        );
    }

    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId) {
        Document updateDoc = new Document("date", date)
                .append("amount", amount)
                .append("elderId", elderId);

        // Update the existing donation in the database
        donationCollection.updateOne(Filters.eq("_id", new ObjectId(donationId)), new Document("$set", updateDoc));

        return getDonation(donationId); // Return updated donation
    }

    @Override
    public boolean cancelDonation(String donationId) {
        // Delete the donation from the database
        donationCollection.deleteOne(Filters.eq("_id", new ObjectId(donationId)));
        return true; // Return true to indicate successful deletion
    }

    // Retrieve a donation by its ID
    public Donation getDonation(String donationId) {
        Document document = donationCollection.find(Filters.eq("_id", new ObjectId(donationId))).first();
        if (document != null) {
            return new Donation(
                    document.getObjectId("_id").toString(),
                    document.getString("date"),
                    document.getDouble("amount"),
                    document.getInteger("elderId"),
                    document.getInteger("donatorId")
            );
        }
        return null; // Return null if the donation is not found
    }

    public void close() {
        mongoClient.close(); // Close MongoDB connection
    }
}
