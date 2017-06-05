package net.bouzuya.blog.views.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import net.bouzuya.blog.BlogPreferences;
import net.bouzuya.blog.R;
import net.bouzuya.blog.loaders.PresenterLoader;
import net.bouzuya.blog.views.adapters.EntryFragmentPagerAdapter;
import net.bouzuya.blog.views.fragments.EntryListFragment;
import net.bouzuya.blog.views.presenters.MainPresenter;
import net.bouzuya.blog.views.presenters.MainPresenterFactory;
import net.bouzuya.blog.views.views.MainView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
        implements EntryListFragment.OnEntrySelectListener,
        MainView {

    private static final int POSITION_LIST = 0;
    private static final int POSITION_DETAIL = 1;
    private static final int PRESENTER_LOADER_ID = 1;

    private EntryFragmentPagerAdapter mAdapter;
    private MainPresenter mPresenter;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    public void onEntrySelect(String date) {
        mPresenter.onSelectEntry(date);
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

    public void showDetail(String date) {
        mAdapter.setDetailDate(date);
        mAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(POSITION_DETAIL);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(date);
    }

    public void showList() {
        mViewPager.setCurrentItem(POSITION_LIST);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("blog.bouzuya.net");
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
                        MainActivity.this.mPresenter = data;
                    }

                    @Override
                    public void onLoaderReset(Loader<MainPresenter> loader) {
                        MainActivity.this.mPresenter = null;
                    }
                });

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mAdapter = new EntryFragmentPagerAdapter(fragmentManager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mPresenter.onSwitchList();
                } else if (position == 1) {
                    mPresenter.onSwitchDetail();
                } else {
                    throw new AssertionError();
                }
            }
        });
        String latestDateOrNull = new BlogPreferences(this).getLatestDate();
        Timber.d("onCreate: LatestDate: " + latestDateOrNull);
    }

    @Override
    protected void onStart() {
        Timber.d("onStart: ");
        super.onStart();

        mPresenter.onAttach(this);
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        Timber.d("onStop: ");
        mPresenter.onDetach();

        super.onStop();
    }
}
