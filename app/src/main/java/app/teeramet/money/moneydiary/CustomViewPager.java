package app.teeramet.money.moneydiary;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by barbie on 30/9/2559.
 */

public class CustomViewPager extends ViewPager {

    boolean enabled;


    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled=true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
