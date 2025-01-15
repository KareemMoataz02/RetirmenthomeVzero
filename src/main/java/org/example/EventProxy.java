package org.example;

import java.util.HashMap;
import java.util.List;

public class EventProxy implements EventService {
    private RealEventService realEventService;
    private HashMap<Integer, Event> eventCache;

    public EventProxy() {
        this.realEventService = new RealEventService();
        this.eventCache = new HashMap<>();
    }

    @Override
    public Event getEventById(int id) {
        // Check if the event is present in the cache
        if (eventCache.containsKey(id)) {
            System.out.println("Fetching event from cache...");
            return eventCache.get(id);
        }

        // If not in cache, fetch from the database
        System.out.println("Fetching event from database...");
        Event event = realEventService.getEventById(id);

        // Cache the event if it is found in the database
        if (event != null) {
            eventCache.put(id, event);
        }

        return event;
    }

    @Override
    public boolean createEvent(int id, String name, String date, String description) {
        boolean success = realEventService.createEvent(id, name, date, description);
        if (success) {
            eventCache.put(id, new Event(id, name, date, description)); // Cache the new event
        }
        return success;
    }

    @Override
    public boolean updateEvent(int id, String name, String date, String description) {
        boolean success = realEventService.updateEvent(id, name, date, description);
        if (success) {
            eventCache.put(id, new Event(id, name, date, description)); // Update cache
        }
        return success;
    }

    @Override
    public boolean deleteEvent(int id) {
        boolean success = realEventService.deleteEvent(id);
        if (success) {
            eventCache.remove(id); // Remove from cache
        }
        return success;
    }

    @Override
    public List<Event> getAllEvents() {
        return realEventService.getAllEvents(); // Delegate to real service
    }
}
