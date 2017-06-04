package net.bouzuya.blog.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.models.EntryDetail;
import net.bouzuya.blog.models.Result;
import net.bouzuya.blog.parsers.EntryDetailParser;

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
import java.util.List;

public class EntryDetailLoader extends AsyncTaskLoader<Result<EntryDetail>> {
    private final String mDateOrNull;
    private final EntryDetailParser mParser;
    private EntryDetail mEntryDetail;

    public EntryDetailLoader(Context context, String dateOrNull) {
        super(context);
        mDateOrNull = dateOrNull;
        mParser = new EntryDetailParser();
    }

    @Override
    protected void onStartLoading() {
        if (mDateOrNull == null) return;
        forceLoad();
    }

    @Override
    public Result<EntryDetail> loadInBackground() {
        if (mDateOrNull == null) throw new AssertionError();
        try {
            URL entryJsonUrl = newEntryJsonUrl(mDateOrNull);
            String jsonString = fetch(entryJsonUrl);
            mEntryDetail = mParser.parse(jsonString);
            return Result.ok(mEntryDetail);
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
