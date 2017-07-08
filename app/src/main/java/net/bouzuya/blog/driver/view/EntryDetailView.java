package net.bouzuya.blog.driver.view;

import net.bouzuya.blog.entity.EntryDetail;

public interface EntryDetailView {
    void hideLoading();

    void showLoading();

    void showEntryDetail(EntryDetail entryDetail);

    void showError(Throwable e);
}
