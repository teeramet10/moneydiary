package app.teeramet.money.moneydiary.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import app.teeramet.money.moneydiary.Controller;
import app.teeramet.money.moneydiary.MyCountDown;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.activity.DescriptionActivity;
import app.teeramet.money.moneydiary.activity.MainActivity;
import app.teeramet.money.moneydiary.adapter.IncomeAndExpenseAdapter;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.DatabaseManagement;
import app.teeramet.money.moneydiary.database.ReadDatabase;

import java.util.ArrayList;

/**
 * Created by barbie on 15/8/2559.
 */
public class IncomeAndExpenseFragment extends MoneyFragment {
    ArrayList<Money> moneyArrayList;
    ArrayList<Money> moneylist;
    Context context;
    IncomeAndExpenseAdapter allAdapter;
    ListView listView;
    View rootView;

    int Position;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public IncomeAndExpenseFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static IncomeAndExpenseFragment newInstance(int sectionNumber) {
        IncomeAndExpenseFragment fragment = new IncomeAndExpenseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_money, container, false);
            listView = (ListView) rootView.findViewById(R.id.listmoney);
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewempty = layoutInflater.inflate(R.layout.emptystate_summary, container, false);
        ((ViewGroup) listView.getParent()).addView(viewempty);
        listView.setEmptyView(viewempty);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    Activity activity = getActivity();
                    FloatingActionButton fab = ((MainActivity) activity).fab;
                    if (scrollState == SCROLL_STATE_IDLE) {
                        MyCountDown fabCountDown = new MyCountDown(1000, 1000, fab);
                        fabCountDown.start();
//                        ((MainActivity)activity).fab.show();
                    } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                        fab.hide();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });

        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Position = position;

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete");
                alertDialog.setMessage("Confirm Delete?");
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseManagement databaseManagement = new DatabaseManagement(context);
                        databaseManagement.deleteMoney(moneyArrayList.get(Position).getId());
                        databaseManagement.closeDatabase();
                        moneylist.remove(Position);
                        allAdapter.notifyDataSetChanged();

                        Activity activity = getActivity();
                        if (activity instanceof MainActivity) {
                            ((MainActivity) activity).calculatePrice();
                        }
                    }
                });
                alertDialog.setNegativeButton(getResources().getString(R.string.cancel), null);
                alertDialog.create().show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Controller controller = new Controller(context);
                controller.sendData(moneyArrayList.get(position), DescriptionActivity.class);
            }
        });

        return rootView;
    }

    @Override
    public void readData(String startdate, String enddate) {
        ReadDatabase readDatabase = new ReadDatabase(context);
        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);

        int idAccount = sharePreferenceHelper.getIdAccountPreference();

        String querydata = "SELECT * FROM " + DatabaseHelper.MONEY_DB + " WHERE " + DatabaseHelper.ID_ACCOUNT + "= ? AND date("
                + DatabaseHelper.MONEY_DATE + "/1000, 'unixepoch') BETWEEN '" + startdate + "' AND '" + enddate + "'"
                + " ORDER BY " + DatabaseHelper.MONEY_DATE + " DESC";
        String[] wherearg = {String.valueOf(idAccount)};


        if (rootView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rootView = inflater.inflate(R.layout.fragment_money, null, false);
            listView = (ListView) rootView.findViewById(R.id.listmoney);
        }

        moneyArrayList = readDatabase.readDatabaseMoney(querydata, wherearg);
        if (moneyArrayList.size() > 0) {
            if (allAdapter == null) {
                moneylist = moneyArrayList;
                allAdapter = new IncomeAndExpenseAdapter(context, moneyArrayList);
                listView.setAdapter(allAdapter);
            } else {
                if (moneylist != null) {
                    moneylist.clear();
                    for (int i = 0; i < moneyArrayList.size(); i++) {
                        moneylist.add(moneyArrayList.get(i));
                    }
                    allAdapter.notifyDataSetChanged();
                }
            }
        } else {
            if (moneylist != null) {
                moneylist.clear();
                allAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Activity main = getActivity();
            if (main instanceof MainActivity) {
                String startdate = ((MainActivity) main).getmStartDate();
                String enddate = ((MainActivity) main).getmEndDate();
                readData(startdate, enddate);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Activity main = getActivity();
        if (main instanceof MainActivity) {
            String startdate = ((MainActivity) main).getmStartDate();
            String enddate = ((MainActivity) main).getmEndDate();
            readData(startdate, enddate);
        }
    }
}
