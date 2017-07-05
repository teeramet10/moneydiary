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
import app.teeramet.money.moneydiary.classmoney.Income;
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
public class IncomeAndExpenseAdapter extends BaseAdapter {
    ArrayList<Money> moneyArrayList;
    DecimalFormat moneyFormat =new DecimalFormat("#,##0.##");
    SimpleDateFormat dateFormat=new SimpleDateFormat("MMM  dd,yyyy");
    Context context;
    ViewHolder viewHolder;
    LayoutInflater inflater;

    public IncomeAndExpenseAdapter(Context context, ArrayList<Money> moneyArrayList) {
        this.moneyArrayList = moneyArrayList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return moneyArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return 10;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        if (moneyArrayList.get(position) instanceof Income) {
            return 0;
        } else return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                convertView = inflater.inflate(R.layout.incomelist, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.income_image);
                viewHolder.tvNamelist = (TextView) convertView.findViewById(R.id.income_namelist);
                viewHolder.tvDate = (TextView) convertView.findViewById(R.id.income_date);
                viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.income_price);
                viewHolder.tvUnit = (TextView) convertView.findViewById(R.id.income_unit);

            } else {
                convertView = inflater.inflate(R.layout.expenselist, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.expense_image);
                viewHolder.tvNamelist = (TextView) convertView.findViewById(R.id.expense_namelist);
                viewHolder.tvDate = (TextView) convertView.findViewById(R.id.expense_date);
                viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.expense_price);
                viewHolder.tvUnit = (TextView) convertView.findViewById(R.id.expense_unit);

            }
            convertView.setTag(viewHolder);

        } else viewHolder = (ViewHolder) convertView.getTag();
//        Money money = moneyArrayList.get(position);

        if (moneyArrayList.get(position) instanceof Income) {

            Income income = (Income) moneyArrayList.get(position);
            if (income.getPathImage() != null) {
                File image = new File(income.getPathImage());
                if (image.exists()) {
                    Picasso.with(context).load(image).fit().error(R.drawable.income).into(viewHolder.imageView);
                }
            } else
                Picasso.with(context).load(R.drawable.income).fit().into(viewHolder.imageView);
            viewHolder.tvNamelist.setText(income.getStrName());
            Date date=new Date(income.getDate());
            viewHolder.tvDate.setText(dateFormat.format(date));
            viewHolder.tvPrice.setText(String.valueOf(moneyFormat.format(income.getPrice())));

        } else if (moneyArrayList.get(position) instanceof Expenses) {

            Expenses expenses = (Expenses) moneyArrayList.get(position);
            if (expenses.getPathImage() != null) {
                File image = new File(expenses.getPathImage());
                if (image.exists()) {
                    Picasso.with(context).load(image).fit().error(R.drawable.expense).into(viewHolder.imageView);
                }
            } else
                Picasso.with(context).load(R.drawable.expense).fit().into(viewHolder.imageView);
            viewHolder.tvNamelist.setText(expenses.getStrName());
            Date date=new Date(expenses.getDate());
            viewHolder.tvDate.setText(dateFormat.format(date));
            viewHolder.tvPrice.setText(String.valueOf(moneyFormat.format(expenses.getPrice())));
        }


        return convertView;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView tvNamelist;
        TextView tvDate;
        TextView tvPrice;

        TextView tvUnit;
    }
}
