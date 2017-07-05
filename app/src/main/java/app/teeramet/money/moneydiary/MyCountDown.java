package app.teeramet.money.moneydiary;

import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;

/**
 * Created by barbie on 19/9/2559.
 */
public class MyCountDown extends CountDownTimer {

    FloatingActionButton fab;

    public MyCountDown(long millisInFuture, long countDownInterval, FloatingActionButton fab) {
        super(millisInFuture, countDownInterval);
        this.fab = fab;
    }

    @Override
    public void onTick(long millisUntilFinished) {


    }

    @Override
    public void onFinish() {

        if (!fab.isShown()) {
            fab.show();
        }
    }
}
