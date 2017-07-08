package net.bouzuya.blog.entity;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OptionalTest {
    @Test
    public void testEmpty() {
        Optional<String> o = Optional.empty();
        assertThat(o.isPresent(), is(false));
        assertThat(o.orElse("def"), is("def"));
    }

    @Test
    public void testOf() {
        Optional<String> o = Optional.of("abc");
        assertThat(o.isPresent(), is(true));
        assertThat(o.get(), is("abc"));
        assertThat(o.orElse("def"), is("abc"));
    }

    @Test
    public void testOfNullable() {
        Optional<String> oNull = Optional.ofNullable(null);
        assertThat(oNull.isPresent(), is(false));
        assertThat(oNull.orElse("def"), is("def"));
        Optional<String> o = Optional.ofNullable("abc");
        assertThat(o.isPresent(), is(true));
        assertThat(o.get(), is("abc"));
        assertThat(o.orElse("def"), is("abc"));
    }

    @Test
    public void testEquals() {
        Optional<String> empty1 = Optional.empty();
        Optional<String> empty2 = Optional.empty();
        Optional<String> value1 = Optional.of("abc");
        Optional<String> value2 = Optional.of("abc");
        Optional<String> value3 = Optional.of("def");
        assertThat(empty1, is(empty1));
        assertThat(empty1, is(empty2));
        assertThat(empty1, is(not(value1)));
        assertThat(value1, is(value1));
        assertThat(value1, is(value2));
        assertThat(value1, is(not(value3)));
    }
}
