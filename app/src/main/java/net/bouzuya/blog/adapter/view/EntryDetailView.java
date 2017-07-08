package net.bouzuya.blog.adapter.view;

public interface EntryDetailView {
    void hideLoading();

    void showEntryDetail(String html);

    void showLoading();

    void showMessage(String s);
}
