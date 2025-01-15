package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Donator {
    private String name;
    private MongoCollection<Document> donatorCollection;

    public Donator(String name) {
        this.name = name;
        MongoDatabase database = Singleton.getInstance().getDatabase();
        this.donatorCollection = database.getCollection("donators");
    }

    public Donation createDonation(String date, double amount, int elderId, int donatorId, String type) {
        Donation newDonation = Donation.createDonation(date, amount, elderId, donatorId, type);
        if (newDonation != null) {
            Document donatorDoc = new Document("name", this.name)
                    .append("donationId", newDonation.getDonationId())
                    .append("date", date)
                    .append("amount", amount)
                    .append("elderId", elderId)
                    .append("donatorId", donatorId)
                    .append("type", type);
            donatorCollection.insertOne(donatorDoc);
        }
        return newDonation;
    }

    public Donation updateDonation(String donationId, String date, double amount, int elderId, String type) {
        return Donation.updateDonation(donationId, date, amount, elderId, type);
    }

    public boolean cancelDonation(String donationId) {
        return Donation.cancelDonation(donationId);
    }

    public String getName() {
        return name;
    }
}
