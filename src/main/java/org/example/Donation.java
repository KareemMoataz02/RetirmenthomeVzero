package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Donation {
    private String donationId;
    private String date;
    private double amount;
    private int elderId;
    private int donatorId;
    private String type;
    private static MongoCollection<Document> donationCollection;

    // Static block to initialize MongoDB connection
    static {
        try {
            MongoDatabase database = Singleton.getInstance().getDatabase();
            donationCollection = database.getCollection("donations");
        } catch (Exception e) {
            System.err.println("Error initializing MongoDB connection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Constructor
    public Donation(String donationId, String date, double amount, int elderId, int donatorId, String type) {
        this.donationId = donationId;
        this.date = date;
        this.amount = amount;
        this.elderId = elderId;
        this.donatorId = donatorId;
        this.type = type;
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

    public String getType() {
        return type;
    }

    // Create a donation
    public static Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        try {
            Document donationDoc = new Document()
                    .append("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("donatorId", donatorId)
                    .append("type", type);

            donationCollection.insertOne(donationDoc);
            String donationId = donationDoc.getObjectId("_id").toString();

            return new Donation(donationId, date, amount, elderId, donatorId, type);
        } catch (Exception e) {
            System.err.println("Error creating donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Update a donation
    public static boolean updateDonation(String donationId, String date, double amount, int elderId, String type) {
        try {
            Document updateDoc = new Document("$set", new Document()
                    .append("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("type", type));

            donationCollection.updateOne(Filters.eq("_id", new ObjectId(donationId)), updateDoc);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating donation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve a donation
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
                        doc.getString("type")
                );
            }
        } catch (Exception e) {
            System.err.println("Error retrieving donation: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Cancel a donation
    public static boolean cancelDonation(String donationId) {
        try {
            var result = donationCollection.deleteOne(Filters.eq("_id", new ObjectId(donationId)));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error canceling donation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return "Donation ID: " + donationId + ", Date: " + date + ", Amount: " + amount +
                ", Elder ID: " + elderId + ", Donator ID: " + donatorId +
                (type != null ? ", Type: " + type : "");
    }
}
