package org.example;

import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Singleton {
    private static Singleton instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private static final String DATABASE_NAME = "retirementHome2"; // Update with your database name
    private static final String CONNECTION_STRING = "mongodb://localhost:27017"; // MongoDB connection string

    // Private constructor to prevent instantiation
    private Singleton() {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DATABASE_NAME);
    }

    // Public method to get the instance of the Singleton
    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    // Getter for the database connection
    public MongoDatabase getDatabase() {
        return database;
    }
}
