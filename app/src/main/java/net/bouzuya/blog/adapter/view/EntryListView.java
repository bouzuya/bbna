package net.bouzuya.blog.adapter.view;

import net.bouzuya.blog.entity.EntryList;

public interface EntryListView {
    void hideLoading();

    void showEntryList(EntryList entryList);

    void showLoading();

    void showMessage(String s);
}
