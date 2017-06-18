package net.bouzuya.blog.app;

import net.bouzuya.blog.app.view.fragment.EntryDetailFragment;
import net.bouzuya.blog.app.view.fragment.EntryListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {BlogApplicationModule.class})
public interface BlogApplicationComponent {
    void inject(EntryDetailFragment fragment);

    void inject(EntryListFragment fragment);

    void inject(MainService service);
}
