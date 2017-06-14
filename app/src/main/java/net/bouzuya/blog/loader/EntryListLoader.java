package net.bouzuya.blog.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.domain.model.EntryList;
import net.bouzuya.blog.domain.model.Result;
import net.bouzuya.blog.request.EntryListRequest;

public class EntryListLoader extends AsyncTaskLoader<Result<EntryList>> {
    public EntryListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Result<EntryList> loadInBackground() {
        return new EntryListRequest().send();
    }
}
