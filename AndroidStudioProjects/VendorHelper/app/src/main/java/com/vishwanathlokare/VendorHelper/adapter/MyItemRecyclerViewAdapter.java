package com.vishwanathlokare.VendorHelper.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.Dialogs.updateDialog;
import com.vishwanathlokare.VendorHelper.Dialogs.showInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyContent}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> implements Filterable {


    private List<DummyContent> mValues;
    private List<DummyContent> exm;

    Context context;

    public void Refresh(List<DummyContent> geteveryone) {
        mValues.clear();
        mValues = (ArrayList) geteveryone;
        notifyDataSetChanged();
    }





    public MyItemRecyclerViewAdapter( List<DummyContent> items,Context c) {
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
        holder.mContentView.setText(mValues.get(position).name);
        //if(mValues.get(position).isPaid()) {
           // holder.paidimg.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
        //}

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                updateDialog d = new updateDialog(mValues.get(position).getName1(),mValues.get(position).isPaid(),mValues.get(position).getPhone(),
                        MyItemRecyclerViewAdapter.this,position,context);
                FragmentManager m = ((AppCompatActivity)context).getSupportFragmentManager();
                d.show(m,"updateDialog");
                return false;
            }
        });


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfo showInfo  = new showInfo(mValues.get(position).getName1(),
                        mValues.get(position).getLine(),
                        mValues.get(position).isPaid(),
                        mValues.get(position).getRenewal(),
                        mValues.get(position).getCard(),
                        mValues.get(position).details,
                        MyItemRecyclerViewAdapter.this,position,mValues.get(position).getPhone(),
                        mValues.get(position).getPackage_amo());

                FragmentManager m = ((AppCompatActivity)context).getSupportFragmentManager();
                showInfo.show(m,"Show info");
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
            paidimg = view.findViewById(R.id.imginfo);
            mContentView =  view.findViewById(R.id.content);

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