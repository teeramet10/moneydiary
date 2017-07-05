package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import app.teeramet.moneydiary.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by barbie on 9/9/2559.
 */
public class SelectIconAdapter extends BaseAdapter {
    ArrayList<Integer> iconlist;
    Context context;
    ViewHolder viewHolder;
    LayoutInflater inflater;

    public SelectIconAdapter(Context context, ArrayList<Integer> iconlist) {
        this.context = context;
        this.iconlist = iconlist;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return iconlist.size();
    }

    @Override
    public Object getItem(int position) {
        return iconlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView = inflater.inflate(R.layout.iconlist, null);
            viewHolder.imbIcon = (ImageView) convertView.findViewById(R.id.grid_image);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        Picasso.with(context).load(iconlist.get(position)).into(viewHolder.imbIcon);
        return convertView;
    }

    public class ViewHolder {
        ImageView imbIcon;
    }
}
