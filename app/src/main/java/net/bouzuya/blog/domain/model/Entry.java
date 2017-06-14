package net.bouzuya.blog.domain.model;

public class Entry {
    private final EntryId id;
    private final String title;

    public Entry(EntryId id, String title) {
        this.id = id;
        this.title = title;
    }

    public EntryId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
