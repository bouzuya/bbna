package net.bouzuya.blog.models;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryDetailTest {
    @Test
    public void simple() throws Exception {
        EntryDetail.EntryDetailBuilder builder = EntryDetail.newBuilder();
        List<String> tags = new ArrayList<>();
        tags.add("tag 1");
        tags.add("tag 2");
        EntryDetail detail = builder
                .setData("Hello")
                .setId(EntryId.fromISO8601DateString("2006-01-02"))
                .setHtml("<p>Hello</p>")
                .setMinutes(5)
                .setPubdate("2006-01-02T15:04:05-07:00")
                .setTags(tags)
                .setTitle("title 1")
                .build();
        assertThat(detail.getData(), is(equalTo("Hello")));
        assertThat(detail.getId().toISO8601DateString(), is(equalTo("2006-01-02")));
        assertThat(detail.getHtml(), is(equalTo("<p>Hello</p>")));
        assertThat(detail.getMinutes(), is(equalTo(5)));
        assertThat(detail.getPubdate(), is(equalTo("2006-01-02T15:04:05-07:00")));
        assertThat(detail.getTags(), is(equalTo(tags)));
        assertThat(detail.getTitle(), is(equalTo("title 1")));
    }
}
