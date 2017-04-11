package net.bouzuya.blog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import net.bouzuya.blog.adapters.EntryAdapter;
import net.bouzuya.blog.models.Entry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView.Adapter mAdapter;
    private List<Entry> mEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEntryList = newEntries();

        RecyclerView entryListView = (RecyclerView) findViewById(R.id.entry_list);
        entryListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        entryListView.setLayoutManager(layoutManager);
        mAdapter = new EntryAdapter(mEntryList);
        entryListView.setAdapter(mAdapter);
    }

    @NonNull
    private List<Entry> newEntries() {
        List<Entry> entryList = new ArrayList<>();
        entryList.add(new Entry("2017-01-01", "entry 1"));
        entryList.add(new Entry("2017-01-02", "entry 2"));
        entryList.add(new Entry("2017-01-03", "entry 3"));
        return entryList;
    }
}
