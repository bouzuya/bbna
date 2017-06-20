package net.bouzuya.blog.driver.presenter;

public interface PresenterFactory<T extends Presenter> {
    T create();
}
