package com.vishwanathlokare.VendorHelper.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.Line;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;

import java.util.ArrayList;
import java.util.List;

public class Paper_adapter extends RecyclerView.Adapter<Paper_adapter.ViewHolder> implements Filterable {

    private final List<String> mValues;
    ArrayList<String> exm;
    Context context;

    public ArrayList<String> getPapers() {
        return papers;
    }

    ArrayList<String> papers = new ArrayList<>();

    private onItemClickListener m_listener;
    private onlongClickListener o_listener;

    public void RemoveItem(int po){
        mValues.remove(po);
        notifyDataSetChanged();
    }
    public interface onItemClickListener {
        void onItemClick (int position);
    }
    public interface  onlongClickListener{
        void onLongClick(int po);
    }
    public void setOnItemClickListener(onItemClickListener listener) {
        m_listener =listener;
    }
    public void setonLongClickListener(onlongClickListener listener) {
        o_listener =listener;
    }

    public Paper_adapter(List<String> items,Context context) {
        exm =  new ArrayList<>(items);
        mValues = items;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_paper_view, parent, false);
        return new ViewHolder(view,m_listener,o_listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (papers.contains(mValues.get(position))){
                    holder.mView.setBackgroundResource(0);
                    papers.remove(mValues.get(position));
                }
                else {

                    holder.mView.setBackgroundColor(Color.rgb(3,218,197));
                    papers.add(mValues.get(position));
                }

            }
        });



        holder.mContentView.setText(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public String getItem(int po) {
        return mValues.get(po);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mContentView;


        public ViewHolder(View view,
                          final onItemClickListener listener,
                          final onlongClickListener o_listener) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (o_listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            o_listener.onLongClick(position);
                        }
                    }
                    return false;
                }
            });
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