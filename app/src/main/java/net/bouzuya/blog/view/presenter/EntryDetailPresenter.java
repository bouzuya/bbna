package net.bouzuya.blog.view.presenter;

import net.bouzuya.blog.domain.model.Optional;
import net.bouzuya.blog.view.view.EntryDetailView;

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
