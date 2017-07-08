package net.bouzuya.blog.driver.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.BlogApplication;
import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class EntryDetailFragment extends Fragment implements EntryDetailView {
    private static final int ENTRY_DETAIL_LOADER_ID = 1;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.indeterminate_bar)
    ProgressBar progressBar;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.entry_detail)
    WebView webView;
    @SuppressWarnings("WeakerAccess")
    @Inject
    EntryDetailPresenter presenter;
    @SuppressWarnings("WeakerAccess")
    @Inject
    EntryRepository entryRepository;
    @SuppressWarnings("WeakerAccess")
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
                if (webView != null) webView.setVisibility(View.VISIBLE);
            }
        });
        EntryDetailView entryDetailView = this;
        entryDetailView.showLoading();

        presenter.onStart();
        return view;
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView: ");
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

    // FIXME: move to presenter
    @Override
    public void loadEntryDetail(Optional<String> selectedDateOptional) {
        webView.setVisibility(View.INVISIBLE);
        presenter.loadEntryDetail(selectedDateOptional)
                .subscribe(new SingleObserver<EntryDetail>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull EntryDetail entryDetail) {
                        View view = EntryDetailFragment.this.getView();
                        if (view == null) return;
                        EntryDetail d = entryDetail;
                        webView.loadData(toHtmlString(d), "text/html; charset=UTF-8", "UTF-8");
                        String message = "load " + d.getId().toISO8601DateString();
                        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        View view = EntryDetailFragment.this.getView();
                        if (view == null) return;
                        EntryDetailView entryDetailView = EntryDetailFragment.this;
                        entryDetailView.hideLoading();
                        Timber.e("onLoadEntryDetailFinished: ", e);
                        String message = "load error";
                        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void showLoading() {
        if (this.progressBar == null) return;
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @NonNull
    private String toHtmlString(EntryDetail d) {
        return new StringBuilder()
                .append("<html>")
                .append("<head></head>")
                .append("<body>")
                .append("<article>")
                .append("<header>")
                .append("<h1 class=\"title\">").append(d.getTitle()).append("</h1>")
                // .append("<p class=\"pubdate\">").append(d.getPubdate()).append("</p>")
                // .append("<p class=\"minutes\">").append(d.getMinutes()).append("</p>")
                // .append(d.getTags().isEmpty() ? "" : "<ul class=\"tags\"><li>")
                // .append(this.join("</li><li>", d.getTags()))
                // .append(d.getTags().isEmpty() ? "" : "</li></ul>")
                .append("</header>")
                .append("<div class=\"body\">").append(d.getHtml()).append("</div>")
                .append("</article>")
                .append("</body>")
                .append("</html>")
                .toString();
    }

//    private String join(String delimiter, List<String> list) {
//        if (list.isEmpty()) return "";
//        StringBuilder builder = new StringBuilder();
//        for (String item : list) {
//            builder.append(delimiter).append(item);
//        }
//        return delimiter.isEmpty()
//                ? builder.toString()
//                : builder.substring(delimiter.length()).toString();
//    }
}
