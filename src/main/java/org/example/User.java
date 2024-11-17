package org.example;

import com.mongodb.client.*;
import org.bson.Document;
import com.mongodb.client.model.Filters;

public class User {
    private int id;
    private String name;
    private static MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> userCollection;

    // Constructor to initialize User object
    public User(int id) {
        this.id = id;
        this.database = Singleton.getInstance().getDatabase(); // Get the database using Singleton
        this.userCollection = database.getCollection("users"); // Collection name for storing user information

        // Fetch user data from the database
        Document userDoc = getUserFromDB(id);
        if (userDoc != null) {
            this.name = userDoc.getString("name");
        }
    }

    // Fetch user data from MongoDB by userId
    private Document getUserFromDB(int id) {
        return userCollection.find(Filters.eq("id", id)).first(); // Query to find user by id
    }

    // Getter for User's ID
    public int getId() {
        return id;
    }

    // Getter for User's name
    public String getName() {
        return name;
    }

    // Method to update User's name
    public void updateName(String newName) {
        this.name = newName;
        updateUserInDB(); // Update the user data in MongoDB
    }

    // Update the user information in MongoDB
    private void updateUserInDB() {
        Document updatedDoc = new Document("name", this.name);
        userCollection.updateOne(Filters.eq("id", this.id), new Document("$set", updatedDoc)); // Update user
    }

    // Optional: Add a method to create a new user in MongoDB if needed
    public static void createUser(int id, String name) {
        MongoCollection<Document> userCollection = Singleton.getInstance().getDatabase().getCollection("users");
        Document newUser = new Document("id", id).append("name", name);
        userCollection.insertOne(newUser);
    }

    // Optional: Add a method to delete a user from MongoDB
    public static void deleteUser(int id) {
        MongoCollection<Document> userCollection = Singleton.getInstance().getDatabase().getCollection("users");
        userCollection.deleteOne(Filters.eq("id", id));
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }
}
