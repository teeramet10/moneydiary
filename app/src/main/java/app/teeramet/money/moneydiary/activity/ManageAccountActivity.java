package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.adapter.AccountGridViewAdapter;
import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.database.ReadDatabase;

import java.util.ArrayList;

public class ManageAccountActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Context context = this;
    ArrayList<Account> mAccountArrayList;
    AccountGridViewAdapter accountGridViewAdapter;
    ImageButton imbBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        imbBack = (ImageButton) findViewById(R.id.backmenu);
        SpacingItemDecoration spacingItemDecoration=new SpacingItemDecoration(16);
        recyclerView.addItemDecoration(spacingItemDecoration);


    }

    public void setAccountArrayList() {
        if(mAccountArrayList!=null){
            if(mAccountArrayList.size()>0){
                mAccountArrayList.clear();
            }
        }

        ReadDatabase readDatabase = new ReadDatabase(context);
        mAccountArrayList = readDatabase.readDatabaseAccount(null,null);

        accountGridViewAdapter = new AccountGridViewAdapter(context, mAccountArrayList);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(accountGridViewAdapter);

    }




    public void addAccount() {
        Intent intent = new Intent(context, AddAndUpdateAccountActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backmenu:
                onBackPressed();
                break;
            case R.id.addaccount:
                addAccount();
                break;
        }
    }

    public class  SpacingItemDecoration extends RecyclerView.ItemDecoration{
        int space;

        public SpacingItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            outRect.left=space;
            outRect.right=space;
            outRect.bottom=space;
            outRect.top=space;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAccountArrayList();
    }
}
