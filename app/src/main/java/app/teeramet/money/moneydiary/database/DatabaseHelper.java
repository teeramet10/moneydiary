package app.teeramet.money.moneydiary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by barbie on 17/8/2559.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
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

    public static final int version = 1;

    public static final String create_db = "CREATE TABLE " + MONEY_DB + "(" + MONEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MONEY_NAME + " TEXT NOT NULL,"
            + ID_ACCOUNT + " INTEGER NOT NULL,"
            + ID_TYPEMONEY + " INTEGER NOT NULL,"
            + ID_CATALOG + " INTEGER NOT NULL,"
            + MONEY_DESCRIPT + " TEXT,"
            + MONEY_DATE + " LONG,"
            + MONEY_TIME + " LONG,"
            + MONEY_PRICE + " DOUBLE NOT NULL,"
            + MONEY_LOCATION + " TEXT,"
            + MONEY_LAT + " DOUBLE,"
            + MONEY_LONG + " DOUBLE,"
            + MONEY_PATHIMAGE + " TEXT  );";

    public static final String create_db_typemoney = "CREATE TABLE " + TYPEMONEY_DB + "(" + TYPEMONEY_ID + " INTEGER PRIMARY KEY,"
            + TYPEMONEY_NAME + " TEXT NOT NULL );";

    public static final String create_db_catalogue = "CREATE TABLE " + CATALOG_DB + "(" + CATALOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CATALOG_NAME + " TEXT NOT NULL,"
            + CATALOG_TYPEMONEYID + " INTEGER NOT NULL,"
            + CATALOG_IMAGE + " TEXT  );";


    public static final String create_db_account = "CREATE TABLE " + ACCOUNT_DB + "(" + ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ACCOUNT_NAME + " TEXT NOT NULL ,"
            + ACCOUNT_IMAGE + " TEXT);";

    int[] idTypemoney = {1, 2};
    String[] nameTypemoney = {"INCOME", "EXPENSE"};

    int[] idCatalog = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    String[] nameTypelist = {"Salary", "Bank", "Income", "Food", "Drink", "Travel", "Education", "Transport"
            , "Tax", "Rent", "Vehicle", "Shopping", "Gift", "Cosmetic", "Health", "Other"};
    int[] typeMoneyCatalog = {1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

    String[] imageCatalog = {"salaryb","profitb","bankb","foodb"
            , "drinkb", "travelb", "educationb", "transportb"
            , "taxb","rentb","vehicleb", "shoppingb", "giftb",
            "cosmeticb", "healthb", "otherb"};

    public DatabaseHelper(Context context) {
        super(context, MONEY_DB, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_db);
        db.execSQL(create_db_typemoney);
        db.execSQL(create_db_catalogue);
        db.execSQL(create_db_account);

        ContentValues contentValues = new ContentValues();

        for (int i = 0; i < idTypemoney.length; i++) {
            contentValues.put(TYPEMONEY_ID, idTypemoney[i]);
            contentValues.put(TYPEMONEY_NAME, nameTypemoney[i]);
            db.insert(TYPEMONEY_DB, null, contentValues);
        }
        contentValues = new ContentValues();
        for (int i = 0; i < idCatalog.length; i++) {
            contentValues.put(CATALOG_ID, idCatalog[i]);
            contentValues.put(CATALOG_NAME, nameTypelist[i]);
            contentValues.put(CATALOG_TYPEMONEYID, typeMoneyCatalog[i]);
            contentValues.put(CATALOG_IMAGE, imageCatalog[i]);
            long id = db.insert(CATALOG_DB, null, contentValues);
        }

        contentValues = new ContentValues();
        contentValues.put(ACCOUNT_ID, 1);
        contentValues.put(ACCOUNT_NAME, "My Account");
        String path= "";
        contentValues.put(ACCOUNT_IMAGE,path);
        long i = db.insert(ACCOUNT_DB, null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MONEY_DB);
        db.execSQL("DROP TABLE IF EXISTS" + TYPEMONEY_DB);
        db.execSQL("DROP TABLE IF EXISTS" + CATALOG_DB);
        db.execSQL("DROP TABLE IF EXISTS" + ACCOUNT_DB);
        onCreate(db);
    }
}
