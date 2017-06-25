package net.bouzuya.blog.driver.repository.request.parser;

import junit.framework.Assert;

import net.bouzuya.blog.entity.EntryDetail;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryDetailResponseParserTest {
    @Test
    public void parse() throws Exception {
        String jsonString = "{" +
                "\"data\": \"Hello\\n\"," +
                "\"date\": \"2017-01-01\"," +
                "\"minutes\": 45," +
                "\"html\": \"<p>Hello</p>\\n\"," +
                "\"pubdate\": \"2017-01-01T23:59:59+09:00\"," +
                "\"tags\": [\"tag1\"]," +
                "\"title\": \"title1\"" +
                "}";
        EntryDetailResponseParser parser = new EntryDetailResponseParser();
        EntryDetail parsed = parser.parse(jsonString);
        assertThat(parsed.getData(), is("Hello\n"));
        assertThat(parsed.getId().toISO8601DateString(), is("2017-01-01"));
        assertThat(parsed.getMinutes(), is(45));
        assertThat(parsed.getHtml(), is("<p>Hello</p>\n"));
        assertThat(parsed.getPubdate(), is("2017-01-01T23:59:59+09:00"));
        assertThat(parsed.getTags().size(), is(1));
        assertThat(parsed.getTags().get(0), is("tag1"));
        assertThat(parsed.getTitle(), is("title1"));
    }

    @Test
    public void parse_error() throws Exception {
        try {
            String jsonString = "{}";
            EntryDetailResponseParser parser = new EntryDetailResponseParser();
            parser.parse(jsonString);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
    }
}
