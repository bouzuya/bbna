package net.bouzuya.blog.models;

import java.util.List;

public final class EntryDetail {
    private final String date;
    private final String title;
    private final String data; // markdown
    private final String html;
    private final int minutes;
    private final String pubdate;
    private final List<String> tags;
    private EntryDetail(
            String date,
            String title,
            String data,
            String html,
            int minutes,
            String pubdate,
            List<String> tags
    ) {
        // TODO: validation
        this.date = date;
        this.title = title;
        this.data = data;
        this.html = html;
        this.minutes = minutes;
        this.pubdate = pubdate;
        this.tags = tags;
    }

    public static EntryDetailBuilder newBuilder() {
        return new EntryDetailBuilder();
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }

    public String getHtml() {
        return html;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getPubdate() {
        return pubdate;
    }

    public List<String> getTags() {
        return tags;
    }

    public static final class EntryDetailBuilder {
        private String date;
        private String title;
        private String data;
        private String html;
        private int minutes;
        private String pubdate;
        private List<String> tags;

        private EntryDetailBuilder() {
        }

        public EntryDetailBuilder setDate(String date) {
            this.date = date;
            return this;
        }

        public EntryDetailBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EntryDetailBuilder setData(String data) {
            this.data = data;
            return this;
        }

        public EntryDetailBuilder setHtml(String html) {
            this.html = html;
            return this;
        }

        public EntryDetailBuilder setMinutes(int minutes) {
            this.minutes = minutes;
            return this;
        }

        public EntryDetailBuilder setPubdate(String pubdate) {
            this.pubdate = pubdate;
            return this;
        }

        public EntryDetailBuilder setTags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public EntryDetail build() {
            return new EntryDetail(date, title, data, html, minutes, pubdate, tags);
        }
    }
}
