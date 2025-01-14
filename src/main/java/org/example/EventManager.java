package org.example;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class EventManager implements Subject {
    private final List<Observer> observers; // List of registered observers

    // Constructor to initialize the observer list
    public EventManager() {
        this.observers = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer); // Add an observer to the list
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer); // Remove an observer from the list
    }

    @Override
    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update("Event notification: New or updated event!"); // Notify all observers
        }
    }

    // Method to create a new event and notify observers
    public boolean createEvent(int id, String name, String date, String description) {
        boolean success = Event.create(id, name, date, description); // Use the Event class to create the event
        if (success) {
            notifyAllObservers(); // Notify observers if the event is successfully created
        }
        return success;
    }

    // Method to retrieve an event by its ID
    public Event getEventById(int id) {
        return Event.get(id); // Use the Event class to retrieve an event by ID
    }

    // Method to update an existing event and notify observers
    public boolean updateEvent(int id, String name, String date, String description) {
        boolean success = Event.update(id, name, date, description); // Use the Event class to update the event
        if (success) {
            notifyAllObservers(); // Notify observers if the event is successfully updated
        }
        return success;
    }

    // Method to delete an event and notify observers
    public boolean deleteEvent(int id) {
        boolean success = Event.delete(id); // Use the Event class to delete the event
        if (success) {
            notifyAllObservers(); // Notify observers if the event is successfully deleted
        }
        return success;
    }

    // Method to retrieve all events as a list of Event objects
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        for (Document doc : Event.getAllEvents()) {
            try {
                Integer eventId = doc.getInteger("id");
                if (eventId != null) {
                    Event event = Event.get(eventId); // Create Event objects from the documents
                    if (event != null) {
                        events.add(event);
                    }
                } else {
                    System.err.println("Document missing 'id' field: " + doc.toJson());
                }
            } catch (Exception e) {
                System.err.println("Error processing document: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return events;
    }
}
