package app.teeramet.money.moneydiary;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
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

    public static final String KEYPREF_FILEPASSCODE ="Passwordfile";
    public static final String KEYPREF_SETPASS ="PASSWORD_BOOLEAN";
    public static final String KEYPREF_PASSCODE ="PASSWORD";

    public static final String KEYPREF_APP ="APP";
    public static final String KEYPREF_YEARSTART ="YEARSTART_APP";
    public static final String KEYPREF_COUNTOPENAPP ="OPEN_APP";
    SharedPreferences sharePreference;
    Context context;
    String[] key ;

    public SharePreferenceHelper(Context context) {
        this.context = context;
    }

    public void setIdAccountPreference(int idaccount) {
        sharePreference = context.getSharedPreferences(KEYPREF_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.putInt(KEYPREF_ACCOUNT, idaccount);
        editor.commit();

    }

    public int getIdAccountPreference() {
        sharePreference = context.getSharedPreferences(KEYPREF_FILENAME, Context.MODE_PRIVATE);
        int idAccount = sharePreference.getInt(KEYPREF_ACCOUNT, 1);
        return idAccount;
    }

    public void setNameAccountPreference(String name) {
        sharePreference = context.getSharedPreferences(KEYPREF_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.putString(KEYPREF_NAMEACCOUNT, name);
        editor.commit();
    }

    public String getNameAccountPreference() {
        sharePreference = context.getSharedPreferences(KEYPREF_FILENAME, Context.MODE_PRIVATE);
        String nameAccount = sharePreference.getString(KEYPREF_NAMEACCOUNT, "My Account");
        return nameAccount;
    }

    public void setImageAccountPreference(String pathimage) {
        sharePreference = context.getSharedPreferences(KEYPREF_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePreference.edit();
        editor.putString(KEYPREF_IMAGEACCOUNT, pathimage);
        editor.commit();
    }

    public String getimageAccountPreference() {
        sharePreference = context.getSharedPreferences(KEYPREF_FILENAME, Context.MODE_PRIVATE);
        String pathimage = sharePreference.getString(KEYPREF_IMAGEACCOUNT, "Error");
        return pathimage;
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

    public boolean getStatusLayerAtPosition(String key){
        sharePreference =context.getSharedPreferences(KEYPREF_STATUSLAYER,Context.MODE_PRIVATE);
        boolean value=sharePreference.getBoolean(key,true);
        return value;
    }

    public void removeStatusLayer(String key) {
        sharePreference = context.getSharedPreferences(KEYPREF_STATUSLAYER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharePreference.edit();
        editor.remove(key).apply();
        editor.commit();
    }



    public boolean getUsePassCode(){
        sharePreference =context.getSharedPreferences(KEYPREF_FILEPASSCODE,Context.MODE_PRIVATE);
        boolean usepasscode= sharePreference.getBoolean(KEYPREF_SETPASS,false);
        return usepasscode;
    }

    public String  getPassCode(){
        sharePreference =context.getSharedPreferences(KEYPREF_FILEPASSCODE,Context.MODE_PRIVATE);
        String passcode= sharePreference.getString(KEYPREF_PASSCODE,"");
        return passcode;
    }

    public void setPassCode(String password){
        sharePreference =context.getSharedPreferences(KEYPREF_FILEPASSCODE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharePreference.edit();
        editor.putBoolean(KEYPREF_SETPASS,true);
        editor.putString(KEYPREF_PASSCODE,password);
        editor.commit();
    }


    public void removePassCode(){
        sharePreference =context.getSharedPreferences(KEYPREF_FILEPASSCODE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharePreference.edit();
        editor.putBoolean(KEYPREF_SETPASS,false);
        editor.remove(KEYPREF_PASSCODE);
        editor.commit();
    }



    public void setKeyStartApp(int yearstart){
        sharePreference =context.getSharedPreferences(KEYPREF_APP,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharePreference.edit();
        editor.putInt(KEYPREF_YEARSTART,yearstart);
        editor.putInt(KEYPREF_COUNTOPENAPP,1);
        editor.commit();
    }

    public int getStartValueApp(){
        sharePreference=context.getSharedPreferences(KEYPREF_APP,Context.MODE_PRIVATE);
        int value =sharePreference.getInt(KEYPREF_COUNTOPENAPP,0);
        return value;
    }

    public int getYearStartApp(){
        sharePreference=context.getSharedPreferences(KEYPREF_APP,Context.MODE_PRIVATE);
        int year =sharePreference.getInt(KEYPREF_YEARSTART,0);
        return year;
    }

}
