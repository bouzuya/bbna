package net.bouzuya.blog.views.presenters;

public interface PresenterFactory<T extends Presenter> {
    T create();
}
