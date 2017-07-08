package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.view_model.EntryDetailViewModel;
import net.bouzuya.blog.driver.view_model.EntryListViewModel;
import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class EntryDetailPresenter implements Presenter<EntryDetailView> {
    private final EntryDetailViewModel entryDetailViewModel;
    private final EntryListViewModel entryListViewModel;

    private Optional<EntryDetailView> view;
    private CompositeDisposable subscriptions;

    public EntryDetailPresenter(
            EntryDetailViewModel entryDetailViewModel,
            EntryListViewModel entryListViewModel
    ) {
        this.entryDetailViewModel = entryDetailViewModel;
        this.entryListViewModel = entryListViewModel;
    }

    @Override
    public void onAttach(EntryDetailView view) {
        this.subscriptions = new CompositeDisposable();
        this.view = Optional.of(view);
        this.subscriptions.add(
                this.entryListViewModel.observable().subscribe(new Consumer<Optional<String>>() {
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
        this.view.get().loadEntryDetail(entryListViewModel.get());
    }

    public Single<EntryDetail> loadEntryDetail(final Optional<String> dateOptional) {
        return entryDetailViewModel.load(dateOptional);
    }
}
