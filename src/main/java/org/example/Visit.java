package asu.eng.models;

public abstract class Visit {
    private int id;
    private String date;
    private String time;
    private int userId;
    private int elderId;

    public Visit(/* type of object returning from database */) {
//        this.id = id;
//        this.date = date;
//        this.time = time;
//        this.userId = userId;
//        this.elderId = elderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getElderId() {
        return elderId;
    }

    public void setElderId(int elderId) {
        this.elderId = elderId;
    }
}
