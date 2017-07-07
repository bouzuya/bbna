package net.bouzuya.blog.driver;

import android.content.Context;
import android.content.SharedPreferences;

import net.bouzuya.blog.entity.Optional;

public class BlogPreferences {
    private static final String PREF_NAME = "blog.bouzuya.net";
    private static final String PREF_KEY_LATEST_DATE = "latest_date";
    private static final String PREF_KEY_SELECTED_DATE = "selected_date";

    private final Context context;

    public BlogPreferences(Context context) {
        this.context = context;
    }

    public Optional<String> getLatestDate() {
        SharedPreferences preferences = getPreferences();
        return Optional.ofNullable(preferences.getString(PREF_KEY_LATEST_DATE, null));
    }

    public Optional<String> getSelectedDate() {
        SharedPreferences preferences = getPreferences();
        return Optional.ofNullable(preferences.getString(PREF_KEY_SELECTED_DATE, null));
    }

    public void setLatestDate(String latestDate) {
        SharedPreferences preferences = getPreferences();
        boolean isOk = preferences.edit().putString(PREF_KEY_LATEST_DATE, latestDate).commit();
        if (!isOk) {
            // TODO
            throw new IllegalStateException();
        }
    }

    public void setSelectedDate(String selectedDate) {
        SharedPreferences preferences = getPreferences();
        boolean isOk = preferences.edit().putString(PREF_KEY_SELECTED_DATE, selectedDate).commit();
        if (!isOk) {
            // TODO
            throw new IllegalStateException();
        }
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
