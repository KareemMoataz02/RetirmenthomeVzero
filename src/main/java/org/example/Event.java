package org.example;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;

public class Event {

    private static final String DATABASE_NAME = "retirementHome";
    private static final String COLLECTION_NAME = "events";
    private static MongoCollection<Document> collection;

    static {
        // Establish MongoDB connection
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    private int id;
    private String name;
    private String date;
    private String description;
    private Observer[] observers;

    // Constructor
    public Event(int id, String name, String date, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.observers = new Observer[0]; // Placeholder for observer management
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Create a new Event
    public static boolean create(String name, String date, String description) {
        try {
            Document document = new Document("name", name)
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
                return new Event(doc.getInteger("id"),
                        doc.getString("name"),
                        doc.getString("date"),
                        doc.getString("description"));
            } else {
                return null; // Not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Update an Event's details
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

    // Delete an Event by ID
    public static boolean delete(int id) {
        try {
            collection.deleteOne(Filters.eq("id", id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Observer Pattern Methods

    public void registerObserver(Observer observer) {
        // Add observer to the list
    }

    public void removeObserver(Observer observer) {
        // Remove observer from the list
    }

    public void notifyAllObservers() {
        // Notify all observers
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
