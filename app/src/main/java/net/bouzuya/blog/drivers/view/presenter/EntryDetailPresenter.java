package net.bouzuya.blog.drivers.view.presenter;

import net.bouzuya.blog.drivers.view.view.EntryDetailView;
import net.bouzuya.blog.entity.Optional;

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
