package org.example;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import com.mongodb.client.model.Filters;

public class MedicineDonation implements DonationBehavior {
    private MongoCollection<Document> donationCollection;
    private String type; // Encapsulated field for medicine type

    // Constructor to set up MongoDB collection connection and initialize medicine type
    public MedicineDonation(String medicineType) {
        MongoDatabase database = Singleton.getInstance().getDatabase();
        this.donationCollection = database.getCollection("medicineDonations");
        this.type = medicineType; // Corrected initialization
    }

    // Create a new donation record in the database
    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        try {
            Document donationDocument = new Document()
                    .append("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("donatorId", donatorId)
                    .append("type", type);

            donationCollection.insertOne(donationDocument);
            ObjectId id = donationDocument.getObjectId("_id");

            return new Donation(id.toString(), date, amount, elderId, donatorId, type);
        } catch (Exception e) {
            System.err.println("Error creating donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Update an existing donation record in the database
    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        try {
            Document updateDocument = new Document("$set", new Document()
                    .append("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("type", type));

            donationCollection.updateOne(Filters.eq("_id", new ObjectId(donationId)), updateDocument);

            // Fetch donatorId from the database if needed
            Document updatedDoc = donationCollection.find(Filters.eq("_id", new ObjectId(donationId))).first();
            int donatorId = (updatedDoc != null) ? updatedDoc.getInteger("donatorId") : -1;

            return new Donation(donationId, date, amount, elderId, donatorId, type);
        } catch (Exception e) {
            System.err.println("Error updating donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Cancel (delete) a donation record from the database
    @Override
    public boolean cancelDonation(String donationId) {
        try {
            var result = donationCollection.deleteOne(Filters.eq("_id", new ObjectId(donationId)));
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            System.err.println("Error canceling donation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
