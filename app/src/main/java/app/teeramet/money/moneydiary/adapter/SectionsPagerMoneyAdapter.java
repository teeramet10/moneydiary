package app.teeramet.money.moneydiary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.teeramet.money.moneydiary.fragment.ExpenseFragment;
import app.teeramet.money.moneydiary.fragment.IncomeAndExpenseFragment;
import app.teeramet.money.moneydiary.fragment.IncomeFragment;

/**
 * Created by barbie on 15/8/2559.
 */
public class SectionsPagerMoneyAdapter extends FragmentStatePagerAdapter {

    public SectionsPagerMoneyAdapter(FragmentManager fm) {
        super(fm);
    }

    Fragment incomeFragment;
    Fragment expenseFragment;
    Fragment summaryFragment;

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                incomeFragment = IncomeFragment.newInstance(0);
                return incomeFragment;
            case 1:
                expenseFragment =ExpenseFragment.newInstance(1);
                return expenseFragment;
            case 2:
                summaryFragment=IncomeAndExpenseFragment.newInstance(2);
                return summaryFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Income";
            case 1:
                return "Expense";
            case 2:
                return "Summary";
        }
        return null;
    }

    public Fragment getIncomeFragment() {
        return incomeFragment;
    }

    public Fragment getExpenseFragment() {
        return expenseFragment;
    }

    public Fragment getSummaryFragment(){
        return summaryFragment;
    }


}
