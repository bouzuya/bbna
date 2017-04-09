package net.bouzuya.blog.models;

public class Entry {
    private final String date;
    private final String title;

    public Entry(String date, String title) {
        this.date = date;
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }
}
