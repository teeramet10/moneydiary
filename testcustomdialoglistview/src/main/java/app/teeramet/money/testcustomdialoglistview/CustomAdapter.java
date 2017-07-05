package app.teeramet.money.testcustomdialoglistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teeramet.money.testcustomdialoglistview.R;

import java.util.ArrayList;

/**
 * Created by barbie on 22/9/2559.
 */

public class CustomAdapter extends BaseAdapter {
    ArrayList<Catalog> catalogs;
    Context context;
    LayoutInflater layoutInflater;
    Boolean[] checklayer;

    public CustomAdapter(ArrayList<Catalog> catalogs,Context context,Boolean[]checklayer) {
        this.context=context;
        this.catalogs=catalogs;
        layoutInflater =LayoutInflater.from(context);
        this.checklayer=new Boolean[catalogs.size()];
        this.checklayer=checklayer;
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
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.listitem,null);
            viewHolder.imageView =(ImageView)convertView.findViewById(R.id.image);
            viewHolder.textView =(TextView) convertView.findViewById(R.id.txt);
            viewHolder.checkBox =(CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.relativeLayout=(RelativeLayout)convertView.findViewById(R.id.layout);
            convertView.setTag(viewHolder);
        }else {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(catalogs.get(position).getPathIcon());
        viewHolder.textView.setText(catalogs.get(position).getStrNameList());
        viewHolder.checkBox.setChecked(checklayer[position]);

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.checkBox.isChecked()){
                    viewHolder.checkBox.setChecked(false);
                    checklayer[position]=false;
                }else {
                    viewHolder.checkBox.setChecked(true);
                    checklayer[position]=true;
                }
            }
        });
        return convertView;
    }

    public Boolean[] getChecklayer(){
        return checklayer;
    }

    public class ViewHolder{
        ImageView imageView;
        CheckBox checkBox;
        TextView textView;
        RelativeLayout relativeLayout;
    }
}
