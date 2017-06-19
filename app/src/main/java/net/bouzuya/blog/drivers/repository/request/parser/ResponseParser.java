package net.bouzuya.blog.drivers.repository.request.parser;

public interface ResponseParser<T> {
    T parse(String responseBody);
}
