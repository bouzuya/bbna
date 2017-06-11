package net.bouzuya.blog.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.models.EntryList;
import net.bouzuya.blog.models.Result;
import net.bouzuya.blog.requests.EntryListRequest;

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
