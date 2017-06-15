package net.bouzuya.blog.app.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.domain.model.EntryList;
import net.bouzuya.blog.domain.model.Result;
import net.bouzuya.blog.domain.repository.EntryRepository;

public class EntryListLoader extends AsyncTaskLoader<Result<EntryList>> {
    private final EntryRepository entryRepository;

    public EntryListLoader(Context context, EntryRepository entryRepository) {
        super(context);
        this.entryRepository = entryRepository;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Result<EntryList> loadInBackground() {
        return this.entryRepository.getAll();
    }
}
