package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class RealEventService implements EventService {
    private MongoCollection<Document> eventCollection;

    public RealEventService() {
        MongoDatabase database = Singleton.getInstance().getDatabase();
        this.eventCollection = database.getCollection("events");
    }

    @Override
    public Event getEventById(int id) {
        Document doc = eventCollection.find(Filters.eq("id", id)).first();
        if (doc != null) {
            return new Event(
                    doc.getInteger("id"),
                    doc.getString("name"),
                    doc.getString("date"),
                    doc.getString("description")
            );
        }
        return null;
    }

    @Override
    public boolean createEvent(int id, String name, String date, String description) {
        try {
            Document doc = new Document("id", id)
                    .append("name", name)
                    .append("date", date)
                    .append("description", description);
            eventCollection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateEvent(int id, String name, String date, String description) {
        try {
            Document updatedDoc = new Document("name", name)
                    .append("date", date)
                    .append("description", description);
            eventCollection.updateOne(Filters.eq("id", id), new Document("$set", updatedDoc));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEvent(int id) {
        try {
            eventCollection.deleteOne(Filters.eq("id", id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        for (Document doc : eventCollection.find()) {
            events.add(new Event(
                    doc.getInteger("id"),
                    doc.getString("name"),
                    doc.getString("date"),
                    doc.getString("description")
            ));
        }
        return events;
    }
}




