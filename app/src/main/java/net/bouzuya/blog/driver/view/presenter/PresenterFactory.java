package net.bouzuya.blog.driver.view.presenter;

public interface PresenterFactory<T extends Presenter> {
    T create();
}
