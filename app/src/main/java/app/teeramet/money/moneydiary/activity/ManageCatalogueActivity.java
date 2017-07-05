package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import app.teeramet.money.moneydiary.ImageManage;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.adapter.CatalogExpanableListViewAdapter;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.DatabaseManagement;
import app.teeramet.money.moneydiary.database.ReadDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class ManageCatalogueActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;
    public static final String INCOME = "Income";
    public static final String OTHER = "Other";

    enum EDITMODE {EDIT, ADD, NONE}

    ;


    EDITMODE editmode = EDITMODE.NONE;

    EditText edtName;
    ImageButton imbIconCatalogue;
    ImageButton imbChangePic;
    ExpandableListView listCatalogue;
    Toolbar toolbar;

    Context context = this;
    ArrayList<Catalog> mCatalogIncomeArrayList = new ArrayList<>();
    ArrayList<Catalog> mCatalogExpenseArrayList = new ArrayList<>();
    CatalogExpanableListViewAdapter mCatalogExpanableListViewAdapter;
    String[] mHeader;
    ReadDatabase readDatabase;

    String defaultIcon = "otherb";
    String oldnamecat = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_catalogue);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listCatalogue = (ExpandableListView) findViewById(R.id.listcatalogue);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setCatalogExpenseArrayList();

        listCatalogue.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                int childId = ExpandableListView.getPackedPositionChild(id);
                int groupId = ExpandableListView.getPackedPositionGroup(id);

                deleteCatalog(childId, groupId);

                return true;
            }
        });

        listCatalogue.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                final int groupId = ExpandableListView.getPackedPositionGroup(id);
