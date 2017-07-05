package app.teeramet.money.moneydiary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.teeramet.money.moneydiary.Controller;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.classmoney.Expenses;
import app.teeramet.money.moneydiary.classmoney.Income;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.classmoney.TypeMoney;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.DatabaseManagement;
import app.teeramet.money.moneydiary.database.ReadDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DescriptionActivity extends AppCompatActivity {
    public static final String PATHIMAGE = "PATHIMAGE";
    public static final int EDITCODE = 102;
    Toolbar toolbar;
    TextView tvName;
    TextView tvCatalogue;
    TextView tvPrice;
    TextView tvDate;
    TextView tvTime;
    TextView tvLocation;
    TextView tvDescription;
    TextView tvTitleDescript;
    ImageView image;

    Context context = this;
    Activity activity = DescriptionActivity.this;
    int mIdMoney;
    String mStrName;
    int mIdAccount;
    int mIdCatalog;
    int mIdTypemoney;
    double mPrice;
    String mDescript;
    long mDate;
    long mTime;
    String mLocationname;
    double mLongtitude;
    double mLattitude;
    String mPathimage;
    String mNameCatalogue;

    DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descript);

        bindWidget();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        String title = sharePreferenceHelper.getNameAccountPreference();
        tvTitleDescript.setText(title);

        Intent intent = getIntent();

        mIdMoney = intent.getIntExtra(DatabaseHelper.MONEY_ID, 0);
        mStrName = intent.getStringExtra(DatabaseHelper.MONEY_NAME);
        mIdAccount = intent.getIntExtra(DatabaseHelper.ID_ACCOUNT, 0);
        mIdCatalog = intent.getIntExtra(DatabaseHelper.ID_CATALOG, 0);
        mIdTypemoney = intent.getIntExtra(DatabaseHelper.ID_TYPEMONEY, 0);
        mPrice = intent.getDoubleExtra(DatabaseHelper.MONEY_PRICE, 0);
        mDescript = intent.getStringExtra(DatabaseHelper.MONEY_DESCRIPT);
        mDate = intent.getLongExtra(DatabaseHelper.MONEY_DATE, 0);
        mTime = intent.getLongExtra(DatabaseHelper.MONEY_TIME, 0);
        mLocationname = intent.getStringExtra(DatabaseHelper.MONEY_LOCATION);
        mLongtitude = intent.getDoubleExtra(DatabaseHelper.MONEY_LONG, 0);
        mLattitude = intent.getDoubleExtra(DatabaseHelper.MONEY_LAT, 0);
        mPathimage = intent.getStringExtra(DatabaseHelper.MONEY_PATHIMAGE);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayImageActivity.class);
                intent.putExtra(PATHIMAGE, mPathimage);
                startActivity(intent);
            }
        });

        setDataOnTextView();

    }

    public void bindWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvName = (TextView) findViewById(R.id.descript_name);
        tvCatalogue = (TextView) findViewById(R.id.descript_catalog);
        tvPrice = (TextView) findViewById(R.id.descript_price);
        tvDate = (TextView) findViewById(R.id.descript_date);
        tvTime = (TextView) findViewById(R.id.descript_time);
        tvLocation = (TextView) findViewById(R.id.descript_location);
        tvDescription = (TextView) findViewById(R.id.descript_detail);
        tvTitleDescript = (TextView) findViewById(R.id.titledescript);
        image = (ImageView) findViewById(R.id.descript_image);
    }

    private String checkCatalog(String[] coloum, String whereclause, String[] wherearg) {
        ReadDatabase readDatabase = new ReadDatabase(context);
        String nameCatalogues = readDatabase.readNameCatalogue(coloum, whereclause, wherearg);
        return nameCatalogues;
    }

    public void eventMenu(View v) {
        switch (v.getId()) {
            case R.id.backmenu:
                onBackPressed();
                break;
            case R.id.editmenu:
                Controller controller = new Controller(context, activity);

                Money income = new Income();
                Money expenses = new Expenses();
                Account account = new Account();
                TypeMoney typeMoney = new TypeMoney();
                Catalog catalog = new Catalog();

                account.setId(mIdAccount);
                typeMoney.setId(mIdTypemoney);
                catalog.setId(mIdCatalog);

                if (mIdTypemoney == 1) {
                    income.setId(mIdMoney);
                    income.setStrName(mStrName);
                    income.setCatalog(catalog);
                    income.setMoneyType(typeMoney);
                    income.setAccount(account);
                    income.setPrice(mPrice);
                    income.setNamelocation(mLocationname);
                    income.setLongtitude(mLongtitude);
                    income.setLattitude(mLattitude);
                    income.setDate(mDate);
                    income.setTime(mTime);
                    income.setDescript(mDescript);
                    income.setPathImage(mPathimage);
                    controller.sendDataForResult(income, EditActivity.class, EDITCODE);
                } else {
                    expenses.setId(mIdMoney);
                    expenses.setStrName(mStrName);
                    expenses.setMoneyType(typeMoney);
                    expenses.setAccount(account);
                    expenses.setCatalog(catalog);
                    expenses.setPrice(mPrice);
                    expenses.setNamelocation(mLocationname);
                    expenses.setLongtitude(mLongtitude);
                    expenses.setLattitude(mLattitude);
                    expenses.setDate(mDate);
                    expenses.setTime(mTime);
                    expenses.setDescript(mDescript);
                    expenses.setPathImage(mPathimage);
                    controller.sendDataForResult(expenses, EditActivity.class, EDITCODE);
                }
                break;

            case R.id.delmenu:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Confirm Delete");
                alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseManagement databaseManagement = new DatabaseManagement(context);
                        databaseManagement.deleteMoney(mIdMoney);
                        databaseManagement.closeDatabase();
                        finish();
                    }
                });

                alertDialog.setNegativeButton("Cancle", null);
                alertDialog.create().show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDITCODE && resultCode == RESULT_OK && data != null) {

            mStrName = data.getStringExtra(DatabaseHelper.MONEY_NAME);
            mIdAccount = data.getIntExtra(DatabaseHelper.ID_ACCOUNT, 0);
            mIdCatalog = data.getIntExtra(DatabaseHelper.ID_CATALOG, 0);
            mIdTypemoney = data.getIntExtra(DatabaseHelper.ID_TYPEMONEY, 0);
            mPrice = data.getDoubleExtra(DatabaseHelper.MONEY_PRICE, 0);
            mDescript = data.getStringExtra(DatabaseHelper.MONEY_DESCRIPT);
            mDate = data.getLongExtra(DatabaseHelper.MONEY_DATE, 0);
            mTime = data.getLongExtra(DatabaseHelper.MONEY_TIME, 0);
            mLocationname = data.getStringExtra(DatabaseHelper.MONEY_LOCATION);
            mLongtitude = data.getDoubleExtra(DatabaseHelper.MONEY_LONG, 0);
            mLattitude = data.getDoubleExtra(DatabaseHelper.MONEY_LAT, 0);
            mPathimage = data.getStringExtra(DatabaseHelper.MONEY_PATHIMAGE);


            final String[] coloum = {DatabaseHelper.CATALOG_NAME};
            String whereclause = DatabaseHelper.CATALOG_ID + "=?";
            String[] wherearg = {String.valueOf(mIdCatalog)};
            mNameCatalogue = checkCatalog(coloum, whereclause, wherearg);

            Date date1 = new Date(mDate);
            Time time1 = new Time(mTime);
            tvName.setText(mStrName);
            tvCatalogue.setText(mNameCatalogue);
            tvPrice.setText(moneyFormat.format(mPrice));
            tvDescription.setText(mDescript);
            tvDate.setText(dateFormat.format(date1));
            tvTime.setText(timeFormat.format(time1));
            tvLocation.setText(mLocationname);
            setImage(mPathimage, image);

        } else if (requestCode == EDITCODE && resultCode == RESULT_CANCELED && data == null) {
//            mIdCatalog = 0;
            final String[] coloum = {DatabaseHelper.CATALOG_NAME};
            String whereclause = DatabaseHelper.CATALOG_ID + "=?";
            String whereclause1 = DatabaseHelper.CATALOG_NAME + "=?";
            String[] wherearg ={String.valueOf(mIdCatalog)};

            String namecatalog =checkCatalog(coloum,whereclause,wherearg);
            if (namecatalog.equals("")) {
                if (mIdTypemoney == 1) {
                    String[] whereincome = {ManageCatalogueActivity.INCOME};
                    mNameCatalogue = checkCatalog(coloum, whereclause1, whereincome);
                    tvCatalogue.setText(mNameCatalogue);
                } else if (mIdTypemoney == 2) {
                    String[] whereexpense = {ManageCatalogueActivity.OTHER};
                    mNameCatalogue = checkCatalog(coloum, whereclause1, whereexpense);
                    tvCatalogue.setText(mNameCatalogue);
                }
            }
        }
    }

    public void setImage(String pathimage, ImageView image) {
        if (pathimage != null) {
            File file = new File(pathimage);
            if (file.exists()) {

                Picasso.with(context).load(file).error(R.drawable.camera48).into(image);
            }
        } else {
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Picasso.with(context).load(R.drawable.camera48).into(image);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setDataOnTextView();

    }

    public void setDataOnTextView() {
        final String[] coloum = {DatabaseHelper.CATALOG_NAME};
        String whereclause = DatabaseHelper.CATALOG_ID + "=?";
        String[] wherearg = {String.valueOf(mIdCatalog)};
        mNameCatalogue = checkCatalog(coloum, whereclause, wherearg);
        if (mNameCatalogue == null || mNameCatalogue.equals("")) {
            if (mIdTypemoney == 1) {
                String[] whereincome = {ManageCatalogueActivity.INCOME};
                mNameCatalogue = checkCatalog(coloum, whereclause, whereincome);
                tvCatalogue.setText(mNameCatalogue);
            } else if (mIdTypemoney == 2) {
                String[] whereexpense = {ManageCatalogueActivity.OTHER};
                mNameCatalogue = checkCatalog(coloum, whereclause, whereexpense);
                tvCatalogue.setText(mNameCatalogue);
            }
        } else tvCatalogue.setText(mNameCatalogue);

        Date date1 = new Date(mDate);
        Time time1 = new Time(mTime);
        tvName.setText(mStrName);
        tvPrice.setText(moneyFormat.format(mPrice));
        tvDescription.setText(mDescript);
        tvDate.setText(dateFormat.format(date1));
        tvTime.setText(timeFormat.format(time1));
        tvLocation.setText(mLocationname);
        setImage(mPathimage, image);
    }
}
