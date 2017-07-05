package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.classmoney.Expenses;
import app.teeramet.money.moneydiary.classmoney.Money;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by barbie on 15/8/2559.
 */
public class ExpenseAdapter extends BaseAdapter {
    ArrayList<Money> expensesArrayList;
    ViewHolder viewHolder;
    LayoutInflater inflater;
    Context context;
    DecimalFormat moneyFormat =new DecimalFormat("#,##0.##");
    SimpleDateFormat dateFormat=new SimpleDateFormat("MMM dd, yyyy");
    public ExpenseAdapter(Context context, ArrayList<Money> expensesArrayList) {
        this.expensesArrayList = expensesArrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return expensesArrayList.size();
    }

    @Override
    public Expenses getItem(int position) {
        return (Expenses) expensesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.expenselist, null);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.expense_image);
            viewHolder.tvNameList = (TextView) convertView.findViewById(R.id.expense_namelist);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.expense_date);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.expense_price);
            viewHolder.tvUnit = (TextView) convertView.findViewById(R.id.expense_unit);

            convertView.setTag(viewHolder);

        } else viewHolder = (ViewHolder) convertView.getTag();

        Expenses expenses = (Expenses) expensesArrayList.get(position);

        if(expenses.getPathImage()!=null){
            File image=new File(expenses.getPathImage());
            if(image.exists()){
                Picasso.with(context).load(image).fit().error(R.drawable.expense).into(viewHolder.image);
            }
        }else Picasso.with(context).load(R.drawable.expense).fit().into(viewHolder.image);

        viewHolder.tvNameList.setText(expenses.getStrName());
        Date date=new Date(expenses.getDate());
        viewHolder.tvDate.setText(dateFormat.format(date));
        viewHolder.tvPrice.setText(String.valueOf(moneyFormat.format(expenses.getPrice())));


    return convertView;
}

private class ViewHolder {
    ImageView image;
    TextView tvNameList;
    TextView tvDate;
    TextView tvPrice;
    TextView tvUnit;
}
}
