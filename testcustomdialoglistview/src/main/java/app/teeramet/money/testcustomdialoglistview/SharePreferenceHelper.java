package app.teeramet.money.testcustomdialoglistview;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by barbie on 4/9/2559.
 */
public class SharePreferenceHelper {
    public static final String KEYPREF_ACCOUNT = "Acount_id";
    public static final String KEYPREF_NAMEACCOUNT = "Account_name";
    public static final String KEYPREF_IMAGEACCOUNT = "Account_image";
    public static final String KEYPREF_FILENAME = "SHARE PREF";
    public static final String KEYPREF_STATUSLAYER = "Status layer";
    SharedPreferences sharePreference;
    Context context;
    String[] key ;

    public SharePreferenceHelper(Context context) {
        this.context = context;
    }


    public void setStatusLayer(String layer, boolean status) {
        sharePreference = context.getSharedPreferences(KEYPREF_STATUSLAYER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.putBoolean(layer, status);
        editor.commit();
    }

    public Boolean[] getStatusLayer(String[] strkey) {
        key=strkey;
        Boolean[] layers;
        sharePreference = context.getSharedPreferences(KEYPREF_STATUSLAYER, Context.MODE_PRIVATE);
        Map<String, ?> layer = sharePreference.getAll();
        layers = new Boolean[layer.size()];

        if (layer.size() > 0 && layers.length != 0 &&key.length!=0) {

            for (int i = 0; i < layers.length; i++) {
                layers[i] = (Boolean) layer.get(key[i]);
            }
            return layers;
        }
        return null;
    }

}
