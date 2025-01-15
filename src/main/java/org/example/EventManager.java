package org.example;

import java.util.ArrayList;
import java.util.List;

public class EventManager implements Subject {
    private final List<Observer> observers;
    private final EventService eventService;

    public EventManager() {
        this.observers = new ArrayList<>();
        this.eventService = new EventProxy(); // Use the proxy
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update("Event notification: New or updated event!");
        }
    }

    public boolean createEvent(int id, String name, String date, String description) {
        boolean success = eventService.createEvent(id, name, date, description);
        if (success) {
            notifyAllObservers();
        }
        return success;
    }

    public Event getEventById(int id) {
        return eventService.getEventById(id);
    }

    public boolean updateEvent(int id, String name, String date, String description) {
        boolean success = eventService.updateEvent(id, name, date, description);
        if (success) {
            notifyAllObservers();
        }
        return success;
    }

    public boolean deleteEvent(int id) {
        boolean success = eventService.deleteEvent(id);
        if (success) {
            notifyAllObservers();
        }
        return success;
    }

    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }
}
