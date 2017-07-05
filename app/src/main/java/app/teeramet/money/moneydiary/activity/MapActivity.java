package app.teeramet.money.moneydiary.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
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
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.ReadDatabase;

import java.util.ArrayList;
import java.util.Map;

public class MapActivity extends AppCompatActivity {
    public static final int ZOOMCENTER = 18;

    MapView mapView;
    //    Callout callout;
    EditText edtData;
    Button btnOk;
    Button btnCancle;
    ImageButton imbBackmenu;
    ImageButton imbReturn;
    ImageButton imbAddPoint;
    TextView tvTitle;
    Toolbar toolbar;

    GraphicsLayer mGraphicsLayer;
    Drawable mMarker;

    LocationDisplayManager mLocationDisplayManager;

    public static Point mLocation = null;
    Point nowLocation = new Point();

    Context context = this;

    int[] mGraphicIDs;

    final SpatialReference wm = SpatialReference.create(102100);
    final SpatialReference egs = SpatialReference.create(4326);


    float X, Y;
    double mLatti;
    double mLongti;
    String mLocationname;

    Point mPointLatlong;
    Point mPointMercator;
    Graphic mGraphic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        bindWidget();

        mLocationDisplayManager = mapView.getLocationDisplayManager();
        mLocationDisplayManager.setLocationListener(new myLocationChange());
        mLocationDisplayManager.start();
        mLocationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);

        imbBackmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArcGISTiledMapServiceLayer tiledlayer = new ArcGISTiledMapServiceLayer
                ("http://services.arcgisonline.com/arcgis/rest/services/World_Street_Map/MapServer");
        mGraphicsLayer = new GraphicsLayer();

        mapView.addLayer(tiledlayer);
        mapView.addLayer(mGraphicsLayer);

        //current location

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            double latti = bundle.getDouble("LAT", 0);
            double longti = bundle.getDouble("LONG", 0);
            String location = bundle.getString("LOCATION");
            int idcatalog = bundle.getInt("CATALOG", 16);

            setPointWhenUpdate(longti, latti, location, idcatalog);
        }

        imbReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.setOnSingleTapListener(null);
                if (imbReturn.isShown()) {
                    Animation fadeout = AnimationUtils.loadAnimation(context, R.anim.fadeout);

                    imbReturn.setAnimation(fadeout);
                    imbReturn.setVisibility(View.INVISIBLE);

                    imbAddPoint.setImageResource(R.drawable.marker48w);
                    tvTitle.setText("Add location");
                }
