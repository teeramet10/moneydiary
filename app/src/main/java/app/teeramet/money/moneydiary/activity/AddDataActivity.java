package app.teeramet.money.moneydiary.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import app.teeramet.money.moneydiary.DecimalDigitsInputFilter;
import app.teeramet.money.moneydiary.ImageManage;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.adapter.SpinnerCatalogAdapter;
import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.classmoney.Expenses;
import app.teeramet.money.moneydiary.classmoney.Income;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.classmoney.TypeMoney;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.DatabaseManagement;
import app.teeramet.money.moneydiary.database.ReadDatabase;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddDataActivity extends AppCompatActivity {
    public static final int CAMERA_CODE = 100;
    public static final int GALLERY_CODE = 101;
    public static final int MAP_CODE = 102;


    DatabaseManagement myDatabase;
    Income mIncome;
    Expenses mExpense;
    Account mAccount;
    Catalog mCatalog;

    Toolbar toolbar;
    FloatingActionButton fabAdd;
    Spinner spnCatalog;
    Button btnTime;
    ImageView imgAddpicture;
    Button btnDate;
    Button btnLocation;
    EditText edtName;
    EditText edtPrice;
    EditText edtDesciption;
    TextView tvTitle;


    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Calendar calendar = Calendar.getInstance();
    String mPathimage;
    String mTypemoney;
    String mLocationname;
    double mLattitude;
    double mLongtitude;
    int mIdAccount;

    String mStrtime;
    long mDate;
    long mTime;
    ArrayList<Catalog> mCatalogArrayList;//
    ArrayList<Catalog> mSelectCatalog; // check mCatalog
    SpinnerCatalogAdapter mSpinnerCatalogAdapter;

    ImageManage mImageManage;

    Context context = this;
    Activity activity= AddDataActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bindWidget();

        getCurrentDateTime();

        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        String title = sharePreferenceHelper.getNameAccountPreference();
        tvTitle.setText(title);

        Intent intent = getIntent();
        mTypemoney = intent.getStringExtra(MainActivity.MONEYTYPE);
        mIdAccount = intent.getIntExtra(MainActivity.ACCOUNT, 1);

        btnTime.setText(timeFormat.format(calendar.getTime()));
        btnDate.setText(dateFormat.format(calendar.getTime()));
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventTimePicker();
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnLocation.getText().toString().trim().equals("")){
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("LAT",mLattitude);
                    intent.putExtra("LONG",mLongtitude);
                    intent.putExtra("LOCATION",mLocationname);
                    int position =spnCatalog.getSelectedItemPosition();
                    Catalog catalog =(Catalog) spnCatalog.getAdapter().getItem(position);
                    intent.putExtra("CATALOG", catalog.getId() );
                    startActivityForResult(intent, MAP_CODE);
                }else
                {
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    startActivityForResult(intent, MAP_CODE);
                }

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
                ImageManage imageManage=new ImageManage(context,activity);
                imageManage.selectImage();
            }
        });

        fabAdd = (FloatingActionButton) findViewById(R.id.button_save);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().length() == 0 && edtPrice.getText().toString().length() == 0) {
                    Toast.makeText(context, "Pleses fill in form name and price", Toast.LENGTH_SHORT).show();
                } else addDatabase();
            }
        });

        spnCatalog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0){
                    mSelectCatalog =new ArrayList<>();
                    mSelectCatalog.add(0,(Catalog)parent.getItemAtPosition(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void bindWidget() {

        spnCatalog = (Spinner) findViewById(R.id.descript_catalog);
        edtName = (EditText) findViewById(R.id.edtname);
        edtPrice = (EditText) findViewById(R.id.descript_price);
        tvTitle =(TextView) findViewById(R.id.title);
        edtDesciption = (EditText) findViewById(R.id.descript_detail);
        btnTime = (Button) findViewById(R.id.descript_time);
        btnDate = (Button) findViewById(R.id.descript_date);
        btnLocation = (Button) findViewById(R.id.descript_location);
        imgAddpicture = (ImageView) findViewById(R.id.descript_image);

        edtPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(11,2)});

    }


    public void setSpnCatalog() {
        mCatalogArrayList =getCatalogueFormDatabase();
        mSpinnerCatalogAdapter = new SpinnerCatalogAdapter(context, mCatalogArrayList);
        spnCatalog.setAdapter(mSpinnerCatalogAdapter);


        if (mSelectCatalog!=null &&mSelectCatalog.size()>0) {

            for (int i = 0; i < mSpinnerCatalogAdapter.getCount(); i++) {

                if (mSpinnerCatalogAdapter.getItem(i).getStrNameList().equals(mSelectCatalog.get(0).getStrNameList())) {
                    spnCatalog.setSelection(i);
                }
            }
        }



    }


    public ArrayList<Catalog> getCatalogueFormDatabase() {
        ReadDatabase readDatabase = new ReadDatabase(context);
        ArrayList<Catalog> catalogList=new ArrayList<>();
        if(mTypemoney.equals(MainActivity.INCOME)){
            String where = DatabaseHelper.CATALOG_TYPEMONEYID + "=?";
            String[] wherearg = {"1"};
            catalogList = readDatabase.readCatalog(where, wherearg);
        }else if(mTypemoney.equals(MainActivity.EXPENSE)){
            String where = DatabaseHelper.CATALOG_TYPEMONEYID + "=?";
            String[] wherearg = {"2"};
            catalogList = readDatabase.readCatalog(where, wherearg);
        }
        return catalogList;
    }

    public void checkCatalog() {
        mSelectCatalog = new ArrayList<>();
        Catalog catalog =(Catalog) spnCatalog.getSelectedItem();
        String[] strCatalog = {catalog.getStrNameList()};
        ReadDatabase readDatabase = new ReadDatabase(context);
        mSelectCatalog = readDatabase.readCatalog(DatabaseHelper.CATALOG_NAME + "=?", strCatalog);

    }

    public void getCurrentDateTime() {
        mStrtime = String.valueOf(calendar.getTime());
        Date date1=new Date();
        mDate =date1.getTime();
        Time time1=new Time(date1.getTime());
        mTime =time1.getTime();
    }

    public void eventDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Date date1 = new Date(year-1900, monthOfYear, dayOfMonth);
                btnDate.setText(String.valueOf(dateFormat.format(date1)));

                mDate = date1.getTime();
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        mImageManage = new ImageManage(context,activity);
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

    public void addDatabase() {

        myDatabase = new DatabaseManagement(context);
        SharePreferenceHelper sharePreferenceHelper=new SharePreferenceHelper(context);
        int idAccount=sharePreferenceHelper.getIdAccountPreference();
        mAccount = new Account();
        mAccount.setId(idAccount);
//        mAccount.setName("myAccount");
//        mAccount.setImage();

        checkCatalog();
        mCatalog = mSelectCatalog.get(0);

        String strName = edtName.getText().toString();
        String strDescript = edtDesciption.getText().toString();
        double price = 0;
        if (edtPrice.getText().length() != 0) {
            price = Double.parseDouble(edtPrice.getText().toString());
        }


        if (mTypemoney.equals(MainActivity.INCOME)) {
            TypeMoney typeMoney = new TypeMoney();
            typeMoney.setId(1);
            typeMoney.setTypeMoneyName(MainActivity.INCOME);

            mIncome = new Income();
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

            myDatabase.addMoney(mIncome);
        } else if (mTypemoney.equals(MainActivity.EXPENSE)) {
            mExpense = new Expenses();
            TypeMoney typeMoney = new TypeMoney();
            typeMoney.setId(2);
            typeMoney.setTypeMoneyName(MainActivity.EXPENSE);

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
            myDatabase.addMoney(mExpense);
        }
        finish();

    }

    public void onClickMenu(View view){
        switch (view.getId()){
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
