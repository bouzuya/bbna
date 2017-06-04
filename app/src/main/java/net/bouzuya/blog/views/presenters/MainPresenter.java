package net.bouzuya.blog.views.presenters;

import net.bouzuya.blog.views.views.MainView;

public class MainPresenter implements Presenter<MainView> {
    private MainView view;
    private String selectedEntryDateOrNull;

    public MainPresenter() {
        this.selectedEntryDateOrNull = null;
    }

    @Override
    public void onAttach(MainView view) {
        this.view = view;
    }

    public void onStart() {
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

    @Override
    public void onDestroy() {
        // do nothing
    }

    @Override
    public void onDetach() {
        this.view = null;
    }
}
