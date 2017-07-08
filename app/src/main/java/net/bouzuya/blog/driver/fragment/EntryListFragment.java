package net.bouzuya.blog.driver.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.bouzuya.blog.R;
import net.bouzuya.blog.adapter.presenter.EntryListPresenter;
import net.bouzuya.blog.driver.BlogApplication;
import net.bouzuya.blog.driver.adapter.EntryAdapter;
import net.bouzuya.blog.driver.view.EntryListView;
import net.bouzuya.blog.entity.Entry;
import net.bouzuya.blog.entity.EntryList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class EntryListFragment extends Fragment implements View.OnClickListener, EntryListView {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.entry_list)
    RecyclerView entryListView;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.indeterminate_bar)
    ProgressBar progressBar;
    @SuppressWarnings("WeakerAccess")
    @Inject
    EntryListPresenter presenter;
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
        presenter
                .loadEntryList()
                .subscribe(new SingleObserver<EntryList>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(@NonNull EntryList entryList) {
                        View view = EntryListFragment.this.getView();
                        if (view == null) return;
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        adapter.changeDataSet(entryList);
                        adapter.notifyDataSetChanged();
                        String message = "load " + entryList.size() + " entries";
                        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        View view = EntryListFragment.this.getView();
                        if (view == null) return;
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        Timber.e("showEntryList: ", e);
                        String message = "load error";
                        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                    }
                });
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
}
