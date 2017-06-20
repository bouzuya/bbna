package net.bouzuya.blog.driver.view;

import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Result;

public interface EntryDetailView {
    void showEntryDetail(Result<EntryDetail> entryDetail);
}
