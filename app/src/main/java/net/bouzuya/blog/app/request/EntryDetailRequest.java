package net.bouzuya.blog.app.request;

import android.support.annotation.NonNull;

import net.bouzuya.blog.domain.model.EntryDetail;
import net.bouzuya.blog.domain.model.Result;
import net.bouzuya.blog.domain.parser.EntryDetailResponseParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EntryDetailRequest {
    private final String date;
    private final EntryDetailResponseParser parser;

    public EntryDetailRequest(EntryDetailResponseParser parser, String date) {
        this.parser = parser;
        this.date = date;
    }

    public Result<EntryDetail> send() {
        try {
            URL entryJsonUrl = newEntryJsonUrl(date);
            String jsonString = fetch(entryJsonUrl);
            EntryDetail entryDetail = parser.parse(jsonString);
            return Result.ok(entryDetail);
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
    private URL newEntryJsonUrl(String date) {
        try {
            return new URL("https://blog.bouzuya.net/" + date.replace('-', '/') + ".json");
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
