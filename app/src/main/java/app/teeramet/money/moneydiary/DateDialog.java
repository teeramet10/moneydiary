package app.teeramet.money.moneydiary;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import app.teeramet.money.moneydiary.database.ReadDatabase;
import app.teeramet.moneydiary.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by barbie on 19/10/2559.
 */

public class DateDialog {
    public static String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static int[] valuemonth = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    SimpleDateFormat yearformat =new SimpleDateFormat("yyyy");
    public int leapyear = 29;
    public int notleapyear = 28;
    public int min = 30;
    public int max = 31;

    Context context;
    Dialog dialog;

    ArrayAdapter monthadapter;
    ArrayAdapter yearadapter;

    ArrayList<String> monthlist = new ArrayList<>();
    ArrayList<String> yearlist = new ArrayList<>();


    Spinner spnMonth;
    Spinner spnYear;
    Button btnCancel;
    Button btnOK;

    View v;

    public DateDialog(Context context) {
        this.context = context;


        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        v = layoutInflater.inflate(R.layout.dialog_calendar, null);
        setWidget(v);
        dialog.setContentView(v);
    }

    private void setWidget(View v) {
        for (int i = 0; i < mMonths.length; i++) {
            monthlist.add(mMonths[i]);
        }
        //TODO CHECK DATABASE TIME YEAR
        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        int yearstartapp = sharePreferenceHelper.getYearStartApp();
        yearlist.add(String.valueOf(yearstartapp));

        ReadDatabase readDatabase = new ReadDatabase(context);
        ArrayList<Long> datelist = readDatabase.readDateMoney();

        for (int i = 0; i < 100; i++) {
            yearstartapp++;

            for (int j = 0; j < datelist.size(); j++) {
                Date date =new Date(datelist.get(j));
                String year = yearformat.format(date);
                if(String.valueOf(yearstartapp).equals(year)) {
                    yearlist.add(String.valueOf(yearstartapp));
                    j=datelist.size()-1;
                }
            }
        }
        spnMonth = (Spinner) v.findViewById(R.id.monthspinner);
        spnYear = (Spinner) v.findViewById(R.id.yearspinner);
        btnOK = (Button) v.findViewById(R.id.btn_ok);
        btnCancel = (Button) v.findViewById(R.id.btn_cancel);

        monthadapter = new ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item, monthlist);
        yearadapter = new ArrayAdapter(context,R.layout.support_simple_spinner_dropdown_item, yearlist);
        spnMonth.setAdapter(monthadapter);
        spnYear.setAdapter(yearadapter);

    }

    public Button getButtonOk() {
        return btnOK;
    }

    public Button getButtonCancel() {
        return btnCancel;
    }

    public Spinner getSpnMonth() {
        return spnMonth;
    }

    public Spinner getSpnYear() {
        return spnYear;
    }

    public void setSelectItemPosition(int month, int year) {
        spnMonth.setSelection(month);
        spnYear.setSelection(year);
    }

    public int getMonthItemPosition() {
        return spnMonth.getSelectedItemPosition();
    }

    public int getYearItemPosition() {
        return spnYear.getSelectedItemPosition();
    }

    public String getMonthValue() {
        return spnMonth.getSelectedItem().toString();
    }

    public String getYearValue() {
        return spnYear.getSelectedItem().toString();
    }

    public Dialog getDateDialog() {
        return dialog;
    }

    public View getViewDialog() {
        return v;
    }

    public int getMonth(int month, int year) {
        switch (month) {
            case 2:
                boolean isLeapYear = ((year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0));
                if (isLeapYear) {
                    return leapyear;
                } else return notleapyear;

            case 1:
            case 4:
            case 6:
            case 9:
            case 11:
                return min;
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
            default:
                return max;

        }
    }
}
