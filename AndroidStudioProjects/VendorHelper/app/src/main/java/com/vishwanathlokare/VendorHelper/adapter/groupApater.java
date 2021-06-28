package com.vishwanathlokare.VendorHelper.adapter;

import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.History_content;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class groupApater extends RecyclerView.Adapter<groupApater.ViewHolder> implements Filterable {
    int amount = 0;
    private Context context1;
    ArrayList<Date> arrayList;
    ArrayList<Date> exm;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvamount;
        TextView tvname;
        RecyclerView rv_member;
        Context context1 ;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvamount = itemView.findViewById(R.id.Total_amount);
            tvname = itemView.findViewById(R.id.text_adapter);
            rv_member = itemView.findViewById(R.id.rv_member);
        }
    }
    public groupApater(Context context , ArrayList<Date> dates){

        arrayList = dates;
        context1 = context;
        exm = new ArrayList<>(dates);
    }
    @NonNull
    @Override
    public groupApater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_raw,parent,false);
        return new groupApater.ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull groupApater.ViewHolder holder, int position) {
          String string_date = DateFormat.getDateInstance(DateFormat.FULL).format(arrayList.get(position));
          holder.tvname.setText(string_date);

          ArrayList<History_content> filtered = new ArrayList<>();
          Database_helper database_helper = new Database_helper(context1);
          ArrayList<History_content> contents = (ArrayList<History_content>) database_helper.get_Trigger();
          for(History_content item :contents ){
              if (item.getDate().compareTo(arrayList.get(position)) == 0){
                  filtered.add(item);

              }
          }
          int amount = 0;
          for (History_content item : filtered){
              amount = amount + item.getAmount();
          }
         holder.tvamount.setText(amount+"");
          memberAdapter memAdapter = new memberAdapter(filtered);
        LinearLayoutManager ly = new LinearLayoutManager(context1){
            @Override
            public boolean canScrollVertically(){
                return  true;
            }
        };
        holder.rv_member.setLayoutManager(ly);
        holder.rv_member.setAdapter(memAdapter);
        RecyclerView.OnItemTouchListener listener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                switch (action){
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };
        holder.rv_member.addOnItemTouchListener(listener);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    @Override
    public Filter getFilter() {
        return exmfilter;
    }
    private Filter exmfilter = new Filter() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Date> filteredlist = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredlist.addAll(exm);
            }
            else {
                String filter_pattern = charSequence.toString().toLowerCase().trim();
                for(Date item : exm){
                    String string_date = DateFormat.getDateInstance(DateFormat.FULL).format(item);
                    if (string_date.toLowerCase().contains(filter_pattern)){
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
            arrayList.clear();
            arrayList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
