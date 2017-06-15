package net.bouzuya.blog.domain.repository;

import net.bouzuya.blog.domain.model.EntryList;
import net.bouzuya.blog.domain.model.Result;

public interface EntryRepository {
    Result<EntryList> getAll();
}
