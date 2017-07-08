package net.bouzuya.blog.adapter.view;

public interface MainView {
    void hideShareButton();

    void share(String title, String url);

    void showShareButton();

    void switchDetail(String title);

    void switchList(String title);
}
