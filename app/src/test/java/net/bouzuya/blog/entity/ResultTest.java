package net.bouzuya.blog.entity;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ResultTest {
    @Test
    public void testOk() throws Exception {
        Result<String> result = Result.ok("foo");
        assertThat(result.isOk(), is(true));
        try {
            //noinspection ThrowableResultOfMethodCallIgnored
            result.getException();
            Assert.fail();
        } catch (IllegalStateException e) {
            assertThat(e, is(not(nullValue())));
        }
        assertThat(result.getValue(), is("foo"));
    }

    @Test
    public void testNg() throws Exception {
        Result<String> result = Result.ng(new IllegalArgumentException("bar"));
        assertThat(result.isOk(), is(false));
        //noinspection ThrowableResultOfMethodCallIgnored
        assertThat(result.getException().getMessage(), is("bar"));
        try {
            result.getValue();
            Assert.fail();
        } catch (IllegalStateException e) {
            assertThat(e, is(not(nullValue())));
        }
    }
}
