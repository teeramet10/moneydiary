package app.teeramet.money.moneydiary.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.adapter.SelectIconAdapter;

import java.util.ArrayList;

public class SelectIconCatalogueActivity extends AppCompatActivity {
    GridView gridView;
    Context context=this;
    ArrayList<Integer> mIconlist=new ArrayList<>();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_icon_catalogue);
        gridView=(GridView)findViewById(R.id.selecticon);

        final String[] nameicon =getResources().getStringArray(R.array.icon);
        int count =nameicon.length;
        int[] resId=new int[count];

        for(int i=0;i<nameicon.length;i++){
            resId[i] = getResources().getIdentifier(nameicon[i],"drawable",getPackageName());
        }

        for(int i=0; i<resId.length;i++){
            mIconlist.add(resId[i]);
        }

        SelectIconAdapter selectIconAdapter=
                new SelectIconAdapter(context,mIconlist);
        gridView.setAdapter(selectIconAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("ICON",nameicon[position]);
                setResult(RESULT_OK,intent);
                finish();
            }
        });


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.backmenu :
                onBackPressed();
                break;
        }
    }
}
