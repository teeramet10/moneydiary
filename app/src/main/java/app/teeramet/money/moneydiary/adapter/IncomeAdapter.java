package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.classmoney.Income;
import app.teeramet.money.moneydiary.classmoney.Money;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by barbie on 10/8/2559.
 */
public class IncomeAdapter extends BaseAdapter {
    ArrayList<Money> incomeArrayList;
    ViewHolder viewHolder;
    LayoutInflater inflater;
    Context context;
    DecimalFormat moneyFormat =new DecimalFormat("#,##0.##");
    SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
    public IncomeAdapter(Context context, ArrayList<Money> incomeArrayList) {
        this.context = context;
        this.incomeArrayList=incomeArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return incomeArrayList.size();
    }

    @Override
    public Income getItem(int position) {
        return (Income)incomeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView = inflater.inflate(R.layout.incomelist,null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.income_image);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.income_namelist);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.income_date);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.income_price);
            viewHolder.tvBath = (TextView) convertView.findViewById(R.id.income_unit);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Income income=(Income) incomeArrayList.get(position);
        if(income.getPathImage()!=null){
            File image=new File(income.getPathImage());
            if(image.exists()){
                Picasso.with(context).load(image).fit().error(R.drawable.income).into(viewHolder.imageView);
            }
        }else Picasso.with(context).load(R.drawable.income).fit().into(viewHolder.imageView);



        viewHolder.tvName.setText(income.getStrName());
        Date date=new Date(income.getDate());
        viewHolder.tvDate.setText(dateFormat.format(date));
        viewHolder.tvPrice.setText(String.valueOf(moneyFormat.format(income.getPrice())));
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvDate;
        TextView tvPrice;

        TextView tvBath;
    }
}
