package net.bouzuya.blog.entity;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ResultTest {
    @Test
    public void simpleOk() throws Exception {
        Result<String> result = Result.ok("foo");
        assertThat(result.isOk(), is(equalTo(true)));
        try {
            //noinspection ThrowableResultOfMethodCallIgnored
            result.getException();
            Assert.fail();
        } catch (IllegalStateException e) {
            assertThat(e, is(not(equalTo(null))));
        }
        assertThat(result.getValue(), is(equalTo("foo")));
    }

    @Test
    public void simpleNg() throws Exception {
        Result<String> result = Result.ng(new IllegalArgumentException("bar"));
        assertThat(result.isOk(), is(equalTo(false)));
        //noinspection ThrowableResultOfMethodCallIgnored
        assertThat(result.getException().getMessage(), is(equalTo("bar")));
        try {
            result.getValue();
            Assert.fail();
        } catch (IllegalStateException e) {
            assertThat(e, is(not(equalTo(null))));
        }
    }
}
