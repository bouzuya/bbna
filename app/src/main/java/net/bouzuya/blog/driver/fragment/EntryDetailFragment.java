package net.bouzuya.blog.driver.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import net.bouzuya.blog.driver.loader.EntryDetailLoader;
import net.bouzuya.blog.driver.view.EntryDetailView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class EntryDetailFragment extends Fragment implements EntryDetailView {
    private static final int ENTRY_DETAIL_LOADER_ID = 1;
    private static final String DATE = "param1";
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
    private Optional<String> dateOptional;
    private Unbinder unbinder;
    private OnEntryLoadListener listener;

    public EntryDetailFragment() {
        // Required empty public constructor
        this.dateOptional = Optional.empty();
    }

    public static EntryDetailFragment newInstance(Optional<String> dateOptional) {
        EntryDetailFragment fragment = new EntryDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(DATE, dateOptional.orElse(null));
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BlogApplication) getActivity().getApplication()).getComponent().inject(this);
        if (context instanceof EntryDetailFragment.OnEntryLoadListener) {
            listener = (EntryDetailFragment.OnEntryLoadListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnEntrySelectListener");
        }
        presenter.onAttach(this);
    }

    public Optional<String> getDateOptional() {
        return dateOptional;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate: ");
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            dateOptional = Optional.ofNullable(arguments.getString(DATE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView: ");
        initEntryDetailLoader(dateOptional);
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
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
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
    public void showEntryDetail(Result<EntryDetail> entryDetail) {
        Timber.d("onLoadEntryDetailFinished: ");
        View view = this.getView();
        if (view == null) return;
        if (entryDetail.isOk()) {
            Timber.d("onLoadEntryDetailFinished: isOk");

            initEntryDetailLoader(Optional.<String>empty());

            EntryDetail d = entryDetail.getValue();
            webView.loadData(toHtmlString(d), "text/html; charset=UTF-8", "UTF-8");
            String message = "load " + d.getId().toISO8601DateString();
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        } else {
            if (progressBar != null) progressBar.setVisibility(View.GONE);
            Exception e = entryDetail.getException();
            Timber.e("onLoadEntryDetailFinished: ", e);
            String message = "load error";
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    private void initEntryDetailLoader(Optional<String> dateOptional) {
        LoaderManager loaderManager = getLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString("date", dateOptional.orElse(null)); // TODO
        LoaderManager.LoaderCallbacks<Result<EntryDetail>> callbacks =
                new LoaderManager.LoaderCallbacks<Result<EntryDetail>>() {
                    @Override
                    public Loader<Result<EntryDetail>> onCreateLoader(int id, Bundle args) {
                        if (id != ENTRY_DETAIL_LOADER_ID) throw new AssertionError();
                        return new EntryDetailLoader(
                                EntryDetailFragment.this.getContext(),
                                entryRepository,
                                Optional.ofNullable(args.getString("date"))
                        );
                    }

                    @Override
                    public void onLoadFinished(
                            Loader<Result<EntryDetail>> loader,
                            Result<EntryDetail> data
                    ) {
                        EntryDetailView entryDetailView = EntryDetailFragment.this;
                        entryDetailView.showEntryDetail(data);
                        if (listener != null && data.isOk()) {
                            listener.onEntryLoad(data.getValue());
                        }
                    }

                    @Override
                    public void onLoaderReset(Loader<Result<EntryDetail>> loader) {
                        // do nothing
                    }
                };
        loaderManager.restartLoader(ENTRY_DETAIL_LOADER_ID, bundle, callbacks);
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

    public interface OnEntryLoadListener {
        void onEntryLoad(EntryDetail entryDetail);
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
