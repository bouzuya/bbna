package net.bouzuya.blog.driver;

import android.content.Context;

import net.bouzuya.blog.adapter.presenter.EntryListPresenter;
import net.bouzuya.blog.adapter.presenter.MainPresenter;
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
    EntryListPresenter providesEntryListPresenter(SelectedDateListener selectedDateListener) {
        return new EntryListPresenter(selectedDateListener);
    }

    @Provides
    MainPresenter providesMainPresenter(SelectedDateListener selectedDateListener) {
        return new MainPresenter(selectedDateListener);
    }

    @Provides
    @Singleton
    SelectedDateListener providesSelectedDateListener(BlogPreferences preferences) {
        return new SelectedDateListener(preferences);
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
