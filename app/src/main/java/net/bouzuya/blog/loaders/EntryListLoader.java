package net.bouzuya.blog.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.bouzuya.blog.models.Entry;
import net.bouzuya.blog.models.Result;
import net.bouzuya.blog.requests.EntryListRequest;

import java.util.List;

public class EntryListLoader extends AsyncTaskLoader<Result<List<Entry>>> {
    public EntryListLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Result<List<Entry>> loadInBackground() {
        return new EntryListRequest().send();
    }
}
