package net.bouzuya.blog.driver.view;

import net.bouzuya.blog.entity.Optional;

public interface MainView {
    void showDetail(String date);

    void showList();

    void updateShareButton(Optional<String> titleOptional, Optional<String> urlOptional);
}
