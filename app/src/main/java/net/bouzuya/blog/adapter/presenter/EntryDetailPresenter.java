package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.EntryDetailListener;
import net.bouzuya.blog.driver.SelectedDateListener;
import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import timber.log.Timber;

public class EntryDetailPresenter implements Presenter<EntryDetailView> {
    private final EntryDetailListener entryDetailListener;
    private final SelectedDateListener selectedDateListener;

    private Optional<EntryDetailView> view;
    private SelectedDateListener.OnChangeListener<Optional<String>> onChangeListener;

    public EntryDetailPresenter(
            EntryDetailListener entryDetailListener,
            SelectedDateListener selectedDateListener
    ) {
        this.entryDetailListener = entryDetailListener;
        this.selectedDateListener = selectedDateListener;
    }

    @Override
    public void onAttach(EntryDetailView view) {
        this.view = Optional.of(view);

        this.onChangeListener = new SelectedDateListener.OnChangeListener<Optional<String>>() {
            @Override
            public void onChange(Optional<String> selectedDate) {
                Timber.d("onChange: %s", selectedDate);
                if (!selectedDate.isPresent()) return; // do nothing
                if (!EntryDetailPresenter.this.view.isPresent()) return; // do nothing
                EntryDetailView entryDetailView = EntryDetailPresenter.this.view.get();
                entryDetailView.showLoading();
                entryDetailView.loadEntryDetail(selectedDate);
            }
        };
        this.selectedDateListener.subscribe(this.onChangeListener);
    }

    @Override
    public void onDestroy() {
        // do nothing
    }

    @Override
    public void onDetach() {
        this.view = Optional.empty();
        this.selectedDateListener.unsubscribe(this.onChangeListener);
    }

    public void onLoadFinished(Optional<EntryDetail> entryDetailOptional) {
        this.entryDetailListener.set(entryDetailOptional);
    }

    public void onStart() {
        this.view.get().loadEntryDetail(selectedDateListener.get());
    }
}