//                int childId = ExpandableListView.getPackedPositionChild(id);

                if (groupPosition == 0) {
                    editmode = EDITMODE.EDIT;
                    addAndEditCatalogue(mCatalogIncomeArrayList, childPosition, groupPosition);

                } else if (groupPosition == 1) {
                    editmode = EDITMODE.EDIT;
                    addAndEditCatalogue(mCatalogExpenseArrayList, childPosition, groupPosition);
                }
                return true;
            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backmenu:
                onBackPressed();
                break;
            case R.id.addcatalogue:
                dialogAddCatalogue();
                break;
        }
    }

    public void dialogAddCatalogue() {
        ReadDatabase readDatabase = new ReadDatabase(context);
        String[] moneyType = readDatabase.readDatabaseMoneyType();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Money type");
        alertDialog.setItems(moneyType, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    editmode = EDITMODE.ADD;
                    addAndEditCatalogue(null, 0, 0);
                } else {
                    editmode = EDITMODE.ADD;
                    addAndEditCatalogue(null, 0, 1);
                }
            }
        });
        alertDialog.create().show();
    }

    public void setCatalogExpenseArrayList() {
        readDatabase = new ReadDatabase(context);
        String where = DatabaseHelper.CATALOG_TYPEMONEYID + "=?";
        String[] wherearg = {"1"};
        mCatalogIncomeArrayList = readDatabase.readCatalog(where, wherearg);

        readDatabase = new ReadDatabase(context);
        String where1 = DatabaseHelper.CATALOG_TYPEMONEYID + "=?";
        String[] wherearg1 = {"2"};
        mCatalogExpenseArrayList = readDatabase.readCatalog(where1, wherearg1);

        readDatabase = new ReadDatabase(context);
        mHeader = readDatabase.readDatabaseMoneyType();
        HashMap<String, ArrayList<Catalog>> childlist = new HashMap<>();

        childlist.put(mHeader[0], mCatalogIncomeArrayList);
        childlist.put(mHeader[1], mCatalogExpenseArrayList);

        mCatalogExpanableListViewAdapter = new CatalogExpanableListViewAdapter(context, mHeader, childlist);
        listCatalogue.setAdapter(mCatalogExpanableListViewAdapter);
        for (int i = 0; i < mHeader.length; i++) {
            listCatalogue.expandGroup(i);
        }
    }

    //when delete catalogue
    public void reflecDatabaseMoney(Catalog catalog) {
        ReadDatabase readDatabase = new ReadDatabase(context);
        ArrayList<Catalog> catalogArrayList;
        String whereclause = DatabaseHelper.CATALOG_NAME + "=? OR " + DatabaseHelper.CATALOG_NAME + "=?";
        String[] wherearg = {INCOME, OTHER};
        catalogArrayList = readDatabase.readCatalog(whereclause, wherearg);

        if (catalog.getIdtypemoney() == 1) {
            DatabaseManagement databaseManagement = new DatabaseManagement(context);
            databaseManagement.updateCatalogueMoney(catalog.getId(), catalogArrayList.get(0).getId());
            databaseManagement.closeDatabase();
        } else if (catalog.getIdtypemoney() == 2) {
            DatabaseManagement databaseManagement = new DatabaseManagement(context);
            databaseManagement.updateCatalogueMoney(catalog.getId(), catalogArrayList.get(1).getId());
            databaseManagement.closeDatabase();
        }

    }

    public void deleteCatalog(final int childId, final int groupId) {

        if (groupId == 0 && childId >= 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Delete " + mCatalogIncomeArrayList.get(childId).getStrNameList() + " ?");
            alertDialog.setPositiveButton("Comfirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCatalogIncomeArrayList.get(childId).getId() == 3) {
                        Toast.makeText(context, "Do not delete catalogue '"+INCOME+"'", Toast.LENGTH_SHORT).show();
                    } else {
                        int idcatalog = mCatalogIncomeArrayList.get(childId).getId();
                        reflecDatabaseMoney(mCatalogIncomeArrayList.get(childId));

                        ReadDatabase readDatabase = new ReadDatabase(context);
                        String[] coloum = {DatabaseHelper.CATALOG_NAME};
                        String whereclause = DatabaseHelper.CATALOG_ID + "=?";
                        String[] wherearg = {String.valueOf(idcatalog)};
                        String namecat = readDatabase.readNameCatalogue(coloum, whereclause, wherearg);

                        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                        sharePreferenceHelper.removeStatusLayer(namecat);

                        DatabaseManagement databaseManagement = new DatabaseManagement(context);
                        databaseManagement.deleteCatalog(idcatalog);
                        databaseManagement.closeDatabase();
                        mCatalogIncomeArrayList.remove(childId);
                        mCatalogExpanableListViewAdapter.notifyDataSetChanged();


                    }
                }
            });
            alertDialog.setNegativeButton("Cancle", null);
            alertDialog.create().show();
        } else if (groupId == 1 && childId >= 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("Delete " + mCatalogExpenseArrayList.get(childId).getStrNameList() + " ?");
            alertDialog.setPositiveButton("Comfirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCatalogExpenseArrayList.get(childId).getId() == 16) {
                        Toast.makeText(context, "Do not delete catalogue '"+OTHER+"'", Toast.LENGTH_SHORT).show();
                    } else {
                        int idcatalog = mCatalogExpenseArrayList.get(childId).getId();

                        reflecDatabaseMoney(mCatalogExpenseArrayList.get(childId));

                        ReadDatabase readDatabase = new ReadDatabase(context);
                        String[] coloum = {DatabaseHelper.CATALOG_NAME};
                        String whereclause = DatabaseHelper.CATALOG_ID + "=?";
                        String[] wherearg = {String.valueOf(idcatalog)};
                        String namecat = readDatabase.readNameCatalogue(coloum, whereclause, wherearg);

                        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                        sharePreferenceHelper.removeStatusLayer(namecat);

                        DatabaseManagement databaseManagement = new DatabaseManagement(context);
                        databaseManagement.deleteCatalog(idcatalog);
                        databaseManagement.closeDatabase();
                        mCatalogExpenseArrayList.remove(childId);
                        mCatalogExpanableListViewAdapter.notifyDataSetChanged();

                    }
                }
            });
            alertDialog.setNegativeButton(getResources().getString(R.string.cancel), null);
            alertDialog.create().show();
        }

    }

    public void addAndEditCatalogue(ArrayList<Catalog> catalog, final int childId, final int groupId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view1 = inflater.inflate(R.layout.dialog_managecatalogue, null);

        edtName = (EditText) view1.findViewById(R.id.edt_namecatalogue);
        imbIconCatalogue = (ImageButton) view1.findViewById(R.id.edt_addimage);
        imbChangePic = (ImageButton) view1.findViewById(R.id.edt_changepic);
        ImageManage imageManage = new ImageManage();
        int resid = imageManage.randomColorBackground();
        imbIconCatalogue.setBackgroundResource(resid);

        if (catalog != null) {
            oldnamecat = catalog.get(childId).getStrNameList();
            edtName.setText(catalog.get(childId).getStrNameList());
            int resIdIcon = getResources().getIdentifier(catalog.get(childId).getPathIcon(), "drawable", getPackageName());
            imbIconCatalogue.setTag(catalog.get(childId).getPathIcon());
            Picasso.with(context).load(resIdIcon).into(imbIconCatalogue);
        }


        imbIconCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectIconCatalogueActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        imbChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectIconCatalogueActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        if (editmode == EDITMODE.ADD) {
            alert.setTitle("Add catalogue");
        } else if (editmode == EDITMODE.EDIT) {
            alert.setTitle("Update catalogue");
        }
        alert.setView(view1);
        alert.setPositiveButton("Comfirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edtName.getText().toString();

                if (groupId == 0) {

                    int id = mCatalogIncomeArrayList.get(childId).getId();

                    if (editmode == EDITMODE.ADD) {
                        if (name.equals("")) {
                            Toast.makeText(context, "Please fill in form name catalogue", Toast.LENGTH_SHORT).show();
                        } else {
                            Catalog catalog = new Catalog();
                            catalog.setIdtypemoney(1);
                            catalog.setStrNameList(name);
                            String nameIcon = (String) imbIconCatalogue.getTag();
                            if (nameIcon == null) {
                                nameIcon = defaultIcon;
                            }
                            catalog.setIcon(nameIcon);

                            SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                            sharePreferenceHelper.setStatusLayer(name, true);

                            DatabaseManagement databaseManagement = new DatabaseManagement(context);
                            databaseManagement.addCatalog(catalog);
                            databaseManagement.closeDatabase();

                            ReadDatabase readDatabase = new ReadDatabase(context);
                            String[] wherearg = {String.valueOf(name)};
                            ArrayList<Catalog> catalogs = readDatabase.
                                    readCatalog(DatabaseHelper.CATALOG_NAME + "=?", wherearg);

                            mCatalogIncomeArrayList.add(catalogs.get(0));
                        }
                    } else if (editmode == EDITMODE.EDIT) {
                        if (name.equals("")) {
                            Toast.makeText(context,"Please fill in form name catalogue", Toast.LENGTH_SHORT).show();
                        } else {
                            Catalog catalog = new Catalog();
                            catalog.setId(id);
                            catalog.setStrNameList(name);
                            String nameIcon = (String) imbIconCatalogue.getTag();
                            catalog.setIcon(nameIcon);

                            SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                            sharePreferenceHelper.removeStatusLayer(oldnamecat);
                            boolean value = sharePreferenceHelper.getStatusLayerAtPosition(oldnamecat);
                            sharePreferenceHelper.setStatusLayer(name, value);

                            DatabaseManagement databaseManagement = new DatabaseManagement(context);
                            databaseManagement.updateCatalog(catalog, id);
                            databaseManagement.closeDatabase();
                            mCatalogIncomeArrayList.set(childId, catalog);
                        }
                    }
                    mCatalogExpanableListViewAdapter.notifyDataSetChanged();
                } else if (groupId == 1) {
                    int id = mCatalogExpenseArrayList.get(childId).getId();

                    if (editmode == EDITMODE.ADD) {
                        if (name.equals("")) {
                            Toast.makeText(context, "Please fill in form name catalogue", Toast.LENGTH_SHORT).show();
                        } else {
                            Catalog catalog = new Catalog();
                            catalog.setIdtypemoney(2);
                            catalog.setStrNameList(name);
                            String nameIcon = (String) imbIconCatalogue.getTag();

                            if (nameIcon == null) {
                                nameIcon = defaultIcon;
                            }
                            catalog.setIcon(nameIcon);


                            SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                            sharePreferenceHelper.setStatusLayer(name, true);

                            DatabaseManagement databaseManagement = new DatabaseManagement(context);
                            databaseManagement.addCatalog(catalog);
                            databaseManagement.closeDatabase();

                            ReadDatabase readDatabase = new ReadDatabase(context);
                            String[] wherearg = {String.valueOf(name)};
                            ArrayList<Catalog> catalogs = readDatabase.
                                    readCatalog(DatabaseHelper.CATALOG_NAME + "=?", wherearg);

                            mCatalogExpenseArrayList.add(catalogs.get(0));
                        }
                    } else if (editmode == EDITMODE.EDIT) {
                        if (name.equals("")) {
                            Toast.makeText(context, "Please fill in form name catalogue", Toast.LENGTH_SHORT).show();
                        } else {
                            Catalog catalog = new Catalog();
                            catalog.setId(id);
                            catalog.setStrNameList(name);
                            String nameIcon = (String) imbIconCatalogue.getTag();
                            catalog.setIcon(nameIcon);

                            SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                            sharePreferenceHelper.removeStatusLayer(oldnamecat);
                            boolean value = sharePreferenceHelper.getStatusLayerAtPosition(oldnamecat);
                            sharePreferenceHelper.setStatusLayer(name, value);

                            DatabaseManagement databaseManagement = new DatabaseManagement(context);
                            databaseManagement.updateCatalog(catalog, id);
                            databaseManagement.closeDatabase();
                            mCatalogExpenseArrayList.set(childId, catalog);
                        }
                    }
                    mCatalogExpanableListViewAdapter.notifyDataSetChanged();
                }

            }
        });
        alert.setNegativeButton(getResources().getString(R.string.cancel), null);
        alert.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String nameIcon = data.getStringExtra("ICON");
            imbIconCatalogue.setTag(nameIcon);
            int resId = getResources().getIdentifier(nameIcon, "drawable", getPackageName());
            Picasso.with(context).load(resId).into(imbIconCatalogue);
        }
    }
}
