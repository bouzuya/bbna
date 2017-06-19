package net.bouzuya.blog.driver.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.EntryId;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

public class EntryDetailLoader extends AsyncTaskLoader<Result<EntryDetail>> {
    private final EntryRepository entryRepository;
    private final Optional<String> mDateOptional;

    public EntryDetailLoader(
            Context context,
            EntryRepository entryRepository,
            Optional<String> dateOptional
    ) {
        super(context);
        this.entryRepository = entryRepository;
        mDateOptional = dateOptional;
    }

    @Override
    protected void onStartLoading() {
        if (!mDateOptional.isPresent()) return;
        forceLoad();
    }

    @Override
    public Result<EntryDetail> loadInBackground() {
        if (!mDateOptional.isPresent()) throw new AssertionError();
        EntryId entryId = EntryId.fromISO8601DateString(mDateOptional.get());
        return this.entryRepository.get(entryId);
    }
}
