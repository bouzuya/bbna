package net.bouzuya.blog.app.repository;


import net.bouzuya.blog.app.repository.request.EntryDetailRequest;
import net.bouzuya.blog.app.repository.request.EntryListRequest;
import net.bouzuya.blog.domain.model.EntryDetail;
import net.bouzuya.blog.domain.model.EntryId;
import net.bouzuya.blog.domain.model.EntryList;
import net.bouzuya.blog.domain.model.Result;
import net.bouzuya.blog.app.repository.request.parser.EntryDetailResponseParser;
import net.bouzuya.blog.app.repository.request.parser.EntryListResponseParser;
import net.bouzuya.blog.domain.repository.EntryRepository;

public class EntryRepositoryImpl implements EntryRepository {
    private final EntryDetailResponseParser entryDetailResponseParser;
    private final EntryListResponseParser entryListResponseParser;

    public EntryRepositoryImpl(
            EntryDetailResponseParser entryDetailResponseParser,
            EntryListResponseParser entryListResponseParser
    ) {
        this.entryDetailResponseParser = entryDetailResponseParser;
        this.entryListResponseParser = entryListResponseParser;
    }

    @Override
    public Result<EntryDetail> get(EntryId id) {
        // TODO: String -> EntryId
        EntryDetailRequest request = new EntryDetailRequest(
                this.entryDetailResponseParser,
                id.toISO8601DateString());
        return request.send();
    }

    @Override
    public Result<EntryList> getAll() {
        EntryListRequest request = new EntryListRequest(this.entryListResponseParser);
        return request.send();
    }
}
