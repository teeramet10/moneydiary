package app.teeramet.money.moneydiary;

import android.content.Context;
import android.widget.TextView;
import app.teeramet.moneydiary.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.DecimalFormat;

/**
 * Created by barbie on 10/10/2559.
 */

public class MyMarkerView extends MarkerView {
    private TextView textView;

    DecimalFormat format=new DecimalFormat("#,##0.##");

    public MyMarkerView(Context context) {
        super(context, R.layout.markview);
        textView =(TextView) findViewById(R.id.textview);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String value =format.format(e.getY());
        textView.setText(value);
    }

    @Override
    public int getXOffset(float xpos) {

        return -(getWidth()/2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -(getHeight());
    }
}
