package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.teeramet.money.moneydiary.ImageManage;
import app.teeramet.money.moneydiary.menu.ListMenu;
import app.teeramet.money.moneydiary.menu.Menu;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.classmoney.Account;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by barbie on 18/8/2559.
 */
public class MenuExpandableListViewAdapter extends BaseExpandableListAdapter {
    Context context;
    ArrayList<String> headerlist = new ArrayList<>();
    HashMap<String, ArrayList<Menu>> childlist;


    public MenuExpandableListViewAdapter(Context context, HashMap<String, ArrayList<Menu>> childlist, ArrayList<String> headerlist) {
        this.context = context;
        this.headerlist = headerlist;
        this.childlist = childlist;
    }

    @Override
    public int getGroupCount() {
        return childlist.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childlist.get(this.headerlist.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerlist.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childlist.get(this.headerlist.get(groupPosition)).get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listgroup, null);
        }

        TextView tvHeader = (TextView) convertView.findViewById(R.id.groupname);
        ImageView imageView =(ImageView) convertView.findViewById(R.id.groupimage);
        tvHeader.setText(headerTitle);

        if(isExpanded){
            Picasso.with(context).load(R.drawable.sortdown).fit().into(imageView);
        }else Picasso.with(context).load(R.drawable.sortright).fit().into(imageView);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Menu menu = (Menu) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listmenu, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.nameaccount);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageaccount);
        ImageView imageView1 = (ImageView) convertView.findViewById(R.id.imageaccount);
//        imageView.setPadding(0, 0, 0, 0);
//        imageView1.setPadding(12,12,12,12);
        ImageManage imageManage=new ImageManage(context);
        Transformation transformation = imageManage.tranform();

        if (menu.getMenu() instanceof Account) {

            txtListChild.setText(((Account) menu.getMenu()).getName());
            if (menu.getPathname() != null) {
                File image = new File(menu.getPathname());
                if (image.exists()) {
                    Picasso.with(context).load(image).transform(transformation).fit().centerCrop().into(imageView);
                } else {
                    Picasso.with(context).load(R.drawable.otherb).error(R.drawable.otherb).transform(transformation).into(imageView1);
                }
            } else {

                Picasso.with(context).load(R.drawable.otherb).error(R.drawable.otherb).transform(transformation).into(imageView1);
            }


        } else if (menu.getMenu() instanceof ListMenu) {
            txtListChild.setText(((ListMenu) menu.getMenu()).getNamelist());
            Picasso.with(context).load(menu.getImage()).transform(transformation).into(imageView1);
        }


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
