package org.example;

import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

public class StandardDonation implements DonationBehavior {
    private MongoDatabase database;
    private MongoCollection<Document> donationCollection;

    // Constructor to initialize MongoDB connection
    public StandardDonation() {
        try {
            // Establish MongoDB connection using Singleton
            this.database = Singleton.getInstance().getDatabase(); // Get the database using Singleton
            this.donationCollection = database.getCollection("donations"); // Collection name for standard donations
        } catch (Exception e) {
            System.err.println("Error initializing StandardDonation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId) {
        try {
            // Create a MongoDB document for the donation
            Document donationDocument = new Document("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("donatorId", donatorId)
                    .append("medicineType", null); // Explicitly set medicineType to null for standard donations

            // Insert the donation into MongoDB
            donationCollection.insertOne(donationDocument);

            // Retrieve the generated ObjectId
            ObjectId objectId = donationDocument.getObjectId("_id");

            // Return a new Donation object with the inserted data
            return new Donation(
                    objectId.toString(),
                    date,
                    amount,
                    elderId,
                    donatorId,
                    null // medicineType is null for standard donations
            );
        } catch (Exception e) {
            System.err.println("Error creating standard donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId) {
        try {
            // Build the update document with provided fields
            Document updateDoc = new Document();
            if (date != null && !date.isEmpty()) {
                updateDoc.append("date", date);
            }
            if (amount >= 0) { // Ensure amount is non-negative
                updateDoc.append("amount", amount);
            }
            if (elderId > 0) { // Ensure elderId is valid
                updateDoc.append("elderId", elderId);
            }

            // Apply the update using $set
            Document setDoc = new Document("$set", updateDoc);

            // Perform the update operation
            UpdateResult result = donationCollection.updateOne(
                    Filters.eq("_id", new ObjectId(donationId)),
                    setDoc
            );

            // Check if the update was acknowledged and matched a document
            if (result.getMatchedCount() == 0) {
                System.err.println("No donation found with ID: " + donationId);
                return null;
            }

            // Retrieve and return the updated donation
            return getDonation(donationId);
        } catch (Exception e) {
            System.err.println("Error updating standard donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean cancelDonation(String donationId) {
        try {
            // Perform the delete operation
            DeleteResult result = donationCollection.deleteOne(Filters.eq("_id", new ObjectId(donationId)));

            // Check if a document was deleted
            if (result.getDeletedCount() > 0) {
                System.out.println("Donation with ID " + donationId + " deleted successfully.");
                return true;
            } else {
                System.err.println("No donation found to delete with ID: " + donationId);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error cancelling standard donation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve a donation by its ID
    public Donation getDonation(String donationId) {
        try {
            // Find the donation document by ID
            Document document = donationCollection.find(Filters.eq("_id", new ObjectId(donationId))).first();

            if (document != null) {
                return new Donation(
                        document.getObjectId("_id").toString(),
                        document.getString("date"),
                        document.getDouble("amount"),
                        document.getInteger("elderId"),
                        document.getInteger("donatorId"),
                        document.getString("medicineType") // This will be null
                );
            } else {
                System.err.println("No donation found with ID: " + donationId);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error retrieving standard donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Removed the close() method as Singleton manages the MongoDB connection
}
