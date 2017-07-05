package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import app.teeramet.money.moneydiary.CustomViewPager;
import app.teeramet.money.moneydiary.DateDialog;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.adapter.SectionsPagerGraphAdapter;
import app.teeramet.money.moneydiary.fragment.MonthGraphFragment;
import app.teeramet.money.moneydiary.fragment.YearGraphFragment;

import java.util.Calendar;

public class GraphActivity extends AppCompatActivity {

    private DateDialog dateDialog;

    private Toolbar toolbar;
    private CustomViewPager viewPager;
    private TextView tvDate;
    private TabLayout tabLayout;
    Context context = this;

    int mMonth = 0;
    int mYear = 0;

    public int leapyear = 29;
    public int notleapyear = 28;
    public int min = 30;
    public int max = 31;

    String strMonth;
    String strYear;
    String mStarttime = getStartDate();
    String mEndtime = getEndDate();
    int valueMonth;

    SectionsPagerGraphAdapter sectionsPagerGraphAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvDate = (TextView) findViewById(R.id.txtdate);
        setSupportActionBar(toolbar);

        viewPager = (CustomViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.setPagingEnabled(false);

        sectionsPagerGraphAdapter = new SectionsPagerGraphAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerGraphAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public TextView getTvDate() {
        return tvDate;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backmenu:
                onBackPressed();
                break;

            case R.id.calendarselect:
                dateDialog();
                dateDialog.getDateDialog().show();
                break;
        }
    }

    public void dateDialog() {
        dateDialog = new DateDialog(context);

        Calendar calendar = Calendar.getInstance();
        int valuemonth = calendar.get(Calendar.MONTH);
        int valueyear = calendar.get(Calendar.YEAR);

        Spinner spnyear = dateDialog.getSpnYear();
        Spinner spnmonth = dateDialog.getSpnMonth();

        for (int i = 0; i < spnyear.getAdapter().getCount(); i++) {
            if (spnyear.getItemAtPosition(i).toString().equals(valueyear)) {
                valueyear = i;
            }
        }

        for (int i = 0; i < spnmonth.getAdapter().getCount(); i++) {
            if (spnmonth.getItemAtPosition(i).toString().equals(valuemonth)) {
                valuemonth = i;
            }
        }

        dateDialog.setSelectItemPosition(mMonth, mYear);
        dateDialog.getButtonOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strMonth = dateDialog.getMonthValue();
                strYear = dateDialog.getYearValue();

                mMonth = dateDialog.getMonthItemPosition();
                mYear = dateDialog.getYearItemPosition();

                dateDialog.setSelectItemPosition(mMonth, mYear);

                valueMonth = dateDialog.valuemonth[mMonth];

                Fragment fragment1 = getSupportFragmentManager().getFragments().get(1);
                Fragment fragment2 = getSupportFragmentManager().getFragments().get(0);

                tvDate.setText(strMonth + " - " + strYear);

                if (fragment1 instanceof MonthGraphFragment) {

                    int dayofmonth = dateDialog.getMonth(valueMonth, Integer.parseInt(strYear));
                    if (valueMonth >= 10) {
                        mStarttime = strYear + "-" + String.valueOf(valueMonth) + "-01";
                        mEndtime = strYear + "-" + String.valueOf(valueMonth) + "-" + dayofmonth;
                    } else {
                        mStarttime = strYear + "-0" + String.valueOf(valueMonth) + "-01";
                        mEndtime = strYear + "-0" + String.valueOf(valueMonth) + "-" + dayofmonth;
                    }
                    ((MonthGraphFragment) fragment1).setDataChart(context, mStarttime, mEndtime, dayofmonth, Integer.parseInt(strYear));
                }

                if (fragment2 instanceof YearGraphFragment) {
                    String starttime = strYear + "-01-01";
                    String endtime = strYear + "-12-31";
                    ((YearGraphFragment) fragment2).setDataChart(context, starttime, endtime, Integer.parseInt(strYear));
                }
                dateDialog.getDateDialog().cancel();
            }

        });
        dateDialog.getButtonCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog.getDateDialog().dismiss();
            }
        });
        dateDialog.getViewDialog();
    }

    public String getStartDate() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        month += 1;
        String startdate = "";
        if (month >= 10) {
            startdate = year + "-" + month + "-01";
        } else {
            startdate = year + "-0" + month + "-01";
        }

        return startdate;
    }

    public String getEndDate() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int dayofmonth = getMonth((month + 1), year);

        month += 1;
        String enddate = "";
        if (month >= 10) {
            enddate = year + "-" + month + "-" + dayofmonth;
        } else {
            enddate = year + "-0" + month + "-" + dayofmonth;
        }
        return enddate;
    }

    public int getMonth(int month, int year) {
        switch (month) {
            case 2:
                boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
                if (isLeapYear) {
                    return leapyear;
                } else return notleapyear;

            case 1:
            case 4:
            case 6:
            case 9:
            case 11:
                return min;
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
            default:
                return max;

        }
    }

}
