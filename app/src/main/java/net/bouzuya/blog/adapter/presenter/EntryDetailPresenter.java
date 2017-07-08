package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.driver.view_model.EntryDetailViewModel;
import net.bouzuya.blog.driver.view_model.EntryListViewModel;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
                        EntryDetailPresenter.this.loadEntryDetail(selectedDate);
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
        loadEntryDetail(entryListViewModel.get());
    }

    private void loadEntryDetail(Optional<String> dateOptional) {
        entryDetailViewModel.load(dateOptional)
                .subscribe(new SingleObserver<EntryDetail>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull EntryDetail entryDetail) {
                        if (!view.isPresent()) return;
                        view.get().showEntryDetail(entryDetail);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        if (!view.isPresent()) return;
                        view.get().showError(e);
                    }
                });
    }
}
