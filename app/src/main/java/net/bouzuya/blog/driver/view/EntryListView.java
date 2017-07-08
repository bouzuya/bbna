package net.bouzuya.blog.driver.view;

import net.bouzuya.blog.entity.EntryList;

public interface EntryListView {
    void hideLoading();

    void showLoading();

    void showEntryList(EntryList entryList);

    void showError(Throwable e);
}
