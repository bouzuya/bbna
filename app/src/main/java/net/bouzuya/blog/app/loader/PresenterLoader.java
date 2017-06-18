package net.bouzuya.blog.app.loader;

import android.content.Context;
import android.support.v4.content.Loader;

import net.bouzuya.blog.domain.model.Optional;
import net.bouzuya.blog.app.view.presenter.Presenter;
import net.bouzuya.blog.app.view.presenter.PresenterFactory;

public class PresenterLoader<T extends Presenter> extends Loader<T> {
    private final PresenterFactory<T> factory;
    private Optional<T> presenter;

    public PresenterLoader(Context context, PresenterFactory<T> factory) {
        super(context);
        this.factory = factory;
        this.presenter = Optional.empty();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (this.presenter.isPresent()) {
            deliverResult(this.presenter.get());
            return;
        }

        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        this.presenter = Optional.of(factory.create());
        deliverResult(this.presenter.get());
    }

    @Override
    protected void onReset() {
        this.presenter.get().onDestroy();
        this.presenter = Optional.empty();
    }
}
