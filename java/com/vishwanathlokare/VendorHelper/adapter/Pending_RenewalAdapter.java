package com.vishwanathlokare.VendorHelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.Dialogs.dialog;

import java.util.ArrayList;
import java.util.List;

public class Pending_RenewalAdapter extends RecyclerView.Adapter<Pending_RenewalAdapter.ViewHolder> implements Filterable {
    private List<DummyContent> mValues;
    private List<DummyContent> exm;

TextView count;
    Context context;

    public void Refresh(List<DummyContent> geteveryone) {
        mValues.clear();
        mValues = (ArrayList) geteveryone;
        notifyDataSetChanged();
    }

    public void check(){

        count.setText(getItemCount() + "");
    }



    public Pending_RenewalAdapter( List<DummyContent> items,Context c,TextView count) {
        this.count = count;
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
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mContentView.setText(mValues.get(position).getName1());

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog dialog = new dialog(mValues.get(position).getPhone(),Pending_RenewalAdapter.this,position,
                        mValues.get(position).getName1(),mValues.get(position).getDetails()
                        ,mValues.get(position).getPackage_amo(),count);
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                dialog.show(manager,"ADD_TO_PAID");

            }
        });

        if(mValues.get(position).isPaid()) {
            holder.paidimg.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24);
        }
        else{
            holder.paidimg.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView paidimg;
        public final TextView mContentView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            paidimg = (ImageView) view.findViewById(R.id.imginfo);
            mContentView = (TextView) view.findViewById(R.id.content);



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


