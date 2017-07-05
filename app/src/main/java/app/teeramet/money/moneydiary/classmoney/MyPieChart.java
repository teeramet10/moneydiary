package app.teeramet.money.moneydiary.classmoney;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.teeramet.money.moneydiary.classmoney.Catalog;
import app.teeramet.money.moneydiary.classmoney.Money;
import app.teeramet.money.moneydiary.classmoney.TopCatalog;
import app.teeramet.moneydiary.R;

/**
 * Created by barbie on 1/28/2017.
 */

public class MyPieChart {
    public static final int MONTH = 1;
    public static final int YEAR = 2;

    private ArrayList<Catalog> catalogArrayList;
    private ArrayList<Money> moneyArrayList;
    private ArrayList<TopCatalog> topArrayList;
    private String[] topCatalog;

    Context context;
    PieChart myPieChart;

    public MyPieChart(Context context, PieChart myPieChart, ArrayList<Money> moneylist, ArrayList<Catalog> catalogs) {
        this.context = context;
        this.myPieChart = myPieChart;
        this.moneyArrayList = moneylist;
        this.catalogArrayList = catalogs;
    }


    public void setAttributeChart(String type) {
        SpannableString span=new SpannableString("Top  use\n"+type+"  catalogue");
        span.setSpan(new RelativeSizeSpan(1.3f),4,span.length(),0);
        span.setSpan(new RelativeSizeSpan(1.6f),0,3,0);
        span.setSpan(new ForegroundColorSpan(Color.GRAY),0,span.length(),0);
        span.setSpan(new StyleSpan(Typeface.ITALIC),0,span.length(),0);
        if(type.equals("Income")){
            span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.income)),8,span.length()-9,0);
        }else if(type.equals("Expense")){
            span.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.expense)),8,span.length()-9,0);
        }else{
            span.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()),8,span.length()-9,0);
        }


        myPieChart.animateXY(1400,1400);
        myPieChart.setUsePercentValues(true);
        myPieChart.setDrawCenterText(true);
        myPieChart.setCenterText(span);
        myPieChart.setDescription("");

        myPieChart.setDrawHoleEnabled(true);
        myPieChart.setHoleColor(Color.BLUE);
        myPieChart.setHoleRadius(50f);
        myPieChart.setTransparentCircleRadius(52f);
        myPieChart.setTransparentCircleColor(Color.WHITE);
        myPieChart.setTransparentCircleAlpha(52);

        myPieChart.setEntryLabelColor(Color.WHITE);
        myPieChart.setEntryLabelTextSize(12f);
        myPieChart.setDrawHoleEnabled(true);
        myPieChart.setHoleColor(Color.WHITE);

        Legend legend = myPieChart.getLegend();
        legend.setDrawInside(false);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setYEntrySpace(0f);
        legend.setXEntrySpace(10f);
        legend.setYOffset(10f);


    }

    public void setDataPieChart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        topArrayList = new ArrayList<>();

        setTopCatalogue();

        for (int i = 0; i < 5; i++) {
            if(i<topArrayList.size()) {
                if(topArrayList.get(i).getValue()>0) {
                    PieEntry pieEntry = new PieEntry((float) topArrayList.get(i).getValue(), topCatalog[i]);
                    entries.add(pieEntry);
                }
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Top Catalogue");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        myPieChart.setData(data);
    }

    private void setTopCatalogue() {


        //add name catalog
        for (int i = 0; i < catalogArrayList.size(); i++) {
            TopCatalog topCatalog = new TopCatalog();
            topCatalog.setCatalog(catalogArrayList.get(i));
            topArrayList.add(topCatalog);
        }

        //increase value
        for (int i = 0; i < moneyArrayList.size(); i++) {
            for (int j = 0; j < catalogArrayList.size(); j++) {
                if (topArrayList.get(j).getCatalog().getId() == moneyArrayList.get(i).getCatalog().getId()) {
                    topArrayList.get(j).increaseValue(moneyArrayList.get(i).getPrice());
                }
            }

        }

        Collections.sort(topArrayList, new Comparator<TopCatalog>() {
            @Override
            public int compare(TopCatalog lhs, TopCatalog rhs) {
                return (String.valueOf(rhs.getValue()).compareTo(String.valueOf(lhs.getValue())));
            }
        });

        topCatalog = new String[topArrayList.size()];
        for (int j = 0; j < topArrayList.size(); j++) {
            if (j < topArrayList.size()) {
                topCatalog[j] = topArrayList.get(j).getCatalog().getStrNameList();
            }
        }
    }
    public PieChart getPieChart() {
        return myPieChart;
    }

}
