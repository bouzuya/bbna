package net.bouzuya.blog.driver.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import net.bouzuya.blog.R;
import net.bouzuya.blog.adapter.presenter.MainPresenter;
import net.bouzuya.blog.driver.AlarmUtils;
import net.bouzuya.blog.driver.BlogApplication;
import net.bouzuya.blog.driver.SelectedDateListener;
import net.bouzuya.blog.driver.fragment.EntryDetailFragment;
import net.bouzuya.blog.driver.fragment.EntryListFragment;
import net.bouzuya.blog.driver.view.MainView;
import net.bouzuya.blog.entity.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements MainView {

    private static final int POSITION_LIST = 0;
    private static final int POSITION_DETAIL = 1;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    @Inject
    MainPresenter presenter;
    @Inject
    SelectedDateListener selectedDateListener;
    private Optional<Intent> shareIntent;
    private MenuItem shareMenuItem;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showList();
                return true;
            case R.id.menu_item_share:
                if (shareIntent.isPresent()) {
                    startActivity(shareIntent.get());
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDetail(String date) {
        viewPager.setCurrentItem(POSITION_DETAIL);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) throw new IllegalStateException();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(date);
        // reset share button
        this.updateShareButton(Optional.<String>empty(), Optional.<String>empty());
    }

    public void showList() {
        viewPager.setCurrentItem(POSITION_LIST);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) throw new IllegalStateException();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("blog.bouzuya.net");
    }

    @Override
    public void updateShareButton(Optional<String> titleOptional, Optional<String> urlOptional) {
        this.shareIntent = titleOptional.isPresent() && urlOptional.isPresent()
                ? Optional.of(newShareIntent(titleOptional.get(), urlOptional.get()))
                : Optional.<Intent>empty();
        if (shareMenuItem != null) {
            shareMenuItem.setVisible(this.shareIntent.isPresent());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BlogApplication) getApplication()).getComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return EntryListFragment.newInstance();
                    case 1:
                        return EntryDetailFragment.newInstance();
                    default:
                        throw new AssertionError();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
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

        AlarmUtils.setAlarm(this.getApplicationContext());
    }

    @Override
    protected void onStart() {
        Timber.d("onStart: ");
        super.onStart();

        presenter.onAttach(this);
        presenter.onStart(getSelectedDateFromIntentDataOrExtra());
    }

    @Override
    protected void onStop() {
        Timber.d("onStop: ");
        presenter.onDetach();

        super.onStop();
    }

    private Optional<String> getSelectedDateFromIntentDataOrExtra() {
        Intent intent = getIntent();
        Optional<String> dateOptional = getSelectedDateFromIntentData(intent);
        return dateOptional.isPresent() ? dateOptional : getSelectedDateFromIntentExtra(intent);
    }

    private Optional<String> getSelectedDateFromIntentData(Intent intent) {
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

    private Optional<String> getSelectedDateFromIntentExtra(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) return Optional.empty();
        String latestDate = extras.getString("latest_date", null);
        return Optional.ofNullable(latestDate);
    }

    private Intent newShareIntent(String title, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        return Intent.createChooser(intent, String.format("share '%s'", url));
    }
}
