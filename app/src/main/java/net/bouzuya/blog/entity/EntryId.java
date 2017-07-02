package net.bouzuya.blog.entity;

import android.support.annotation.NonNull;

public class EntryId implements Comparable<EntryId> {
    private final String date;

    private EntryId(String date) {
        if (date == null) throw new IllegalArgumentException();
        if (!date.matches("\\A[0-9]{4}-[01][0-9]-[0-3][0-9]\\z"))
            throw new IllegalArgumentException();
        this.date = date;
    }

    public static EntryId fromISO8601DateString(String date) {
        return new EntryId(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntryId entryId = (EntryId) o;
        return date.equals(entryId.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public int compareTo(@NonNull EntryId entryId) {
        return toISO8601DateString().compareTo(entryId.toISO8601DateString());
    }

    public String toISO8601DateString() {
        return this.date;
    }

    public Url toJsonUrl() {
        String baseUrl = "https://blog.bouzuya.net";
        String path = "/" + this.date.replaceAll("-", "/") + "/index.json";
        return Url.parse(baseUrl + path).get();
    }

    public Url toUrl() {
        String baseUrl = "https://blog.bouzuya.net";
        String path = "/" + this.date.replaceAll("-", "/") + "/";
        return Url.parse(baseUrl + path).get();
    }
}
