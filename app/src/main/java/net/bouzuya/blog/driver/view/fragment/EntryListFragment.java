package net.bouzuya.blog.driver.view.fragment;

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

import net.bouzuya.blog.R;
import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.BlogApplication;
import net.bouzuya.blog.driver.loader.EntryListLoader;
import net.bouzuya.blog.driver.loader.PresenterLoader;
import net.bouzuya.blog.driver.view.adapter.EntryAdapter;
import net.bouzuya.blog.driver.view.presenter.EntryListPresenter;
import net.bouzuya.blog.driver.view.presenter.EntryListPresenterFactory;
import net.bouzuya.blog.driver.view.view.EntryListView;
import net.bouzuya.blog.entity.Entry;
import net.bouzuya.blog.entity.EntryList;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class EntryListFragment extends Fragment implements View.OnClickListener, EntryListView {

    private static final int ENTRY_LIST_LOADER_ID = 0;
    private static final int PRESENTER_LOADER_ID = 2;
    @BindView(R.id.entry_list)
    RecyclerView mEntryListView;
    @Inject
    EntryRepository entryRepository;
    private OnEntrySelectListener mListener;
    private EntryAdapter mAdapter;
    private Optional<EntryListPresenter> mPresenter;
    private Unbinder unbinder;

    public EntryListFragment() {
        // Required empty public constructor
    }

    public static EntryListFragment newInstance() {
        EntryListFragment fragment = new EntryListFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        Timber.d("onAttach: ");
        super.onAttach(context);
        ((BlogApplication) getActivity().getApplication()).getComponent().inject(this);
        if (context instanceof OnEntrySelectListener) {
            mListener = (OnEntrySelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEntrySelectListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.d("onCreate: EntryListFragment");
        super.onCreate(savedInstanceState);
        initEntryListLoader();

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(
                PRESENTER_LOADER_ID,
                null,
                new LoaderManager.LoaderCallbacks<EntryListPresenter>() {
                    @Override
                    public Loader<EntryListPresenter> onCreateLoader(int id, Bundle args) {
                        return new PresenterLoader<>(
                                EntryListFragment.this.getContext(), new EntryListPresenterFactory()
                        );
                    }

                    @Override
                    public void onLoadFinished(
                            Loader<EntryListPresenter> loader, EntryListPresenter data
                    ) {
                        EntryListFragment.this.mPresenter = Optional.of(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<EntryListPresenter> loader) {
                        EntryListFragment.this.mPresenter = Optional.empty();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView: ");
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
        String date = entry.getId().toISO8601DateString();
        Timber.d("onClick: " + date);
        if (mListener != null) {
            mListener.onEntrySelect(date);
        }
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView: ");
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        Timber.d("onDetach: ");
        super.onDetach();
        mListener = null;
    }

    public void onLoadEntryListFinished(Result<EntryList> data) {
        Timber.d("onLoadEntryListFinished: ");
        if (data.isOk()) {
            Timber.d("onLoadEntryListFinished: isOk");
            EntryList newEntryList = data.getValue();
            mAdapter.changeDataSet(newEntryList);
            mAdapter.notifyDataSetChanged();
            String message = "load " + newEntryList.size() + " entries";
            Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
        } else {
            Exception e = data.getException();
            Timber.e("onLoadEntryListFinished: ", e);
            String message = "load error";
            Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
        }
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
                        EntryListFragment.this.onLoadEntryListFinished(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<Result<EntryList>> loader) {
                        // do nothing
                    }
                };
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(ENTRY_LIST_LOADER_ID, null, callbacks);
    }

    public interface OnEntrySelectListener {
        void onEntrySelect(String date);
    }
}
