package net.bouzuya.blog.driver.view.presenter;

import net.bouzuya.blog.driver.view.view.EntryListView;

public class EntryListPresenter implements Presenter<EntryListView> {
    private EntryListView view;

    public EntryListPresenter() {
    }

    @Override
    public void onAttach(EntryListView view) {
        this.view = view;
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