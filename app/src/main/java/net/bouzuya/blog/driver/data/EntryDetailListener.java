package net.bouzuya.blog.driver.data;

import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;

public class EntryDetailListener {
    private final BehaviorSubject<Optional<EntryDetail>> subject; // mutable

    public EntryDetailListener() {
        this.subject = BehaviorSubject.createDefault(Optional.<EntryDetail>empty());
    }

    public Optional<EntryDetail> get() {
        return this.subject.getValue();
    }

    public void set(Optional<EntryDetail> entryDetailOptional) {
        this.subject.onNext(entryDetailOptional);
    }

    public Observable<Optional<EntryDetail>> observable() {
        return this.subject.observeOn(AndroidSchedulers.mainThread());
    }
}
