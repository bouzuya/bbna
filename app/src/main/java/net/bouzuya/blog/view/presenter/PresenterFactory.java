package net.bouzuya.blog.view.presenter;

public interface PresenterFactory<T extends Presenter> {
    T create();
}
