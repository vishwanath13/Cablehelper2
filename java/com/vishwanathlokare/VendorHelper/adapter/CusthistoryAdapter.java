package com.vishwanathlokare.VendorHelper.adapter;

import android.icu.text.DateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.CustHistory;

import java.util.ArrayList;

public class CusthistoryAdapter extends RecyclerView.Adapter<CusthistoryAdapter.ViewHolder> {
    public CusthistoryAdapter(ArrayList<CustHistory> list) {
        this.list = list;
    }

    ArrayList<CustHistory> list;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_history,parent,false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull CusthistoryAdapter.ViewHolder holder, int position) {
        String string_date = DateFormat.getDateInstance(DateFormat.FULL).format(list.get(position).getDate());
        holder.date.setText(string_date);
        holder.amount.setText(list.get(position).getAmount()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.datename);
            amount = (TextView) itemView.findViewById(R.id.amountpaid);

        }
    }
}
