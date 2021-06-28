package com.vishwanathlokare.VendorHelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.DummyContent;

import java.util.ArrayList;
import java.util.List;

public class Pending_CutomerAdapter extends RecyclerView.Adapter<Pending_CutomerAdapter.ViewHolder> implements Filterable {
    private List<DummyContent> mValues;
    private List<DummyContent> exm;

    Context context;

    public void Refresh(List<DummyContent> geteveryone) {
        mValues.clear();
        mValues = (ArrayList) geteveryone;
        notifyDataSetChanged();
    }





    public Pending_CutomerAdapter( List<DummyContent> items,Context c) {
        mValues = items;
        exm =  new ArrayList<>(items);
        context = c;
    }
    public String getname(int po) {
        return mValues.get(po).getName1();

    }
    public void RemoveItem(int position) {
        mValues.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_history, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.amount.setText(mValues.get(position).getPackage_amo() + "");
        //if(mValues.get(position).isPaid()) {
        // holder.paidimg.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        //}

        holder.date.setText(mValues.get(position).getName1());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
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
    @Override
    public Filter getFilter() {
        return exmfilter;
    }
    private Filter exmfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DummyContent> filteredlist = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredlist.addAll(exm);
            }
            else {
                String filter_pattern = charSequence.toString().toLowerCase().trim();
                for(DummyContent item : exm){
                    if (item.getName1().toLowerCase().contains(filter_pattern)){
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mValues.clear();
            mValues.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

