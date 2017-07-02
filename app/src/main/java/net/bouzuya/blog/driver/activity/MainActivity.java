package net.bouzuya.blog.driver.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.bouzuya.blog.R;
import net.bouzuya.blog.adapter.presenter.MainPresenter;
import net.bouzuya.blog.adapter.presenter.MainPresenterFactory;
import net.bouzuya.blog.driver.BlogPreferences;
import net.bouzuya.blog.driver.adapter.EntryFragmentPagerAdapter;
import net.bouzuya.blog.driver.fragment.EntryDetailFragment;
import net.bouzuya.blog.driver.fragment.EntryListFragment;
import net.bouzuya.blog.driver.loader.PresenterLoader;
import net.bouzuya.blog.driver.view.MainView;
import net.bouzuya.blog.entity.EntryDetail;
import net.bouzuya.blog.entity.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements EntryListFragment.OnEntrySelectListener,
        EntryDetailFragment.OnEntryLoadListener,
        MainView {

    private static final int POSITION_LIST = 0;
    private static final int POSITION_DETAIL = 1;
    private static final int PRESENTER_LOADER_ID = 1;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private EntryFragmentPagerAdapter adapter;
    private MainPresenter presenter;
    private Intent shareIntent;
    private MenuItem shareMenuItem;

    @Override
    public void onEntrySelect(String date) {
        presenter.onSelectEntry(date);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == POSITION_DETAIL) {
            viewPager.setCurrentItem(POSITION_LIST);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        shareMenuItem = menu.findItem(R.id.menu_item_share);
        return true;
    }

    @Override
    public void onEntryLoad(EntryDetail entryDetail) {
        presenter.onLoadEntry(entryDetail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showList();
                return true;
            case R.id.menu_item_share:
                if (shareIntent != null) {
                    startActivity(shareIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDetail(String date) {
        adapter.setDetailDate(date);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(POSITION_DETAIL);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) throw new IllegalStateException();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(date);
        // reset share button
        this.shareIntent = null;
        if (shareMenuItem != null) {
            shareMenuItem.setVisible(false);
        }
    }

    public void showList() {
        viewPager.setCurrentItem(POSITION_LIST);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) throw new IllegalStateException();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("blog.bouzuya.net");

        String title = "blog.bouzuya.net";
        String url = "https://blog.bouzuya.net/";
        this.shareIntent = newShareIntent(title, url);
        if (shareMenuItem != null) {
            shareMenuItem.setVisible(true);
        }
    }

    @Override
    public void updateShareButton(EntryDetail entryDetail) {
        String url = entryDetail.getId().toUrl().toUrlString();
        String date = entryDetail.getId().toISO8601DateString();
        String title = entryDetail.getTitle();
        String dateAndTitle = String.format("%s %s", date, title);
        this.shareIntent = newShareIntent(dateAndTitle, url);
        if (shareMenuItem != null) {
            shareMenuItem.setVisible(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate: ");
        super.onCreate(savedInstanceState);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(
                PRESENTER_LOADER_ID,
                null,
                new LoaderManager.LoaderCallbacks<MainPresenter>() {
                    @Override
                    public Loader<MainPresenter> onCreateLoader(int id, Bundle args) {
                        return new PresenterLoader<>(MainActivity.this, new MainPresenterFactory());
                    }

                    @Override
                    public void onLoadFinished(Loader<MainPresenter> loader, MainPresenter data) {
                        MainActivity.this.presenter = data;
                    }

                    @Override
                    public void onLoaderReset(Loader<MainPresenter> loader) {
                        MainActivity.this.presenter = null;
                    }
                });

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new EntryFragmentPagerAdapter(fragmentManager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    presenter.onSwitchList();
                } else if (position == 1) {
                    presenter.onSwitchDetail();
                } else {
                    throw new AssertionError();
                }
            }
        });
        Optional<String> latestDateOptional = new BlogPreferences(this).getLatestDate();
        Timber.d("onCreate: LatestDate: " + latestDateOptional);
    }

    @Override
    protected void onStart() {
        Timber.d("onStart: ");
        super.onStart();

        Optional<String> selectedDateOptional = getSelectedDate();
        presenter.onAttach(this);
        presenter.onStart(selectedDateOptional);
    }

    @Override
    protected void onStop() {
        Timber.d("onStop: ");
        presenter.onDetach();

        super.onStop();
    }

    private Optional<String> getSelectedDate() {
        Intent intent = getIntent();
        Optional<String> urlOptional = getURL();
        if (urlOptional.isPresent()) return urlOptional;
        Bundle extras = intent.getExtras();
        if (extras == null) return Optional.empty();
        String latestDate = extras.getString("latest_date", null);
        return Optional.ofNullable(latestDate);
    }

    private Optional<String> getURL() {
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data == null) return Optional.empty();
        if (data.getPath().equals("/")) return Optional.empty();
        Pattern pattern = Pattern.compile("\\A/(\\d{4})/(\\d{2})/(\\d{2})/\\z");
        Matcher matcher = pattern.matcher(data.getPath());
        if (!matcher.matches()) return Optional.empty();
        String yearString = matcher.group(1);
        String monthString = matcher.group(2);
        String dateString = matcher.group(3);
        return Optional.of(yearString + "-" + monthString + "-" + dateString);
    }

    private Intent newShareIntent(String title, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        return Intent.createChooser(intent, String.format("share '%s'", url));
    }
}
