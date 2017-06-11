package net.bouzuya.blog.models;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EntryTest {
    @Test
    public void simple() throws Exception {
        Entry entry = new Entry("2006-02-03", "title 1");
        assertThat(entry.getDate(), is(equalTo("2006-02-03")));
        assertThat(entry.getTitle(), is(equalTo("title 1")));
    }
}
