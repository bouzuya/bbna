package net.bouzuya.blog.driver;

import net.bouzuya.blog.entity.Optional;

public class SelectedDateListener {
    private final BlogPreferences preferences;
    private Optional<OnChangeListener<Optional<String>>> listener;

    public SelectedDateListener(BlogPreferences preferences) {
        this.preferences = preferences;
        this.listener = Optional.empty();
    }

    public Optional<String> get() {
        return this.preferences.getSelectedDate();
    }

    public void set(Optional<String> selectedDate) {
        Optional<String> oldSelectedDate = this.preferences.getSelectedDate();
        if (oldSelectedDate.equals(selectedDate)) return; // not changed
        this.preferences.setSelectedDate(selectedDate.orElse(null));
        if (this.listener.isPresent())
            this.listener.get().onChange(selectedDate);
    }

    public void subscribe(OnChangeListener<Optional<String>> listener) {
        this.listener = Optional.of(listener);
    }

    public void unsubscribe() {
        this.listener = Optional.empty();
    }

    public interface OnChangeListener<T> {
        void onChange(T value);
    }
}
