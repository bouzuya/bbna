package net.bouzuya.blog.driver.repository.request.parser;

import junit.framework.Assert;

import net.bouzuya.blog.entity.EntryList;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryListResponseParserTest {
    @Test
    public void parse() throws Exception {
        String jsonString = "[" +
                "{" +
                "\"date\": \"2017-01-01\"," +
                "\"minutes\": 45," +
                "\"pubdate\": \"2017-01-01T23:59:59+09:00\"," +
                "\"tags\": [\"tag1\"]," +
                "\"title\": \"title1\"" +
                "}" +
                "]";
        EntryListResponseParser parser = new EntryListResponseParser();
        EntryList parsed = parser.parse(jsonString);
        assertThat(parsed.size(), is(1));
        assertThat(parsed.get(0).getId().toISO8601DateString(), is("2017-01-01"));
        // not supported yet
//        assertThat(parsed.get(0).getMinutes(), is(45));
//        assertThat(parsed.get(0).getPubdate(), is("2017-01-01T23:59:59+09:00"));
//        assertThat(parsed.get(0).getTags().size(), is(1));
//        assertThat(parsed.get(0).getTags().get(0), is("tag1"));
        assertThat(parsed.get(0).getTitle(), is("title1"));
    }

    @Test
    public void parse_error() throws Exception {
        try {
            String jsonString = "";
            EntryListResponseParser parser = new EntryListResponseParser();
            parser.parse(jsonString);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
    }
}
