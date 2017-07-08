package net.bouzuya.blog.entity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryDetailTest {
    @Test
    public void test() throws Exception {
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
        assertThat(detail.getData(), is("Hello"));
        assertThat(detail.getId().toISO8601DateString(), is("2006-01-02"));
        assertThat(detail.getHtml(), is("<p>Hello</p>"));
        assertThat(detail.getMinutes(), is(5));
        assertThat(detail.getPubdate(), is("2006-01-02T15:04:05-07:00"));
        assertThat(detail.getTags(), is(tags));
        assertThat(detail.getTitle(), is("title 1"));
    }
}
