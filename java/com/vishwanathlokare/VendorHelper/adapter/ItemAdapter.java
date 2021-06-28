package com.vishwanathlokare.VendorHelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.DummyContent;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    ArrayList<DummyContent> list;
    private onItemClickListener mlistener;
    private onlongClickListener olistener;



    public interface onItemClickListener {
        void onItemClick (int position);
    }
    public interface  onlongClickListener{
        void onLongClick(int po);
    }

    public ItemAdapter(ArrayList<DummyContent> list) {
        this.list = list;
    }
    public void setOnItemClickListener(onItemClickListener listener) {
        mlistener =listener;
    }
    public void setonLongClickListener(onlongClickListener listener) {
        olistener =listener;
    }
    public String getname(int po) {
        return list.get(po).getName1();

    }
    public void RemoveItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item,parent,false);

        return new ViewHolder(v,mlistener,olistener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mContentView.setText(list.get(position).getName1());
        if(list.get(position).isPaid()){
            holder.image.setImageResource(R.drawable.ic_baseline_sentiment_satisfied_alt_24);
        }
        else{
            holder.image.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied_24);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mContentView;
        View mView;
        ImageView image;
        public ViewHolder(View view, final onItemClickListener listener, final onlongClickListener o_listener) {
            super(view);
            mView = view;
            image = (ImageView) view.findViewById(R.id.imginfo);
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


    }
}
