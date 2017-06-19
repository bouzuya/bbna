package net.bouzuya.blog.driver;

import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.repository.EntryRepositoryImpl;
import net.bouzuya.blog.driver.repository.request.parser.EntryDetailResponseParser;
import net.bouzuya.blog.driver.repository.request.parser.EntryDetailResponseParserImpl;
import net.bouzuya.blog.driver.repository.request.parser.EntryListResponseParser;
import net.bouzuya.blog.driver.repository.request.parser.EntryListResponseParserImpl;

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
