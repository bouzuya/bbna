package net.bouzuya.blog.adapter.presenter;

public interface PresenterFactory<T extends Presenter> {
    T create();
}
