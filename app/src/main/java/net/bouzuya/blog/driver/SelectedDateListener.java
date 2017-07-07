package net.bouzuya.blog.driver;

import net.bouzuya.blog.entity.Optional;

import java.util.ArrayList;
import java.util.List;

public class SelectedDateListener {
    private final BlogPreferences preferences;
    private final List<OnChangeListener<Optional<String>>> listeners; // mutable

    public SelectedDateListener(BlogPreferences preferences) {
        this.preferences = preferences;
        this.listeners = new ArrayList<>();
    }

    public Optional<String> get() {
        return this.preferences.getSelectedDate();
    }

    public void set(Optional<String> selectedDate) {
        Optional<String> oldSelectedDate = this.preferences.getSelectedDate();
        if (oldSelectedDate.equals(selectedDate)) return; // not changed
        this.preferences.setSelectedDate(selectedDate.orElse(null));
        for (OnChangeListener<Optional<String>> listener : this.listeners) {
            listener.onChange(selectedDate);
        }
    }

    public void subscribe(OnChangeListener<Optional<String>> listener) {
        this.listeners.add(listener);
    }

    public void unsubscribe(OnChangeListener<Optional<String>> listener) {
        this.listeners.remove(listener);
    }

    public interface OnChangeListener<T> {
        void onChange(T value);
    }
}
