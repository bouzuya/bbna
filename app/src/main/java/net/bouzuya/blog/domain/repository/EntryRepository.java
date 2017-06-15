package net.bouzuya.blog.domain.repository;

import net.bouzuya.blog.domain.model.EntryDetail;
import net.bouzuya.blog.domain.model.EntryId;
import net.bouzuya.blog.domain.model.EntryList;
import net.bouzuya.blog.domain.model.Result;

public interface EntryRepository {
    Result<EntryDetail> get(EntryId id);

    Result<EntryList> getAll();
}
