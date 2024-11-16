package org.example;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;

public class VolunteerReservation {

    private static final String DATABASE_NAME = "retirementHome";
    private static final String COLLECTION_NAME = "volunteerReservations";
    private static MongoCollection<Document> collection;

    static {
        // Establish MongoDB connection
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

        // Create collection if it doesn't exist
        if (!database.listCollectionNames().into(new java.util.ArrayList<>()).contains(COLLECTION_NAME)) {
            database.createCollection(COLLECTION_NAME);
            System.out.println("Collection 'volunteerReservations' created successfully.");
        }

        // Get the volunteerReservations collection
        collection = database.getCollection(COLLECTION_NAME);
    }

    private String id;  // Changed id to String
    private int volunteerId;
    private int eventId;
    private String status; // New status field

    // Constructor
    public VolunteerReservation(String id, int volunteerId, int eventId, String status) {
        this.id = id;  // Set id as String
        this.volunteerId = volunteerId;
        this.eventId = eventId;
        this.status = status; // Set status
    }

    // Create a new volunteer reservation with a default status (e.g., "Pending")
    public static boolean create(int volunteerId, int eventId) {
        try {
            String id = java.util.UUID.randomUUID().toString();  // Generate a random UUID as id
            Document document = new Document("id", id)  // Use id as String
                    .append("volunteerId", volunteerId)
                    .append("eventId", eventId)
                    .append("status", "Pending"); // Default status
            collection.insertOne(document);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a volunteer reservation by id
    public static VolunteerReservation get(String id) {
        try {
            Document doc = collection.find(Filters.eq("id", id)).first();
            if (doc != null) {
                return new VolunteerReservation(doc.getString("id"),
                        doc.getInteger("volunteerId"),
                        doc.getInteger("eventId"),
                        doc.getString("status")); // Retrieve status as well
            } else {
                return null; // Not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Remove a volunteer reservation by id
    public static boolean remove(String id) {
        try {
            collection.deleteOne(Filters.eq("id", id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update the status of a volunteer reservation
    public boolean updateStatus(String newStatus) {
        try {
            // Update the status field in the database
            Document updateDoc = new Document("$set", new Document("status", newStatus));
            collection.updateOne(Filters.eq("id", this.id), updateDoc);
            this.status = newStatus; // Update the status in the object as well
            return true;
        } catch (Exception e) {
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

    public static void main(String[] args) {
        // Test the functionality
        // Create a new volunteer reservation
        boolean created = VolunteerReservation.create(101, 202);
        System.out.println("Created VolunteerReservation: " + created);

        // Retrieve a volunteer reservation by ID
        VolunteerReservation reservation = VolunteerReservation.get("some-unique-id"); // Replace with actual id
        if (reservation != null) {
            System.out.println("Retrieved VolunteerReservation: " + reservation);
        } else {
            System.out.println("VolunteerReservation not found");
        }

        // Update the reservation status
        if (reservation != null) {
            boolean statusUpdated = reservation.updateStatus("Confirmed");
            System.out.println("Updated status: " + statusUpdated);
            System.out.println("Updated VolunteerReservation: " + reservation);
        }

        // Remove a volunteer reservation by ID
        boolean removed = VolunteerReservation.remove("some-unique-id"); // Replace with actual id
        System.out.println("Removed VolunteerReservation: " + removed);
    }
}
