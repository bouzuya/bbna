package net.bouzuya.blog.driver.data;

import net.bouzuya.blog.driver.BlogPreferences;
import net.bouzuya.blog.entity.Optional;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class SelectedDateListener {
    private final BlogPreferences preferences;
    private final BehaviorSubject<Optional<String>> subject; // mutable

    public SelectedDateListener(BlogPreferences preferences) {
        this.preferences = preferences;
        this.subject = BehaviorSubject.createDefault(this.preferences.getSelectedDate());
        // TODO: dispose
        this.subject.distinctUntilChanged().subscribe(new Consumer<Optional<String>>() {
            @Override
            public void accept(@NonNull Optional<String> selectedDate) throws Exception {
                SelectedDateListener.this.preferences.setSelectedDate(selectedDate.orElse(null));
            }
        });
    }

    public Optional<String> get() {
        return this.subject.getValue();
    }

    public void set(Optional<String> selectedDate) {
        this.subject.onNext(selectedDate);
    }

    public Observable<Optional<String>> observable() {
        return this.subject.distinctUntilChanged();
    }
}
