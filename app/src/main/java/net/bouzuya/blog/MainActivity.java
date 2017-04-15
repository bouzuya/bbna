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
import net.bouzuya.blog.loaders.EntryDetailLoader;
import net.bouzuya.blog.loaders.EntryListLoader;
import net.bouzuya.blog.models.Entry;
import net.bouzuya.blog.models.EntryDetail;
import net.bouzuya.blog.models.Result;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int ENTRY_DETAIL_LOADER_ID = 1;
    public static final int ENTRY_LIST_LOADER_ID = 0;

    private static final String TAG = MainActivity.class.getSimpleName();

    private EntryAdapter mAdapter;
    private RecyclerView mEntryListView;

    public void onLoadEntryDetailFinished(Result<EntryDetail> data) {
        Log.d(TAG, "onLoadEntryDetailFinished: ");
        if (data.isOk()) {
            Log.d(TAG, "onLoadEntryDetailFinished: isOk");

            initEntryDetailLoader(null);

            EntryDetail newEntryDetail = data.getValue();
            String message = "load " + newEntryDetail.getDate() + "";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            EntryDetail d = newEntryDetail;

            Intent intent = new Intent(MainActivity.this, EntryDetailActivity.class);
            String html =
                    "<html><head></head><body><h1>" + d.getDate() + "</h1></body></html>";

            intent.putExtra("html", html);
            startActivity(intent);
        } else {
            Exception e = data.getException();
            Log.e(TAG, "onLoadEntryDetailFinished: ", e);
            String message = "load error";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public void onLoadEntryListFinished(Result<List<Entry>> data) {
        Log.d(TAG, "onLoadEntryListFinished: ");
        if (data.isOk()) {
            Log.d(TAG, "onLoadEntryListFinished: isOk");
            List<Entry> newEntryList = data.getValue();
            mAdapter.changeDataSet(newEntryList);
            mAdapter.notifyDataSetChanged();
            String message = "load " + newEntryList.size() + " entries";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            Exception e = data.getException();
            Log.e(TAG, "onLoadEntryListFinished: ", e);
            String message = "load error";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEntryListView = (RecyclerView) findViewById(R.id.entry_list);
        mEntryListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mEntryListView.setLayoutManager(layoutManager);
        mAdapter = new EntryAdapter() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.layoutView.setOnClickListener(MainActivity.this);
                return viewHolder;
            }
        };
        mEntryListView.setAdapter(mAdapter);

        initEntryListLoader();
        initEntryDetailLoader(null);
    }

    @Override
    public void onClick(View view) {
        if (mEntryListView == null) {
            return;
        }
        int position = mEntryListView.getChildAdapterPosition(view);
        Entry entry = mAdapter.getItem(position);
        Log.d(TAG, "onClick: " + entry.getDate());

        String date = entry.getDate();
        initEntryDetailLoader(date);
    }

    private void initEntryDetailLoader(String dateOrNull) {
        LoaderManager loaderManager = getSupportLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString("date", dateOrNull);
        LoaderManager.LoaderCallbacks<Result<EntryDetail>> callbacks =
                new LoaderManager.LoaderCallbacks<Result<EntryDetail>>() {
                    @Override
                    public Loader<Result<EntryDetail>> onCreateLoader(int id, Bundle args) {
                        if (id != ENTRY_DETAIL_LOADER_ID) throw new AssertionError();
                        String dateOrNull = args.getString("date");
                        return new EntryDetailLoader(MainActivity.this, dateOrNull);
                    }

                    @Override
                    public void onLoadFinished(
                            Loader<Result<EntryDetail>> loader,
                            Result<EntryDetail> data
                    ) {
                        MainActivity.this.onLoadEntryDetailFinished(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<Result<EntryDetail>> loader) {
                        // do nothing
                    }
                };
        loaderManager.restartLoader(ENTRY_DETAIL_LOADER_ID, bundle, callbacks);
    }

    private void initEntryListLoader() {
        LoaderManager.LoaderCallbacks<Result<List<Entry>>> callbacks =
                new LoaderManager.LoaderCallbacks<Result<List<Entry>>>() {
                    @Override
                    public Loader<Result<List<Entry>>> onCreateLoader(int id, Bundle args) {
                        if (id != ENTRY_LIST_LOADER_ID) throw new AssertionError();
                        return new EntryListLoader(MainActivity.this);
                    }

                    @Override
                    public void onLoadFinished(
                            Loader<Result<List<Entry>>> loader,
                            Result<List<Entry>> data
                    ) {
                        MainActivity.this.onLoadEntryListFinished(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<Result<List<Entry>>> loader) {
                        // do nothing
                    }
                };
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(ENTRY_LIST_LOADER_ID, null, callbacks);
    }
}
