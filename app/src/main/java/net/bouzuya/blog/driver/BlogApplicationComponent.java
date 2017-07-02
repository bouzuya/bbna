package net.bouzuya.blog.driver;

import net.bouzuya.blog.driver.activity.MainActivity;
import net.bouzuya.blog.driver.fragment.EntryDetailFragment;
import net.bouzuya.blog.driver.fragment.EntryListFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {BlogApplicationModule.class})
public interface BlogApplicationComponent {
    void inject(EntryDetailFragment fragment);

    void inject(EntryListFragment fragment);

    void inject(MainActivity activity);

    void inject(MainService service);
}
