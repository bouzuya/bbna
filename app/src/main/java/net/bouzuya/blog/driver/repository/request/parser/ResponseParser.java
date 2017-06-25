package net.bouzuya.blog.driver.repository.request.parser;

interface ResponseParser<T> {
    T parse(String responseBody);
}
