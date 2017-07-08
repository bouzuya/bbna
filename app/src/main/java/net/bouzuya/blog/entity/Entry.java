package net.bouzuya.blog.entity;

public class Entry {
    private final EntryId id;
    private final String title;

    private Entry(EntryId id, String title) {
        this.id = id;
        this.title = title;
    }

    public static Entry of(EntryId id, String title) {
        return new Entry(id, title);
    }

    public EntryId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Entry{" + id + " " + title + '}';
    }
}
