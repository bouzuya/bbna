package net.bouzuya.blog.driver.view_model;

import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.EntryId;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class EntryDetailViewModel {
    private final EntryRepository entryRepository;
    private final BehaviorSubject<Optional<EntryDetail>> subject; // mutable

    public EntryDetailViewModel(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
        this.subject = BehaviorSubject.createDefault(Optional.<EntryDetail>empty());
    }

    public Optional<EntryDetail> get() {
        return this.subject.getValue();
    }

    public Single<EntryDetail> load(final Optional<String> dateOptional) {
        return Single.create(
                new SingleOnSubscribe<EntryDetail>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<EntryDetail> e) throws Exception {
                        if (!dateOptional.isPresent()) throw new AssertionError();
                        EntryId entryId = EntryId.fromISO8601DateString(dateOptional.get());
                        Result<EntryDetail> result = entryRepository.get(entryId);
                        if (result.isOk()) {
                            subject.onNext(Optional.of(result.getValue()));
                            e.onSuccess(result.getValue());
                        } else {
                            e.onError(result.getException());
                        }
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Optional<EntryDetail>> observable() {
        return this.subject.observeOn(AndroidSchedulers.mainThread());
    }
}
