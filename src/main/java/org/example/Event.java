package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private static final String COLLECTION_NAME = "events"; // MongoDB collection name
    private static MongoCollection<Document> collection; // Static MongoDB collection

    static {
        // Static initializer to set up MongoDB collection
        MongoDatabase database = Singleton.getInstance().getDatabase();
        collection = database.getCollection(COLLECTION_NAME);
    }

    private int id;
    private String name;
    private String date;
    private String description;

    // Constructor
    public Event(int id, String name, String date, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    // Create a new Event with a specified ID and insert it into MongoDB
    public static boolean create(int id, String name, String date, String description) {
        try {
            Document document = new Document("id", id)
                    .append("name", name)
                    .append("date", date)
                    .append("description", description);
            collection.insertOne(document);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get an Event by ID
    public static Event get(int id) {
        try {
            Document doc = collection.find(Filters.eq("id", id)).first();
            if (doc != null) {
                return new Event(
                        doc.getInteger("id"),
                        doc.getString("name"),
                        doc.getString("date"),
                        doc.getString("description")
                );
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update an Event's details in MongoDB
    public static boolean update(int id, String name, String date, String description) {
        try {
            Document updatedDoc = new Document("name", name)
                    .append("date", date)
                    .append("description", description);
            collection.updateOne(Filters.eq("id", id), new Document("$set", updatedDoc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete an Event by ID from MongoDB
    public static boolean delete(int id) {
        try {
            collection.deleteOne(Filters.eq("id", id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all events from the MongoDB collection
    public static List<Document> getAllEvents() {
        try {
            return collection.find().into(new ArrayList<>()); // Retrieve all documents from the collection
        } catch (Exception e) {
            System.err.println("Error retrieving events: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
