package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.EntryDetailListener;
import net.bouzuya.blog.driver.SelectedDateListener;
import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.EntryId;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class EntryDetailPresenter implements Presenter<EntryDetailView> {
    private final EntryRepository entryRepository;
    private final EntryDetailListener entryDetailListener;
    private final SelectedDateListener selectedDateListener;

    private Optional<EntryDetailView> view;
    private SelectedDateListener.OnChangeListener<Optional<String>> onChangeListener;

    public EntryDetailPresenter(
            EntryDetailListener entryDetailListener,
            EntryRepository entryRepository,
            SelectedDateListener selectedDateListener
    ) {
        this.entryDetailListener = entryDetailListener;
        this.entryRepository = entryRepository;
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

    public Single<EntryDetail> loadEntryDetail(final Optional<String> dateOptional) {
        return Single.create(
                new SingleOnSubscribe<EntryDetail>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<EntryDetail> e) throws Exception {
                        if (!dateOptional.isPresent()) throw new AssertionError();
                        EntryId entryId = EntryId.fromISO8601DateString(dateOptional.get());
                        Result<EntryDetail> result = entryRepository.get(entryId);
                        if (result.isOk()) {
                            e.onSuccess(result.getValue());
                        } else {
                            e.onError(result.getException());
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
