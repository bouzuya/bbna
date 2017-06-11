package net.bouzuya.blog.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.models.EntryDetail;
import net.bouzuya.blog.models.Optional;
import net.bouzuya.blog.models.Result;
import net.bouzuya.blog.requests.EntryDetailRequest;

public class EntryDetailLoader extends AsyncTaskLoader<Result<EntryDetail>> {
    private final Optional<String> mDateOptional;

    public EntryDetailLoader(Context context, Optional<String> dateOptional) {
        super(context);
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
        return new EntryDetailRequest(mDateOptional.get()).send();
    }
}
