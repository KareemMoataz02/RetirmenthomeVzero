package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class VolunteerReservation {

    private static final String COLLECTION_NAME = "volunteerReservations";
    private static MongoCollection<Document> collection;

    // Static block to initialize the MongoDB collection using Singleton
    static {
        try {
            // Get the database from Singleton
            MongoDatabase database = Singleton.getInstance().getDatabase();
            collection = database.getCollection(COLLECTION_NAME);

            // Ensure collection exists, create if it doesn't
            if (!database.listCollectionNames().into(new java.util.ArrayList<>()).contains(COLLECTION_NAME)) {
                database.createCollection(COLLECTION_NAME);
                System.out.println("Collection 'volunteerReservations' created successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String id;  // ID of the reservation
    private int volunteerId; // Volunteer ID
    private int eventId; // Event ID
    private String status; // Status of the reservation

    // Constructor
    public VolunteerReservation(String id, int volunteerId, int eventId, String status) {
        this.id = id;
        this.volunteerId = volunteerId;
        this.eventId = eventId;
        this.status = status;
    }

    // Create a new volunteer reservation with a default status (e.g., "Pending")
    public static boolean create(int volunteerId, int eventId) {
        try {
            String id = new ObjectId().toString();
            Document document = new Document("id", id)
                    .append("volunteerId", volunteerId)
                    .append("eventId", eventId)
                    .append("status", "Pending");
            collection.insertOne(document);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get a volunteer reservation by ID
    public static VolunteerReservation getId(String id) {
        try {
            Document doc = collection.find(Filters.eq("id", id)).first();
            if (doc != null) {
                return new VolunteerReservation(
                        doc.getString("id"),
                        doc.getInteger("volunteerId"),
                        doc.getInteger("eventId"),
                        doc.getString("status")
                );
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error retrieving reservation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Remove a volunteer reservation by ID
    public static boolean remove(String id) {
        try {
            collection.deleteOne(Filters.eq("id", id));
            return true;
        } catch (Exception e) {
            System.err.println("Error removing reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Update the status of a volunteer reservation
    public boolean updateStatus(String newStatus) {
        try {
            Document updateDoc = new Document("$set", new Document("status", newStatus));
            collection.updateOne(Filters.eq("id", this.id), updateDoc);
            this.status = newStatus;
            return true;
        } catch (Exception e) {
            System.err.println("Error updating reservation status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return "VolunteerReservation{" +
                "id='" + id + '\'' +
                ", volunteerId=" + volunteerId +
                ", eventId=" + eventId +
                ", status='" + status + '\'' +
                '}';
    }
}
