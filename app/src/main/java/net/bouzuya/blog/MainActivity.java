package net.bouzuya.blog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.bouzuya.blog.adapters.EntryAdapter;
import net.bouzuya.blog.loaders.EntryListLoader;
import net.bouzuya.blog.models.Entry;
import net.bouzuya.blog.models.Result;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Result<List<Entry>>>, View.OnClickListener {

    public static final int ENTRY_LIST_LOADER_ID = 0;

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView.Adapter mAdapter;
    private List<Entry> mEntryList;
    private RecyclerView mEntryListView;

    @Override
    public Loader<Result<List<Entry>>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        switch (id) {
            case ENTRY_LIST_LOADER_ID:
                return new EntryListLoader(this);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Result<List<Entry>>> loader, Result<List<Entry>> data) {
        Log.d(TAG, "onLoadFinished: ");
        if (data.isOk()) {
            Log.d(TAG, "onLoadFinished: isOk");
            List<Entry> newEntryList = data.getValue();
            mEntryList.clear();
            mEntryList.addAll(newEntryList);
            mAdapter.notifyDataSetChanged();
            String message = "load " + newEntryList.size() + " entries";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            Exception e = data.getException();
            Log.e(TAG, "onLoadFinished: isNg", e);
            String message = "load error";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Result<List<Entry>>> loader) {
        Log.d(TAG, "onLoaderReset: ");
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEntryList = new ArrayList<>();

        mEntryListView = (RecyclerView) findViewById(R.id.entry_list);
        mEntryListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEntryListView.setLayoutManager(layoutManager);
        mAdapter = new EntryAdapter(mEntryList) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.layoutView.setOnClickListener(MainActivity.this);
                return viewHolder;
            }
        };
        mEntryListView.setAdapter(mAdapter);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(ENTRY_LIST_LOADER_ID, null, this);
    }

    @Override
    public void onClick(View view) {
        if (mEntryListView == null) { return; }
        if (mEntryList == null) { return; }
        int position = mEntryListView.getChildAdapterPosition(view);
        Entry entry = mEntryList.get(position);
        Log.d(TAG, "onClick: " + entry.getDate());

        Intent intent = new Intent(MainActivity.this, EntryDetailActivity.class);
        String html =
                "<html><head></head><body><h1>" + entry.getDate() + "</h1></body></html>";
        intent.putExtra("html", html);
        startActivity(intent);
    }
}
