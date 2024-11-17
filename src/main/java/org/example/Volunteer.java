package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Volunteer {

    private static final String DATABASE_NAME = "retirementHome";
    private static final String RESERVATION_COLLECTION = "volunteerReservations";
    private static final String EVENT_COLLECTION = "events";
    private static final String VOLUNTEER_COLLECTION = "volunteers"; // Add a collection for volunteers
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> reservationCollection;
    private MongoCollection<Document> eventCollection;
    private MongoCollection<Document> volunteerCollection; // Mongo collection for volunteers
    private User volunteer;

    // Constructor initializes MongoDB client, database, and collections
    public Volunteer(User volunteer) {
        try {
            this.mongoClient = MongoClients.create("mongodb://localhost:27017");
            this.database = mongoClient.getDatabase(DATABASE_NAME);
            this.reservationCollection = database.getCollection(RESERVATION_COLLECTION);
            this.eventCollection = database.getCollection(EVENT_COLLECTION);
            this.volunteerCollection = database.getCollection(VOLUNTEER_COLLECTION); // Initialize volunteer collection
            this.volunteer = volunteer;
        } catch (Exception e) {
            System.err.println("Error initializing MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create a volunteer and store in the database
    public boolean createVolunteer(String name, int age) {
        try {
            String volunteerId = new ObjectId().toString(); // Generate a unique volunteer ID
            Document volunteerDoc = new Document("id", volunteerId)
                    .append("name", name)
                    .append("age", age);

            volunteerCollection.insertOne(volunteerDoc);
            System.out.println("Volunteer added to database: " + volunteerDoc);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating volunteer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Volunteer makes a reservation for an event
    public boolean makeReservation(int eventId) {
        try {
            Document eventDoc = eventCollection.find(Filters.eq("id", eventId)).first();
            if (eventDoc != null) {
                String reservationId = new ObjectId().toString();
                Document reservationDoc = new Document("id", reservationId)
                        .append("volunteerId", this.volunteer.getId())
                        .append("eventId", eventId)
                        .append("status", "Pending");

                reservationCollection.insertOne(reservationDoc);
                return true;
            }
            return false; // Event not found
        } catch (Exception e) {
            System.err.println("Error making reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Cancel a reservation
    public boolean cancelReservation(String reservationId) {
        try {
            reservationCollection.deleteOne(Filters.eq("id", reservationId));
            return true;
        } catch (Exception e) {
            System.err.println("Error canceling reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
