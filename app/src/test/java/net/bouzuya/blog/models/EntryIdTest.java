package net.bouzuya.blog.models;

import junit.framework.Assert;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class EntryIdTest {
    @Test
    public void compareTo() throws Exception {
        EntryId entryId1 = EntryId.fromISO8601DateString("2006-01-01");
        EntryId entryId2 = EntryId.fromISO8601DateString("2006-01-02");
        assertThat(entryId1.compareTo(entryId2), is("2006-01-01".compareTo("2006-01-02"))); // < 0
        assertThat(entryId1.compareTo(entryId1), is("2006-01-01".compareTo("2006-01-01"))); // == 0
        assertThat(entryId2.compareTo(entryId1), is("2006-01-02".compareTo("2006-01-01"))); // > 0
    }

    @Test
    public void equals() throws Exception {
        EntryId entryId11 = EntryId.fromISO8601DateString("2006-01-01");
        EntryId entryId12 = EntryId.fromISO8601DateString("2006-01-01");
        EntryId entryId2 = EntryId.fromISO8601DateString("2006-01-02");
        assertThat(entryId11, is(entryId11));
        assertThat(entryId11, is(entryId12));
        assertThat(entryId11, is(not(entryId2)));
    }

    @Test
    public void fromISO8601DateString() throws Exception {
        EntryId entryId = EntryId.fromISO8601DateString("2006-01-02");
        assertThat(entryId.toISO8601DateString(), is("2006-01-02"));
        try {
            EntryId.fromISO8601DateString("20060102");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void toISO8601DateString() throws Exception {
        EntryId entryId = EntryId.fromISO8601DateString("2006-01-02");
        assertThat(entryId.toISO8601DateString(), is("2006-01-02"));
    }
}