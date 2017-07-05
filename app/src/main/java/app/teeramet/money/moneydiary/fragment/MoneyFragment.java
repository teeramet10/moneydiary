package app.teeramet.money.moneydiary.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by barbie on 24/8/2559.
 */
public abstract class MoneyFragment extends Fragment {
    public abstract void readData(String  startdate,String  enddate);
}
