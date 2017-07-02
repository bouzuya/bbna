package net.bouzuya.blog.entity;

import java.net.MalformedURLException;
import java.net.URL;

public class Url {
    private final URL url;

    private Url(URL url) {
        this.url = url;
    }

    public static Optional<Url> parse(String s) {
        try {
            Url url = new Url(new URL(s));
            return Optional.of(url);
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Url url1 = (Url) o;

        return url.equals(url1.url);

    }

    public String getHost() {
        return url.getHost();
    }

    public String getPathname() {
        return url.getPath();
    }

    public String getProtocol() {
        return url.getProtocol() + ":";
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    public String toUrlString() {
        return this.url.toString();
    }

    @Override
    public String toString() {
        return "Url{" + url + '}';
    }
}
