package net.bouzuya.blog.domain.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryListTest {
    @Test
    public void empty() throws Exception {
        EntryList empty = EntryList.empty();
        assertThat(empty.size(), is(0));
    }

    @Test
    public void fromEntryArray() throws Exception {
        Entry entry1 = new Entry(EntryId.fromISO8601DateString("2006-01-02"), "title 1");
        Entry entry2 = new Entry(EntryId.fromISO8601DateString("2006-01-03"), "title 2");
        Entry[] entryArray = new Entry[]{entry1, entry2};
        EntryList entryList = EntryList.fromEntryArray(entryArray);
        assertThat(entryList.size(), is(2));
        assertThat(entryList.get(0), is(entry2)); // order by desc
        assertThat(entryList.get(1), is(entry1)); // order by desc
    }

    @Test
    public void get() throws Exception {
        Entry entry1 = new Entry(EntryId.fromISO8601DateString("2006-01-02"), "title 1");
        Entry[] entryArray = new Entry[]{entry1};
        EntryList entryList = EntryList.fromEntryArray(entryArray);
        assertThat(entryList.get(0), is(entry1));
        // assertThat(entryList.get(1), is(entry1)); // TODO
    }

    @Test
    public void getLatestEntry() throws Exception {
        Entry entry1 = new Entry(EntryId.fromISO8601DateString("2006-01-02"), "title 1");
        Entry entry2 = new Entry(EntryId.fromISO8601DateString("2006-01-03"), "title 2");
        Entry[] entryArray = new Entry[]{entry1, entry2};
        EntryList entryList = EntryList.fromEntryArray(entryArray);
        assertThat(entryList.getLatestEntry().get(), is(entry2));
    }

    @Test
    public void size() throws Exception {
        Entry entry1 = new Entry(EntryId.fromISO8601DateString("2006-01-02"), "title 1");
        Entry entry2 = new Entry(EntryId.fromISO8601DateString("2006-01-03"), "title 2");
        Entry[] entryArray = new Entry[]{entry1, entry2};
        EntryList entryList = EntryList.fromEntryArray(entryArray);
        assertThat(entryList.size(), is(2));
    }
}