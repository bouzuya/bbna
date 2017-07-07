package net.bouzuya.blog.driver.view;

import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

public interface EntryDetailView {
    void hideLoading();
    void showEntryDetail(Result<EntryDetail> entryDetail);
    void showLoading();
    void loadEntryDetail(Optional<String> selectedDate); // FIXME
}
