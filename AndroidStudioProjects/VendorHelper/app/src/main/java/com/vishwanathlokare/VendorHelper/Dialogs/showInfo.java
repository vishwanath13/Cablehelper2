package com.vishwanathlokare.VendorHelper.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.vishwanathlokare.VendorHelper.ui.Customer_History;
import com.vishwanathlokare.VendorHelper.adapter.MyItemRecyclerViewAdapter;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;
import com.vishwanathlokare.VendorHelper.ui.home.HomeFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class showInfo extends DialogFragment {
    Date m_date;
    String m_package;
    Long m_card;
    long l_phone;
    Integer rokda;
    MyItemRecyclerViewAdapter adpter;
    int position;
    TextView text_phone;
    boolean paid;
    TextView text_card;
    TextView text_package;
    EditText amount;
    Button btn;
    Button btn_pay;
    Button btn_history;
    String name;
    String line;

    public showInfo(String name, String line, Boolean paid, Date date, Long card, String tpackage
            , MyItemRecyclerViewAdapter myItemRecyclerViewAdapter, int mposition, long l_phone,Integer rokda){
        this.rokda = rokda;

        m_card = card;
        m_package = tpackage;
        m_date= date;
        this.name = name;
        this.paid = paid;
        adpter = myItemRecyclerViewAdapter;
        position = mposition;
        this.l_phone =l_phone;
        this.line = line;

    }

    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.show_info_dialog,null);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String date_string =  format.format(m_date);
        // text_phone.setText(date_string);
        builder.setView(v);
        builder.setTitle(adpter.getname(position).toUpperCase() + " > " + date_string)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }
                );

        text_card = (TextView) v.findViewById(R.id.text_card);
        text_package = (TextView) v.findViewById(R.id.text_package);
        text_phone = (TextView) v.findViewById(R.id.text_phone);
        amount = (EditText)  v.findViewById(R.id.amountTaken);
        btn_history = (Button) v.findViewById(R.id.button3);
        btn_pay = (Button) v.findViewById(R.id.Paidinfo);

        amount.setText(rokda + "");


        if (paid){
            builder.setIcon(R.drawable.ic_baseline_sentiment_satisfied_alt_24);
            btn_pay.setVisibility(View.GONE);
            amount.setVisibility(View.GONE);
        }
        else{
            builder.setIcon(R.drawable.ic_baseline_sentiment_very_dissatisfied_24);
        }


        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database_helper db = new Database_helper(getContext());
                if (!amount.getText().toString().equals("")) {

                if (!paid) {
                    boolean t = db.pay(Integer.parseInt(amount.getText().toString()), l_phone);

                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY,0);
                    cal.set(Calendar.MINUTE ,0);
                    cal.set(Calendar.MILLISECOND,0);
                    cal.set(Calendar.SECOND,0);
                    boolean hv = db.add_one_Trigger(adpter.getname(position), Integer.parseInt(amount.getText().toString()), cal.getTime());
                    adpter.Refresh(db.geteveryone());

                    if(t && hv){
                        Toast.makeText(getContext(),"Paid Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(),"Could not Add to paid",Toast.LENGTH_SHORT).show();
                    }
                    dismiss();
                }

            }
                else Toast.makeText(getContext(),"Please provide amount to be taken",Toast.LENGTH_SHORT).show();
            }

        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Customer_History freg = Customer_History.newInstance(name);


                FragmentTransaction frt = getFragmentManager().beginTransaction();
                frt.replace(R.id.nav_host_fragment,freg);
                frt.addToBackStack("hi").commit();

                adpter.notifyDataSetChanged();
                dismiss();
            }
        });




        text_card.setText(m_card + "");

        text_phone.setText(line);
        text_package.setText(m_package);
        btn = (Button) v.findViewById(R.id.button);
        Toast.makeText(getActivity(),"package" + m_package,Toast.LENGTH_SHORT).show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+l_phone));
                startActivity(intent);
            }
        });


        return builder.create();
    }
}
