package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import app.teeramet.money.moneydiary.CheckConnectInternet;
import app.teeramet.money.moneydiary.ImageManage;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.adapter.DialogMenuLayerAdapter;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.ReadDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class MapShowActivity extends AppCompatActivity  {
    public static final String NAME = "NAME";
    public static final String PRICE = "PRICE";
    public static final String CATALOG = "CATALOG";
    public static final String DATE = "DATE";
    public static final String TIME = "TIME";
    public static final String DESCRIPT = "DESCRIPT";
    public static final String PATHNAME = "PATHNAME";
    public static final String LOCATIONNAME = "LOCATIONNAME";


    DecimalFormat moneyFormat = new DecimalFormat("#,##0.##");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    ArrayList<Money> mMoneyArrayList = new ArrayList<>();
    ArrayList<Catalog> mCatalogArrayList = new ArrayList<>();

    final SpatialReference wm = SpatialReference.create(102100);
    final SpatialReference egs = SpatialReference.create(4326);

    Context context = this;

    LocationDisplayManager mLocationDisplayManager;
    public static Point mLocation = null;
    Point nowLocation = new Point();

    MapView mapView;
    RelativeLayout relativeLayoutDetail;
    TextView tvLocation;
    TextView tvName;
    TextView tvPrice;
    TextView tvDate;
    TextView tvDescript;
    ImageView imageView;

    GraphicsLayer[] mGraphicsLayer;
    Graphic mGraphic;

    Animation slideup;
    Animation slidedown;

    Drawable mMarker;
    Drawable mMarker1;
    String mNamelocation;

    String[] mStrLayer;
    Boolean[] mCheckLayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);

        bindWidget();

        mLocationDisplayManager = mapView.getLocationDisplayManager();
        mLocationDisplayManager.setLocationListener(new myLocationChange());
        mLocationDisplayManager.start();
        mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);

        ArcGISTiledMapServiceLayer tiledlayer = new
                ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer");
        mapView.addLayer(tiledlayer);
        setMenuLayer();
        markPointOnMap();

        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {
                setSingleTap(v, v1);
            }
        });

        relativeLayoutDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        if (relativeLayoutDetail.getVisibility() == View.VISIBLE) {
                            relativeLayoutDetail.setAnimation(slidedown);
                            relativeLayoutDetail.setVisibility(View.INVISIBLE);
                            break;
                        }
                }
                return true;
            }
        });



    }

    public void bindWidget() {
        mapView = (MapView) findViewById(R.id.mapview);
        relativeLayoutDetail = (RelativeLayout) findViewById(R.id.detailform);
        tvLocation=(TextView) findViewById(R.id.location);
        tvName = (TextView) findViewById(R.id.name);
        tvPrice = (TextView) findViewById(R.id.price);
        tvDate = (TextView) findViewById(R.id.date);
        tvDescript = (TextView) findViewById(R.id.descript);
        imageView = (ImageView) findViewById(R.id.image);
        slideup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slideup);
        slidedown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slidedown);
    }

    public void markPointOnMap() {
        ReadDatabase readDatabase = new ReadDatabase(context);
        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        int idAccount = sharePreferenceHelper.getIdAccountPreference();

        String querydata = "SELECT * FROM "+ DatabaseHelper.MONEY_DB +" WHERE "+ DatabaseHelper.ID_ACCOUNT +"=?";
        String[] whereArg = {String.valueOf(idAccount)};
        mMoneyArrayList = readDatabase.readDatabaseMoney(querydata, whereArg);

        if (mMoneyArrayList.size() > 0) {
            for (int i = 0; i < mMoneyArrayList.size(); i++) {
                if (mMoneyArrayList.get(i).getLongtitude() != 0
                        && mMoneyArrayList.get(i).getLattitude() != 0
                        && mMoneyArrayList.get(i).getNamelocation() != null
                        && mMoneyArrayList.get(i).getCatalog().getId() != 0) {
                    //get pathIcon set marker
                    readDatabase = new ReadDatabase(context);
                    String whereclause = DatabaseHelper.CATALOG_ID + "=?";
                    String[] wherearg = {String.valueOf(mMoneyArrayList.get(i).getCatalog().getId())};
                    String pathIcon = readDatabase.readCatalog(whereclause, wherearg).get(0).getPathIcon();

                    int resId = getResources().getIdentifier(pathIcon, "drawable", getPackageName());

                    int idcat = mMoneyArrayList.get(i).getCatalog().getId();
                    readDatabase = new ReadDatabase(context);
                    ArrayList<Catalog> catalogs = readDatabase.readCatalog(DatabaseHelper.CATALOG_ID + "=?", new String[]{String.valueOf(idcat)});


                    String name = mMoneyArrayList.get(i).getStrName();
                    String price = String.valueOf(mMoneyArrayList.get(i).getPrice());
                    String namecatalog = catalogs.get(0).getStrNameList();
                    String strdate = String.valueOf(mMoneyArrayList.get(i).getDate());
                    String strtime = String.valueOf(mMoneyArrayList.get(i).getTime());
                    String descript = mMoneyArrayList.get(i).getDescript();
                    String pathname = mMoneyArrayList.get(i).getPathImage();
                    mNamelocation = mMoneyArrayList.get(i).getNamelocation();
                    double lattitude = mMoneyArrayList.get(i).getLattitude();
                    double longtitude = mMoneyArrayList.get(i).getLongtitude();

                    Point point = new Point(longtitude, lattitude);
                    Point mercator = (Point) GeometryEngine.project(point, egs, wm);  //convert webmercator
                    mGraphic = setPointOnMap(mercator, resId, R.drawable.marker);


                    String strId = String.valueOf(mMoneyArrayList.get(i).getCatalog().getId());

                    int position = -1;
                    for (int j = 0; j < mGraphicsLayer.length; j++) {
                        //loop find name equal if name not equal position = -1
                        if (mGraphicsLayer[j].getName().equals(strId)) {
                            position = j;
                        }
                    }
                    if (position != -1) {
                        mGraphicsLayer[position].addGraphic(mGraphic);
                        int[] graphicId = new int[1];

                        if (mGraphicsLayer[position].getGraphicIDs() != null) {
                            graphicId = mGraphicsLayer[position].getGraphicIDs();
                        }
                        if (graphicId.length > 0) {
                            mGraphic = mGraphicsLayer[position].getGraphic(graphicId[0]);
                            if (mGraphic != null) {
                                int uid = mGraphic.getUid();
                                Map<String, Object> attribute = mGraphic.getAttributes();
                                attribute.put(LOCATIONNAME, mNamelocation);
                                attribute.put(NAME, name);
                                attribute.put(PRICE, String.valueOf(price));
                                attribute.put(CATALOG, namecatalog);
                                attribute.put(DATE, strdate);
                                attribute.put(TIME, strtime);
                                attribute.put(DESCRIPT, descript);
                                attribute.put(PATHNAME, pathname);

                                mGraphicsLayer[position].updateGraphic(uid, attribute);
                            }

                        }
                    }
                }

            }


        }

    }

    public Graphic setPointOnMap(Point mercator, int resId, int resId1) {
        mMarker = resizeDrawable(resId, 30, 30);
        mMarker1 = resizeDrawable(resId1, 56, 88);

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mMarker);
        PictureMarkerSymbol pictureMarkerSymbol1 = new PictureMarkerSymbol(mMarker1);
        pictureMarkerSymbol1.setOffsetY(-22);
        CompositeSymbol compositeSymbol = new CompositeSymbol();

        compositeSymbol.add(pictureMarkerSymbol1);
        compositeSymbol.add(pictureMarkerSymbol);

        Graphic graphic = new Graphic(mercator, compositeSymbol);
        return graphic;

    }

    public Drawable resizeDrawable(int resId, int width, int height) {
        Drawable drawable = getResources().getDrawable(resId);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmapresize = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return new BitmapDrawable(getResources(), bitmapresize);
    }


    private class myLocationChange implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }
            boolean zoomToMe = (mLocation == null);
            mLocation = new Point(location.getLongitude(), location.getLatitude());
            if (zoomToMe) {
                Point p = (Point) GeometryEngine.project(mLocation, egs, wm);
                mapView.zoomToResolution(p, 0.0);
                nowLocation = (Point) GeometryEngine.project(mLocation, egs, wm);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(context, "GPS Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(context, "GPS Disabled", Toast.LENGTH_SHORT).show();
        }

    } //myLocation

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_mylocation:
                if (mLocation != null) {
                    Point p = (Point) GeometryEngine.project(mLocation, egs, wm);
                    mapView.zoomToResolution(p, 5);

                }
                break;
            case R.id.btn_zoomin:
                mapView.zoomin();
                break;
            case R.id.btn_zoomout:
                mapView.zoomout();
                break;
            case R.id.backmenu:
                onBackPressed();
                break;
            case R.id.layermenu:
                dialogShowAndHideLayer();
                break;
            case R.id.close:
                if (relativeLayoutDetail.getVisibility() == View.VISIBLE) {
                    relativeLayoutDetail.startAnimation(slidedown);
                    relativeLayoutDetail.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    public void setSingleTap(float x, float y) {
        if (x != 0 && y != 0) {
            Point point = mapView.toMapPoint(x, y);

            int[] idGraphic = new int[1];
            int[] id;
            int position = -1;
            for (int i = 0; i < mGraphicsLayer.length; i++) {
                id = mGraphicsLayer[i].getGraphicIDs(x, y, 20, 1);
                if (id.length > 0) {
                    idGraphic = id;
                    position = i;
                }
            }
            if (position > -1 && idGraphic.length > 0) {
                mapView.centerAt(point, true);

                Graphic graphic = mGraphicsLayer[position].getGraphic(idGraphic[0]);
                String locationname = String.valueOf(graphic.getAttributeValue(LOCATIONNAME));
                String name = String.valueOf(graphic.getAttributeValue(NAME));
                String price = String.valueOf(graphic.getAttributeValue(PRICE));
                String namecatlog = String.valueOf(graphic.getAttributeValue(CATALOG));
                String strdate = String.valueOf(graphic.getAttributeValue(DATE));
                String strtime = String.valueOf(graphic.getAttributeValue(TIME));
                String descript = String.valueOf(graphic.getAttributeValue(DESCRIPT));
                String pathname = String.valueOf(graphic.getAttributeValue(PATHNAME));

                Date date = new Date(Long.parseLong(strdate));
                Time time = new Time(Long.parseLong(strtime));

                tvLocation.setText("Location : "+locationname);
                tvName.setText(name);
                tvPrice.setText(moneyFormat.format(Double.parseDouble(price)));
                tvDate.setText(dateFormat.format(date.getTime()) + " ," + timeFormat.format(time.getTime()));
                tvDescript.setText(descript);

                ImageManage imageManage = new ImageManage();
                Transformation transformation = imageManage.tranform();
                File file = new File(pathname);
                if (file.exists()) {
                    imageView.setPadding(0, 0, 0, 0);
                    Picasso.with(context).load(file).centerCrop().fit().transform(transformation).into(imageView);
                } else {
                    imageView.setPadding(12, 12, 12, 12);
                    Picasso.with(context).load(R.drawable.otherb).transform(transformation).into(imageView);
                }

                if (relativeLayoutDetail.getVisibility() == View.INVISIBLE) {
                    relativeLayoutDetail.startAnimation(slideup);
                    relativeLayoutDetail.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void setMenuLayer() {
        ArrayList<Catalog> incomeArrayList;
        ArrayList<Catalog> expenseArraylist;

        ReadDatabase readDatabase = new ReadDatabase(context);
        incomeArrayList = readDatabase.readCatalog(DatabaseHelper.CATALOG_TYPEMONEYID + "=?", new String[]{String.valueOf(1)});
        int count1 = incomeArrayList.size();

        readDatabase = new ReadDatabase(context);
        expenseArraylist = readDatabase.readCatalog(DatabaseHelper.CATALOG_TYPEMONEYID + "=?", new String[]{String.valueOf(2)});
        int count2 = expenseArraylist.size();

        int sumcount = count1 + count2;
        mCheckLayer = new Boolean[sumcount];
        mStrLayer = new String[sumcount];
        mGraphicsLayer = new GraphicsLayer[sumcount];
        String[] idcatalog = new String[sumcount];

        int position = 0;
        for (int i = 0; i < sumcount; i++) {
            if (i < count1) {
                mCatalogArrayList.add(incomeArrayList.get(i));
                idcatalog[i] = String.valueOf(incomeArrayList.get(i).getId());
            } else if (i >= count1) {
                mCatalogArrayList.add(expenseArraylist.get(position));
                idcatalog[i] = String.valueOf(expenseArraylist.get(position).getId());
                position++;
            }
        }

        for (int j = 0; j < sumcount; j++) {
            mStrLayer[j] = mCatalogArrayList.get(j).getStrNameList();
        }


        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        Boolean[] checklayers = sharePreferenceHelper.getStatusLayer(mStrLayer);

        for (int i = 0; i < sumcount; i++) {
            mGraphicsLayer[i] = new GraphicsLayer();
            if (checklayers != null) {
                mCheckLayer[i] = checklayers[i];
            }

            String strId = idcatalog[i];
            mGraphicsLayer[i].setName(strId);
            mapView.addLayer(mGraphicsLayer[i]);
        }

    }

    public void dialogShowAndHideLayer() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Select Layer");
        dialog.setCancelable(true);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View viewdialog = layoutInflater.inflate(R.layout.listview_layer, null);
        ListView listViewmenu = (ListView) viewdialog.findViewById(R.id.layerlist);
        final DialogMenuLayerAdapter dialogMenuLayerAdapter = new DialogMenuLayerAdapter(context, mCatalogArrayList, mCheckLayer);
        listViewmenu.setAdapter(dialogMenuLayerAdapter);

        dialog.setView(viewdialog);

        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCheckLayer = dialogMenuLayerAdapter.getChecklayer();

                for (int i = 0; i < mCheckLayer.length; i++) {
                    if (mCheckLayer[i]) {
                        mGraphicsLayer[i].setVisible(true);
                    } else {
                        mGraphicsLayer[i].setVisible(false);
                    }
                }

                SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                for (int i = 0; i < mStrLayer.length; i++) {
                    sharePreferenceHelper.setStatusLayer(mStrLayer[i], mCheckLayer[i]);
                }
                dialogMenuLayerAdapter.notifyDataSetChanged();

            }
        });
        dialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mCheckLayer();
            }
        });
        dialog.create().show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.unpause();

        final String statusnet = CheckConnectInternet.getConnectStatusString(context);
        if (statusnet.equals(CheckConnectInternet.STATUS_MOBILE) || statusnet.equals(CheckConnectInternet.STATUS_WIFI)) {

            Layer[] layer = mapView.getLayers();

            for (int i = 0; i < layer.length; i++) {
                layer[i].reinitializeLayer(null);
            }

        } else if (statusnet.equals(CheckConnectInternet.STATUS_NOT)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("No Internet Connection ");
            alertDialog.setMessage("Please connect to internet.");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override

                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.create().show();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.pause();
    }
}
