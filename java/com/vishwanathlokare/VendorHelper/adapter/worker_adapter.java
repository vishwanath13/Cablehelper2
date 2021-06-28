package com.vishwanathlokare.VendorHelper.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.Workers;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class worker_adapter extends RecyclerView.Adapter<worker_adapter.ViewHolder> {
    ArrayList<Workers> workers;
    Context context;
     ArrayList<Workers> presenty = new ArrayList<>();
    public worker_adapter(ArrayList<Workers> workers, Context context) {
        this.workers = workers;
        this.context = context;

    }

    public ArrayList<Workers> getPresenty() {
        return presenty;
    }

    public  void set_all_present(){
        notifyDataSetChanged();
        presenty.clear();
        presenty.addAll(workers);
    }

    public  void set_all_absent(){
        presenty.clear();
    }



    @NonNull
    @Override
    public worker_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_worker_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final worker_adapter.ViewHolder holder, final int position) {

        holder.name.setText(workers.get(position).getName().toUpperCase());

        holder.present.setChecked(workers.get(position).isPresent());


        holder.present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.present.isChecked()) {
                    presenty.add(workers.get(position));


                }
                else{
                    presenty.remove(workers.get(position));


                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DO YOU REALLY WANT TO DELETE THIS WORKER").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Database_helper database_helper = new Database_helper(context);
                        database_helper.Delete_worker(workers.get(position).getName());
                        refresh((ArrayList) database_helper.get_workers());
                        Toast.makeText(context,"Deleted Succesfully",Toast.LENGTH_SHORT).show();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNeutralButton("UPDATE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        View v = LayoutInflater.from(context).inflate(R.layout.add_new_worker,null,false);
                        final EditText name = (EditText) v.findViewById(R.id.worker_name);
                        final EditText phone = (EditText) v.findViewById(R.id.phone_worker);
                        name.setText(workers.get(position).getName());
                        phone.setText(workers.get(position).getPhone() + "");
                        Database_helper database_helper = new Database_helper(context);
                        builder1.setView(v).setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                String regphone = "(0,91)?[7-9][0-9]{9}";
                                Pattern Pphone = Pattern.compile(regphone);
                                Matcher Mphone = Pphone.matcher(phone.getText().toString());

                                String regadd = "^(?![0-9_])[\\p{L}\\d .'-_]+$";
                                Pattern padd = Pattern.compile(regadd);
                                Matcher Mpackage = padd.matcher(name.getText().toString());

                                if (!Mphone.matches()) {

                                    Toast.makeText(context, "Please Provide Valid Phone Number ", Toast.LENGTH_SHORT).show();
                                } else if (!Mpackage.matches()) {
                                    Toast.makeText(context, "Worker name should not start with a character", Toast.LENGTH_SHORT).show();
                                } else {
                                    Workers w = new Workers(name.getText().toString(), Long.parseLong(phone.getText().toString()),
                                            workers.get(position).getCount(), workers.get(position).isPresent());
                                    Database_helper database_helper = new Database_helper(context);
                                    boolean done = database_helper.update_worker(w, workers.get(position).getName());
                                    Toast.makeText(context, "Success" + done, Toast.LENGTH_SHORT).show();
                                    refresh((ArrayList) database_helper.get_workers());
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setTitle("UPDATE").create().show();

                    }
                }).create().show();
                return false;
            }
        });




        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View v = LayoutInflater.from(context).inflate(R.layout.worker_info,null,false);
                TextView Lines = v.findViewById(R.id.Line_worker_info);

                final Database_helper database_helper = new Database_helper(context);
                ArrayList<String> lines = (ArrayList) database_helper.get_Workers_Lines(workers.get(position).getName());


                StringBuilder sb = new StringBuilder();
               for(int j=0; j < lines.size(); j++){
                   if (j == 0) {
                       sb.append(lines.get(j));
                   }
                   else{
                       sb.append(" , " + lines.get(j));
                   }
               }
               Lines.setText(sb);

                int amount = database_helper.get_worker_amount(workers.get(position).getName());

                TextView Amount = (TextView) v.findViewById(R.id.Amount_earned);
                if (workers.get(position).getCount() == 0){
                    Amount.setText(0 +"" );
                }
                else{
                    int i = workers.get(position).getCount();
                    float base = amount;
                    float actual_amount = base/30 * i;
                    Amount.setText( ((int) actual_amount) + "");
                }
                final TextView Totalday = (TextView) v.findViewById(R.id.DayCount);
                Totalday.setText(workers.get(position).getCount() + "");
                Button call = (Button) v.findViewById(R.id.Call);
                Button Refresh = (Button) v.findViewById(R.id.Refresh);

                Refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setTitle("DO YOU REALLY WANT TO REFRESH THE WORKERS DAY");
                        builder1.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                boolean done = database_helper.Refresh(workers.get(position).getName());
                                if(done) {
                                    Toast.makeText(context,workers.get(position).getName() + " is refreshed Successfully ",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(context, workers.get(position).getName() + " is refreshed Successfully ",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder1.create().show();
                    }
                });


                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+workers.get(position).getPhone()));
                        context.startActivity(intent);
                    }
                });



                builder.setView(v)
                        .setTitle(workers.get(position).getName().toUpperCase())
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return workers.size();
    }

    public void refresh(ArrayList<Workers> workers) {
        this.workers.clear();
        this.workers = workers;
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView name;
        CheckBox present;
        public ViewHolder(View view){
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.worker_name);
            present = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }
}
