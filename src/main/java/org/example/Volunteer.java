package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Volunteer {

    private static final String DATABASE_NAME = "retirementHome";
    private static final String RESERVATION_COLLECTION = "volunteerReservations";
    private static final String EVENT_COLLECTION = "events";
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> reservationCollection;
    private MongoCollection<Document> eventCollection;
    private User volunteer;

    // Constructor initializes MongoDB client, database, and collections
    public Volunteer(User volunteer) {
        this.mongoClient = MongoClients.create("mongodb://localhost:27017"); // MongoDB connection
        this.database = mongoClient.getDatabase(DATABASE_NAME);
        this.reservationCollection = database.getCollection(RESERVATION_COLLECTION);
        this.eventCollection = database.getCollection(EVENT_COLLECTION);
        this.volunteer = volunteer; // Volunteer making the reservation
    }

    // Make a reservation for a volunteer to an event
    public VolunteerReservation makeReservation(int eventId) {
        try {
            // Check if the event exists in the database
            Document eventDocument = eventCollection.find(Filters.eq("eventId", eventId)).first();
            if (eventDocument == null) {
                throw new IllegalArgumentException("Event not found with eventId: " + eventId);
            }

            // Create reservation document for the volunteer
            Document reservationDocument = new Document("eventId", eventId)
                    .append("volunteerId", volunteer.getId()) // Linking the volunteer
                    .append("status", "Reserved"); // Default status

            // Insert the reservation into the database
            reservationCollection.insertOne(reservationDocument);

            // Create and return a VolunteerReservation object
            return new VolunteerReservation(
                    reservationDocument.getObjectId("_id").toString(),
                    eventId,
                    volunteer.getId(),
                    "Reserved"
            );
        } catch (Exception e) {
            System.err.println("Error while making reservation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Retrieve a reservation by reservationId
    public VolunteerReservation getReservation(int reservationId) {
        try {
            // Find reservation document using reservationId
            Document reservationDocument = reservationCollection.find(Filters.eq("_id", new ObjectId(String.valueOf(reservationId)))).first();
            if (reservationDocument != null) {
                // Return a VolunteerReservation object populated from the document
                return new VolunteerReservation(
                        reservationDocument.getObjectId("_id").toString(),
                        reservationDocument.getInteger("eventId"),
                        reservationDocument.getInteger("volunteerId"),
                        reservationDocument.getString("status")
                );
            }
            return null; // If not found, return null
        } catch (Exception e) {
            System.err.println("Error while retrieving reservation: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Cancel a reservation by reservationId
    public boolean cancelReservation(int reservationId) {
        try {
            // Find the reservation and update its status to "Cancelled"
            Document result = reservationCollection.findOneAndUpdate(
                    Filters.eq("_id", new ObjectId(String.valueOf(reservationId))),
                    new Document("$set", new Document("status", "Cancelled"))
            );
            return result != null; // Return true if the reservation was updated, false otherwise
        } catch (Exception e) {
            System.err.println("Error while cancelling reservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Close the MongoDB client connection
    public void close() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing MongoDB client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
