package net.bouzuya.blog.views.presenters;

import net.bouzuya.blog.views.views.EntryDetailView;

public class EntryDetailPresenter implements Presenter<EntryDetailView> {
    private EntryDetailView view;

    public EntryDetailPresenter() {
    }

    @Override
    public void onAttach(EntryDetailView view) {
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
