package org.example;

import org.bson.Document;  // Assuming MongoDB is used
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.types.ObjectId;

public class MedicineDonation implements DonationBehavior {
    private MongoCollection<Document> donationCollection;

    // Constructor to set up MongoDB collection connection
    public MedicineDonation() {
        MongoDatabase database = Singleton.getInstance().getDatabase();
        this.donationCollection = database.getCollection("medicineDonations");
    }

    // Create a new donation record in the database
    @Override
    public Donation createDonation(String date, double amount, int elderId, int donatorId) {
        Document donationDocument = new Document()
                .append("date", date)
                .append("amount", amount)
                .append("elderId", elderId)
                .append("donatorId", donatorId)
                .append("medicineType", "Generic"); // Default or could be a parameter

        donationCollection.insertOne(donationDocument);
        ObjectId id = donationDocument.getObjectId("_id");
        return new Donation(id.toString(), date, amount, elderId, donatorId);
    }

    // Update an existing donation record in the database
    @Override
    public Donation updateDonation(String donationId, String date, double amount, int elderId) {
        Document updateDocument = new Document("$set", new Document()
                .append("date", date)
                .append("amount", amount)
                .append("elderId", elderId));

        donationCollection.updateOne(new Document("_id", new ObjectId(donationId)), updateDocument);
        return new Donation(donationId, date, amount, elderId, -1);  // donatorId not updated, pass as -1 or fetch if needed
    }

    // Cancel (delete) a donation record from the database
    @Override
    public boolean cancelDonation(String donationId) {
        var result = donationCollection.deleteOne(new Document("_id", new ObjectId(donationId)));
        return result.getDeletedCount() > 0;
    }
}
