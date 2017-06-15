package net.bouzuya.blog.app.repository;


import net.bouzuya.blog.app.request.EntryListRequest;
import net.bouzuya.blog.domain.model.EntryList;
import net.bouzuya.blog.domain.model.Result;
import net.bouzuya.blog.domain.repository.EntryRepository;

public class EntryRepositoryImpl implements EntryRepository {
    @Override
    public EntryList getAll() {
        Result<EntryList> result = new EntryListRequest().send();
        if (result.isOk()) {
            return result.getValue();
        } else {
            // TODO
            throw new RuntimeException(result.getException());
        }
    }
}
