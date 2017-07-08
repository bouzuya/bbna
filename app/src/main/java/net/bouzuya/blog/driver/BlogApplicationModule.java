package net.bouzuya.blog.driver;

import android.content.Context;

import net.bouzuya.blog.adapter.presenter.EntryDetailPresenter;
import net.bouzuya.blog.adapter.presenter.EntryListPresenter;
import net.bouzuya.blog.adapter.presenter.MainPresenter;
import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.data.EntryDetailListener;
import net.bouzuya.blog.driver.data.SelectedDateListener;
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
    EntryDetailListener providesEntryDetailListener() {
        return new EntryDetailListener();
    }

    @Provides
    EntryDetailPresenter providesEntryDetailPresenter(
            EntryDetailListener entryDetailListener,
            EntryRepository entryRepository,
            SelectedDateListener selectedDateListener
    ) {
        return new EntryDetailPresenter(entryDetailListener, entryRepository, selectedDateListener);
    }

    @Provides
    EntryListPresenter providesEntryListPresenter(
            EntryRepository entryRepository,
            SelectedDateListener selectedDateListener
    ) {
        return new EntryListPresenter(entryRepository, selectedDateListener);
    }

    @Provides
    MainPresenter providesMainPresenter(
            EntryDetailListener entryDetailListener,
            SelectedDateListener selectedDateListener
    ) {
        return new MainPresenter(entryDetailListener, selectedDateListener);
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
