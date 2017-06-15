package net.bouzuya.blog.app.repository;


import net.bouzuya.blog.app.request.EntryListRequest;
import net.bouzuya.blog.domain.model.EntryList;
import net.bouzuya.blog.domain.model.Result;
import net.bouzuya.blog.domain.repository.EntryRepository;

public class EntryRepositoryImpl implements EntryRepository {
    @Override
    public Result<EntryList> getAll() {
        return new EntryListRequest().send();
    }
}
