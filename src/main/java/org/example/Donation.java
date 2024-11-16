package org.example;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

public class Donation {
    private String donationId;
    private String date;
    private double amount;
    private int elderId;
    private int donatorId;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> donationCollection;

    // Constructor to initialize MongoDB connection
    public Donation() {
        // MongoDB client connection setup
        this.mongoClient = MongoClients.create("mongodb://localhost:27017"); // Connection string
        this.database = mongoClient.getDatabase("retirementHome"); // Database name
        this.donationCollection = database.getCollection("donations"); // Collection name
    }

    // Constructor to create Donation object from fields
    public Donation(String donationId, String date, double amount, int elderId, int donatorId) {
        this.donationId = donationId;
        this.date = date;
        this.amount = amount;
        this.elderId = elderId;
        this.donatorId = donatorId;
    }

    // Getter methods
    public String getDonationId() {
        return donationId;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public int getElderId() {
        return elderId;
    }

    public int getDonatorId() {
        return donatorId;
    }

    @Override
    public String toString() {
        return "Donation ID: " + donationId + ", Date: " + date + ", Amount: " + amount;
    }

    // MongoDB operations:

    // Method to create a new donation in the MongoDB database
    public Donation createDonation(String date, double amount, int elderId, int donatorId) {
        // Create a MongoDB document for donation
        Document donationDoc = new Document("date", date)
                .append("amount", amount)
                .append("elderId", elderId)
                .append("donatorId", donatorId);

        // Insert the document into MongoDB collection
        donationCollection.insertOne(donationDoc);

        // After inserting, retrieve the generated _id to set the donationId
        String donationId = donationDoc.getObjectId("_id").toString();

        // Return a new Donation object created with the inserted data
        return new Donation(donationId, date, amount, elderId, donatorId);
    }

    // Method to update an existing donation in MongoDB
    public boolean updateDonation(String donationId, String date, double amount, int elderId) {
        // Create the updated data for the donation
        Document updateDoc = new Document("date", date)
                .append("amount", amount)
                .append("elderId", elderId);

        // Update the donation in the MongoDB collection
        donationCollection.updateOne(Filters.eq("_id", new ObjectId(donationId)), new Document("$set", updateDoc));

        return true;
    }

    // Method to delete a donation from MongoDB
    public boolean cancelDonation(String donationId) {
        // Delete the donation from the collection
        donationCollection.deleteOne(Filters.eq("_id", new ObjectId(donationId)));

        return true;
    }

    // Method to retrieve a donation from MongoDB by its donationId
    public Donation getDonation(String donationId) {
        // Retrieve the donation document by donationId (_id)
        Document doc = donationCollection.find(Filters.eq("_id", new ObjectId(donationId))).first();

        if (doc != null) {
            // Create a Donation object from the MongoDB document and return it
            return new Donation(
                    doc.getObjectId("_id").toString(),
                    doc.getString("date"),
                    doc.getDouble("amount"),
                    doc.getInteger("elderId"),
                    doc.getInteger("donatorId")
            );
        } else {
            // Return null if the donation is not found
            return null;
        }
    }

    // Close MongoDB connection when done
    public void close() {
        mongoClient.close(); // Close the MongoDB client connection
    }
}
