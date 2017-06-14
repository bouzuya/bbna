package net.bouzuya.blog.domain.repository;

import net.bouzuya.blog.domain.model.EntryList;

public interface EntryRepository {
    EntryList getAll();
}
