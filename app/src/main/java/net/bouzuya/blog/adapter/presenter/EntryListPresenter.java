package net.bouzuya.blog.adapter.presenter;

import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.data.SelectedDateListener;
import net.bouzuya.blog.driver.view.EntryListView;
import net.bouzuya.blog.entity.Entry;
import net.bouzuya.blog.entity.EntryList;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class EntryListPresenter implements Presenter<EntryListView> {
    private final EntryRepository entryRepository;
    private final SelectedDateListener selectedDateListener;
    private EntryListView view;

    public EntryListPresenter(
            EntryRepository entryRepository,
            SelectedDateListener selectedDateListener
    ) {
        this.entryRepository = entryRepository;
        this.selectedDateListener = selectedDateListener;
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
        selectedDateListener.set(Optional.of(date));
    }

    public Single<EntryList> loadEntryList() {
        return Single.create(new SingleOnSubscribe<EntryList>() {
            @Override
            public void subscribe(@NonNull SingleEmitter<EntryList> e) throws Exception {
                Result<EntryList> result = entryRepository.getAll();
                if (result.isOk()) {
                    e.onSuccess(result.getValue());
                } else {
                    e.onError(result.getException());
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
