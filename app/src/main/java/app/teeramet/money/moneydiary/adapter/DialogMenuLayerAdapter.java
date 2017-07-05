package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by barbie on 23/9/2559.
 */

public class DialogMenuLayerAdapter extends BaseAdapter {
    Context context;
    private ArrayList<Catalog> catalogs;
    private Boolean[] checklayer;
    private LayoutInflater inflater;


    public DialogMenuLayerAdapter(Context context, ArrayList<Catalog> catalogs, Boolean[] checklayer) {
        this.catalogs = catalogs;
        this.checklayer = checklayer;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return catalogs.size();
    }

    @Override
    public Object getItem(int position) {
        return catalogs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.dialog_layer, null);
            viewHolder.imageIcon = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.nameLayer = (TextView) convertView.findViewById(R.id.txt);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Catalog catalog = catalogs.get(position);

        int resId = context.getResources().getIdentifier(catalog.getPathIcon(), "drawable", context.getPackageName());


        /*
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.checkBox.isChecked()) {
                    viewHolder.checkBox.setChecked(false);
                    checklayer[position] = false;
                } else {
                    viewHolder.checkBox.setChecked(true);
                    checklayer[position] = true;
                }

            }
        });
        */

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.checkBox.performClick();
            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checklayer[position] = true;
                } else {
                    checklayer[position] = false;
                }
            }
        });
        Picasso.with(context).load(resId).into(viewHolder.imageIcon);
        viewHolder.nameLayer.setText(catalog.getStrNameList());
        viewHolder.checkBox.setChecked(checklayer[position]);
        return convertView;
    }

    public Boolean[] getChecklayer() {
        return checklayer;
    }

    private class ViewHolder {
        ImageView imageIcon;
        CheckBox checkBox;
        TextView nameLayer;
        LinearLayout linearLayout;
    }
}
