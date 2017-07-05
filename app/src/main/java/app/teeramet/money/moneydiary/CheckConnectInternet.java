package app.teeramet.money.moneydiary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by barbie on 19/9/2559.
 */
public class CheckConnectInternet {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECT = 0;
    public static String STATUS_WIFI = "Wifi enable";
    public static String STATUS_MOBILE = "Mobile data enable";
    public static String STATUS_NOT = "Not Connecting";

    private static int getConnectingStatus(Context context) {
        ConnectivityManager connect = (ConnectivityManager) context.getSystemService((Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetwork = connect.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }

        }
        return TYPE_NOT_CONNECT;
    }

    public static String getConnectStatusString(Context context) {
        int con = getConnectingStatus(context);
        String status = null;

        if (con == TYPE_WIFI) {
            status = STATUS_WIFI;
        } else if (con == TYPE_MOBILE) {
            status = STATUS_MOBILE;
        } else if (con == TYPE_NOT_CONNECT) {
            status = STATUS_NOT;
        }
        return status;
    }
}
