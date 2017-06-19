package net.bouzuya.blog.driver.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.bouzuya.blog.R;
import net.bouzuya.blog.app.repository.EntryRepository;
import net.bouzuya.blog.driver.BlogApplication;
import net.bouzuya.blog.driver.loader.EntryDetailLoader;
import net.bouzuya.blog.driver.loader.PresenterLoader;
import net.bouzuya.blog.driver.view.presenter.EntryDetailPresenter;
import net.bouzuya.blog.driver.view.presenter.EntryDetailPresenterFactory;
import net.bouzuya.blog.driver.view.view.EntryDetailView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;
import net.bouzuya.blog.entity.Result;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class EntryDetailFragment extends Fragment implements EntryDetailView {
    public static final int ENTRY_DETAIL_LOADER_ID = 1;
    public static final int PRESENTER_LOADER_ID = 2;
    private static final String DATE = "param1";
    @BindView(R.id.entry_detail)
    WebView mWebView;
    @Inject
    EntryRepository entryRepository;
    private Optional<String> mDateOptional;
    private Optional<EntryDetailPresenter> mPresenter;
    private Unbinder unbinder;

    public EntryDetailFragment() {
        // Required empty public constructor
        this.mDateOptional = Optional.empty();
    }

    public static EntryDetailFragment newInstance(Optional<String> dateOptional) {
        EntryDetailFragment fragment = new EntryDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(DATE, dateOptional.isPresent() ? dateOptional.get() : null);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BlogApplication) getActivity().getApplication()).getComponent().inject(this);
    }

    public Optional<String> getDateOptional() {
        return mDateOptional;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate: ");
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mDateOptional = Optional.ofNullable(arguments.getString(DATE));
        }
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(
                PRESENTER_LOADER_ID,
                null,
                new LoaderManager.LoaderCallbacks<EntryDetailPresenter>() {
                    @Override
                    public Loader<EntryDetailPresenter> onCreateLoader(int id, Bundle args) {
                        return new PresenterLoader<>(
                                EntryDetailFragment.this.getContext(),
                                new EntryDetailPresenterFactory()
                        );
                    }

                    @Override
                    public void onLoadFinished(
                            Loader<EntryDetailPresenter> loader, EntryDetailPresenter data
                    ) {
                        EntryDetailFragment.this.mPresenter = Optional.of(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<EntryDetailPresenter> loader) {
                        EntryDetailFragment.this.mPresenter = Optional.empty();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView: ");
        initEntryDetailLoader(mDateOptional);
        View view = inflater.inflate(R.layout.fragment_entry_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView: ");
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initEntryDetailLoader(Optional<String> dateOptional) {
        LoaderManager loaderManager = getLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString("date", dateOptional.isPresent() ? dateOptional.get() : null); // TODO
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
                        onLoadEntryDetailFinished(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<Result<EntryDetail>> loader) {
                        // do nothing
                    }
                };
        loaderManager.restartLoader(ENTRY_DETAIL_LOADER_ID, bundle, callbacks);
    }


    private void onLoadEntryDetailFinished(Result<EntryDetail> data) {
        Timber.d("onLoadEntryDetailFinished: ");
        if (data.isOk()) {
            Timber.d("onLoadEntryDetailFinished: isOk");

            initEntryDetailLoader(Optional.<String>empty());

            EntryDetail newEntryDetail = data.getValue();
            String message = "load " + newEntryDetail.getId().toISO8601DateString() + "";
            Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
            EntryDetail d = newEntryDetail;

            String html = new StringBuilder()
                    .append("<html>")
                    .append("<head></head>")
                    .append("<body>")
                    .append("<article>")
                    .append("<header>")
                    .append("<h1 class=\"title\">").append(d.getTitle()).append("</h1>")
//                    .append("<p class=\"pubdate\">").append(d.getPubdate()).append("</p>")
//                    .append("<p class=\"minutes\">").append(d.getMinutes()).append("</p>")
//                    .append(d.getTags().isEmpty() ? "" : "<ul class=\"tags\"><li>")
//                    .append(this.join("</li><li>", d.getTags()))
//                    .append(d.getTags().isEmpty() ? "" : "</li></ul>")
                    .append("</header>")
                    .append("<div class=\"body\">").append(d.getHtml()).append("</div>")
                    .append("</article>")
                    .append("</body>")
                    .append("</html>")
                    .toString();

            mWebView.loadData(html, "text/html; charset=UTF-8", "UTF-8");
        } else {
            Exception e = data.getException();
            Timber.e("onLoadEntryDetailFinished: ", e);
            String message = "load error";
            Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
        }
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