package net.bouzuya.blog.loaders;

import android.content.Context;
import android.support.v4.content.Loader;

import net.bouzuya.blog.views.presenters.Presenter;
import net.bouzuya.blog.views.presenters.PresenterFactory;

public class PresenterLoader<T extends Presenter> extends Loader<T> {
    private final PresenterFactory<T> factory;
    private T presenter;

    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (this.presenter != null) {
            deliverResult(this.presenter);
            return;
        }

        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        this.presenter = factory.create();
        deliverResult(this.presenter);
    }

    @Override
    protected void onReset() {
        this.presenter.onDestroy();
        this.presenter = null;
    }
}
