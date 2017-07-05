package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.classmoney.Catalog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by barbie on 2/9/2559.
 */
public class CatalogExpanableListViewAdapter extends BaseExpandableListAdapter {
    Context context;
    String[] headerlist;
    HashMap<String, ArrayList<Catalog>> childlist ;

    public CatalogExpanableListViewAdapter(Context context, String[] headerlist, HashMap<String, ArrayList<Catalog>> childlist) {
        this.context=context;
        this.headerlist = headerlist;
        this.childlist = childlist;
    }

    @Override
    public int getGroupCount() {
        return childlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childlist.get(this.headerlist[groupPosition]).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerlist[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childlist.get(this.headerlist[groupPosition]).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listgroup,null);
        }

        TextView tvHeader = (TextView) convertView.findViewById(R.id.groupname);
        ImageView imageView=(ImageView) convertView.findViewById(R.id.groupimage);

        tvHeader.setText(headerTitle);


        if(isExpanded){
            //open
            Picasso.with(context).load(R.drawable.sortdown).fit().into(imageView);
        }else Picasso.with(context).load(R.drawable.sortright).fit().into(imageView);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Catalog catalog=(Catalog) getChild(groupPosition,childPosition);
        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listcatalogue, null);
        }
        ImageView iconCatalog = (ImageView) convertView.findViewById(R.id.iconcatalogue);
        TextView tvNameCatalogue = (TextView) convertView.findViewById(R.id.namecatalogue);

        int resIddrawable =context.getResources().getIdentifier(catalog.getPathIcon(),"drawable",context.getPackageName());
        tvNameCatalogue.setText(catalog.getStrNameList());
        Picasso.with(context).load(resIddrawable).into(iconCatalog);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
