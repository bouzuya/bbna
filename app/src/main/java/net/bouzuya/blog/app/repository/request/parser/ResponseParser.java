package net.bouzuya.blog.app.repository.request.parser;

public interface ResponseParser<T> {
    T parse(String responseBody);
}
