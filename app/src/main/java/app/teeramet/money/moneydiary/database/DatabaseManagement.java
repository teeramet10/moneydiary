package app.teeramet.money.moneydiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.classmoney.Money;

/**
 * Created by barbie on 17/8/2559.
 */
public class DatabaseManagement {
    public static final String MONEY_DB = "money_db";
    public static final String MONEY_ID = "id";
    public static final String MONEY_NAME = "name";
    public static final String ID_ACCOUNT = "idaccount";
    public static final String ID_TYPEMONEY = "idtypemoney";
    public static final String ID_CATALOG = "idcatalog";
    public static final String MONEY_DESCRIPT = "descript";
    public static final String MONEY_DATE = "moneydate";
    public static final String MONEY_TIME = "moneytime";
    public static final String MONEY_PRICE = "price";
    public static final String MONEY_LOCATION = "location";
    public static final String MONEY_LAT = "lattitude";
    public static final String MONEY_LONG = "longtitude";
    public static final String MONEY_PATHIMAGE = "pathimage";

    public static final String TYPEMONEY_DB = "typemoney_db";
    public static final String TYPEMONEY_ID = "id";
    public static final String TYPEMONEY_NAME = "name";

    public static final String CATALOG_DB = "typelist_db";
    public static final String CATALOG_ID = "id";
    public static final String CATALOG_TYPEMONEYID = "idtypemoney";
    public static final String CATALOG_NAME = "name";
    public static final String CATALOG_IMAGE = "image";

    public static final String ACCOUNT_DB = "account";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_NAME = "name";
    public static final String ACCOUNT_IMAGE = "image";


    SQLiteDatabase myDatabase;
    DatabaseHelper myHelper;

    Context context;

    public DatabaseManagement() {
    }

    public DatabaseManagement(Context context) {
        this.context = context;
        myHelper = new DatabaseHelper(context);
        myDatabase = myHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        if (myDatabase.isOpen()) {
            myDatabase.close();
        }
    }

    public void addMoney(Money money) {
        ContentValues cv = getDataMoney(money);
        myDatabase.insert(MONEY_DB, null, cv);


    }

    public void updateMoney(Money money, String[] id) {
        ContentValues contentValues = getDataMoney(money);
        myDatabase.update(MONEY_DB, contentValues, MONEY_ID+"=?", id);

    }

    public void updateCatalogueMoney(int oldid,int newid){
        String[] newidarray={String.valueOf(oldid)};
        ContentValues contentValues=new ContentValues();
        contentValues.put(DatabaseHelper.ID_CATALOG,newid);
        myDatabase.update(MONEY_DB,contentValues,ID_CATALOG+"=?",newidarray);
    }

    public ContentValues getDataMoney(Money money) {
        ContentValues cv = new ContentValues();
        cv.put(MONEY_NAME, money.getStrName());
        cv.put(ID_TYPEMONEY, money.getMoneyType().getId());
        cv.put(ID_ACCOUNT, money.getAccount().getId());
        cv.put(ID_CATALOG, money.getCatalog().getId());
        cv.put(MONEY_DESCRIPT, money.getDescript());
        cv.put(MONEY_DATE,money.getDate());
        cv.put(MONEY_TIME,money.getTime());
        cv.put(MONEY_PRICE, money.getPrice());
        cv.put(MONEY_LOCATION, money.getNamelocation());
        cv.put(MONEY_LAT, money.getLattitude());
        cv.put(MONEY_LONG, money.getLongtitude());
        cv.put(MONEY_PATHIMAGE, money.getPathImage());
        return cv;
    }

    public void addCatalog(Catalog catalog) {
        ContentValues cv = new ContentValues();
        cv.put(CATALOG_NAME, catalog.getStrNameList());
        cv.put(CATALOG_TYPEMONEYID,catalog.getIdtypemoney());
        cv.put(CATALOG_IMAGE,catalog.getPathIcon());
        myDatabase.insert(CATALOG_DB, null, cv);
    }

    public void updateCatalog(Catalog catalog,int id){
        String[] idlist={String.valueOf(id)};
        ContentValues cv=new ContentValues();
        cv.put(CATALOG_NAME,catalog.getStrNameList());
        cv.put(CATALOG_IMAGE,catalog.getPathIcon());
        myDatabase.update(CATALOG_DB,cv,CATALOG_ID+"=?",idlist);
    }

    public void deleteCatalog(int idCatalog){
        String[] id={String.valueOf(idCatalog)};
        myDatabase.delete(DatabaseHelper.CATALOG_DB,DatabaseHelper.CATALOG_ID+"=?",id);
    }
    public void addAccount(Account account) {
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_NAME, account.getName());
        cv.put(ACCOUNT_IMAGE,account.getImage());
        myDatabase.insert(ACCOUNT_DB, null, cv);
    }

    public void updateAccount(Account account,int id){
        String [] wherearg ={String.valueOf(id)};
        ContentValues contentValues=new ContentValues();
        contentValues.put(ACCOUNT_NAME,account.getName());
        contentValues.put(ACCOUNT_IMAGE,account.getImage());
        myDatabase.update(ACCOUNT_DB,contentValues,ACCOUNT_ID+"=?",wherearg);

    }
    public void deleteAccount(Account account){
        String[] idAccount ={String.valueOf(account.getId())};
        myDatabase.delete(DatabaseHelper.ACCOUNT_DB,DatabaseHelper.ACCOUNT_ID+"=?",idAccount);

    }
    public void deleteMoney(int idmoney){
        String[] id={String.valueOf(idmoney)};
        myDatabase.delete(DatabaseHelper.MONEY_DB,DatabaseHelper.MONEY_ID+"=?",id);
    }



}
