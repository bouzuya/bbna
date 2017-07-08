package net.bouzuya.blog.driver;

import net.bouzuya.blog.entity.Optional;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SelectedDateListener {
    private final BlogPreferences preferences;
    private final BehaviorSubject<Optional<String>> subject; // mutable

    public SelectedDateListener(BlogPreferences preferences) {
        this.preferences = preferences;
        this.subject = BehaviorSubject.create();
    }

    public Optional<String> get() {
        return this.preferences.getSelectedDate();
    }

    public void set(Optional<String> selectedDate) {
        Optional<String> oldSelectedDate = this.preferences.getSelectedDate();
        if (oldSelectedDate.equals(selectedDate)) return; // not changed
        this.preferences.setSelectedDate(selectedDate.orElse(null));
        this.subject.onNext(selectedDate);
    }

    public Observable<Optional<String>> observable() {
        return this.subject;
    }
}
