package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by barbie on 2/9/2559.
 */
public class SpinnerCatalogAdapter extends BaseAdapter {
    Context context;
    ArrayList<Catalog> catalogArrayList;
    LayoutInflater inflater;
    ViewHolder viewHolder;

    public SpinnerCatalogAdapter(Context context, ArrayList<Catalog> catalogArrayList) {
        this.context = context;
        this.catalogArrayList = catalogArrayList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return catalogArrayList.size();
    }

    @Override
    public Catalog getItem(int position) {
        return catalogArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.listcatalogue,null);
            viewHolder.tvName=(TextView) convertView.findViewById(R.id.namecatalogue);
            viewHolder.imageIcon=(ImageView) convertView.findViewById(R.id.iconcatalogue);
            convertView.setTag(viewHolder);
        }else viewHolder=(ViewHolder) convertView.getTag();

        Catalog catalog=catalogArrayList.get(position);
        int catalogId =context.getResources().getIdentifier(catalog.getPathIcon(),"drawable",context.getPackageName());
        viewHolder.tvName.setText(catalog.getStrNameList());
        Picasso.with(context).load(catalogId).error(R.drawable.otherb).into(viewHolder.imageIcon);

        return convertView;
    }

    public class  ViewHolder{
        TextView tvName;
        ImageView imageIcon;
    }
}
