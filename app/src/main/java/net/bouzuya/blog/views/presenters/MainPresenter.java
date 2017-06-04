package net.bouzuya.blog.views.presenters;

import net.bouzuya.blog.views.views.MainView;

public class MainPresenter {
    private final MainView view;
    private String selectedEntryDateOrNull;

    public MainPresenter(MainView view) {
        this.selectedEntryDateOrNull = null;
        this.view = view;
    }

    public void onCreate() {
        this.view.showList();
    }

    public void onSelectEntry(String date) {
        this.selectedEntryDateOrNull = date;
        this.view.showDetail(selectedEntryDateOrNull);
    }

    public void onSwitchDetail() {
        this.view.showDetail(selectedEntryDateOrNull);
    }

    public void onSwitchList() {
        this.view.showList();
    }
}
