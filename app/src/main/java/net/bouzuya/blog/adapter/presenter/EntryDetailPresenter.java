package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.data.EntryDetailListener;
import net.bouzuya.blog.driver.data.SelectedDateListener;
import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class EntryDetailPresenter implements Presenter<EntryDetailView> {
    private final EntryDetailListener entryDetailListener;
    private final SelectedDateListener selectedDateListener;

    private Optional<EntryDetailView> view;
    private CompositeDisposable subscriptions;

    public EntryDetailPresenter(
            EntryDetailListener entryDetailListener,
            SelectedDateListener selectedDateListener
    ) {
        this.entryDetailListener = entryDetailListener;
        this.selectedDateListener = selectedDateListener;
    }

    @Override
    public void onAttach(EntryDetailView view) {
        this.subscriptions = new CompositeDisposable();
        this.view = Optional.of(view);
        this.subscriptions.add(
                this.selectedDateListener.observable().subscribe(new Consumer<Optional<String>>() {
                    @Override
                    public void accept(@NonNull Optional<String> selectedDate) throws Exception {
                        Timber.d("onChange: %s", selectedDate);
                        if (!selectedDate.isPresent()) return; // do nothing
                        if (!EntryDetailPresenter.this.view.isPresent()) return; // do nothing
                        EntryDetailView entryDetailView = EntryDetailPresenter.this.view.get();
                        entryDetailView.showLoading();
                        entryDetailView.loadEntryDetail(selectedDate);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        // do nothing
    }

    @Override
    public void onDetach() {
        this.view = Optional.empty();
        this.subscriptions.dispose();
    }

    public void onStart() {
        this.view.get().loadEntryDetail(selectedDateListener.get());
    }

    public Single<EntryDetail> loadEntryDetail(final Optional<String> dateOptional) {
        return entryDetailListener.load(dateOptional);
    }
}
