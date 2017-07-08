package net.bouzuya.blog.driver;

import android.content.Context;

import net.bouzuya.blog.adapter.presenter.EntryDetailPresenter;
import net.bouzuya.blog.adapter.presenter.EntryListPresenter;
import net.bouzuya.blog.adapter.presenter.MainPresenter;
import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.repository.EntryRepositoryImpl;
import net.bouzuya.blog.driver.repository.request.parser.EntryDetailResponseParser;
import net.bouzuya.blog.driver.repository.request.parser.EntryListResponseParser;
import net.bouzuya.blog.driver.view_model.EntryDetailViewModel;
import net.bouzuya.blog.driver.view_model.EntryListViewModel;

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
    EntryDetailViewModel providesEntryDetailListener(EntryRepository entryRepository) {
        return new EntryDetailViewModel(entryRepository);
    }

    @Provides
    EntryDetailPresenter providesEntryDetailPresenter(
            EntryDetailViewModel entryDetailViewModel,
            EntryListViewModel entryListViewModel
    ) {
        return new EntryDetailPresenter(entryDetailViewModel, entryListViewModel);
    }

    @Provides
    EntryListPresenter providesEntryListPresenter(
            EntryRepository entryRepository,
            EntryListViewModel entryListViewModel
    ) {
        return new EntryListPresenter(entryRepository, entryListViewModel);
    }

    @Provides
    MainPresenter providesMainPresenter(
            EntryDetailViewModel entryDetailViewModel,
            EntryListViewModel entryListViewModel
    ) {
        return new MainPresenter(entryDetailViewModel, entryListViewModel);
    }

    @Provides
    @Singleton
    EntryListViewModel providesSelectedDateListener(BlogPreferences preferences) {
        return new EntryListViewModel(preferences);
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
