package net.bouzuya.blog.driver.view;

import net.bouzuya.blog.entity.Optional;

public interface EntryDetailView {
    void hideLoading();

    void showLoading();

    void loadEntryDetail(Optional<String> selectedDate); // FIXME
}
