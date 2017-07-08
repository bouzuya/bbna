package net.bouzuya.blog.driver.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.bouzuya.blog.R;
import net.bouzuya.blog.adapter.presenter.EntryDetailPresenter;
import net.bouzuya.blog.driver.BlogApplication;
import net.bouzuya.blog.driver.view.EntryDetailView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EntryDetailFragment extends Fragment implements EntryDetailView {
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.indeterminate_bar)
    ProgressBar progressBar;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.entry_detail)
    WebView webView;
    @SuppressWarnings("WeakerAccess")
    @Inject
    EntryDetailPresenter presenter;
    private Unbinder unbinder;

    public EntryDetailFragment() {
        // Required empty public constructor
    }

    public static EntryDetailFragment newInstance() {
        return new EntryDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_entry_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        webView.setWebViewClient(new WebViewClient() {
            // https://stackoverflow.com/questions/36484074/
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                view.getContext().startActivity(intent);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                EntryDetailView entryDetailView = EntryDetailFragment.this;
                entryDetailView.showLoading();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                EntryDetailView entryDetailView = EntryDetailFragment.this;
                entryDetailView.hideLoading();
            }
        });
        presenter.onStart();
        return view;
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

    @Override
    public void hideLoading() {
        if (this.progressBar == null) return;
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEntryDetail(String html) {
        webView.loadData(html, "text/html; charset=UTF-8", "UTF-8");
    }

    @Override
    public void showLoading() {
        if (this.progressBar == null) return;
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String message) {
        View view = getView();
        if (view == null) return;
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
