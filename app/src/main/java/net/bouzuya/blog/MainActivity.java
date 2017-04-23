package net.bouzuya.blog;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.bouzuya.blog.adapters.EntryFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements EntryListFragment.OnEntrySelectListener {

    private static final int POSITION_LIST = 0;
    private static final int POSITION_DETAIL = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private EntryFragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;

    @Override
    public void onEntrySelect(String date) {
        mAdapter.setDetailDate(date);
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(POSITION_DETAIL);
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
    }

    @Override
    public void onBackPressed() {
        if (mViewPager != null && mViewPager.getCurrentItem() == POSITION_DETAIL) {
            mViewPager.setCurrentItem(POSITION_LIST);
        } else {
            super.onBackPressed();
        }
    }
}
