package net.bouzuya.blog.app.repository;

import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.EntryId;
import net.bouzuya.blog.entity.EntryList;
import net.bouzuya.blog.entity.Result;

public interface EntryRepository {
    Result<EntryDetail> get(EntryId id);

    Result<EntryList> getAll();
}
