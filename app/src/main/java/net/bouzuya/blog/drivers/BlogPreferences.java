package net.bouzuya.blog.drivers;

import android.content.Context;
import android.content.SharedPreferences;

import net.bouzuya.blog.domain.model.Optional;

public class BlogPreferences {
    private static final String PREF_NAME = "blog.bouzuya.net";
    private static final String PREF_KEY_LATEST_DATE = "latest_date";

    private final Context mContext;

    public BlogPreferences(Context context) {
        mContext = context;
    }

    public Optional<String> getLatestDate() {
        SharedPreferences preferences = getPreferences();
        return Optional.ofNullable(preferences.getString(PREF_KEY_LATEST_DATE, null));
    }

    public void setLatestDate(String latestDate) {
        SharedPreferences preferences = getPreferences();
        boolean isOk = preferences.edit().putString(PREF_KEY_LATEST_DATE, latestDate).commit();
        if (!isOk) {
            // TODO
            throw new IllegalStateException();
        }
    }

    private SharedPreferences getPreferences() {
        return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
