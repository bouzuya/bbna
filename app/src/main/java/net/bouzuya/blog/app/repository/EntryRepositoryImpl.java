package net.bouzuya.blog.app.repository;


import net.bouzuya.blog.app.request.EntryDetailRequest;
import net.bouzuya.blog.app.request.EntryListRequest;
import net.bouzuya.blog.domain.model.EntryDetail;
import net.bouzuya.blog.domain.model.EntryId;
import net.bouzuya.blog.domain.model.EntryList;
import net.bouzuya.blog.domain.model.Result;
import net.bouzuya.blog.domain.repository.EntryRepository;

public class EntryRepositoryImpl implements EntryRepository {
    @Override
    public Result<EntryDetail> get(EntryId id) {
        // TODO: String -> EntryId
        return new EntryDetailRequest(id.toISO8601DateString()).send();
    }

    @Override
    public Result<EntryList> getAll() {
        return new EntryListRequest().send();
    }
}