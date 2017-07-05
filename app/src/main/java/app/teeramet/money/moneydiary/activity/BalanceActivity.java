package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;

import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.adapter.SectionsPagerBalanceAdapter;
import app.teeramet.money.moneydiary.classmoney.Balance;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.classmoney.Pager;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.ReadDatabase;
import app.teeramet.moneydiary.R;

public class BalanceActivity extends AppCompatActivity {

    private Context context = this;
    ArrayList<Integer> year =new ArrayList<>();
    ArrayList<Pager> pagers = new ArrayList<>();

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private SectionsPagerBalanceAdapter mSectionsPagerBalanceAdapter;
    private SharePreferenceHelper sharePreferenceHelper;

    private int idAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        bindWidget();
        getDataformDatabase();
        setmViewPager();


    }

    private void bindWidget() {
        mViewPager = (ViewPager) findViewById(R.id.container);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backmenu:
                onBackPressed();
                break;
        }
    }

    private void getDataformDatabase() {
        SharePreferenceHelper sharePreferenceHelper=new SharePreferenceHelper(context);
        int startyear =sharePreferenceHelper.getYearStartApp();
        year.add(startyear);
        for(int i=0;i<20;i++){
            year.add(++startyear);
        }

        for(int i=0;i<year.size();i++){
            Pager pager =new Pager(String.valueOf(year.get(i)));
            pager.setYear(year.get(i));
            pagers.add(pager);
        }

    }

    private void setmViewPager() {
        mSectionsPagerBalanceAdapter = new SectionsPagerBalanceAdapter(context, pagers);
        mViewPager.setAdapter(mSectionsPagerBalanceAdapter);
        tabLayout.setupWithViewPager(mViewPager);

    }
}
