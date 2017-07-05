package app.teeramet.money.testcustomdialoglistview;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


import com.teeramet.money.testcustomdialoglistview.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    CustomAdapter customadapter;
    Button button;
    ListView listview;
    ArrayList<Catalog> catalogs = new ArrayList<>();
    String[] layername ={"Salary", "Bank", "Profit", "Food", "Drink", "Travel", "Education", "Transport"
            , "Tax", "Rent"};
    Boolean[] layer;

    int[] imageCatalog = {R.drawable.bankb, R.drawable.cafeb, R.drawable.clothb, R.drawable.cosmeticb, R.drawable.mastercard, R.drawable.healthb,
            R.drawable.giftb, R.drawable.foodb, R.drawable.educationb, R.drawable.drinkb, R.drawable.healthb};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
        layer = sharePreferenceHelper.getStatusLayer(layername);


        for (int i = 0; i < 10; i++) {
            Catalog catalog = new Catalog();
            catalog.setStrNameList(layername[i]);
            catalog.setIcon(imageCatalog[i]);
            catalogs.add(catalog);

        }
        if (layer == null) {

            SharePreferenceHelper sharePreferenceHelper1 = new SharePreferenceHelper(context);
            for (int i = 0; i < 10; i++) {
                sharePreferenceHelper1.setStatusLayer(catalogs.get(i).getStrNameList(), true);
            }
        }

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                LayoutInflater layoutinflater = LayoutInflater.from(context);
                View view = layoutinflater.inflate(R.layout.list, null);



                listview = (ListView) view.findViewById(R.id.list_items);
                customadapter = new CustomAdapter(catalogs, context, layer);
                listview.setAdapter(customadapter);


                alert.setView(view);
                alert.setTitle("Select Layer");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        layer = customadapter.getChecklayer();
                        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);
                        for (int i = 0; i < 10; i++) {
                            sharePreferenceHelper.setStatusLayer(catalogs.get(i).getStrNameList(), layer[i]);
                            customadapter.notifyDataSetChanged();
                        }
                    }
                });
                alert.setNegativeButton("CANCEL", null);
                alert.create().show();
            }
        });


    }
}
