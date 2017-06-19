package net.bouzuya.blog.drivers.view.presenter;

public interface PresenterFactory<T extends Presenter> {
    T create();
}
