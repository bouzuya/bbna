package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.view.EntryListView;
import net.bouzuya.blog.driver.view_model.EntryListViewModel;
import net.bouzuya.blog.entity.Entry;
import net.bouzuya.blog.entity.EntryList;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EntryListPresenter implements Presenter<EntryListView> {
    private final EntryRepository entryRepository;
    private final EntryListViewModel entryListViewModel;
    private EntryListView view;

    public EntryListPresenter(
            EntryRepository entryRepository,
            EntryListViewModel entryListViewModel
    ) {
        this.entryRepository = entryRepository;
        this.entryListViewModel = entryListViewModel;
    }

    @Override
    public void onAttach(EntryListView view) {
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

    public void onSelectEntry(Entry entry) {
        String date = entry.getId().toISO8601DateString();
        entryListViewModel.set(Optional.of(date));
    }

    public void onStart() {
        loadEntryList();
    }

    private void loadEntryList() {
        Single
                .create(new SingleOnSubscribe<EntryList>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<EntryList> e) throws Exception {
                        Result<EntryList> result = entryRepository.getAll();
                        if (result.isOk()) {
                            e.onSuccess(result.getValue());
                        } else {
                            e.onError(result.getException());
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<EntryList>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull EntryList entryList) {
                        view.hideLoading();
                        view.showEntryList(entryList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.hideLoading();
                        view.showError(e);
                    }
                });
    }

}
