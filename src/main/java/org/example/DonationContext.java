package org.example;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DonationContext {
    protected DonationState currentState;
    protected static MongoCollection<Document> donationCollection;
    protected String donationId;

    // Static block to initialize MongoDB connection
    static {
        try {
            MongoDatabase database = Singleton.getInstance().getDatabase();
            donationCollection = database.getCollection("donations");
        } catch (Exception e) {
            System.err.println("Error initializing MongoDB connection: " + e.getMessage());
        }
    }

    // Constructor
    public DonationContext(String donationId, String initialState) {
        this.donationId = donationId;
        this.currentState = initializeState(initialState);
    }

    // Initialize state based on status
    private DonationState initializeState(String status) {
        switch (status) {
            case "PendingApproval":
                return new PendingApprovalState();
            case "Processing":
                return new ProcessingState();
            case "Completed":
                return new CompletedState();
            case "Rejected":
                return new RejectedState();
            default:
                System.err.println("Unknown status: " + status + ". Setting to PendingApprovalState.");
                return new PendingApprovalState();
        }
    }

    // Setter for state
    public void setState(DonationState state) {
        this.currentState = state;
    }

    // Action methods
    public void nextState() {
        if (currentState != null) {
            currentState.nextState(this);
        } else {
            System.err.println("Current state is not set.");
        }
    }

    public void previousState() {
        if (currentState != null) {
            currentState.previousState(this);
        } else {
            System.err.println("Current state is not set.");
        }
    }

    public void printCurrentState() {
        if (currentState != null) {
            currentState.printCurrentState();
        } else {
            System.err.println("Current state is not set.");
        }
    }

    // Update the status in the database
    public void updateStatusInDatabase(String status) {
        try {
            Document updateDoc = new Document("$set", new Document("status", status));
            com.mongodb.client.result.UpdateResult result = donationCollection.updateOne(Filters.eq("_id", new ObjectId(donationId)), updateDoc);
            if (result.getMatchedCount() > 0) {
                if (result.getModifiedCount() > 0) {
                    System.out.println("Updated status in database to: " + status);
                } else {
                    System.out.println("Status was already: " + status);
                }
            } else {
                System.err.println("No donation found with ID: " + donationId);
            }
        } catch (Exception e) {
            System.err.println("Error updating status in database: " + e.getMessage());
        }
    }
}
