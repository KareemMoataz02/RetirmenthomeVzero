package org.example;

import java.util.List;

public interface EventService {
    Event getEventById(int id); // Fetch event details by ID
    boolean createEvent(int id, String name, String date, String description); // Create a new event
    boolean updateEvent(int id, String name, String date, String description); // Update an event
    boolean deleteEvent(int id); // Delete an event
    List<Event> getAllEvents(); // Fetch all events
}