//                if (callout.isShowing()) {
//                    callout.hide();
//                }
            }
        });
    }

    public void bindWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mapView = (MapView) findViewById(R.id.mapview);
        imbBackmenu = (ImageButton) findViewById(R.id.backmenu);
        imbReturn = (ImageButton) findViewById(R.id.backevent);
        imbAddPoint = (ImageButton) findViewById(R.id.btn_addpoint);
        tvTitle = (TextView) findViewById(R.id.title);
    }

    public void addDataOnMap(float x, float y) {
        if (x != 0 && y != 0) {
            Point point = mapView.toMapPoint(x, y);
            if(point != null) {
                this.X = x;
                this.Y = y;
                //latlong
                mPointLatlong = (Point) GeometryEngine.project(point, mapView.getSpatialReference(), egs);
                mLatti = mPointLatlong.getY();
                mLongti = mPointLatlong.getX();

                //mercator
                Point point2 = new Point(mLongti, mLatti);
                mPointMercator = (Point) GeometryEngine.project(point2, egs, wm);
                mPointMercator.getX();
                mPointMercator.getY();


                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View view = inflater.inflate(R.layout.dialog_addpoint, null);
                edtData = (EditText) view.findViewById(R.id.edt_callout);
                btnOk = (Button) view.findViewById(R.id.btn_ok);
                btnCancle = (Button) view.findViewById(R.id.btn_cancel);

                //TODO DIALOG
                mapView.centerAndZoom(mLatti, mLongti, ZOOMCENTER);
                final Dialog dialogaddpoint = new Dialog(context);
                dialogaddpoint.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialogaddpoint.setContentView(view);
                dialogaddpoint.setCancelable(false);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLocationname = edtData.getText().toString();
                        if (mLocationname.length() == 0 || mLocationname.equals("")) {
                            Toast.makeText(context, "Please fill in form", Toast.LENGTH_SHORT).show();
                        } else {
                            if (mPointMercator != null) {
                                mMarker = resizeDrawable(R.drawable.marker, 56, 88);
                                PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(mMarker);
                                mGraphic = new Graphic(mPointMercator, pictureMarkerSymbol);
                                int a = mGraphicsLayer.addGraphic(mGraphic);

                                mGraphicIDs = mGraphicsLayer.getGraphicIDs(X, Y, 10, 1);

                                if (mGraphicIDs.length > 0) {
                                    mGraphic = mGraphicsLayer.getGraphic(mGraphicIDs[0]);
                                    int uid = mGraphic.getUid();

                                    Map<String, Object> attribute = mGraphic.getAttributes();
                                    attribute.put("NAME", mLocationname);
                                    mGraphicsLayer.updateGraphic(uid, attribute);

                                }

                                Intent intent = new Intent();
                                intent.putExtra("LAT", mLatti);
                                intent.putExtra("LONG", mLongti);
                                intent.putExtra("LOCATION", mLocationname);
                                setResult(AddDataActivity.RESULT_OK, intent);
                                finish();
                            }
                        }
                    }
                });
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogaddpoint.dismiss();
                    }
                });

                dialogaddpoint.show();
            }
        }else{
            AlertDialog.Builder alertDialog= new AlertDialog.Builder(context);
            alertDialog.setTitle("No Internet Connection ");
            alertDialog.setMessage("Please connect to internet.");
            alertDialog.create().show();

        }
    }

    public void setPointWhenUpdate(double longtitude, double lattitude, String namelocation, int idcatalog) {
        Point point = new Point(longtitude, lattitude);
        Point mercator = (Point) GeometryEngine.project(point, egs, wm);  //convert webmercator

        mapView.centerAndZoom(lattitude, longtitude, ZOOMCENTER);

        String whereclause = DatabaseHelper.CATALOG_ID + "=?";
        String[] wherearg = {String.valueOf(idcatalog)};
        ReadDatabase readDatabase = new ReadDatabase(context);
        ArrayList<Catalog> catalogs = readDatabase.readCatalog(whereclause, wherearg);
        String namecat = catalogs.get(0).getPathIcon();
        int resId = context.getResources().getIdentifier(namecat, "drawable", getPackageName());

        mGraphic = setPointOnMap(mercator, resId, R.drawable.marker);
        mGraphicsLayer.addGraphic(mGraphic);

        int[] graphicId = mGraphicsLayer.getGraphicIDs();

        if (graphicId.length > 0) {
//                            int index =graphicId.length-1;
            mGraphic = mGraphicsLayer.getGraphic(graphicId[0]);
            int uid = mGraphic.getUid();
            Map<String, Object> attribute = mGraphic.getAttributes();
            attribute.put("NAME", namelocation);
            mGraphicsLayer.updateGraphic(uid, attribute);
        }
    }

    public Graphic setPointOnMap(Point mercator, int resId, int resId1) {
        Drawable Marker = resizeDrawable(resId, 30, 30);
        Drawable Marker1 = resizeDrawable(resId1, 56, 88);

        PictureMarkerSymbol pictureMarkerSymbol = new PictureMarkerSymbol(Marker);
        PictureMarkerSymbol pictureMarkerSymbol1 = new PictureMarkerSymbol(Marker1);
        pictureMarkerSymbol1.setOffsetY(-23);
        CompositeSymbol compositeSymbol = new CompositeSymbol();

        compositeSymbol.add(pictureMarkerSymbol1);
        compositeSymbol.add(pictureMarkerSymbol);

        Graphic graphic = new Graphic(mercator, compositeSymbol);
        return graphic;

    }

    public void eventMap() {
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                if(x!=0 && y!=0) {
                    addDataOnMap(x, y);
                }
            }
        });
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addpoint:
                if (!imbReturn.isShown()) {
                    Animation fadein = AnimationUtils.loadAnimation(context, R.anim.fadein);

                    imbReturn.setAnimation(fadein);
                    imbReturn.setVisibility(View.VISIBLE);
                    imbAddPoint.setImageResource(R.drawable.marker48b);
                    tvTitle.setText("Add point on map");
                }
                eventMap();
                break;
            case R.id.btn_zoomin:
                mapView.zoomin();
                break;
            case R.id.btn_zoomout:
                mapView.zoomout();
                break;
            case R.id.btn_center:
                if (mLocation != null) {
                    Point p = (Point) GeometryEngine.project(mLocation, egs, wm);
                    mapView.zoomToResolution(p, 5);
                }
                break;
        }
    }

    public Drawable resizeDrawable(int resId, int width, int height) {
        Drawable drawable = getResources().getDrawable(resId);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmapresize = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return new BitmapDrawable(getResources(), bitmapresize);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.unpause();

        String status = CheckConnectInternet.getConnectStatusString(context);
        if (status.equals(CheckConnectInternet.STATUS_NOT)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setTitle("No Internet Connection ");
            alertDialog.setMessage("Please connect to internet.");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
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


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//    }
//
//    private void askForPermission(String permission, Integer requestCode) {
//        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, permission)) {
//
//                //This is called if user has denied the permission before
//                //In this case I am just asking the permission again
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
//
//            } else {
//
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
//            }
//        } else {
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
//        }
//    }
}
