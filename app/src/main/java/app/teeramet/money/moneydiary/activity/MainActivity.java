package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import app.teeramet.money.moneydiary.DateDialog;
import app.teeramet.money.moneydiary.ImageManage;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.adapter.MenuExpandableListViewAdapter;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.fragment.ExpenseFragment;
import app.teeramet.money.moneydiary.fragment.IncomeAndExpenseFragment;
import app.teeramet.money.moneydiary.fragment.IncomeFragment;
import app.teeramet.money.moneydiary.menu.ListMenu;
import app.teeramet.money.moneydiary.menu.Menu;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.adapter.SectionsPagerMoneyAdapter;
import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.classmoney.Expenses;
import app.teeramet.money.moneydiary.classmoney.Income;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.ReadDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final String MONEYTYPE = "MONEYTYPE";
    public static final String INCOME = "INCOME";
    public static final String EXPENSE = "EXPENSE";
    public static final String ACCOUNT = "KEYPREF_ACCOUNT";

    public SimpleDateFormat monthformat =new SimpleDateFormat("MMM");
    public SimpleDateFormat yearformat =new SimpleDateFormat("yyyy");

    public int leapyear = 29;
    public int notleapyear = 28;
    public int min = 30;
    public int max = 31;

    Context context = this;

    private SectionsPagerMoneyAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private TextView tvNameAccount;
    private TextView tvSumPrice;
    private TextView tvDate;
    private TabLayout tabLayout;
    private ExpandableListView expandableListView;
    private ImageView imvAccount;
    public FloatingActionButton fab;

    ImageButton imbBurgerMenu;
    ImageButton imbDateMenu;
    ImageButton imbBalanceMenu;

    DateDialog dateDialog;


    ArrayList<Account> mAccountArrayList = new ArrayList<>();
    ArrayList<Money> mMoneyArrayList = new ArrayList<>();
    HashMap<String, ArrayList<Menu>> mGroupList = new HashMap<>();
    ArrayList<String> mHeaderlist;
    String[] mMoneyType;

    DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");

    String money_type;
    int mIdAccount;

    int positionmonth = 0;
    int positionyear = 0;

    String strMonth = "";
    String strYear = "";

    String mStartDate = getStartDate();
    String mEndDate = getEndDate();

    boolean mBackApp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindWidget();
        setTextDate();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mBackApp = bundle.getBoolean(MenuPassCodeActivity.BACKMENUPASSCODE);
        }

        mangeSharePreference();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setmViewPager();

        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        mIdAccount = sharePreferenceHelper.getIdAccountPreference();
        String nameAccount = sharePreferenceHelper.getNameAccountPreference();
        String pathImgAccount = sharePreferenceHelper.getimageAccountPreference();

        tvNameAccount.setText(nameAccount);

        File file = new File(pathImgAccount);
        if (file.exists()) {
            ImageManage imageManage = new ImageManage(context);
            Transformation transform = imageManage.tranform();
            imvAccount.setPadding(0, 0, 0, 0);
            Picasso.with(context).load(file).fit().centerCrop().transform(transform).into(imvAccount);
        } else {
            ImageManage imageManage = new ImageManage();
            int background = imageManage.randomColorBackground();
            imvAccount.setBackgroundResource(background);
            Picasso.with(context).load(R.drawable.otherb).into(imvAccount);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogAdd();

            }
        });

        imbBurgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        imbDateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialogMenu();
                dateDialog.getDateDialog().show();
            }
        });

        imbBalanceMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,BalanceActivity.class);
                startActivity(intent);
            }
        });



    }

    public void setTextDate(){
        Date date=new Date();
        String currentmonth =monthformat.format(date);
        String currentyear =yearformat.format(date);
        tvDate.setText(currentmonth +" - "+ currentyear);
    }

    private void bindWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        tvNameAccount = (TextView) findViewById(R.id.nameaccount);
        tvSumPrice = (TextView) findViewById(R.id.sumprice);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        imbBurgerMenu = (ImageButton) findViewById(R.id.burgermenu);
        imbDateMenu = (ImageButton) findViewById(R.id.date);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        expandableListView = (ExpandableListView) findViewById(R.id.listmenu);
        imvAccount = (ImageView) findViewById(R.id.accountimage);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tvDate =(TextView) findViewById(R.id.txtdate);
        imbBalanceMenu =(ImageButton) findViewById(R.id.menubalance);
    }


    public void dialogAdd() {
        ReadDatabase readDatabase = new ReadDatabase(context);
        mMoneyType = readDatabase.readDatabaseMoneyType();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Money type");
        alertDialog.setItems(mMoneyType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(getApplicationContext(), AddDataActivity.class);
                    money_type = INCOME;
                    intent.putExtra(ACCOUNT, mIdAccount);
                    intent.putExtra(MONEYTYPE, money_type);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), AddDataActivity.class);
                    money_type = EXPENSE;
                    intent.putExtra(MONEYTYPE, money_type);
                    intent.putExtra(ACCOUNT, mIdAccount);
                    startActivity(intent);
                }
            }
        });
        alertDialog.create().show();
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

    public String getmStartDate() {
        return mStartDate;
    }

    public String getmEndDate() {
        return mEndDate;
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


    public void calculatePrice() {
        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        mIdAccount = sharePreferenceHelper.getIdAccountPreference();

        String whereClause = DatabaseHelper.ID_ACCOUNT + " =? ";
        String[] whereArg = {String.valueOf(mIdAccount)};

        ReadDatabase readDatabase = new ReadDatabase(context);
        mMoneyArrayList = readDatabase.readDatabaseMoneyForCal(whereClause, whereArg);
        double sumincome = 0;
        double sumexpense = 0;
        double sum = 0;
        for (int i = 0; i < mMoneyArrayList.size(); i++) {
            if (mMoneyArrayList.get(i) instanceof Income) {
                double value = mMoneyArrayList.get(i).getPrice();
                sumincome += value;
            } else if (mMoneyArrayList.get(i) instanceof Expenses) {
                double value = mMoneyArrayList.get(i).getPrice();
                sumexpense += value;
            }

        }
        sum = sumincome - sumexpense;

        tvSumPrice.setText(String.valueOf(moneyFormat.format(sum)));


    }

    public void setMenuNavigationDrawer() {
        ReadDatabase readDatabase = new ReadDatabase(context);
        mAccountArrayList = readDatabase.readDatabaseAccount(null, null);

        ArrayList<Menu> Accountlist = new ArrayList<>();
        for (int i = 0; i < mAccountArrayList.size(); i++) {
            Menu menuObject = new Menu(mAccountArrayList.get(i), mAccountArrayList.get(i).getImage());
            Accountlist.add(menuObject);
        }

        String[] menu = {"Manage Account", "Manage Catalogue", "Location", "Report", "Security"};
        int[] imageicon = {R.drawable.moneybag48b, R.drawable.planner48, R.drawable.map48b, R.drawable.graphb, R.drawable.key};
        //input data listmenu
        ArrayList<ListMenu> ListMenulist = new ArrayList<>();
        for (int i = 0; i < menu.length; i++) {
            ListMenu listMenu = new ListMenu(menu[i], imageicon[i]);
            ListMenulist.add(listMenu);
        }
        //add data to arraylist menu
        ArrayList<Menu> menulist = new ArrayList<>();
        for (int i = 0; i < menu.length; i++) {
            Menu menuObject = new Menu(ListMenulist.get(i), ListMenulist.get(i).getImageint());
            menulist.add(menuObject);
        }

        String[] header = {"Menu", "Account"};
        mHeaderlist = new ArrayList<>();
        for (int i = 0; i < header.length; i++) {
            mHeaderlist.add(header[i]);
        }

        mGroupList.put(mHeaderlist.get(0), menulist);
        mGroupList.put(mHeaderlist.get(1), Accountlist);

        MenuExpandableListViewAdapter expandableListViewAdapter = new MenuExpandableListViewAdapter(context, mGroupList, mHeaderlist);
        expandableListView.setAdapter(expandableListViewAdapter);

    }

    public void setExpandableListView() {
        for (int i = 0; i < expandableListView.getExpandableListAdapter().getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    switch (childPosition) {
                        case 0:
                            Intent intent = new Intent(context, ManageAccountActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Intent intent1 = new Intent(context, ManageCatalogueActivity.class);
                            startActivity(intent1);
                            break;
                        case 2:
                            Intent intent2 = new Intent(context, MapShowActivity.class);
                            startActivity(intent2);
                            break;

                        case 3:
                            Intent intent3 = new Intent(context, GraphActivity.class);
                            startActivity(intent3);
                            break;

                        case 4:
                            SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                            boolean usepasscode = sharePreferenceHelper.getUsePassCode();
                            if (usepasscode) {
                                Intent intent4 = new Intent(context, PassCodeActivity.class);
                                intent4.putExtra(MenuPassCodeActivity.PASSCODE, MenuPassCodeActivity.KEYTOMENU);
                                startActivity(intent4);
                            } else {
                                Intent intent4 = new Intent(context, MenuPassCodeActivity.class);
                                startActivity(intent4);
                            }

                    }
                } else if (groupPosition == 1) {
                    Menu menu = (Menu) mGroupList.get(mHeaderlist.get(groupPosition)).get(childPosition);
                    if (menu.getMenu() instanceof Account) {
                        Log.i("TESTACCOUNT", String.valueOf(((Account) menu.getMenu()).getId()));

                        int idAccount = ((Account) menu.getMenu()).getId();
                        String nameAccount = ((Account) menu.getMenu()).getName();
                        String pathimage = menu.getPathname();

                        if (mIdAccount != idAccount) {
                            SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                            sharePreferenceHelper.setIdAccountPreference(idAccount);
                            sharePreferenceHelper.setNameAccountPreference(nameAccount);
                            sharePreferenceHelper.setImageAccountPreference(pathimage);

                            Intent intent = new Intent(context, MainActivity.class);
                            finish();
                            overridePendingTransition(R.anim.transition_right_in, R.anim.transition_left_out);
                            startActivity(intent);


                            calculatePrice();
                        }

                    }
                }

                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }

                return false;
            }
        });

    }

    public void setmViewPager() {
        mSectionsPagerAdapter = new SectionsPagerMoneyAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public void mangeSharePreference() {

        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);

        boolean usepasscode = sharePreferenceHelper.getUsePassCode();
        if (usepasscode && !mBackApp) {
            Intent intent = new Intent(context, PassCodeActivity.class);
            intent.putExtra(MenuPassCodeActivity.PASSCODE, MenuPassCodeActivity.KEYPASS);
            startActivity(intent);
        }


        ReadDatabase readDatabase = new ReadDatabase(context);
        ArrayList<Catalog> incomecataloglist = readDatabase.readCatalog(DatabaseHelper.CATALOG_TYPEMONEYID + "=?", new String[]{String.valueOf(1)});
        readDatabase = new ReadDatabase(context);
        ArrayList<Catalog> expensecataloglist = readDatabase.readCatalog(DatabaseHelper.CATALOG_TYPEMONEYID + "=?", new String[]{String.valueOf(2)});

        int count = (incomecataloglist.size() + expensecataloglist.size());
        String[] namecatalog = new String[count];
        int positioncatalog = 0;

        for (int i = 0; i < count; i++) {
            if (i < incomecataloglist.size()) {
                namecatalog[i] = incomecataloglist.get(i).getStrNameList();
            } else if (i >= incomecataloglist.size()) {
                namecatalog[i] = expensecataloglist.get(positioncatalog).getStrNameList();
                positioncatalog++;
            }
        }

        sharePreferenceHelper = new SharePreferenceHelper(context);
        Boolean[] status = sharePreferenceHelper.getStatusLayer(namecatalog);
        if (status == null || status.length == 0) {
            for (int i = 0; i < count; i++) {
                sharePreferenceHelper.setStatusLayer(namecatalog[i], true);
            }
        }

        sharePreferenceHelper = new SharePreferenceHelper(context);
        int value = sharePreferenceHelper.getStartValueApp();
        if (value == 0) {
            Calendar calendar = Calendar.getInstance();
            int startyear = calendar.get(Calendar.YEAR);
            sharePreferenceHelper.setKeyStartApp(startyear);
        }


    }

    public void dateDialogMenu() {
        dateDialog=new DateDialog(context);

        Calendar calendar = Calendar.getInstance();
        int valuemonth = calendar.get(Calendar.MONTH);
        int valueyear = calendar.get(Calendar.YEAR);

        Spinner spnyear = dateDialog.getSpnYear();
        Spinner spnmonth =dateDialog.getSpnMonth();

        for (int i = 0; i < spnyear.getAdapter().getCount(); i++) {
            if (spnyear.getItemAtPosition(i).toString().equals(valueyear)) {
                positionyear = i;
            }
        }
        for (int i = 0; i < spnmonth.getAdapter().getCount(); i++) {
            if (spnmonth.getItemAtPosition(i).toString().equals(valuemonth)) {
                positionmonth = i;
            }
        }

        dateDialog.setSelectItemPosition(positionmonth, positionyear);
        dateDialog.getButtonOk().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionmonth = dateDialog.getMonthItemPosition();
                positionyear = dateDialog.getYearItemPosition();

                dateDialog.setSelectItemPosition(positionmonth, positionyear);
                strMonth = dateDialog.getMonthValue();
                strYear = dateDialog.getYearValue();

                int valuemonth = dateDialog.valuemonth[positionmonth];
                int dayofmonth = dateDialog.getMonth(valuemonth, Integer.parseInt(strYear));
                if (valuemonth >= 10) {
                    mStartDate = strYear + "-" + String.valueOf(valuemonth) + "-01";
                    mEndDate = strYear + "-" + String.valueOf(valuemonth) + "-" + dayofmonth;
                } else {
                    mStartDate = strYear + "-0" + String.valueOf(valuemonth) + "-01";
                    mEndDate = strYear + "-0" + String.valueOf(valuemonth) + "-" + dayofmonth;
                }

                tvDate.setText(strMonth + " - " + strYear);

                Fragment incomefragment = mSectionsPagerAdapter.getIncomeFragment();
                Fragment expensefragment = mSectionsPagerAdapter.getExpenseFragment();
                Fragment summaryfragment = mSectionsPagerAdapter.getSummaryFragment();


                if (incomefragment instanceof IncomeFragment && incomefragment != null) {
                    ((IncomeFragment) incomefragment).readData(mStartDate, mEndDate);
                }

                if (expensefragment instanceof ExpenseFragment && expensefragment != null) {
                    ((ExpenseFragment) expensefragment).readData(mStartDate, mEndDate);
                }

                if (summaryfragment instanceof IncomeAndExpenseFragment && summaryfragment != null) {
                    ((IncomeAndExpenseFragment) summaryfragment).readData(mStartDate, mEndDate);
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        calculatePrice();
        setMenuNavigationDrawer();
        setExpandableListView();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}
