package net.bouzuya.blog.driver;

import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import java.util.ArrayList;
import java.util.List;

public class EntryDetailListener {
    private final List<OnChangeListener<Optional<EntryDetail>>> listeners; // mutable
    private Optional<EntryDetail> entryDetailOptional;

    public EntryDetailListener() {
        this.listeners = new ArrayList<>();
        this.entryDetailOptional = Optional.empty();
    }

    public Optional<EntryDetail> get() {
        return this.entryDetailOptional;
    }

    public void set(Optional<EntryDetail> entryDetailOptional) {
        this.entryDetailOptional = entryDetailOptional;
        for (OnChangeListener<Optional<EntryDetail>> listener : this.listeners) {
            listener.onChange(this.entryDetailOptional);
        }
    }

    public void subscribe(OnChangeListener<Optional<EntryDetail>> listener) {
        this.listeners.add(listener);
    }

    public void unsubscribe(OnChangeListener<Optional<EntryDetail>> listener) {
        this.listeners.remove(listener);
    }

    public interface OnChangeListener<T> {
        void onChange(T value);
    }
}
