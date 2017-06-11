package net.bouzuya.blog.views.presenters;

import net.bouzuya.blog.models.Optional;
import net.bouzuya.blog.views.views.EntryDetailView;

public class EntryDetailPresenter implements Presenter<EntryDetailView> {
    private Optional<EntryDetailView> view;

    public EntryDetailPresenter() {
    }

    @Override
    public void onAttach(EntryDetailView view) {
        this.view = Optional.of(view);
    }

    @Override
    public void onDestroy() {
        // do nothing
    }

    @Override
    public void onDetach() {
        this.view = Optional.empty();
    }
}
