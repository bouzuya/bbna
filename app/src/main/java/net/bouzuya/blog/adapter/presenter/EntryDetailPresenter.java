package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.EntryDetailListener;
import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

public class EntryDetailPresenter implements Presenter<EntryDetailView> {
    private final EntryDetailListener entryDetailListener;
    private Optional<EntryDetailView> view;

    public EntryDetailPresenter(EntryDetailListener entryDetailListener) {
        this.entryDetailListener = entryDetailListener;
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

    public void onLoadFinished(Optional<EntryDetail> entryDetailOptional) {
        this.entryDetailListener.set(entryDetailOptional);
    }
}
