package org.example;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Elder {

    private static final String DATABASE_NAME = "retirementHome";
    private static final String COLLECTION_NAME = "elders";
    private static MongoClient mongoClient;
    private static MongoCollection<Document> collection;
    private static final Logger logger = Logger.getLogger(Elder.class.getName());

    // Static block to initialize the MongoDB client and collection
    static {
        try {
            // Establish MongoDB connection
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            collection = database.getCollection(COLLECTION_NAME);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing MongoDB connection", e);
        }
    }

    private int id;
    private String name;

    // Constructor
    public Elder(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Create a new Elder
    public static boolean create(int id, String name) {
        try {
            // Create a document to insert into MongoDB
            Document document = new Document("id", id)
                    .append("name", name);
            collection.insertOne(document);
            logger.info("Elder created successfully: " + name);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating elder with id: " + id, e);
            return false;
        }
    }

    // Get an Elder by ID
    public static Elder get(int id) {
        try {
            // Query the collection for an elder by id
            Document doc = collection.find(Filters.eq("id", id)).first();
            if (doc != null) {
                return new Elder(doc.getInteger("id"), doc.getString("name"));
            } else {
                logger.info("Elder not found with id: " + id);
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving elder with id: " + id, e);
            return null;
        }
    }

    // Update an Elder's name
    public static boolean update(int id, String newName) {
        try {
            // Update the elder's name based on id
            Document updatedDoc = new Document("name", newName);
            collection.updateOne(Filters.eq("id", id), new Document("$set", updatedDoc));
            logger.info("Elder updated successfully: " + id);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating elder with id: " + id, e);
            return false;
        }
    }

    // Delete an Elder by ID
    public static boolean delete(int id) {
        try {
            // Delete the elder by id
            collection.deleteOne(Filters.eq("id", id));
            logger.info("Elder deleted successfully: " + id);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error deleting elder with id: " + id, e);
            return false;
        }
    }

    // Method to close the MongoDB connection
    public static void close() {
        try {
            if (mongoClient != null) {
                mongoClient.close();
                logger.info("MongoDB connection closed");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error closing MongoDB connection", e);
        }
    }

    // Override toString for a more readable representation
    @Override
    public String toString() {
        return "Elder{id=" + id + ", name='" + name + "'}";
    }
}
