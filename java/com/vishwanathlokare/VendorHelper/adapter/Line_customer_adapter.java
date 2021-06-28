package com.vishwanathlokare.VendorHelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.DummyContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Line_customer_adapter extends RecyclerView.Adapter<Line_customer_adapter.ViewHolder> implements Filterable {
    public ArrayList<DummyContent> dm;
    public  ArrayList<DummyContent> exm;
    Context context;

    public Line_customer_adapter(ArrayList<DummyContent> dm, Context context) {
        this.dm = dm;
        exm = new ArrayList<>(dm);
        this.context = context;
    }



    @NonNull
    @Override
    public Line_customer_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.complaint,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Line_customer_adapter.ViewHolder holder, int position) {

        holder.name.setText(dm.get(position).getName1());
        holder.papers.setText(dm.get(position).getDetails());
        holder.id.setText((position + 1) + "");
    }

    @Override
    public int getItemCount() {
        return dm.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<DummyContent> filteredlist = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0) {
                filteredlist.addAll(exm);
            }
            else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for(DummyContent dm : exm){
                    if(dm.getName1().toLowerCase().contains(pattern)){
                        filteredlist.add(dm);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dm.clear();
            dm.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
    public void swaped() {
        Collections.sort(dm,DummyContent.ComparePo);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView name;
        Button id;
        TextView papers;
        public ViewHolder(View view){
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.textView7);
            id = (Button) view.findViewById(R.id.button2);
            papers = (TextView) view.findViewById(R.id.textView8);
        }
    }
}
