package net.bouzuya.blog.app.view.presenter;

public interface PresenterFactory<T extends Presenter> {
    T create();
}
