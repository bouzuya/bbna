package net.bouzuya.blog.driver.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.bouzuya.blog.R;
import net.bouzuya.blog.adapter.presenter.EntryListPresenter;
import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.BlogApplication;
import net.bouzuya.blog.driver.adapter.EntryAdapter;
import net.bouzuya.blog.driver.loader.EntryListLoader;
import net.bouzuya.blog.driver.view.EntryListView;
import net.bouzuya.blog.entity.Entry;
import net.bouzuya.blog.entity.EntryList;
import net.bouzuya.blog.entity.Result;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class EntryListFragment extends Fragment implements View.OnClickListener, EntryListView {
    private static final int ENTRY_LIST_LOADER_ID = 0;

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.entry_list)
    RecyclerView entryListView;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.indeterminate_bar)
    ProgressBar progressBar;
    @SuppressWarnings("WeakerAccess")
    @Inject
    EntryListPresenter presenter;
    @SuppressWarnings("WeakerAccess")
    @Inject
    EntryRepository entryRepository;
    private EntryAdapter adapter;
    private Unbinder unbinder;

    public EntryListFragment() {
        // Required empty public constructor
    }

    public static EntryListFragment newInstance() {
        return new EntryListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BlogApplication) getActivity().getApplication()).getComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEntryListLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        entryListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(entryListView.getContext());
        entryListView.setLayoutManager(layoutManager);
        adapter = new EntryAdapter() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.layoutView.setOnClickListener(EntryListFragment.this);
                return viewHolder;
            }
        };
        entryListView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (entryListView == null) return;
        int position = entryListView.getChildAdapterPosition(view);
        Entry item = adapter.getItem(position);
        presenter.onSelectEntry(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetach();
    }

    private void initEntryListLoader() {
        LoaderManager.LoaderCallbacks<Result<EntryList>> callbacks =
                new LoaderManager.LoaderCallbacks<Result<EntryList>>() {
                    @Override
                    public Loader<Result<EntryList>> onCreateLoader(int id, Bundle args) {
                        if (id != ENTRY_LIST_LOADER_ID) throw new AssertionError();
                        return new EntryListLoader(
                                EntryListFragment.this.getContext(),
                                entryRepository);
                    }

                    @Override
                    public void onLoadFinished(
                            Loader<Result<EntryList>> loader,
                            Result<EntryList> data
                    ) {
                        EntryListFragment.this.showEntryList(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<Result<EntryList>> loader) {
                        // do nothing
                    }
                };
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(ENTRY_LIST_LOADER_ID, null, callbacks);
    }

    private void showEntryList(Result<EntryList> result) {
        Timber.d("showEntryList: ");
        View view = this.getView();
        if (view == null) return;
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        if (result.isOk()) {
            EntryList newEntryList = result.getValue();
            adapter.changeDataSet(newEntryList);
            adapter.notifyDataSetChanged();
            String message = "load " + newEntryList.size() + " entries";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } else {
            Exception e = result.getException();
            Timber.e("showEntryList: ", e);
            String message = "load error";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }
}
