package net.bouzuya.blog.driver.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.bouzuya.blog.driver.fragment.EntryDetailFragment;
import net.bouzuya.blog.driver.fragment.EntryListFragment;
import net.bouzuya.blog.entity.Optional;

public class EntryFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final int POSITION_LIST = 0;
    private static final int POSITION_DETAIL = 1;

    private Optional<String> dateOptional;

    public EntryFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        dateOptional = Optional.empty();
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
                return EntryDetailFragment.newInstance(dateOptional);
            default:
                throw new AssertionError();
        }
    }


    @Override
    public long getItemId(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            return dateOptional.isPresent()
                    ? entryDateToItemId(dateOptional.get())
                    : 1;
        } else {
            throw new AssertionError();
        }
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof EntryDetailFragment) {
            EntryDetailFragment fragment = (EntryDetailFragment) object;
            return dateOptional.isPresent() && dateOptional.equals(fragment.getDateOptional())
                    ? POSITION_UNCHANGED
                    : POSITION_NONE;
        }
        return super.getItemPosition(object);
    }


    public void setDetailDate(String date) {
        dateOptional = Optional.of(date);
    }

    private long entryDateToItemId(String date) {
        String[] ymdStrings = date.split("-");
        int y = Integer.parseInt(ymdStrings[0]);
        int m = Integer.parseInt(ymdStrings[1]);
        int d = Integer.parseInt(ymdStrings[2]);
        return y * 10000 + m * 100 + d;
    }
}
