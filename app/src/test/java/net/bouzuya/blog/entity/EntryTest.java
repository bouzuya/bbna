package net.bouzuya.blog.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryTest {
    @Test
    public void test() throws Exception {
        Entry entry = Entry.of(EntryId.fromISO8601DateString("2006-02-03"), "title 1");
        assertThat(entry.getId().toISO8601DateString(), is("2006-02-03"));
        assertThat(entry.getTitle(), is("title 1"));
    }
}
