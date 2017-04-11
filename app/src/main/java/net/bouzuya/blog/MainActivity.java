package net.bouzuya.blog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.bouzuya.blog.adapters.EntryAdapter;
import net.bouzuya.blog.models.Entry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter mAdapter;
    private List<Entry> mEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEntryList = newEntries();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.entry_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EntryAdapter(mEntryList);
        recyclerView.setAdapter(mAdapter);
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
