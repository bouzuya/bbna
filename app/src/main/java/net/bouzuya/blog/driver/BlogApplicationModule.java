package net.bouzuya.blog.driver;

import android.content.Context;

import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.repository.EntryRepositoryImpl;
import net.bouzuya.blog.driver.repository.request.parser.EntryDetailResponseParser;
import net.bouzuya.blog.driver.repository.request.parser.EntryListResponseParser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class BlogApplicationModule {
    private final Context context;

    BlogApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    Context providesContext() {
        return this.context;
    }

    @Provides
    BlogPreferences providesBlogPreferences(Context context) {
        return new BlogPreferences(context);
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
