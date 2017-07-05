package app.teeramet.money.moneydiary.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import app.teeramet.money.moneydiary.ImageManage;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.database.DatabaseHelper;
import app.teeramet.money.moneydiary.database.DatabaseManagement;
import app.teeramet.money.moneydiary.database.ReadDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;

public class AddAndUpdateAccountActivity extends AppCompatActivity {
    public static final int CAMERA_CODE = 100;
    public static final int GALLERY_CODE = 101;

    ImageButton imbAddPicAccount;
    EditText edtNameAccount;
    TextView tvTitle;
    Context context = this;
    Activity activity = AddAndUpdateAccountActivity.this;
    String mPath;
    int mIdAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        bindWidget();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            tvTitle.setText("Edit account");
            mIdAccount = bundle.getInt("ID");
            String nameAccount = bundle.getString("NAMEACCOUNT");
            mPath = bundle.getString("PATHIMAGE");

            edtNameAccount.setText(nameAccount);
            File file = new File(mPath);
            if (file.exists()) {
                ImageManage imageManage = new ImageManage(context, activity);
                Transformation transform = imageManage.tranform();
                Picasso.with(context).load(file).fit().transform(transform).centerCrop().into(imbAddPicAccount);
            } else
                Picasso.with(context).load(R.drawable.otherb).error(R.drawable.otherb).into(imbAddPicAccount);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            ImageManage imageManage = new ImageManage(context, activity);
            Transformation transform = imageManage.tranform();
            imageManage.Camera(data);
            mPath = imageManage.saveImageToStorage(data);

            File file = new File(mPath);
            if (file.exists()) {
                Picasso.with(context).load(file).fit().transform(transform).centerCrop().error(R.drawable.otherb).into(imbAddPicAccount);
            } else
                Picasso.with(context).load(R.drawable.otherb).fit().error(R.drawable.otherb).into(imbAddPicAccount);

        } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            ImageManage imageManage = new ImageManage(context, activity);
            Transformation transform = imageManage.tranform();
            mPath = imageManage.Gallery(data);

            File file = new File(mPath);
            if (file.exists()) {
                Picasso.with(context).load(file).fit().centerCrop().transform(transform).error(R.drawable.otherb).into(imbAddPicAccount);
            } else
                Picasso.with(context).load(R.drawable.otherb).fit().error(R.drawable.otherb).into(imbAddPicAccount);
        }
    }

    public void bindWidget() {
        imbAddPicAccount = (ImageButton) findViewById(R.id.addpicaccount);
        edtNameAccount = (EditText) findViewById(R.id.addnameaccount);
        tvTitle = (TextView) findViewById(R.id.title);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backmenu:
                onBackPressed();
                break;
            case R.id.addpic:
                ImageManage imageManage = new ImageManage(context, activity);
                imageManage.selectImage();

                break;
            case R.id.addpicaccount:
                ImageManage imageManage1 = new ImageManage(context, activity);
                imageManage1.selectImage();
                break;

            case R.id.fabaddaccount:
                if (edtNameAccount.getText().toString().trim().length() != 0) {
                    Account account = new Account();
                    account.setName(edtNameAccount.getText().toString());
                    if (mPath == null || mPath.equals("")) {
                        account.setImage("otherb");
                    } else {
                        account.setImage(mPath);
                    }

                    ReadDatabase readDatabase = new ReadDatabase(context);
                    String where = DatabaseHelper.ACCOUNT_ID + "=?";
                    String[] wherearg = {String.valueOf(mIdAccount)};
                    ArrayList<Account> arrayList = readDatabase.readDatabaseAccount(where, wherearg);

                    if (arrayList.size() == 0) {
                        DatabaseManagement databaseManagement = new DatabaseManagement(context);
                        databaseManagement.addAccount(account);
                        finish();
                    } else {
                        DatabaseManagement databaseManagement = new DatabaseManagement(context);
                        databaseManagement.updateAccount(account, mIdAccount);
                        finish();
                    }
                } else
                    Toast.makeText(context, "Pleses fill in form account namel", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
