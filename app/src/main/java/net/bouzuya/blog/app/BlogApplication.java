package net.bouzuya.blog.app;

import android.app.Application;

import net.bouzuya.blog.BuildConfig;

import timber.log.Timber;

public class BlogApplication extends Application {

    private BlogApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerBlogApplicationComponent.builder().build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public BlogApplicationComponent getComponent() {
        return component;
    }
}
