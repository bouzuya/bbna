package net.bouzuya.blog.requests;

import android.support.annotation.NonNull;

import net.bouzuya.blog.models.EntryList;
import net.bouzuya.blog.models.Result;
import net.bouzuya.blog.parsers.EntryListParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EntryListRequest {

    private final EntryListParser parser;

    public EntryListRequest() {
        parser = new EntryListParser();
    }

    public Result<EntryList> send() {
        try {
            URL entryListJsonUrl = newEntryListJsonUrl();
            String jsonString = fetch(entryListJsonUrl);
            EntryList entryList = parser.parse(jsonString);
            return Result.ok(entryList);
        } catch (Exception e) {
            return Result.ng(e);
        }
    }

    @NonNull
    private String fetch(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            return readInputStream(urlConnection.getInputStream());
        } finally {
            urlConnection.disconnect();
        }
    }

    @NonNull
    private URL newEntryListJsonUrl() {
        try {
            return new URL("https://blog.bouzuya.net/posts.json");
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

    @NonNull
    private String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }
    }
}
