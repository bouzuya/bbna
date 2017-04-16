package net.bouzuya.blog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.bouzuya.blog.loaders.EntryDetailLoader;
import net.bouzuya.blog.models.EntryDetail;
import net.bouzuya.blog.models.Result;

public class MainActivity extends AppCompatActivity
        implements EntryListFragment.OnEntrySelectListener {
    public static final int ENTRY_DETAIL_LOADER_ID = 1;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onEntrySelect(String date) {
        initEntryDetailLoader(date);
    }

    public void onLoadEntryDetailFinished(Result<EntryDetail> data) {
        Log.d(TAG, "onLoadEntryDetailFinished: ");
        if (data.isOk()) {
            Log.d(TAG, "onLoadEntryDetailFinished: isOk");

            initEntryDetailLoader(null);

            EntryDetail newEntryDetail = data.getValue();
            String message = "load " + newEntryDetail.getDate() + "";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            EntryDetail d = newEntryDetail;

            Intent intent = new Intent(MainActivity.this, EntryDetailActivity.class);
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

            intent.putExtra("html", html);
            startActivity(intent);
        } else {
            Exception e = data.getException();
            Log.e(TAG, "onLoadEntryDetailFinished: ", e);
            String message = "load error";
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(R.id.main, EntryListFragment.newInstance())
                .commit();

        initEntryDetailLoader(null);
    }

    private void initEntryDetailLoader(String dateOrNull) {
        LoaderManager loaderManager = getSupportLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString("date", dateOrNull);
        LoaderManager.LoaderCallbacks<Result<EntryDetail>> callbacks =
                new LoaderManager.LoaderCallbacks<Result<EntryDetail>>() {
                    @Override
                    public Loader<Result<EntryDetail>> onCreateLoader(int id, Bundle args) {
                        if (id != ENTRY_DETAIL_LOADER_ID) throw new AssertionError();
                        String dateOrNull = args.getString("date");
                        return new EntryDetailLoader(MainActivity.this, dateOrNull);
                    }

                    @Override
                    public void onLoadFinished(
                            Loader<Result<EntryDetail>> loader,
                            Result<EntryDetail> data
                    ) {
                        MainActivity.this.onLoadEntryDetailFinished(data);
                    }

                    @Override
                    public void onLoaderReset(Loader<Result<EntryDetail>> loader) {
                        // do nothing
                    }
                };
        loaderManager.restartLoader(ENTRY_DETAIL_LOADER_ID, bundle, callbacks);
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
