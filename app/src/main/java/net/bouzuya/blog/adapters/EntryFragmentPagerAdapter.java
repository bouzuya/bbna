package net.bouzuya.blog.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.bouzuya.blog.views.fragments.EntryDetailFragment;
import net.bouzuya.blog.views.fragments.EntryListFragment;

public class EntryFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int POSITION_LIST = 0;
    private static final int POSITION_DETAIL = 1;

    private String mDate;

    public EntryFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mDate = null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITION_LIST:
                return EntryListFragment.newInstance();
            case POSITION_DETAIL:
                return EntryDetailFragment.newInstance(mDate);
            default:
                throw new AssertionError();
        }
    }


    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return mDate == null
                    ? 1
                    : entryDateToItemId(mDate);
        } else {
            throw new AssertionError();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof EntryDetailFragment) {
            EntryDetailFragment fragment = (EntryDetailFragment) object;
            return mDate != null && mDate.equals(fragment.getDate())
                    ? POSITION_UNCHANGED
                    : POSITION_NONE;
        }
        return super.getItemPosition(object);
    }


    public void setDetailDate(String date) {
        mDate = date;
    }

    private long entryDateToItemId(String date) {
        String[] ymdStrings = date.split("-");
        int y = Integer.parseInt(ymdStrings[0]);
        int m = Integer.parseInt(ymdStrings[1]);
        int d = Integer.parseInt(ymdStrings[2]);
        return y * 10000 + m * 100 + d;
    }
}
