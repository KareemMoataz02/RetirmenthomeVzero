package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Donation extends DonationContext {
    private String date;
    private double amount;
    private int elderId;
    private int donatorId;
    private String type;

    // Constructor
    public Donation(String donationId, String date, double amount, int elderId, int donatorId, String type, String status) {
        super(donationId, status);
        this.date = date;
        this.amount = amount;
        this.elderId = elderId;
        this.donatorId = donatorId;
        this.type = type;
    }

    // Getters
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

    public String getType() {
        return type;
    }

    public String getStatus() {
        return currentState instanceof RejectedState ? "Rejected" :
                currentState instanceof CompletedState ? "Completed" :
                        currentState instanceof ProcessingState ? "Processing" :
                                "PendingApproval";
    }

    // Method to set Rejected State
    public void setRejectedState() {
        System.out.println("Donation state set to Rejected.");
        setState(new RejectedState());
        updateStatusInDatabase("Rejected");
    }

    // Additional CRUD Operations
    public static Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        String id = new ObjectId().toString();
        Donation donation = new Donation(id, date, amount, elderId, donatorId, type, "PendingApproval");
        // Insert donation into the database
        try {
            Document doc = new Document("_id", new ObjectId(id))
                    .append("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("donatorId", donatorId)
                    .append("type", type)
                    .append("status", "PendingApproval");
            donationCollection.insertOne(doc);
            return donation;
        } catch (Exception e) {
            System.err.println("Error creating donation: " + e.getMessage());
            return null;
        }
    }

    public static Donation getDonation(String donationId) {
        try {
            Document doc = donationCollection.find(Filters.eq("_id", new ObjectId(donationId))).first();
            if (doc != null) {
                return new Donation(
                        doc.getObjectId("_id").toString(),
                        doc.getString("date"),
                        doc.getDouble("amount"),
                        doc.getInteger("elderId"),
                        doc.getInteger("donatorId"),
                        doc.getString("type"),
                        doc.getString("status")
                );
            }
        } catch (Exception e) {
            System.err.println("Error retrieving donation: " + e.getMessage());
        }
        return null;
    }

    public static Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        try {
            Document updateDoc = new Document("$set", new Document("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("type", type));
            donationCollection.updateOne(Filters.eq("_id", new ObjectId(donationId)), updateDoc);
            return getDonation(donationId);
        } catch (Exception e) {
            System.err.println("Error updating donation: " + e.getMessage());
        }
        return null;
    }

    public static boolean cancelDonation(String donationId) {
        try {
            donationCollection.deleteOne(Filters.eq("_id", new ObjectId(donationId)));
            return true;
        } catch (Exception e) {
            System.err.println("Error cancelling donation: " + e.getMessage());
            return false;
        }
    }

    // Ensure the collection is accessible
    public static MongoCollection<Document> getDonationCollection() {
        return donationCollection;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "donationId='" + donationId + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                ", elderId=" + elderId +
                ", donatorId=" + donatorId +
                ", type='" + type + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
