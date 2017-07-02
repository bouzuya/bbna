package net.bouzuya.blog.driver.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bouzuya.blog.R;
import net.bouzuya.blog.entity.Entry;
import net.bouzuya.blog.entity.EntryList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {
    private EntryList entryList;

    public EntryAdapter() {
        entryList = EntryList.empty();
    }

    public void changeDataSet(EntryList entryList) {
        this.entryList = entryList;
    }

    public Entry getItem(int position) {
        if (position < 0 || getItemCount() <= position) throw new IllegalArgumentException();
        return entryList.get(position);
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Entry entry = entryList.get(position);
        holder.dateTextView.setText(entry.getId().toISO8601DateString());
        holder.titleTextView.setText(entry.getTitle());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.entry)
        public ViewGroup layoutView;
        @BindView(R.id.entry_date)
        public TextView dateTextView;
        @BindView(R.id.entry_title)
        public TextView titleTextView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
