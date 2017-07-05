package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;

public class PassCodeActivity extends AppCompatActivity implements View.OnClickListener {
    public static String REPASSCODE = "REPASS";

    Context context = this;
    String mCode = "";
    String mReCode = "";
    String mMyPasscode = "";
    int mRequestCode;
    StringBuilder mGetPasscode = new StringBuilder(mCode);
    String STATUS_PASSCODE = MenuPassCodeActivity.KEYPASS;

    private Button btnValue0;
    private Button btnValue1;
    private Button btnValue2;
    private Button btnValue3;
    private Button btnValue4;
    private Button btnValue5;
    private Button btnValue6;
    private Button btnValue7;
    private Button btnValue8;
    private Button btnValue9;
    private ImageButton btnClear;
    private ImageButton btnCancle;
    private ImageView imvPass1;
    private ImageView imvPass2;
    private ImageView imvPass3;
    private ImageView imvPass4;

    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);

        bindWidget();

        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);

        mMyPasscode = sharePreferenceHelper.getPassCode();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            STATUS_PASSCODE = bundle.getString(MenuPassCodeActivity.PASSCODE);
            mRequestCode = bundle.getInt(MenuPassCodeActivity.REQUESTCODE);
        }

        if (STATUS_PASSCODE.equals(MenuPassCodeActivity.SETPASS)) {
            tvText.setText(getResources().getText(R.string.set_passcode));
        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.CHANGEPASS)) {
            tvText.setText(getResources().getText(R.string.change_passcode));
        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.KEYPASS)) {
            tvText.setText(getResources().getText(R.string.enter_passcode));
        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.REMOVEPASS)) {
            tvText.setText(getResources().getText(R.string.enter_passcode));
        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.CONFIRM)) {
            mReCode = bundle.getString(REPASSCODE);
            tvText.setText(getResources().getText(R.string.re_passcode));
        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.KEYTOMENU)) {
            tvText.setText(getResources().getText(R.string.enter_passcode));
        }

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGetPasscode.length() > 0) {
                    switch (mGetPasscode.length()) {
                        case 1:
                            mGetPasscode.deleteCharAt(0);
                            if (imvPass1.getVisibility() == View.VISIBLE) {
                                imvPass1.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case 2:
                            mGetPasscode.deleteCharAt(1);
                            if (imvPass2.getVisibility() == View.VISIBLE) {
                                imvPass2.setVisibility(View.INVISIBLE);
                            }
                            break;
                        case 3:
                            mGetPasscode.deleteCharAt(2);
                            if (imvPass3.getVisibility() == View.VISIBLE) {
                                imvPass3.setVisibility(View.INVISIBLE);
                            }
                            break;
                    }
                }
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (STATUS_PASSCODE.equals(MenuPassCodeActivity.CONFIRM)) {
            Intent intent = new Intent(context, MenuPassCodeActivity.class);
            startActivity(intent);
        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.KEYPASS)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }


    public void bindWidget() {
        btnValue0 = (Button) findViewById(R.id.value0);
        btnValue1 = (Button) findViewById(R.id.value1);
        btnValue2 = (Button) findViewById(R.id.value2);
        btnValue3 = (Button) findViewById(R.id.value3);
        btnValue4 = (Button) findViewById(R.id.value4);
        btnValue5 = (Button) findViewById(R.id.value5);
        btnValue6 = (Button) findViewById(R.id.value6);
        btnValue7 = (Button) findViewById(R.id.value7);
        btnValue8 = (Button) findViewById(R.id.value8);
        btnValue9 = (Button) findViewById(R.id.value9);
        btnClear = (ImageButton) findViewById(R.id.clear);
        btnCancle = (ImageButton) findViewById(R.id.cancel);
        tvText = (TextView) findViewById(R.id.text);

        imvPass1 = (ImageView) findViewById(R.id.inpass1);
        imvPass2 = (ImageView) findViewById(R.id.inpass2);
        imvPass3 = (ImageView) findViewById(R.id.inpass3);
        imvPass4 = (ImageView) findViewById(R.id.inpass4);

       setEvent(this);
    }

    private void setEvent(View.OnClickListener i){
        btnValue0.setOnClickListener(i);
        btnValue1.setOnClickListener(i);
        btnValue2.setOnClickListener(i);
        btnValue3.setOnClickListener(i);
        btnValue4.setOnClickListener(i);
        btnValue5.setOnClickListener(i);
        btnValue6.setOnClickListener(i);
        btnValue7.setOnClickListener(i);
        btnValue8.setOnClickListener(i);
        btnValue9.setOnClickListener(i);

    }



    @Override
    public void onClick(View v) {
        String passcode = ((Button) v).getText().toString();
        mGetPasscode.append(passcode);
        checkPasscode();
    }

    public void checkPasscode() {

        if (mGetPasscode.length() > 0) {
            if (mGetPasscode.length() == 1) {
                imvPass1.setVisibility(View.VISIBLE);
            } else if (mGetPasscode.length() == 2) {
                imvPass2.setVisibility(View.VISIBLE);
            } else if (mGetPasscode.length() == 3) {
                imvPass3.setVisibility(View.VISIBLE);
            } else if (mGetPasscode.length() == 4) {
                imvPass4.setVisibility(View.VISIBLE);
                setEvent(null);

                final CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        if (STATUS_PASSCODE.equals(MenuPassCodeActivity.SETPASS)) {
                            if (!mGetPasscode.toString().equals("")) {
                                Intent intent = new Intent(context, PassCodeActivity.class);
                                String code = mGetPasscode.toString();
                                intent.putExtra(REPASSCODE, code);
                                intent.putExtra(MenuPassCodeActivity.PASSCODE, MenuPassCodeActivity.CONFIRM);
                                startActivityForResult(intent, mRequestCode);
                            } else {
                                clearPassCode();
                            }

                        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.CHANGEPASS)) {
                            Intent intent = new Intent(context, PassCodeActivity.class);
                            String code = mGetPasscode.toString();
                            intent.putExtra(REPASSCODE, code);
                            intent.putExtra(MenuPassCodeActivity.PASSCODE, MenuPassCodeActivity.CONFIRM);
                            startActivityForResult(intent, mRequestCode);

                        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.KEYPASS)
                                || STATUS_PASSCODE.equals(MenuPassCodeActivity.REMOVEPASS)
                                || STATUS_PASSCODE.equals(MenuPassCodeActivity.KEYTOMENU)) {

                            if (mGetPasscode.toString().equals(mMyPasscode)) {

                                if (STATUS_PASSCODE.equals(MenuPassCodeActivity.KEYPASS)) {
                                    finish();
                                } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.REMOVEPASS)) {
                                    setResult(RESULT_OK);
                                    finish();
                                } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.KEYTOMENU)) {
                                    Intent intent = new Intent(context, MenuPassCodeActivity.class);
                                    startActivity(intent);
                                }

                            } else {
                                clearPassCode();
                            }
                        } else if (STATUS_PASSCODE.equals(MenuPassCodeActivity.CONFIRM)) {
                            if (mReCode.equals(mGetPasscode.toString())) {
                                String passcode = mGetPasscode.toString();
                                if (!passcode.equals("")) {
                                    SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                                    sharePreferenceHelper.setPassCode(passcode);
                                    STATUS_PASSCODE = MenuPassCodeActivity.KEYPASS;
                                    Intent intent = new Intent(context, MenuPassCodeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            } else {
                                clearPassCode();
                            }
                        }


                    }
                };

                countDownTimer.start();

            } else {
                clearPassCode();
            }
        }
    }

    public void clearPassCode() {
        mCode = "";
        mGetPasscode = new StringBuilder(mCode);
        imvPass1.setVisibility(View.INVISIBLE);
        imvPass2.setVisibility(View.INVISIBLE);
        imvPass3.setVisibility(View.INVISIBLE);
        imvPass4.setVisibility(View.INVISIBLE);
        tvText.setText("Wrong Passcode");
        setEvent(this);

    }

    public void setActivity(){
        mCode = "";
        mGetPasscode = new StringBuilder(mCode);
        imvPass1.setVisibility(View.INVISIBLE);
        imvPass2.setVisibility(View.INVISIBLE);
        imvPass3.setVisibility(View.INVISIBLE);
        imvPass4.setVisibility(View.INVISIBLE);
        setEvent(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActivity();
    }
}
