package net.bouzuya.blog.entity;

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

    @Test
    public void test_parse() throws Exception {
        Url url1 = Url.parse("http://blog.bouzuya.net/2006/01/02/").get();
        Url url2 = Url.parse("https://example.com/2006/01/02/").get();
        Url url3 = Url.parse("https://blog.bouzuya.net/").get();
        Url url4 = Url.parse("https://blog.bouzuya.net/2006/01/02/").get();
        Optional<EntryId> id1 = EntryId.parse(url1);
        Optional<EntryId> id2 = EntryId.parse(url2);
        Optional<EntryId> id3 = EntryId.parse(url3);
        Optional<EntryId> id4 = EntryId.parse(url4);
        assertThat(id1.isPresent(), is(false));
        assertThat(id2.isPresent(), is(false));
        assertThat(id3.isPresent(), is(false));
        assertThat(id4.isPresent(), is(true));
        EntryId entryId = id4.get();
        assertThat(entryId.toUrl(), is(url4));
    }

    @Test
    public void test_toJsonUrl() throws Exception {
        EntryId entryId = EntryId.fromISO8601DateString("2006-01-02");
        Url url = Url.parse("https://blog.bouzuya.net/2006/01/02/index.json").get();
        assertThat(entryId.toJsonUrl(), is(url));
    }

    @Test
    public void test_toUrl() throws Exception {
        EntryId entryId = EntryId.fromISO8601DateString("2006-01-02");
        Url url = Url.parse("https://blog.bouzuya.net/2006/01/02/").get();
        assertThat(entryId.toUrl(), is(url));
    }
}