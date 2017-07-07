package net.bouzuya.blog.driver;

import net.bouzuya.blog.entity.Optional;

import timber.log.Timber;

public class SelectedDateListener {
    private final BlogPreferences preferences;
    private Optional<OnChangeListener<Optional<String>>> listener;

    public SelectedDateListener(BlogPreferences preferences) {
        this.preferences = preferences;
        this.listener = Optional.empty();
    }

    public Optional<String> get() {
        Optional<String> selectedDate = this.preferences.getSelectedDate();
        Timber.d("get: %s", selectedDate);
        return selectedDate;
    }

    public void set(Optional<String> selectedDate) {
        Timber.d("set: %s, listener=%b", selectedDate, this.listener.isPresent());
        this.preferences.setSelectedDate(selectedDate.orElse(null));
        if (this.listener.isPresent())
            this.listener.get().onChange(selectedDate);
    }

    public void subscribe(OnChangeListener<Optional<String>> listener) {
        Timber.d("subscribe: ");
        this.listener = Optional.of(listener);
    }

    public void unsubscribe() {
        this.listener = Optional.empty();
    }

    public interface OnChangeListener<T> {
        void onChange(T value);
    }
}
