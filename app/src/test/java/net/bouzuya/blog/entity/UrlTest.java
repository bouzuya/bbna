package net.bouzuya.blog.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

public class UrlTest {

    @Test
    public void test() {
        Optional<Url> nullUrl = Url.parse(null);
        assertThat(nullUrl.isPresent(), is(false));
        Optional<Url> emptyUrl = Url.parse("");
        assertThat(emptyUrl.isPresent(), is(false));
        Optional<Url> invalidUrl = Url.parse("invalid url");
        assertThat(invalidUrl.isPresent(), is(false));
        Optional<Url> parsed = Url.parse("https://blog.bouzuya.net");
        assertThat(parsed.isPresent(), is(true));
        Url url = parsed.get();
        assertThat(url.toUrlString(), is("https://blog.bouzuya.net"));

        Url url2 = Url.parse("https://blog.bouzuya.net").get();
        Url url3 = Url.parse("https://blog.bouzuya.net/2006/01/02/").get();
        assertThat(url, is(url));
        assertThat(url, is(url2));
        assertThat(url, is(not(url3)));
    }
}