package app.teeramet.money.moneydiary.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.teeramet.money.moneydiary.ImageManage;
import app.teeramet.moneydiary.R;
import app.teeramet.money.moneydiary.SharePreferenceHelper;
import app.teeramet.money.moneydiary.activity.AddAndUpdateAccountActivity;
import app.teeramet.money.moneydiary.classmoney.Account;
import app.teeramet.money.moneydiary.database.DatabaseManagement;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by barbie on 6/9/2559.
 */
public class AccountGridViewAdapter extends RecyclerView.Adapter<AccountGridViewAdapter.ViewHolder> {
    Context context;
    ArrayList<Account> accountArrayList = new ArrayList<>();
    LayoutInflater inflater;
    int mPosition;

    public AccountGridViewAdapter(Context context, ArrayList<Account> accountArrayList) {
        this.context = context;
        this.accountArrayList = accountArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.cardviewaccount, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        Account account = accountArrayList.get(position);
        ImageManage imageManage=new ImageManage();
        int resId=imageManage.randomColorBackground();

        viewHolder.tvNameAccount.setText(account.getName());
        viewHolder.imageView.setBackgroundResource(resId);

        File imagefile=new File(account.getImage());
        imageManage=new ImageManage(context);
        Transformation tranform =imageManage.tranform();

        if(imagefile.exists()){
            viewHolder.imageView.setPadding(0,0,0,0);
            Picasso.with(context).load(imagefile).transform(tranform).fit().centerCrop().into(viewHolder.imageView);
        }else Picasso.with(context).load(R.drawable.otherb).transform(tranform).error(R.drawable.otherb).into(viewHolder.imageView);



        viewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = viewHolder.getAdapterPosition();
                eventRecycleView(position);
                return false;
            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddAndUpdateAccountActivity.class);
                int position=viewHolder.getAdapterPosition();
                Account account =accountArrayList.get(position);
                intent.putExtra("ID",account.getId());
                intent.putExtra("NAMEACCOUNT",account.getName());
                intent.putExtra("PATHIMAGE",account.getImage());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public void eventRecycleView(int position) {
        mPosition = position;
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Delete Account?");
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Account account = accountArrayList.get(mPosition);
                SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(context);

                if (accountArrayList.size() > 1) {
                    deleteAccount(account);
                    int id=accountArrayList.get(mPosition).getId();
                    accountArrayList.remove(mPosition);
                    if (sharePreferenceHelper.getIdAccountPreference() ==id ) {
                        int idAccount = accountArrayList.get(0).getId();
                        sharePreferenceHelper.setIdAccountPreference(idAccount);
                    }
                }else Toast.makeText(context,"Do not delete account",Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
        });
        alert.setNegativeButton(context.getResources().getString(R.string.cancel),null);
        alert.create().show();
    }

    public void deleteAccount(Account account) {
        DatabaseManagement databaseManagement = new DatabaseManagement(context);
        databaseManagement.deleteAccount(account);
        databaseManagement.deleteMoney(account.getId());
        databaseManagement.closeDatabase();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvNameAccount;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.grid_image);
            tvNameAccount = (TextView) itemView.findViewById(R.id.grid_name);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }
    }

}
