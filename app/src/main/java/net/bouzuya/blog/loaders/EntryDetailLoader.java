package net.bouzuya.blog.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.models.EntryDetail;
import net.bouzuya.blog.models.Result;
import net.bouzuya.blog.requests.EntryDetailRequest;

public class EntryDetailLoader extends AsyncTaskLoader<Result<EntryDetail>> {
    private final String mDateOrNull;

    public EntryDetailLoader(Context context, String dateOrNull) {
        super(context);
        mDateOrNull = dateOrNull;
    }

    @Override
    protected void onStartLoading() {
        if (mDateOrNull == null) return;
        forceLoad();
    }

    @Override
    public Result<EntryDetail> loadInBackground() {
        if (mDateOrNull == null) throw new AssertionError();
        return new EntryDetailRequest(mDateOrNull).send();
    }
}
