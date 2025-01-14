package org.example;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;
import com.mongodb.client.model.Filters;

public class MoneyDonation implements DonationBehavior {
    private MongoCollection<Document> donationCollection;

    // Constructor to initialize MongoDB connection
    public MoneyDonation() {
        MongoDatabase database = Singleton.getInstance().getDatabase();
        this.donationCollection = database.getCollection("moneyDonations");
    }

    // Implement createDonation method to insert a new donation into MongoDB
    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId) {
        Document donationDocument = new Document()
                .append("date", date)
                .append("amount", amount)
                .append("elderId", elderId)
                .append("donatorId", donatorId);
        // Explicitly set medicineType to null for money donations
        donationDocument.append("medicineType", null);

        donationCollection.insertOne(donationDocument);
        ObjectId id = donationDocument.getObjectId("_id");
        return new Donation(id.toString(), date, amount, elderId, donatorId, null);
    }

    // Implement updateDonation method to update an existing donation in MongoDB
    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId) {
        Document updateDocument = new Document("$set", new Document()
                .append("date", date)
                .append("amount", amount)
                .append("elderId", elderId)
                // Ensure that medicineType remains null or is explicitly set to null
                .append("medicineType", null));

        donationCollection.updateOne(Filters.eq("_id", new ObjectId(donationId)), updateDocument);
        return new Donation(donationId, date, amount, elderId, -1, null); // donatorId remains unchanged or set appropriately
    }

    // Implement cancelDonation method to delete a donation from MongoDB
    @Override
    public boolean cancelDonation(String donationId) {
        var result = donationCollection.deleteOne(Filters.eq("_id", new ObjectId(donationId)));
        return result.getDeletedCount() > 0;
    }
}
