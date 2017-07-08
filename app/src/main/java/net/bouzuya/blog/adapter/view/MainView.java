package net.bouzuya.blog.adapter.view;

import net.bouzuya.blog.entity.Optional;

public interface MainView {
    void switchDetail(String title);

    void switchList(String title);

    void updateShareButton(Optional<String> titleOptional, Optional<String> urlOptional);
}
