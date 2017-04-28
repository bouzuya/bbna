package net.bouzuya.blog;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import net.bouzuya.blog.adapters.EntryFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements EntryListFragment.OnEntrySelectListener {

    private static final int POSITION_LIST = 0;
    private static final int POSITION_DETAIL = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private EntryFragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private String mDate;

    @Override
    public void onEntrySelect(String date) {
        mDate = date;
        showDetail(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new EntryFragmentPagerAdapter(fragmentManager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    MainActivity.this.showList();
                } else if (position == 1) {
                    MainActivity.this.showDetail(mDate);
                } else {
                    throw new AssertionError();
                }
            }
        });
        showList();

        String latestDateOrNull = new BlogPreferences(this).getLatestDate();
        Log.d(TAG, "onCreate: LatestDate: " + latestDateOrNull);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager != null && mViewPager.getCurrentItem() == POSITION_DETAIL) {
            mViewPager.setCurrentItem(POSITION_LIST);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDetail(String date) {
        mAdapter.setDetailDate(date);
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(POSITION_DETAIL);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(date);
    }

    private void showList() {
        mViewPager.setCurrentItem(POSITION_LIST);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("blog.bouzuya.net");
    }
}
