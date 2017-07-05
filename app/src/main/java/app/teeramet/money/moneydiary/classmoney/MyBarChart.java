package app.teeramet.money.moneydiary.classmoney;

import android.content.Context;
import android.graphics.Color;

import app.teeramet.money.moneydiary.MyMarkerView;
import app.teeramet.money.moneydiary.classmoney.Money;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by barbie on 10/10/2559.
 */

public class MyBarChart {
    public static final int MONTH = 1;
    public static final int YEAR = 2;
    String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    SimpleDateFormat dayformat = new SimpleDateFormat("dd");
    SimpleDateFormat monthformat = new SimpleDateFormat("MM");
    SimpleDateFormat yearformat = new SimpleDateFormat("yyyy");

    Context context;

    public MyBarChart(Context context) {
        this.context = context;

    }

    public BarChart setBarChart(BarChart mBarChart, final int type) {
        Legend l = mBarChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setXEntrySpace(4f);

        XAxis xl = mBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xl.setAxisMinValue(0f);
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);
        xl.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                if (type == MONTH) {
                    return String.valueOf((int) value + 1);
                } else if (type == YEAR) {
                    if (value < 0 || value > 11) {
                        return "";
                    } else return mMonths[(int) value];
                } else return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });


        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxid = mBarChart.getAxisRight();
        rightAxid.setValueFormatter(new LargeValueFormatter());
        rightAxid.setDrawGridLines(false);
        rightAxid.setAxisMinValue(0f);


        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x3 dataset
        float barWidth = 0.45f; // x3 dataset
        // (0.3 + 0.02) * 3 + 0.04 = 1.00 -> interval per "group"
        BarData mBarData = mBarChart.getBarData();
        if (mBarData != null) {
            mBarChart.getBarData().setBarWidth(barWidth);
            mBarChart.getBarData().setDrawValues(false);
            mBarChart.setDescription("");
            mBarChart.getXAxis().setAxisMinValue(0f);
            mBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            mBarChart.getXAxis().setAxisMaxValue(mBarData.getXMax() + 1f);
            mBarChart.groupBars(0, groupSpace, barSpace);


            MarkerView markerView = new MyMarkerView(context);
            mBarChart.setMarkerView(markerView);
            mBarChart.animateY(3000);

        }
        Date date=new Date();
        if(type==MONTH){
            int currentday =Integer.valueOf(dayformat.format(date));
            mBarChart.zoomAndCenter(7f,0f,(float) currentday,0f, YAxis.AxisDependency.LEFT);
        }else if(type ==YEAR){
            int currentmonth =Integer.valueOf(monthformat.format(date));
            mBarChart.zoomAndCenter(3f,0f,(float)currentmonth,0f, YAxis.AxisDependency.LEFT);
        }

        mBarChart.invalidate();
        return mBarChart;

    }

    public ArrayList<Money>[] setMoneyList(ArrayList<Money> moneylist, int sizelist, int currentyear, int type) {
        ArrayList<Money>[] moneymonth = new ArrayList[sizelist];

        for (int i = 0; i < moneymonth.length; i++) {
            moneymonth[i] = new ArrayList<>();
        }
        //getcurrentyear to check

        for (int i = 0; i < moneylist.size(); i++) {
            if (moneylist.size() != 0) {
                Date date = new Date(moneylist.get(i).getDate());
                String strdate = dayformat.format(date);
                String strmonth =monthformat.format(date);
                String stryear = yearformat.format(date);
                int count = 0;
                for (int j = 1; j <= sizelist; j++) {
                    if (type == MONTH && Integer.parseInt(strdate) == j && Integer.parseInt(stryear) == currentyear) {
                        moneymonth[count].add(moneylist.get(i));
                    }

                    if(type ==YEAR &&Integer.parseInt(strmonth)==j&&Integer.parseInt(stryear)==currentyear){
                        moneymonth[count].add(moneylist.get(i));
                    }
                    count++;
                }
            }
        }
        return moneymonth;
    }


    public ArrayList<BarEntry> setMoneyEntry(ArrayList<Money>[] moneymonth) {
        BarEntry barEntry;
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < moneymonth.length; i++) {

            float sum = calculate(moneymonth[i]);
            barEntry = new BarEntry(i, sum);
            barEntries.add(barEntry);

        }
        return barEntries;

    }

    public float calculate(ArrayList<Money> money) {
        float sum = 0;
        for (int i = 0; i < money.size(); i++) {
            sum += money.get(i).getPrice();
        }
        return sum;
    }

}
