package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;

public class MenuPassCodeActivity extends AppCompatActivity {
    public static int SETPASS_CODE = 101;
    public static int CHANGEPASS_CODE = 101;
    public static int REMOVEPASS_CODE = 102;

    public static String PASSCODE = "PASSCODE";
    public static String SETPASS = "SETPASS";
    public static String CHANGEPASS = "CHANGEPASS";
    public static String KEYPASS = "KEYPASS";
    public static String REMOVEPASS = "REMOVEPASS";
    public static String CONFIRM = "CONFIRMPASS";
    public static String KEYTOMENU ="KEYTOMENU";
    public static String REQUESTCODE = "REQUESTCODE";
    public static String BACKMENUPASSCODE="BACKMENU";

    RelativeLayout rlSetPass;
    RelativeLayout rlChangePass;
    Switch switchPass;
    boolean usepass;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pass_code);
        rlSetPass = (RelativeLayout) findViewById(R.id.formsetpass);
        rlChangePass = (RelativeLayout) findViewById(R.id.formchangepass);
        switchPass = (Switch) findViewById(R.id.switchcode);

        setCheckSwitch();

        rlSetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PassCodeActivity.class);
                if (switchPass.isChecked()) {
                    intent.putExtra(PASSCODE, REMOVEPASS);
                    intent.putExtra(REQUESTCODE, REMOVEPASS_CODE);
                    startActivityForResult(intent, REMOVEPASS_CODE);
                } else {
                    intent.putExtra(PASSCODE, SETPASS);
                    intent.putExtra(REQUESTCODE, SETPASS_CODE);
                    startActivityForResult(intent, SETPASS_CODE);
                }
            }
        });

        rlChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usepass) {
                    Intent intent = new Intent(context, PassCodeActivity.class);
                    intent.putExtra(PASSCODE, CHANGEPASS);
                    intent.putExtra(REQUESTCODE, CHANGEPASS);
                    startActivityForResult(intent, CHANGEPASS_CODE);
                } else Toast.makeText(context, "Not use passcode", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setCheckSwitch();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backmenu:
                onBackPressed();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK ) {

            if (requestCode == REMOVEPASS_CODE) {
                SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                sharePreferenceHelper.removePassCode();
                setCheckSwitch();
            }
        }
    }

    public void setCheckSwitch(){
        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        usepass = sharePreferenceHelper.getUsePassCode();

        if (usepass) {
            switchPass.setChecked(usepass);
        }else {
            switchPass.setChecked(usepass);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(BACKMENUPASSCODE,true);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
