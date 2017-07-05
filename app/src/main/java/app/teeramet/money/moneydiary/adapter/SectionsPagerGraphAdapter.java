package app.teeramet.money.moneydiary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.teeramet.money.moneydiary.fragment.MonthGraphFragment;
import app.teeramet.money.moneydiary.fragment.YearGraphFragment;

/**
 * Created by barbie on 26/9/2559.
 */

public class SectionsPagerGraphAdapter extends FragmentStatePagerAdapter {

    public SectionsPagerGraphAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MonthGraphFragment.newInstance(0);
            case 1:
                return YearGraphFragment.newInstance(1);

            default:
                return null;
        }

    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Month";
            case 1:
                return "Year";
        }

        return null;
    }


}
