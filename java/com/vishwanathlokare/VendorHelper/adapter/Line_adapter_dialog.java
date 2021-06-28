package com.vishwanathlokare.VendorHelper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;

import java.util.ArrayList;
import java.util.List;



public class Line_adapter_dialog extends RecyclerView.Adapter<Line_adapter_dialog.ViewHolder> implements Filterable {

    private final List<String> mValues;
    ArrayList<String> exm;
    Context context;
   Integer pos = 0;


    public String getPapers() {
        return papers;
    }

    String papers = "";



    public Line_adapter_dialog(List<String> items,Context context) {
        exm =  new ArrayList<>(items);
        mValues = items;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_paper_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

       if(position == pos){
            papers = mValues.get(position);
            holder.mView.setBackgroundColor(Color.rgb(3,218,197));
        }
        else {
            holder.mView.setBackgroundResource(0);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == pos) {
                    pos = 0;
                    notifyDataSetChanged();
                }
                else{
                    pos = position;
                    notifyDataSetChanged();


                }

            }
        });

        holder.mContentView.setText(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mContentView;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);

        }
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


    @Override
    public Filter getFilter() {
        return exmfilter;
    }
    private Filter exmfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredlist = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredlist.addAll(exm);
            }
            else {
                String filter_pattern = charSequence.toString().toLowerCase().trim();
                for(String item : exm){
                    if (item.toLowerCase().contains(filter_pattern)){
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