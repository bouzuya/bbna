package net.bouzuya.blog.domain.parser;

public interface ResponseParser<T> {
    T parse(String responseBody);
}
