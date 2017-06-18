package net.bouzuya.blog.app;

import net.bouzuya.blog.app.repository.EntryRepositoryImpl;
import net.bouzuya.blog.domain.repository.EntryRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class BlogApplicationModule {
    @Provides
    @Singleton
    EntryRepository providesEntryRepository() {
        return new EntryRepositoryImpl();
    }
}
