package net.bouzuya.blog.driver.repository.request;

import android.support.annotation.NonNull;

import net.bouzuya.blog.driver.repository.request.parser.EntryDetailResponseParser;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.EntryId;
import net.bouzuya.blog.entity.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EntryDetailRequest {
    private final EntryId entryId;
    private final EntryDetailResponseParser parser;

    public EntryDetailRequest(EntryDetailResponseParser parser, EntryId entryId) {
        this.parser = parser;
        this.entryId = entryId;
    }

    public Result<EntryDetail> send() {
        try {
            URL entryJsonUrl = newEntryJsonUrl(entryId);
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
    private URL newEntryJsonUrl(EntryId entryId) {
        try {
            return new URL(entryId.toJsonUrl().toUrlString());
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
