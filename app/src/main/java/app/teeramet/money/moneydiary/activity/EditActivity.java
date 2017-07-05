package app.teeramet.money.moneydiary.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import app.teeramet.money.moneydiary.Controller;
import app.teeramet.money.moneydiary.DecimalDigitsInputFilter;
import app.teeramet.money.moneydiary.ImageManage;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.adapter.SpinnerCatalogAdapter;
import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.classmoney.Expenses;
import app.teeramet.money.moneydiary.classmoney.Income;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.classmoney.TypeMoney;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.DatabaseManagement;
import app.teeramet.money.moneydiary.database.ReadDatabase;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    public static final int CAMERA_CODE = 100;
    public static final int GALLERY_CODE = 101;
    public static final int MAP_CODE = 102;
    private final String[] LIST = {"Take a Camera", "Choose in Gallery"};

    DatabaseManagement myDatabase;
    Context context = this;
    Activity activity = EditActivity.this;
    Money mIncome;
    Money mExpense;
    Account mAccount;
    Catalog mCatalog;

    Toolbar toolbar;
    FloatingActionButton fabSave;
    Spinner spnCatalog;
    Button btnTime;
    ImageView imgAddpicture;
    TextView tvTitle;
    Button btnDate;
    Button btnLocation;
    EditText edtName;
    EditText edtPrice;
    EditText edtDesciption;

    int mIdMoney;
    int mIdAccount;
    int mIdCatalog;
    int mIdTypemoney;
    String mNameCatalog;
    String mStrName;
    String mDescript;
    String mLocationname;
    String mPathimage;
    String mTypemoney;
    long mDate;
    long mTime;
    double mPrice;
    double mLongtitude;
    double mLattitude;


    Calendar calendar = Calendar.getInstance();

    ArrayList<Catalog> mCatalogArrayList;//
    ArrayList<Catalog> mSelectCatalog; // check mCatalog
    SpinnerCatalogAdapter mSpinnerCatalogAdapter;

    ImageManage mImageManage;

    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bindWidget();

        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        String title = sharePreferenceHelper.getNameAccountPreference();
        tvTitle.setText(title);

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

        if (mIdTypemoney == 1) {
            mTypemoney = MainActivity.INCOME;
        } else mTypemoney = MainActivity.EXPENSE;


        edtName.setText(mStrName);
        edtPrice.setText(String.valueOf(moneyFormat.format(mPrice)));
        edtDesciption.setText(mDescript);
        btnTime.setText(timeFormat.format(mTime));
        btnDate.setText(dateFormat.format(mDate));
        btnLocation.setText(mLocationname);


        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTimePicker();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("LAT",mLattitude);
                intent.putExtra("LONG",mLongtitude);
                intent.putExtra("LOCATION",mLocationname);
                intent.putExtra("CATALOG",mIdCatalog);
                startActivityForResult(intent, MAP_CODE);
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDatePicker();
            }
        });

        imgAddpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO CHECK FILL FORM
                if (edtName.getText().toString().length() >0 && edtPrice.getText().toString().length() > 0) {
                    updateData();
                } else
                    Toast.makeText(context, "Please fill in form name and mPrice", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void bindWidget() {
        spnCatalog = (Spinner) findViewById(R.id.descript_catalog);
        tvTitle = (TextView) findViewById(R.id.title);
        edtName = (EditText) findViewById(R.id.edtname);
        edtPrice = (EditText) findViewById(R.id.descript_price);
        edtDesciption = (EditText) findViewById(R.id.descript_detail);
        btnTime = (Button) findViewById(R.id.descript_time);
        btnDate = (Button) findViewById(R.id.descript_date);
        btnLocation = (Button) findViewById(R.id.descript_location);
        imgAddpicture = (ImageView) findViewById(R.id.descript_image);
        fabSave = (FloatingActionButton) findViewById(R.id.button_save);

        edtPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(11,2)});
    }

    public void spinnerManage(ArrayList<Catalog> catalogs) {
//        catalognamelist = new ArrayList<>();
//        for (int i = 0; i < catalogs.size(); i++) {
//            catalognamelist.add(catalogs.get(i).getStrNameList());
//        }

        mSpinnerCatalogAdapter = new SpinnerCatalogAdapter(context, mCatalogArrayList);
        spnCatalog.setAdapter(mSpinnerCatalogAdapter);

    }


    public void getCatalogueFormDatabase() {
        ReadDatabase readDatabase = new ReadDatabase(context);
        if (mTypemoney.equals(MainActivity.INCOME)) {
            String where = DatabaseHelper.CATALOG_TYPEMONEYID + "=?";
            String[] wherearg = {"1"};
            mCatalogArrayList = readDatabase.readCatalog(where, wherearg);
        } else if (mTypemoney.equals(MainActivity.EXPENSE)) {
            String where = DatabaseHelper.CATALOG_TYPEMONEYID + "=?";
            String[] wherearg = {"2"};
            mCatalogArrayList = readDatabase.readCatalog(where, wherearg);
        }
    }

    public void setSpnCatalog() {
        getCatalogueFormDatabase();
        spinnerManage(mCatalogArrayList);

        ReadDatabase readDatabase = new ReadDatabase(context);
        String[] idCataloglist = {String.valueOf(mIdCatalog)};
        mSelectCatalog = readDatabase.readCatalog(DatabaseHelper.CATALOG_ID + "=?", idCataloglist);

        if (mSelectCatalog.size() == 0) {
            if (mIdTypemoney == 1) {
                readDatabase = new ReadDatabase(context);
                String[] cataloglist = {ManageCatalogueActivity.INCOME};
                mSelectCatalog = readDatabase.readCatalog(DatabaseHelper.CATALOG_NAME + "=?", cataloglist);
            } else {
                readDatabase = new ReadDatabase(context);
                String[] cataloglist = {ManageCatalogueActivity.OTHER};
                mSelectCatalog = readDatabase.readCatalog(DatabaseHelper.CATALOG_NAME + "=?", cataloglist);
            }

        }

        for (int i = 0; i < mSpinnerCatalogAdapter.getCount(); i++) {

            if (mSpinnerCatalogAdapter.getItem(i).getStrNameList().equals(mSelectCatalog.get(0).getStrNameList())) {
                spnCatalog.setSelection(i);
            }
        }
    }

    public void checkCatalog() {
        mSelectCatalog = new ArrayList<>();
        Catalog catalog = (Catalog) spnCatalog.getSelectedItem();
        String[] strCatalog = {catalog.getStrNameList()};
        ReadDatabase readDatabase = new ReadDatabase(context);
        mSelectCatalog = readDatabase.readCatalog(DatabaseHelper.CATALOG_NAME + "=?", strCatalog);

    }

    public void eventDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date1 = new Date(year - 1900, monthOfYear, dayOfMonth);
                btnDate.setText(String.valueOf(dateFormat.format(date1.getTime())));
                mDate = date1.getTime();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void eventTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                btnTime.setText(String.valueOf(hourOfDay + " : " + minute));
                Time time1 = new Time(hourOfDay, minute, 0);
                mTime = time1.getTime();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    public void selectImage() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Select");
        alert.setItems(LIST, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(Intent.createChooser(intent, "Take a Pic"), CAMERA_CODE);
                    }

                } else if (which == 1) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_CODE);
                }
            }
        });
        alert.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        mImageManage = new ImageManage(context, activity);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {

            mPathimage = mImageManage.Gallery(data);

            imgAddpicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgAddpicture.setImageURI(Uri.parse(mPathimage));

        } else if (requestCode == CAMERA_CODE && resultCode == RESULT_OK && data != null) {

            mImageManage.Camera(data);
            mPathimage = mImageManage.saveImageToStorage(data);

            imgAddpicture.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imgAddpicture.setImageURI(Uri.parse(mPathimage));
        } else if (requestCode == MAP_CODE && resultCode == RESULT_OK && data != null) {
            mLocationname = data.getStringExtra("LOCATION");
            mLattitude = data.getDoubleExtra("LAT", 0);
            mLongtitude = data.getDoubleExtra("LONG", 0);
            btnLocation.setText(mLocationname);
        }

    }

    public void updateData() {
        String[] idMoneylist = {String.valueOf(mIdMoney)};
        myDatabase = new DatabaseManagement(context);
        mAccount = new Account();
        mAccount.setId(mIdAccount);
//        mAccount.setName("myAccount");
//        mAccount.setImage();

        checkCatalog();
        mCatalog = mSelectCatalog.get(0);

        String strName = edtName.getText().toString();
        String strDescript = edtDesciption.getText().toString();
        double price = 0;
        if (edtPrice.getText().length() != 0) {
            String strPrice = edtPrice.getText().toString();
            String[] pricearray = strPrice.split(",");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pricearray.length; i++) {
                sb.append(pricearray[i]);
            }
            strPrice = sb.toString();
            price = Double.parseDouble(strPrice);
        }
        Controller controller = new Controller(context, activity);

        if (mTypemoney.equals(MainActivity.INCOME)) {
            TypeMoney typeMoney = new TypeMoney();
            typeMoney.setId(1);
            typeMoney.setTypeMoneyName(MainActivity.INCOME);

            mIncome = new Income();
//            mIncome.setId(mIdMoney);
            mIncome.setStrName(strName);
            mIncome.setAccount(mAccount);
            mIncome.setCatalog(mCatalog);
            mIncome.setMoneyType(typeMoney);
            mIncome.setPrice(price);
            mIncome.setDate(mDate);
            mIncome.setTime(mTime);
            mIncome.setNamelocation(mLocationname);
            mIncome.setLattitude(mLattitude);
            mIncome.setLongtitude(mLongtitude);
            mIncome.setPathImage(mPathimage);
            mIncome.setDescript(strDescript);
            myDatabase.updateMoney(mIncome, idMoneylist);
            myDatabase.closeDatabase();
            controller.sendDataBack(mIncome);

        } else if (mTypemoney.equals(MainActivity.EXPENSE)) {
            mExpense = new Expenses();
            TypeMoney typeMoney = new TypeMoney();
            typeMoney.setId(2);
            typeMoney.setTypeMoneyName(MainActivity.EXPENSE);

//            mExpense.setId(mIdMoney);
            mExpense.setAccount(mAccount);
            mExpense.setStrName(strName);
            mExpense.setCatalog(mCatalog);
            mExpense.setMoneyType(typeMoney);
            mExpense.setPrice(price);
            mExpense.setDate(mDate);
            mExpense.setTime(mTime);
            mExpense.setNamelocation(mLocationname);
            mExpense.setLattitude(mLattitude);
            mExpense.setLongtitude(mLongtitude);
            mExpense.setPathImage(mPathimage);
            mExpense.setDescript(strDescript);
            myDatabase.updateMoney(mExpense, idMoneylist);
            myDatabase.closeDatabase();
            controller.sendDataBack(mExpense);
        }

    }


    public void onClickMenu(View view) {
        switch (view.getId()) {
            case R.id.backmenu:
                onBackPressed();
                break;
            case R.id.managecat:
                Intent intent = new Intent(context, ManageCatalogueActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpnCatalog();
    }
}
