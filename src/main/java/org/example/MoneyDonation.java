package org.example;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import com.mongodb.client.model.Filters;

public class MoneyDonation implements DonationBehavior {
    private MongoCollection<Document> donationCollection;
    private String type; // Encapsulated field for currency

    // Constructor to initialize MongoDB connection and currency
    public MoneyDonation(String type) {
        MongoDatabase database = Singleton.getInstance().getDatabase();
        this.donationCollection = database.getCollection("moneyDonations");
        this.type = type; // Initialize currency
    }

    // Implement createDonation method to insert a new donation into MongoDB
    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        try {
            Document donationDocument = new Document()
                    .append("date", date)
                    .append("amount", amount)
                    .append("type", type) // Use the provided type parameter
                    .append("elderId", elderId)
                    .append("donatorId", donatorId);

            donationCollection.insertOne(donationDocument);
            ObjectId id = donationDocument.getObjectId("_id");
            return new Donation(id.toString(), date, amount, elderId, donatorId, type);
        } catch (Exception e) {
            System.err.println("Error creating donation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Implement updateDonation method to update an existing donation in MongoDB
    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        try {
            Document updateDocument = new Document("$set", new Document()
                    .append("date", date)
                    .append("amount", amount)
                    .append("type", type) // Update type with the provided value
                    .append("elderId", elderId));

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

    // Implement cancelDonation method to delete a donation from MongoDB
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
