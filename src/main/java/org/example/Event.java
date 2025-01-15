package org.example;

public class Event {
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

    // Getters
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
