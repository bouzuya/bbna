package net.bouzuya.blog.driver.repository.request.parser;

public interface ResponseParser<T> {
    T parse(String responseBody);
}
