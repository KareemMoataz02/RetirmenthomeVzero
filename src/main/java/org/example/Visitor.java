package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Visitor extends User {
    private NormalVisit normalVisit;
    private static MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> visitCollection;
    private MongoCollection<Document> visitorCollection;

    // Constructor to initialize the MongoDB connection and fetch the visitor data
    public Visitor(int id) {
        super(id); // Initialize the User class with visitor ID
        this.database = Singleton.getInstance().getDatabase(); // Get the database using Singleton
        this.visitCollection = database.getCollection("normalVisits") ;
        this.visitorCollection = database.getCollection("visitors");
        this.normalVisit = fetchNormalVisit(id);
    }

    // Create a new visitor and save it to the database
    public void createVisitor(int id, String name, int age) {
        // Create a new document for the visitor
        Document visitorDoc = new Document("id", id)
                .append("name", name)
                .append("age", age);

        // Insert the visitor into the visitor collection
        visitorCollection.insertOne(visitorDoc);
        System.out.println("New visitor created: " + name);
    }

    // Fetch Visitor's normal visit details from the database using their ID
    private NormalVisit fetchNormalVisit(int visitorId) {
        Document doc = visitCollection.find(Filters.eq("visitorId", visitorId)).first();
        if (doc != null) {
            return new NormalVisit(
                    doc.getObjectId("_id").toString(),
                    doc.getString("date"),
                    doc.getString("time"),
                    doc.getInteger("elderId"),
                    doc.getInteger("visitorId"),
                    doc.getString("visitStatus")
            );
        }
        return null; // Return null if no visit found
    }

    // Create a new normal visit and save it to the database
    public NormalVisit createNormalVisit(String date, String time, int elderId, String visitStatus) {
        // Create a new document for the visit with the visitor's ID
        Document visitDoc = new Document("date", date)
                .append("time", time)
                .append("elderId", elderId)
                .append("visitorId", this.getId()) // Use the visitor's ID
                .append("visitStatus", visitStatus); // Add the status of the visit

        visitCollection.insertOne(visitDoc); // Insert the document into MongoDB

        // Convert the MongoDB document into a NormalVisit object and return it
        return new NormalVisit(
                visitDoc.getObjectId("_id").toString(),
                date,
                time,
                elderId,
                this.getId(),
                visitStatus // Return the status as well
        );
    }

    // Retrieve a normal visit by visitId
    public NormalVisit getNormalVisit(String visitId) {
        Document document = visitCollection.find(Filters.eq("_id", new ObjectId(visitId))).first();
        if (document != null) {
            return new NormalVisit(
                    document.getObjectId("_id").toString(),
                    document.getString("date"),
                    document.getString("time"),
                    document.getInteger("elderId"),
                    document.getInteger("visitorId"),
                    document.getString("visitStatus") // Retrieve visitStatus
            );
        }
        return null; // Return null if no matching visit found
    }

    // Update a normal visit by visitId
    public NormalVisit updateNormalVisit(String visitId, String date, String time, int elderId, String visitStatus) {
        Document updatedDoc = new Document("date", date)
                .append("time", time)
                .append("elderId", elderId)
                .append("visitStatus", visitStatus); // Include visitStatus in the update

        visitCollection.updateOne(Filters.eq("_id", new ObjectId(visitId)),
                new Document("$set", updatedDoc)); // Update MongoDB document

        return getNormalVisit(visitId); // Return the updated visit object
    }

    // Cancel a normal visit by visitId
    public boolean cancelNormalVisit(String visitId) {
        Document result = visitCollection.findOneAndDelete(Filters.eq("_id", new ObjectId(visitId))); // Remove the document
        return result != null; // Return true if the visit was deleted successfully
    }

    // Close the MongoDB client connection (good practice to release resources)
    public void close() {
        mongoClient.close(); // Close the connection after use
    }

    // Other methods related to visitor and visits can be added here
}
