package net.bouzuya.blog.drivers;

import net.bouzuya.blog.drivers.repository.request.parser.EntryDetailResponseParserImpl;
import net.bouzuya.blog.drivers.repository.request.parser.EntryListResponseParserImpl;
import net.bouzuya.blog.drivers.repository.EntryRepositoryImpl;
import net.bouzuya.blog.drivers.repository.request.parser.EntryDetailResponseParser;
import net.bouzuya.blog.drivers.repository.request.parser.EntryListResponseParser;
import net.bouzuya.blog.domain.repository.EntryRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class BlogApplicationModule {
    @Provides
    @Singleton
    EntryDetailResponseParser providesEntryDetailResponseParser() {
        return new EntryDetailResponseParserImpl();
    }

    @Provides
    @Singleton
    EntryListResponseParser providesEntryListResponseParser() {
        return new EntryListResponseParserImpl();
    }

    @Provides
    @Singleton
    EntryRepository providesEntryRepository(
            EntryDetailResponseParser entryDetailResponseParser,
            EntryListResponseParser entryListResponseParser
    ) {
        return new EntryRepositoryImpl(
                entryDetailResponseParser,
                entryListResponseParser
        );
    }
}
