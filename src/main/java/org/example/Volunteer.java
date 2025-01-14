package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Volunteer implements Observer {
    private static final String RESERVATION_COLLECTION = "volunteerReservations";
    private static final String EVENT_COLLECTION = "events";
    private static final String VOLUNTEER_COLLECTION = "volunteers";
    private MongoDatabase database;
    private MongoCollection<Document> reservationCollection;
    private MongoCollection<Document> eventCollection;
    private MongoCollection<Document> volunteerCollection;
    User volunteer;
    private boolean isAvailable; // Volunteer availability status

    // Constructor initializes database and collections using Singleton
    public Volunteer(User volunteer) {
        try {
            this.database = Singleton.getInstance().getDatabase();
            this.reservationCollection = database.getCollection(RESERVATION_COLLECTION);
            this.eventCollection = database.getCollection(EVENT_COLLECTION);
            this.volunteerCollection = database.getCollection(VOLUNTEER_COLLECTION);
            this.volunteer = volunteer;
            this.isAvailable = true; // Default availability is true
        } catch (Exception e) {
            System.err.println("Error initializing MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create a volunteer and store in the database
    public boolean createVolunteer(String name, int age) {
        try {
            String volunteerId = new ObjectId().toString();
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

    // Retrieve all reservations for the volunteer
    public List<Document> getReservations() {
        try {
            return reservationCollection.find(Filters.eq("volunteerId", this.volunteer.getId())).into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error retrieving reservations: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Set volunteer availability
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // Check volunteer availability
    public boolean isAvailable() {
        return isAvailable;
    }

    // Update volunteer details
    public boolean updateVolunteerInfo(String name, int age) {
        try {
            Document updateDoc = new Document("$set", new Document("name", name).append("age", age));
            volunteerCollection.updateOne(Filters.eq("id", volunteer.getId()), updateDoc);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating volunteer info: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Observer method to handle event updates
    @Override
    public void update(String eventDetails) {
        System.out.println("Volunteer " + volunteer.getName() + " received event update: " + eventDetails);
    }
}
