package net.bouzuya.blog.app;

import net.bouzuya.blog.app.parser.EntryDetailResponseParserImpl;
import net.bouzuya.blog.app.parser.EntryListResponseParserImpl;
import net.bouzuya.blog.app.repository.EntryRepositoryImpl;
import net.bouzuya.blog.domain.parser.EntryDetailResponseParser;
import net.bouzuya.blog.domain.parser.EntryListResponseParser;
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
