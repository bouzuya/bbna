package net.bouzuya.blog.driver.view;

public interface EntryDetailView {
    void hideLoading();

    void showEntryDetail(String html);

    void showLoading();

    void showMessage(String s);
}
