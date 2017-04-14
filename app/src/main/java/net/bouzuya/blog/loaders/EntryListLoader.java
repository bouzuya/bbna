package net.bouzuya.blog.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.models.Entry;
import net.bouzuya.blog.models.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntryListLoader extends AsyncTaskLoader<Result<List<Entry>>> {

    public EntryListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Result<List<Entry>> loadInBackground() {
        try {
            URL entryListJsonUrl = newEntryListJsonUrl();
            String jsonString = fetch(entryListJsonUrl);
            List<Entry> entryList = parse(jsonString);
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

    @NonNull
    private List<Entry> parse(String jsonString) throws JSONException {
        List<Entry> entryList = parseEntryListJson(new JSONArray(jsonString));
        // order by desc
        Collections.sort(entryList, new Comparator<Entry>() {
            @Override
            public int compare(Entry e1, Entry e2) {
                return e2.getDate().compareTo(e1.getDate());
            }
        });
        return entryList;
    }

    @NonNull
    private Entry parseEntryJson(JSONObject jsonEntry) throws JSONException {
        String date = jsonEntry.getString("date");
        String title = jsonEntry.getString("title");
        return new Entry(date, title);
    }

    @NonNull
    private List<Entry> parseEntryListJson(JSONArray jsonEntryList) throws JSONException {
        List<Entry> entryList = new ArrayList<>();
        for (int i = 0; i < jsonEntryList.length(); i++) {
            JSONObject jsonEntry = jsonEntryList.getJSONObject(i);
            Entry entry = parseEntryJson(jsonEntry);
            entryList.add(entry);
        }
        return entryList;
    }
}
