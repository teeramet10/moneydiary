package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.teeramet.money.moneydiary.classmoney.Balance;
import app.teeramet.moneydiary.R;

/**
 * Created by barbie on 1/31/2017.
 */

public class BalanceAdapter extends BaseAdapter {
    ArrayList<Balance> balanceArrayList;
    Context context;
    ViewHolder viewHolder;
    LayoutInflater inflater;

    public BalanceAdapter(Context context, ArrayList<Balance> balanceArrayList) {
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.balanceArrayList = balanceArrayList;
    }

    @Override
    public int getCount() {
        return balanceArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return balanceArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView ==null){
            viewHolder =new ViewHolder();
            convertView =inflater.inflate(R.layout.view_balance,parent,false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.tvMoney =(TextView) convertView.findViewById(R.id.txtmoney);
            viewHolder.tvMonth =(TextView) convertView.findViewById(R.id.txtmonth);
            viewHolder.tvUnit =(TextView) convertView.findViewById(R.id.txtunit);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(balanceArrayList.size()>0) {
            Balance balance = balanceArrayList.get(position);


            viewHolder.tvMoney.setText(String.valueOf(balance.getMoney()));
            viewHolder.tvUnit.setText("฿");
            viewHolder.tvMonth.setText(balance.getMonth());
            if(balance.getMoney()>0){
                viewHolder.tvMoney.setTextColor(context.getResources().getColor(R.color.income));
            }else {
                viewHolder.tvMoney.setTextColor(context.getResources().getColor(R.color.expense));
            }
            Picasso.with(context).load(balance.getImageId()).into(viewHolder.imageView);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        TextView tvMonth;
        TextView tvMoney;
        TextView tvUnit;
    }
}
