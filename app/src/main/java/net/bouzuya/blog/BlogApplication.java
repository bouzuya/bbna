package net.bouzuya.blog;

import android.app.Application;

import timber.log.Timber;

public class BlogApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
