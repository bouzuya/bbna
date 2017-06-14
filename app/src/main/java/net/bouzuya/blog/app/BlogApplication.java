package net.bouzuya.blog.app;

import android.app.Application;

import net.bouzuya.blog.BuildConfig;

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
