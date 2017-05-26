package net.bouzuya.blog.views.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.bouzuya.blog.R;
import net.bouzuya.blog.loaders.EntryDetailLoader;
import net.bouzuya.blog.models.EntryDetail;
import net.bouzuya.blog.models.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EntryDetailFragment extends Fragment {
    public static final int ENTRY_DETAIL_LOADER_ID = 1;

    private static final String DATE = "param1";
    private static final String TAG = EntryDetailFragment.class.getSimpleName();

    private String mDate;
    private Unbinder unbinder;

    @BindView(R.id.entry_detail)
    WebView mWebView;

    public EntryDetailFragment() {
        // Required empty public constructor
    }

    public static EntryDetailFragment newInstance(String date) {
        EntryDetailFragment fragment = new EntryDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putString(DATE, date);
        fragment.setArguments(arguments);
        return fragment;
    }

    public String getDate() {
        return mDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mDate = arguments.getString(DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initEntryDetailLoader(mDate);
        View view = inflater.inflate(R.layout.fragment_entry_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initEntryDetailLoader(String dateOrNull) {
        LoaderManager loaderManager = getLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString("date", dateOrNull);
        LoaderManager.LoaderCallbacks<Result<EntryDetail>> callbacks =
                new LoaderManager.LoaderCallbacks<Result<EntryDetail>>() {
                    @Override
                    public Loader<Result<EntryDetail>> onCreateLoader(int id, Bundle args) {
                        if (id != ENTRY_DETAIL_LOADER_ID) throw new AssertionError();
                        String dateOrNull = args.getString("date");
                        return new EntryDetailLoader(
                                EntryDetailFragment.this.getContext(), dateOrNull
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
        Log.d(TAG, "onLoadEntryDetailFinished: ");
        if (data.isOk()) {
            Log.d(TAG, "onLoadEntryDetailFinished: isOk");

            initEntryDetailLoader(null);

            EntryDetail newEntryDetail = data.getValue();
            String message = "load " + newEntryDetail.getDate() + "";
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
            Log.e(TAG, "onLoadEntryDetailFinished: ", e);
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
