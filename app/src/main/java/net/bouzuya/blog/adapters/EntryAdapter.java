package net.bouzuya.blog.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bouzuya.blog.R;
import net.bouzuya.blog.models.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewGroup layoutView;
        public TextView dateTextView;
        public TextView titleTextView;

        public ViewHolder(View view) {
            super(view);
            this.layoutView = (ViewGroup) view.findViewById(R.id.entry);
            this.dateTextView = (TextView) view.findViewById(R.id.entry_date);
            this.titleTextView = (TextView) view.findViewById(R.id.entry_title);
        }
    }

    private final List<Entry> EMPTY_ENTRY_LIST = new ArrayList<>(); // null object
    private List<Entry> mEntryList;

    public EntryAdapter() {
        mEntryList = EMPTY_ENTRY_LIST;
    }

    public void changeDataSet(List<Entry> entryList) {
        mEntryList = entryList == null ? EMPTY_ENTRY_LIST : entryList;
    }

    public Entry getItem(int position) {
        if (position < 0 || getItemCount() <= position) throw new IllegalArgumentException();
        return mEntryList.get(position);
    }

    @Override
    public int getItemCount() {
        return mEntryList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Entry entry = mEntryList.get(position);
        holder.dateTextView.setText(entry.getDate());
        holder.titleTextView.setText(entry.getTitle());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
}
