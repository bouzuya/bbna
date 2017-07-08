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
import net.bouzuya.blog.adapter.view.MainView;
import net.bouzuya.blog.driver.AlarmUtils;
import net.bouzuya.blog.driver.BlogApplication;
import net.bouzuya.blog.driver.fragment.EntryDetailFragment;
import net.bouzuya.blog.driver.fragment.EntryListFragment;
import net.bouzuya.blog.entity.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final int POSITION_LIST = 0;
    private static final int POSITION_DETAIL = 1;

    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
    @Inject
    MainPresenter presenter;
    private MenuItem shareMenuItem;

    @Override
    public void hideShareButton() {
        if (shareMenuItem == null) return;
        shareMenuItem.setVisible(false);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.onSwitchList();
                return true;
            case R.id.menu_item_share:
                presenter.onClickShare();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void share(String title, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        Intent shareIntent = Intent.createChooser(intent, String.format("share '%s'", url));
        startActivity(shareIntent);
    }

    @Override
    public void showShareButton() {
        if (shareMenuItem == null) return;
        shareMenuItem.setVisible(true);
    }

    @Override
    public void switchDetail(String title) {
        if (viewPager == null) return;
        viewPager.setCurrentItem(POSITION_DETAIL);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) throw new IllegalStateException();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(title);
    }

    @Override
    public void switchList(String title) {
        if (viewPager == null) return;
        viewPager.setCurrentItem(POSITION_LIST);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) throw new IllegalStateException();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BlogApplication) getApplication()).getComponent().inject(this);
        presenter.onAttach(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case POSITION_LIST:
                        return EntryListFragment.newInstance();
                    case POSITION_DETAIL:
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
                if (position == POSITION_LIST) {
                    presenter.onSwitchList();
                } else if (position == POSITION_DETAIL) {
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
        super.onStart();
        presenter.onStart(getSelectedDateFromIntentDataOrExtra());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
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
}
