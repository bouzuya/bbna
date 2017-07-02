package net.bouzuya.blog.driver.view;

import net.bouzuya.blog.entity.EntryDetail;

public interface MainView {
    void showDetail(String date);

    void showList();

    void updateShareButton(EntryDetail entryDetail);
}
