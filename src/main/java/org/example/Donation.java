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
    private String medicineType; // Corrected to lowercase 'private'
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> donationCollection;

    // Constructor to initialize MongoDB connection using Singleton
    public Donation() {
        // Get MongoDatabase instance from Singleton
        this.database = Singleton.getInstance().getDatabase(); // Corrected to assign once
        this.donationCollection = database.getCollection("donations"); // Collection name
    }

    // Constructor to create Donation object from fields
    public Donation(String donationId, String date, double amount, int elderId, int donatorId, String medicineType) {
        this.donationId = donationId;
        this.date = date;
        this.amount = amount;
        this.elderId = elderId;
        this.donatorId = donatorId;
        this.medicineType = medicineType;
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

    public String getMedicineType() {
        return medicineType;
    }

    @Override
    public String toString() {
        return "Donation ID: " + donationId + ", Date: " + date + ", Amount: " + amount +
                (medicineType != null ? ", Medicine Type: " + medicineType : "");
    }

    // MongoDB operations:

    // Method to create a new donation in the MongoDB database
    public Donation createDonation(String date, double amount, int elderId, int donatorId, String medicineType) {
        try {
            // Create a MongoDB document for donation
            Document donationDoc = new Document("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("donatorId", donatorId);

            if (medicineType != null && !medicineType.isEmpty()) {
                donationDoc.append("medicineType", medicineType);
            }

            // Insert the document into MongoDB collection
            donationCollection.insertOne(donationDoc);

            // After inserting, retrieve the generated _id to set the donationId
            String donationId = donationDoc.getObjectId("_id").toString();

            // Return a new Donation object created with the inserted data
            if (medicineType != null && !medicineType.isEmpty()) {
                return new Donation(donationId, date, amount, elderId, donatorId, medicineType);
            } else {
                return new Donation(donationId, date, amount, elderId, donatorId, null);
            }
        } catch (Exception e) {
            System.err.println("Error creating donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Overloaded method without medicineType for non-medicine donations
    public Donation createDonation(String date, double amount, int elderId, int donatorId) {
        return createDonation(date, amount, elderId, donatorId, null);
    }

    // Method to update an existing donation in MongoDB
    public boolean updateDonation(String donationId, String date, double amount, int elderId, String medicineType) {
        try {
            // Create the updated data for the donation
            Document updateDoc = new Document();
            if (date != null && !date.isEmpty()) {
                updateDoc.append("date", date);
            }
            if (amount >= 0) { // Assuming amount can't be negative
                updateDoc.append("amount", amount);
            }
            if (elderId > 0) { // Assuming valid elderId is positive
                updateDoc.append("elderId", elderId);
            }
            if (medicineType != null) {
                updateDoc.append("medicineType", medicineType);
            }

            Document setDoc = new Document("$set", updateDoc);

            // Update the donation in the MongoDB collection
            donationCollection.updateOne(Filters.eq("_id", new ObjectId(donationId)), setDoc);

            // Optionally, you can verify the update
            Document updatedDoc = donationCollection.find(Filters.eq("_id", new ObjectId(donationId))).first();
            if (updatedDoc != null) {
                System.out.println("Donation with ID " + donationId + " updated successfully.");
                return true;
            } else {
                System.err.println("Donation with ID " + donationId + " not found.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error updating donation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Overloaded method without medicineType for non-medicine donations
    public boolean updateDonation(String donationId, String date, double amount, int elderId) {
        return updateDonation(donationId, date, amount, elderId, null);
    }

    // Method to delete a donation from MongoDB
    public boolean cancelDonation(String donationId) {
        try {
            // Delete the donation from the collection
            var result = donationCollection.deleteOne(Filters.eq("_id", new ObjectId(donationId)));
            if (result.getDeletedCount() > 0) {
                System.out.println("Donation with ID " + donationId + " deleted successfully.");
                return true;
            } else {
                System.err.println("Donation with ID " + donationId + " not found.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error deleting donation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Method to retrieve a donation from MongoDB by its donationId
    public Donation getDonation(String donationId) {
        try {
            // Retrieve the donation document by donationId (_id)
            Document doc = donationCollection.find(Filters.eq("_id", new ObjectId(donationId))).first();

            if (doc != null) {
                // Create a Donation object from the MongoDB document and return it
                String medType = doc.getString("medicineType");
                if (medType != null) {
                    return new Donation(
                            doc.getObjectId("_id").toString(),
                            doc.getString("date"),
                            doc.getDouble("amount"),
                            doc.getInteger("elderId"),
                            doc.getInteger("donatorId"),
                            medType
                    );
                } else {
                    return new Donation(
                            doc.getObjectId("_id").toString(),
                            doc.getString("date"),
                            doc.getDouble("amount"),
                            doc.getInteger("elderId"),
                            doc.getInteger("donatorId"),
                            null
                    );
                }
            } else {
                // Return null if the donation is not found
                System.err.println("Donation with ID " + donationId + " not found.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error retrieving donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Close MongoDB connection when done
    public void close() {
        if (mongoClient != null) {
            mongoClient.close(); // Close the MongoDB client connection
        }
    }
}
