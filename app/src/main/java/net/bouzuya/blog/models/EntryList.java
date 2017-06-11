package net.bouzuya.blog.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntryList {
    private final List<Entry> list;

    private EntryList(List<Entry> list) {
        this.list = list;
    }

    public static EntryList fromEntryArray(Entry[] entryArray) {
        List<Entry> entryList = Arrays.asList(entryArray);
        // order by desc
        Collections.sort(entryList, new Comparator<Entry>() {
            @Override
            public int compare(Entry e1, Entry e2) {
                return e2.getId().compareTo(e1.getId());
            }
        });
        return new EntryList(entryList);
    }

    public static EntryList empty() {
        return new EntryList(Collections.<Entry>emptyList());
    }

    public Optional<Entry> getLatestEntry() {
        return this.list.isEmpty() ? Optional.<Entry>empty() : Optional.of(this.list.get(0));
    }

    public Entry get(int position) {
        return this.list.get(position);
    }

    public int size() {
        return this.list.size();
    }
}
