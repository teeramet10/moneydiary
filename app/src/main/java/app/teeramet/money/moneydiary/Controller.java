package app.teeramet.money.moneydiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.database.DatabaseHelper;

/**
 * Created by barbie on 25/8/2559.
 */
public class Controller {
    Context context;
    Activity activity;
    Intent intent;
    public static final String SENDDATA = "SENDDATA";

    public Controller() {
    }

    public Controller(Context context) {
        this.context = context;
    }

    public Controller(Activity activity) {
        this.activity = activity;
    }

    public Controller(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void sendData(Money money, Class<?> cls) {
        intent = new Intent(context, cls);

        intent.putExtra(DatabaseHelper.MONEY_ID, money.getId());
        intent.putExtra(DatabaseHelper.MONEY_NAME, money.getStrName());
        intent.putExtra(DatabaseHelper.ID_ACCOUNT, money.getAccount().getId());
        intent.putExtra(DatabaseHelper.ID_CATALOG, money.getCatalog().getId());
        intent.putExtra(DatabaseHelper.ID_TYPEMONEY, money.getMoneyType().getId());
        intent.putExtra(DatabaseHelper.MONEY_PRICE, money.getPrice());
        intent.putExtra(DatabaseHelper.MONEY_DESCRIPT, money.getDescript());
        intent.putExtra(DatabaseHelper.MONEY_DATE, money.getDate());
        intent.putExtra(DatabaseHelper.MONEY_TIME, money.getTime());
        intent.putExtra(DatabaseHelper.MONEY_LOCATION, money.getNamelocation());
        intent.putExtra(DatabaseHelper.MONEY_LONG, money.getLongtitude());
        intent.putExtra(DatabaseHelper.MONEY_LAT, money.getLattitude());
        intent.putExtra(DatabaseHelper.MONEY_PATHIMAGE, money.getPathImage());

        context.startActivity(intent);
    }

    public void sendDataForResult(Money money, Class<?> cls, int code) {
        intent = new Intent(context, cls);

        intent.putExtra(DatabaseHelper.MONEY_ID, money.getId());
        intent.putExtra(DatabaseHelper.MONEY_NAME, money.getStrName());
        intent.putExtra(DatabaseHelper.ID_ACCOUNT, money.getAccount().getId());
        intent.putExtra(DatabaseHelper.ID_CATALOG, money.getCatalog().getId());
        intent.putExtra(DatabaseHelper.ID_TYPEMONEY, money.getMoneyType().getId());
        intent.putExtra(DatabaseHelper.MONEY_PRICE, money.getPrice());
        intent.putExtra(DatabaseHelper.MONEY_DESCRIPT, money.getDescript());
        intent.putExtra(DatabaseHelper.MONEY_DATE, money.getDate());
        intent.putExtra(DatabaseHelper.MONEY_TIME, money.getTime());
        intent.putExtra(DatabaseHelper.MONEY_LOCATION, money.getNamelocation());
        intent.putExtra(DatabaseHelper.MONEY_LONG, money.getLongtitude());
        intent.putExtra(DatabaseHelper.MONEY_LAT, money.getLattitude());
        intent.putExtra(DatabaseHelper.MONEY_PATHIMAGE, money.getPathImage());

        activity.startActivityForResult(intent, code);
    }

    public void sendDataBack(Money money){
        intent = new Intent();

        intent.putExtra(DatabaseHelper.MONEY_ID, money.getId());
        intent.putExtra(DatabaseHelper.MONEY_NAME, money.getStrName());
        intent.putExtra(DatabaseHelper.ID_ACCOUNT, money.getAccount().getId());
        intent.putExtra(DatabaseHelper.ID_CATALOG, money.getCatalog().getId());
        intent.putExtra(DatabaseHelper.ID_TYPEMONEY, money.getMoneyType().getId());
        intent.putExtra(DatabaseHelper.MONEY_PRICE, money.getPrice());
        intent.putExtra(DatabaseHelper.MONEY_DESCRIPT, money.getDescript());
        intent.putExtra(DatabaseHelper.MONEY_DATE, money.getDate());
        intent.putExtra(DatabaseHelper.MONEY_TIME, money.getTime());
        intent.putExtra(DatabaseHelper.MONEY_LOCATION, money.getNamelocation());
        intent.putExtra(DatabaseHelper.MONEY_LONG, money.getLongtitude());
        intent.putExtra(DatabaseHelper.MONEY_LAT, money.getLattitude());
        intent.putExtra(DatabaseHelper.MONEY_PATHIMAGE, money.getPathImage());

        activity.setResult(Activity.RESULT_OK,intent);
        activity.finish();

    }




}
