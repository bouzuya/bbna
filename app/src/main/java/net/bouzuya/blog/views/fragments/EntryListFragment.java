package net.bouzuya.blog.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.bouzuya.blog.R;
import net.bouzuya.blog.loaders.EntryListLoader;
import net.bouzuya.blog.models.Entry;
import net.bouzuya.blog.models.Result;
import net.bouzuya.blog.views.adapters.EntryAdapter;
import net.bouzuya.blog.views.presenters.EntryListPresenter;
import net.bouzuya.blog.views.views.EntryListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EntryListFragment extends Fragment implements View.OnClickListener, EntryListView {

    private Unbinder unbinder;

    public interface OnEntrySelectListener {
        void onEntrySelect(String date);
    }

    private static final int ENTRY_LIST_LOADER_ID = 0;
    private static final String TAG = EntryListFragment.class.getSimpleName();

    private OnEntrySelectListener mListener;
    private EntryAdapter mAdapter;
    private EntryListPresenter mPresenter;

    @BindView(R.id.entry_list)
    RecyclerView mEntryListView;

    public static EntryListFragment newInstance() {
        EntryListFragment fragment = new EntryListFragment();
        return fragment;
    }

    public EntryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEntrySelectListener) {
            mListener = (OnEntrySelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEntrySelectListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEntryListLoader();
        mPresenter = new EntryListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        mEntryListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mEntryListView.getContext());
        mEntryListView.setLayoutManager(layoutManager);
        mAdapter = new EntryAdapter() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.layoutView.setOnClickListener(EntryListFragment.this);
                return viewHolder;
            }
        };
        mEntryListView.setAdapter(mAdapter);
        return view;
    }


    @Override
    public void onClick(View view) {
        if (mEntryListView == null) return;
        int position = mEntryListView.getChildAdapterPosition(view);
        Entry entry = mAdapter.getItem(position);
        String date = entry.getDate();
        Log.d(TAG, "onClick: " + date);
        if (mListener != null) {
            mListener.onEntrySelect(date);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onLoadEntryListFinished(Result<List<Entry>> data) {
        Log.d(TAG, "onLoadEntryListFinished: ");
        if (data.isOk()) {
            Log.d(TAG, "onLoadEntryListFinished: isOk");
            List<Entry> newEntryList = data.getValue();
            mAdapter.changeDataSet(newEntryList);
            mAdapter.notifyDataSetChanged();
            String message = "load " + newEntryList.size() + " entries";
            Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
        } else {
            Exception e = data.getException();
            Log.e(TAG, "onLoadEntryListFinished: ", e);
            String message = "load error";
            Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void initEntryListLoader() {
        LoaderManager.LoaderCallbacks<Result<List<Entry>>> callbacks =
                new LoaderManager.LoaderCallbacks<Result<List<Entry>>>() {
                    @Override
                    public Loader<Result<List<Entry>>> onCreateLoader(int id, Bundle args) {
                        if (id != ENTRY_LIST_LOADER_ID) throw new AssertionError();
                        return new EntryListLoader(EntryListFragment.this.getContext());
                    }

                    @Override
                    public void onLoadFinished(
                            Loader<Result<List<Entry>>> loader,
                            Result<List<Entry>> data
                    ) {
                        EntryListFragment.this.onLoadEntryListFinished(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<Result<List<Entry>>> loader) {
                        // do nothing
                    }
                };
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(ENTRY_LIST_LOADER_ID, null, callbacks);
    }

}
