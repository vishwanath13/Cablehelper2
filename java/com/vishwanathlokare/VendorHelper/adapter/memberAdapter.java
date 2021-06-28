package com.vishwanathlokare.VendorHelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.History_content;

import java.util.ArrayList;

public class memberAdapter extends RecyclerView.Adapter<memberAdapter.ViewHolder> {
    ArrayList<History_content> member;
    public memberAdapter(ArrayList<History_content> arrayList) {
        member = arrayList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_layout,parent,false);


        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.membername.setText(member.get(position).getName().toString());
        holder.member_amount.setText(member.get(position).getAmount() + "");

    }

    @Override
    public int getItemCount() {
        return member.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView membername;
        TextView member_amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            membername = itemView.findViewById(R.id.member_name);
            member_amount = itemView.findViewById(R.id.member_amount);

        }
    }
}
