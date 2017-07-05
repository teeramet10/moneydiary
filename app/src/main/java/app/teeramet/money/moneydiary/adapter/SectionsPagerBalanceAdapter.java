package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.classmoney.Balance;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.classmoney.Pager;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.ReadDatabase;
import app.teeramet.moneydiary.R;

/**
 * Created by barbie on 1/31/2017.
 */

public class SectionsPagerBalanceAdapter extends PagerAdapter {
    public static String[] month = {"January", "Febuary", "March", "April", "May", "June", "July"
            , "August", "September", "October", "November", "December"};

    public static int imagemonth[] = {R.drawable.calendar1, R.drawable.calendar2, R.drawable.calendar3,
            R.drawable.calendar4, R.drawable.calendar5, R.drawable.calendar6,
            R.drawable.calendar7, R.drawable.calendar8, R.drawable.calendar9,
            R.drawable.calendar10, R.drawable.calendar11, R.drawable.calendar12,};

    SimpleDateFormat dayformat = new SimpleDateFormat("dd");
    SimpleDateFormat monthformat = new SimpleDateFormat("MM");
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");

    Context context;
    private ArrayList<Pager> pagerArrayList;
    private ArrayList<Money> moneyArrayList;
    private ArrayList<Money>[] moneymonth;
    private LayoutInflater layoutInflater;

    private SharePreferenceHelper sharePreferenceHelper;
    private int idAccount;

    public SectionsPagerBalanceAdapter(Context context, ArrayList<Pager> pagerArrayList) {
        this.pagerArrayList = pagerArrayList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pagerArrayList.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View pageview = layoutInflater.inflate(R.layout.view_listbalance, container, false);

        ArrayList<Balance> balanceArrayList =setDataListView(pagerArrayList.get(position).getYear());

        ListView listView = (ListView) pageview.findViewById(R.id.listview);
        View viewempty = layoutInflater.inflate(R.layout.emptystate_balance, container, false);
        ((ViewGroup) listView.getParent()).addView(viewempty);
        listView.setEmptyView(viewempty);

        BalanceAdapter balanceAdapter = new BalanceAdapter(context, balanceArrayList);
        listView.setAdapter(balanceAdapter);

        container.addView(pageview);
        return pageview;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return pagerArrayList.get(position).getTitle();
    }

    public ArrayList<Balance> setDataListView(int year) {
        String strcurrentyear = String.valueOf(year);
        String starttime = strcurrentyear + "-01-01";
        String endtime = strcurrentyear + "-12-31";

        String str = "SELECT * FROM " + DatabaseHelper.MONEY_DB + " WHERE " + DatabaseHelper.ID_ACCOUNT + "= ?"
                + " AND date(" + DatabaseHelper.MONEY_DATE + "/1000, 'unixepoch') BETWEEN '" + starttime + "' AND '" + endtime + "'";
        String[] arg = {String.valueOf(getIdAccount())};

        ReadDatabase readDatabase = new ReadDatabase(context);
        moneyArrayList = readDatabase.readDatabaseMoney(str, arg);

        if (moneyArrayList.size() != 0) {
            moneymonth = setMoneyList(moneyArrayList, 12, year);

            ArrayList<Balance> balanceArrayList = new ArrayList<>();

            for (int i = 0; i < moneymonth.length; i++) {
                double income = 0;
                double expense = 0;
                for (int j = 0; j < moneymonth[i].size(); j++) {

                    if (moneymonth[i].get(j).getMoneyType().getId() == 1) {
                        income += moneymonth[i].get(j).getPrice();
                    } else if (moneymonth[i].get(j).getMoneyType().getId() == 2) {
                        expense += moneymonth[i].get(j).getPrice();
                    }

                }

                double balancemonth = income - expense;

                Balance balance = new Balance(month[i], balancemonth);
                balance.setImageId(imagemonth[i]);
                balanceArrayList.add(balance);


            }
            return balanceArrayList;
        }
        return new ArrayList<>();
    }


    public ArrayList<Money>[] setMoneyList(ArrayList<Money> moneylist, int sizelist,
                                           int currentyear) {
        ArrayList<Money>[] moneymonth = new ArrayList[sizelist];

        for (int i = 0; i < moneymonth.length; i++) {
            moneymonth[i] = new ArrayList<>();
        }
        //getcurrentyear to check

        for (int i = 0; i < moneylist.size(); i++) {
            if (moneylist.size() != 0) {
                Date date = new Date(moneylist.get(i).getDate());
                String strdate = dayformat.format(date);
                String strmonth = monthformat.format(date);
                String stryear = yearformat.format(date);
                int count = 0;
                for (int j = 1; j <= sizelist; j++) {

                    if (Integer.parseInt(strmonth) == j && Integer.parseInt(stryear) == currentyear) {
                        moneymonth[count].add(moneylist.get(i));
                    }
                    count++;
                }
            }
        }
        return moneymonth;
    }

    public String getIdAccount() {
        sharePreferenceHelper = new SharePreferenceHelper(context);
        idAccount = sharePreferenceHelper.getIdAccountPreference();
        return String.valueOf(idAccount);
    }

}
