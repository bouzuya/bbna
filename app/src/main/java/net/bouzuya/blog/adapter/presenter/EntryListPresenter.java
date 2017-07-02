package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.view.EntryListView;

import javax.inject.Inject;

public class EntryListPresenter implements Presenter<EntryListView> {
    private EntryListView view;

    @Inject
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
