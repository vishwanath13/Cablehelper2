package com.vishwanathlokare.VendorHelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.complaint_model;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;

import java.util.ArrayList;

public class complaintAdapter extends RecyclerView.Adapter<complaintAdapter.ViewHolder> {
    private onLongClickListener listener;
    ArrayList<complaint_model> arrayList;
    Context context;

    public void onLongClickListener(onLongClickListener mlistener) {
        listener = mlistener;
    }
    public interface onLongClickListener {
        void onLongClick (int position);
    }



    public complaintAdapter(ArrayList<complaint_model> list,Context co){
         context = co;
         arrayList = list;


    }

    public void Remove(int po){
        arrayList.remove(po);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.complaint,parent,false);
        return new ViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.complaint.setText(arrayList.get(position).getComplaint());
        holder.Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database_helper database_helper = new Database_helper(context);
                boolean g = database_helper.update_complaint(arrayList.get(position).getName(),holder.complaint.getText().toString());
                if (g) {
                    Toast.makeText(context, "updated Successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, " Could not updated", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        EditText complaint;
        Button Save;

        public ViewHolder(@NonNull final View itemView, final onLongClickListener listener1) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView7);
            complaint = (EditText) itemView.findViewById(R.id.textView8);
            Save = (Button) itemView.findViewById(R.id.button2);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Database_helper database_helper = new Database_helper(context);
                    database_helper.delete_complaint(arrayList.get(getAdapterPosition()));
                    arrayList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    return true;
                }

            });



        }
    }
}
