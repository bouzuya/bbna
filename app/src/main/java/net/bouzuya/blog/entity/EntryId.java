package net.bouzuya.blog.entity;

import android.support.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntryId implements Comparable<EntryId> {
    private final String date;

    private EntryId(String date) {
        if (date == null) throw new IllegalArgumentException();
        if (!date.matches("\\A[0-9]{4}-[01][0-9]-[0-3][0-9]\\z"))
            throw new IllegalArgumentException();
        this.date = date;
    }

    public static Optional<EntryId> parse(Url url) {
        if (!url.getProtocol().equals("https:")) return Optional.empty();
        if (!url.getHost().equals("blog.bouzuya.net")) return Optional.empty();
        Pattern pattern = Pattern.compile("\\A/([0-9]{4})/([01][0-9])/([0-3][0-9])/\\z");
        Matcher matcher = pattern.matcher(url.getPathname());
        if (!matcher.matches()) return Optional.empty();
        String y = matcher.group(1);
        String m = matcher.group(2);
        String d = matcher.group(3);
        try {
            EntryId entryId = new EntryId(y + "-" + m + "-" + d);
            return Optional.of(entryId);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
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
