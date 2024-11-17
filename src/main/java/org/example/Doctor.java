package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Doctor extends User {
    private MongoCollection<Document> medicalVisitCollection;
    private MongoCollection<Document> doctorCollection;

    // Constructor to initialize User object (Doctor)
    public Doctor(int id, String name) {
        super(id); // Initialize User with id and name
        MongoDatabase database = Singleton.getInstance().getDatabase(); // Get the database using Singleton
        this.medicalVisitCollection = database.getCollection("medicalVisits"); // Collection name for medical visits
        this.doctorCollection = database.getCollection("doctors"); // Collection name for storing doctor information
    }

    // Method to create a new medical visit and save it to the database
    public MedicalVisit makeMedicalVisit(String date, String time, int elderId, String status) {
        Document medicalVisitDocument = new Document("date", date)
                .append("time", time)
                .append("elderId", elderId)
                .append("doctorId", getId()) // Using Doctor's id from User class
                .append("status", status); // Status of the visit

        medicalVisitCollection.insertOne(medicalVisitDocument); // Insert into MongoDB

        // Convert the MongoDB document into a MedicalVisit object and return it
        return new MedicalVisit(
                medicalVisitDocument.getObjectId("_id").toString(),
                date,
                time,
                elderId,
                getId(),
                status
        );
    }

    // Retrieve a medical visit by visitId
    public MedicalVisit getMedicalVisit(String visitId) {
        Document document = medicalVisitCollection.find(Filters.eq("_id", new ObjectId(visitId))).first();
        if (document != null) {
            return new MedicalVisit(
                    document.getObjectId("_id").toString(),
                    document.getString("date"),
                    document.getString("time"),
                    document.getInteger("elderId"),
                    document.getInteger("doctorId"),
                    document.getString("status")
            );
        }
        return null;
    }

    // Update a medical visit
    public MedicalVisit updateMedicalVisit(String visitId, String date, String time, int elderId, String status) {
        Document updatedDoc = new Document("date", date)
                .append("time", time)
                .append("elderId", elderId)
                .append("status", status);

        // Perform the update operation
        medicalVisitCollection.updateOne(Filters.eq("_id", new ObjectId(visitId)),
                new Document("$set", updatedDoc));

        return getMedicalVisit(visitId);
    }

    // Cancel a medical visit
    public boolean cancelMedicalVisit(String visitId) {
        Document result = medicalVisitCollection.findOneAndDelete(Filters.eq("_id", new ObjectId(visitId)));
        return result != null;
    }

    // Method to append a doctor to the database
    public boolean appendDoctorToDatabase() {
        try {
            // Create a document for the doctor to be inserted into the "doctors" collection
            Document doctorDocument = new Document("id", getId())
                    .append("name", getName());

            // Insert the doctor into the database
            doctorCollection.insertOne(doctorDocument);

            // Log success and return true
            System.out.println("Doctor added to the database successfully!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Close MongoDB client connection (Singleton instance is closed globally)
    public void close() {
        // No need to close the MongoDB client here, as the Singleton manages the connection globally
    }

    // Method to display doctor details
    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
