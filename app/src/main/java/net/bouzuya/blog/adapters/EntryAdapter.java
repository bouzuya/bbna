package net.bouzuya.blog.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.bouzuya.blog.R;
import net.bouzuya.blog.models.Entry;

import java.util.List;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDateTextView;
        public TextView mTitleTextView;

        public ViewHolder(View view) {
            super(view);
            TextView dateTextView = (TextView) view.findViewById(R.id.entry_date);
            mDateTextView = dateTextView;
            TextView titleTextView = (TextView) view.findViewById(R.id.entry_title);
            mTitleTextView = titleTextView;
        }
    }

    private List<Entry> entryList;

    public EntryAdapter(List<Entry> entryList) {
        this.entryList = entryList;
    }

    @Override
    public int getItemCount() {
        return this.entryList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Entry entry = this.entryList.get(position);
        holder.mDateTextView.setText(entry.getDate());
        holder.mTitleTextView.setText(entry.getTitle());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entry, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
}
