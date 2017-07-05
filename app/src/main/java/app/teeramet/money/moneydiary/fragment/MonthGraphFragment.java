package app.teeramet.money.moneydiary.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.teeramet.money.moneydiary.classmoney.MyPieChart;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.activity.GraphActivity;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.ReadDatabase;
import app.teeramet.money.moneydiary.classmoney.MyBarChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthGraphFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    private BarChart mBarChart;
    private PieChart mIncomePieChart;
    private PieChart mExpensePieChart;
    BarData mBarData;

    ArrayList<Money> mIncomemoney;
    ArrayList<Money> mExpensemoney;
    ArrayList<Money>[] mIncomemonth;
    ArrayList<Money>[] mExpensemonth;

    ArrayList<BarEntry> incomeEntries = new ArrayList<>();
    ArrayList<BarEntry> expenseEntrie = new ArrayList<>();

    int mIdAccount;
    Context context;
    Calendar mCalendar;
    int mCurrentyear;
    int mCurrentMonth;
    int mDayOfMonth;

    public MonthGraphFragment() {

    }

    public static MonthGraphFragment newInstance(int sectionNumber) {
        MonthGraphFragment fragment = new MonthGraphFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        mBarChart = (BarChart) view.findViewById(R.id.barchart);
        mIncomePieChart = (PieChart) view.findViewById(R.id.incomepiechart);
        mExpensePieChart =(PieChart) view.findViewById(R.id.expensepiechart);

        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        mIdAccount = sharePreferenceHelper.getIdAccountPreference();

        mCalendar = Calendar.getInstance();
        mCurrentyear = mCalendar.get(Calendar.YEAR);
        mCurrentMonth = mCalendar.get(Calendar.MONTH)+1;
        mDayOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String strcurrentyear = String.valueOf(mCurrentyear);
        String strcurrentmonth;
        if(mCurrentMonth<10) {
            strcurrentmonth = String.valueOf("0"+mCurrentMonth);
        }else{
            strcurrentmonth = String.valueOf(mCurrentMonth);
        }
        String strcurrentday = String.valueOf(mDayOfMonth);

        String starttime = strcurrentyear + "-" + strcurrentmonth + "-01";
        String endtime = strcurrentyear + "-" + strcurrentmonth + "-" + strcurrentday;

        setDataChart(context, starttime, endtime,mDayOfMonth,mCurrentyear);

        Activity graphActivity =getActivity();
        if(graphActivity instanceof GraphActivity){
            ((GraphActivity) graphActivity).getTvDate().setText(mMonths[mCurrentMonth-1]+" - "+strcurrentyear);
        }

        return view;
    }


    public void setDataChart(Context context, String starttime, String endtime, int dayofmonth, int year) {

        String str = "SELECT * FROM " + DatabaseHelper.MONEY_DB + " WHERE " + DatabaseHelper.ID_ACCOUNT + "= ? AND " + DatabaseHelper.ID_TYPEMONEY + " =?" +
                " AND date(" + DatabaseHelper.MONEY_DATE + "/1000, 'unixepoch') BETWEEN '" + starttime + "' AND '" + endtime + "'";
        String[] arg = {String.valueOf(mIdAccount), String.valueOf(1)};

        String str1 = "SELECT * FROM " + DatabaseHelper.MONEY_DB + " WHERE " + DatabaseHelper.ID_ACCOUNT + "= ? AND " + DatabaseHelper.ID_TYPEMONEY + "= ?"
                + " AND date(" + DatabaseHelper.MONEY_DATE + "/1000, 'unixepoch') BETWEEN '" + starttime + "' AND '" + endtime + "'";
        String[] arg1 = {String.valueOf(mIdAccount), String.valueOf(2)};

        ReadDatabase readDatabase = new ReadDatabase(context);
        mIncomemoney = readDatabase.readDatabaseMoney(str, arg);

        readDatabase = new ReadDatabase(context);
        mExpensemoney = readDatabase.readDatabaseMoney(str1, arg1);

        setMyBarChart(dayofmonth,year);
        setDataIncomePieChart();
        setDataExpensePieChart();

    }

    public void setMyBarChart(int dayofmonth,int year){
        MyBarChart myBarChart = new MyBarChart(context);
        mIncomemonth = myBarChart.setMoneyList(mIncomemoney, dayofmonth,year,MyBarChart.MONTH);
        mExpensemonth = myBarChart.setMoneyList(mExpensemoney,dayofmonth,year,MyBarChart.MONTH);

        incomeEntries = myBarChart.setMoneyEntry(mIncomemonth);
        expenseEntrie = myBarChart.setMoneyEntry(mExpensemonth);

        BarDataSet incomeData, expenseData;


        if (mBarChart.getData() != null &&mBarChart.getData().getDataSetCount()>0) {
            incomeData = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            expenseData = (BarDataSet) mBarChart.getData().getDataSetByIndex(1);
            incomeData.setValues(incomeEntries);
            expenseData.setValues(expenseEntrie);

            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();

        } else {
            incomeData = new BarDataSet(incomeEntries, "INCOME");
            incomeData.setColor(getResources().getColor(R.color.chart1));
            expenseData = new BarDataSet(expenseEntrie, "EXPENSE");
            expenseData.setColor(getResources().getColor(R.color.chart2));


            ArrayList<IBarDataSet> barDataSets = new ArrayList<>();
            barDataSets.add(incomeData);
            barDataSets.add(expenseData);

            mBarData = new BarData(barDataSets);
            mBarData.setValueFormatter(new LargeValueFormatter());
            mBarChart.setData(mBarData);


        }
        mBarChart = myBarChart.setBarChart(mBarChart, MyBarChart.MONTH);
        mBarChart.invalidate();
    }

    private ArrayList<Catalog> readCatalog(String where ,String[] wherearg) {
        ReadDatabase readDatabase = new ReadDatabase(context);
        ArrayList<Catalog> catalogArrayList = readDatabase.readCatalog(where, wherearg);
        return catalogArrayList;
    }

    public void setDataIncomePieChart(){
        ArrayList<Catalog> incomecat =readCatalog(DatabaseHelper.CATALOG_TYPEMONEYID+"=?",new String[]{"1"});
        MyPieChart myPieChart =new MyPieChart(context, mIncomePieChart,mIncomemoney,incomecat);
        myPieChart.setDataPieChart();
        myPieChart.setAttributeChart("Income");
        mIncomePieChart =myPieChart.getPieChart();
        mIncomePieChart.invalidate();
    }

    public void setDataExpensePieChart(){
        ArrayList<Catalog> expensecat =readCatalog(DatabaseHelper.CATALOG_TYPEMONEYID+"=?",new String[]{"2"});
        MyPieChart myPieChart =new MyPieChart(context, mExpensePieChart,mExpensemoney,expensecat);
        myPieChart.setDataPieChart();
        myPieChart.setAttributeChart("Expense");
        mExpensePieChart =myPieChart.getPieChart();
        mExpensePieChart.invalidate();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
