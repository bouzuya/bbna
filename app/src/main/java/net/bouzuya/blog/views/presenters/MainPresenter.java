package net.bouzuya.blog.views.presenters;

import net.bouzuya.blog.views.views.MainView;

public class MainPresenter {
    private final MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void onCreate() {
        this.view.showList();
    }
}
