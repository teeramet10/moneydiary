package app.teeramet.money.moneydiary.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.classmoney.Expenses;
import app.teeramet.money.moneydiary.classmoney.Income;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.classmoney.TypeMoney;

import java.util.ArrayList;

/**
 * Created by barbie on 24/8/2559.
 */
public class ReadDatabase {
    DatabaseHelper myHelper;
    SQLiteDatabase myDatabase;
    ArrayList<Money> moneyArrayList;
    Context context;

    public ReadDatabase(Context context) {
        this.context = context;
        myHelper = new DatabaseHelper(context);
        myDatabase = myHelper.getReadableDatabase();
    }


    public ArrayList<Money> readDatabaseMoney(String querydata, String[] where) {
        DatabaseHelper myHelper = new DatabaseHelper(context);
        SQLiteDatabase myDatabase = myHelper.getReadableDatabase();
        moneyArrayList = new ArrayList<>();

        Cursor mCursor = myDatabase.rawQuery(querydata, where);
        int idIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_ID);
        int nameIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_NAME);
        int idAccountIndex = mCursor.getColumnIndex(DatabaseHelper.ID_ACCOUNT);
        int idtListIndex = mCursor.getColumnIndex(DatabaseHelper.ID_CATALOG);
        int idtMoneyIndex = mCursor.getColumnIndex(DatabaseHelper.ID_TYPEMONEY);
        int descriptIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_DESCRIPT);
        int idPrice = mCursor.getColumnIndex(DatabaseHelper.MONEY_PRICE);
        int timeIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_TIME);
        int dateIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_DATE);
        int locationIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_LOCATION);
        int latIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_LAT);
        int longIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_LONG);
        int pathIndex = mCursor.getColumnIndex(DatabaseHelper.MONEY_PATHIMAGE);


        while (mCursor.moveToNext()) {
            Money income = new Income();
            Money expenses = new Expenses();
            Account account = new Account();
            TypeMoney typeMoney = new TypeMoney();
            Catalog catalog = new Catalog();

            if (mCursor.getInt(idtMoneyIndex) == 1) {
                account.setId(mCursor.getInt(idAccountIndex));
                catalog.setId(mCursor.getInt(idtListIndex));
                catalog.setStrNameList(mCursor.getString(idtListIndex));
                typeMoney.setId(mCursor.getInt(idtMoneyIndex));

                income.setId(mCursor.getInt(idIndex));
                income.setStrName(mCursor.getString(nameIndex));
                income.setAccount(account);
                income.setCatalog(catalog);
                income.setMoneyType(typeMoney);
                income.setPrice(mCursor.getDouble(idPrice));
                income.setDescript(mCursor.getString(descriptIndex));
                income.setDate(mCursor.getLong(dateIndex));
                income.setTime(mCursor.getLong(timeIndex));
                income.setNamelocation(mCursor.getString(locationIndex));
                income.setLattitude(mCursor.getDouble(latIndex));
                income.setLongtitude(mCursor.getDouble(longIndex));
                income.setPathImage(mCursor.getString(pathIndex));

                if (income instanceof Money) {
                    moneyArrayList.add(income);
                }

            } else {
                account.setId(mCursor.getInt(idAccountIndex));
                catalog.setId(mCursor.getInt(idtListIndex));
                typeMoney.setId(mCursor.getInt(idtMoneyIndex));

                expenses.setId(mCursor.getInt(idIndex));
                expenses.setStrName(mCursor.getString(nameIndex));
                expenses.setAccount(account);
                expenses.setCatalog(catalog);
                expenses.setMoneyType(typeMoney);
                expenses.setPrice(mCursor.getDouble(idPrice));
                expenses.setDescript(mCursor.getString(descriptIndex));
                expenses.setDate(mCursor.getLong(dateIndex));
                expenses.setTime(mCursor.getLong(timeIndex));
                expenses.setNamelocation(mCursor.getString(locationIndex));
                expenses.setLattitude(mCursor.getDouble(latIndex));
                expenses.setLongtitude(mCursor.getDouble(longIndex));
                expenses.setPathImage(mCursor.getString(pathIndex));
                if (expenses instanceof Expenses) {
                    moneyArrayList.add(expenses);
                }
            }

        }
        mCursor.close();
        myDatabase.close();

        return moneyArrayList;
    }

    public ArrayList<Catalog> readCatalog(String where, String[] wherearg) {
        Cursor mCursor = myDatabase.query(DatabaseHelper.CATALOG_DB, null, where, wherearg, null, null, null);
        ArrayList<Catalog> catalogs = new ArrayList<>();

        int catalogidIndex = mCursor.getColumnIndex(DatabaseHelper.CATALOG_ID);
        int catalognameIndex = mCursor.getColumnIndex(DatabaseHelper.CATALOG_NAME);
        int catalogtypemoney = mCursor.getColumnIndex(DatabaseHelper.CATALOG_TYPEMONEYID);
        int catalogicon = mCursor.getColumnIndex(DatabaseHelper.CATALOG_IMAGE);

        while (mCursor.moveToNext()) {
            Catalog catalog = new Catalog();
            catalog.setId(mCursor.getInt(catalogidIndex));
            catalog.setStrNameList(mCursor.getString(catalognameIndex));
            catalog.setIdtypemoney(mCursor.getInt(catalogtypemoney));
            catalog.setIcon(mCursor.getString(catalogicon));
            catalogs.add(catalog);
        }
        mCursor.close();
        closeDatabase();
        return catalogs;
    }

    public void closeDatabase() {
        if (myDatabase.isOpen()) {
            myDatabase.close();
        }
    }

    public String readNameCatalogue(String[] coloum, String whereclause, String[] wherearg) {
        Cursor mCursor = myDatabase.query(DatabaseHelper.CATALOG_DB, coloum, whereclause, wherearg, null, null, null);
        int nameIndex = mCursor.getColumnIndex(coloum[0]);
        Catalog catalog = null;
        while (mCursor.moveToNext()) {
            catalog = new Catalog();
            catalog.setStrNameList(mCursor.getString(nameIndex));
        }
        if (catalog != null) {
            String nameCatalogue = catalog.getStrNameList();
            return nameCatalogue;
        }

        mCursor.close();
        closeDatabase();
        return "";
    }

    public String[] readDatabaseMoneyType() {
        String[] moneyType = new String[2];
        DatabaseHelper myHelper = new DatabaseHelper(context);
        SQLiteDatabase myDatabase = myHelper.getReadableDatabase();
        Cursor mCursor = myDatabase.query(DatabaseHelper.TYPEMONEY_DB, null, null, null, null, null, null);
        int typemoney = mCursor.getColumnIndex(DatabaseHelper.TYPEMONEY_NAME);
        int i = 0;
        while (mCursor.moveToNext()) {
            moneyType[i] = mCursor.getString(typemoney);
            i++;
        }
        return moneyType;
    }

    public ArrayList<Money> readDatabaseMoneyForCal(String whereclause, String[] wherearg) {
        ArrayList<Money> moneyArrayList = new ArrayList<>();

        String[] coloum = {DatabaseHelper.ID_ACCOUNT, DatabaseHelper.ID_TYPEMONEY, DatabaseHelper.MONEY_PRICE};

        Cursor mCursor = myDatabase.query(DatabaseHelper.MONEY_DB, coloum, whereclause, wherearg, null, null, null);

        int idAccountIndex = mCursor.getColumnIndex(DatabaseHelper.ID_ACCOUNT);
        int idtMoneyIndex = mCursor.getColumnIndex(DatabaseHelper.ID_TYPEMONEY);
        int idPrice = mCursor.getColumnIndex(DatabaseHelper.MONEY_PRICE);


        while (mCursor.moveToNext()) {
            Money income = new Income();
            Money expenses = new Expenses();
            Account account = new Account();
            TypeMoney typeMoney = new TypeMoney();
            if (mCursor.getInt(idtMoneyIndex) == 1) {
                account.setId(mCursor.getInt(idAccountIndex));
                typeMoney.setId(mCursor.getInt(idtMoneyIndex));

                income.setAccount(account);
                income.setMoneyType(typeMoney);
                income.setPrice(mCursor.getDouble(idPrice));
                moneyArrayList.add(income);
            } else {
                account.setId(mCursor.getInt(idAccountIndex));
                typeMoney.setId(mCursor.getInt(idtMoneyIndex));

                expenses.setAccount(account);
                expenses.setMoneyType(typeMoney);
                expenses.setPrice(mCursor.getDouble(idPrice));

                moneyArrayList.add(expenses);
            }

        }
        myDatabase.close();
        mCursor.close();
        return moneyArrayList;
    }

    public ArrayList<Account> readDatabaseAccount(String whereclause, String[] wherearg) {
        myHelper = new DatabaseHelper(context);
        myDatabase = myHelper.getReadableDatabase();
        Cursor mCursor = myDatabase.query(DatabaseHelper.ACCOUNT_DB, null, whereclause, wherearg, null, null, null);
        ArrayList<Account> accountArrayList = new ArrayList<>();
        int idAccountIndex = mCursor.getColumnIndex(DatabaseHelper.ACCOUNT_ID);
        int nameAccountIndex = mCursor.getColumnIndex(DatabaseHelper.ACCOUNT_NAME);
        int imageAccountIndex = mCursor.getColumnIndex(DatabaseHelper.ACCOUNT_IMAGE);
        while (mCursor.moveToNext()) {
            Account account = new Account(mCursor.getInt(idAccountIndex), mCursor.getString(nameAccountIndex));
            account.setImage(mCursor.getString(imageAccountIndex));
            accountArrayList.add(account);
        }
        mCursor.close();
        myDatabase.close();
        return accountArrayList;
    }

    public ArrayList<Long> readDateMoney() {
        myHelper = new DatabaseHelper(context);
        myDatabase = myHelper.getReadableDatabase();

        String[] coloum = new String[]{DatabaseHelper.MONEY_DATE};
        Cursor mCursor = myDatabase.query(DatabaseHelper.MONEY_DB, coloum, null, null, null, null, null);
        int dateMoney = mCursor.getColumnIndex(DatabaseHelper.MONEY_DATE);
        ArrayList<Long> datelist = new ArrayList<>();
        while (mCursor.moveToNext()) {
            datelist.add(mCursor.getLong(dateMoney));
        }
        mCursor.close();
        myDatabase.close();
        return datelist;
    }
}
