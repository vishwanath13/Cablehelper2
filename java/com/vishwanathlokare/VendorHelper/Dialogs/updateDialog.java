package com.vishwanathlokare.VendorHelper.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.vishwanathlokare.VendorHelper.adapter.MyItemRecyclerViewAdapter;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;
import com.vishwanathlokare.VendorHelper.models.complaint_model;
import com.vishwanathlokare.VendorHelper.ui.update_Fragment;


public class updateDialog extends DialogFragment {
    String name1;
    Long compaint;
    MyItemRecyclerViewAdapter adapter;
    int position;
    boolean paid;
    Context context;
    public updateDialog(String name, boolean paid, Long phone, MyItemRecyclerViewAdapter ad,int po,Context context){
        name1 = name;
        compaint = phone;
        adapter = ad;
        this.paid = paid;
        this.context = context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }




    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_updatedialog,null,false);

        final EditText complaint = (EditText) v.findViewById(R.id.complaint_text);
        Button btn_update = (Button) v.findViewById(R.id.add);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             update_Fragment frag = update_Fragment.newInstance(compaint);
             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,frag,"find").addToBackStack(null).commit();
             dismiss();

            }
        });

        Button btn_delete = (Button) v.findViewById(R.id.Delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database_helper database_helper = new Database_helper(getContext());
                boolean b = database_helper.delete_everyone(compaint);
                Toast.makeText(getContext(), "deleted " + b , Toast.LENGTH_SHORT).show();

                adapter.Refresh(database_helper.geteveryone());

                dismiss();



            }
        });



        if(paid) {
            builder.setIcon(R.drawable.ic_baseline_sentiment_satisfied_alt_24);
        }
        else {
            builder.setIcon(R.drawable.ic_baseline_sentiment_very_dissatisfied_24);
        }
        builder.setTitle(name1.toUpperCase())
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                         String compaint1 = complaint.getText().toString();
                        complaint_model com = new complaint_model(name1, compaint1);
                        Database_helper database_helper = new Database_helper(getContext());

                        boolean added = database_helper.add_complaint(com);
                        Toast.makeText(getContext(), "success" + added, Toast.LENGTH_SHORT).show();


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }}).setView(v);


        return builder.create();
    }
}
