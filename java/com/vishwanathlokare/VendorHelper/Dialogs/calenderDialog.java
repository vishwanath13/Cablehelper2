package com.vishwanathlokare.VendorHelper.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.vishwanathlokare.VendorHelper.adapter.ItemAdapter;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;

import java.util.Calendar;
import java.util.Date;

public class calenderDialog extends DialogFragment {
    ItemAdapter adpater;
    int position;
    Long phone;
    CalendarView calendarView;
    TextView textView;

    Date renew_date;

    public  calenderDialog(ItemAdapter madapter, int pos,Long mphone,Date Renew){
        adpater = madapter;
        position = pos;
        phone = mphone;
        renew_date = Renew;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.calender_dialog, null);
        calendarView = (CalendarView) v.findViewById(R.id.calendarView);
        textView = (TextView) v.findViewById(R.id.textView2);

        calendarView.setDate(renew_date.getTime());
        textView.setText(DateFormat.getDateInstance(DateFormat.FULL).format(calendarView.getDate()));

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,i);
                cal.set(Calendar.MONTH,i1);
                cal.set(Calendar.DAY_OF_MONTH,i2);
                String string_date = DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime());
                textView.setText(string_date);
                calendarView.setDate(cal.getTime().getTime());


            }
        });
        builder.setView(v).setTitle(adpater.getname(position).toUpperCase())
                .setPositiveButton("RENEW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Database_helper database_helper = new Database_helper(getContext());
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date(calendarView.getDate()));
                        cal.add(Calendar.DAY_OF_MONTH,30);
                        boolean u = database_helper.change_renewal(cal.getTime(),phone);
                        dismiss();

                        adpater.notifyDataSetChanged();

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });





        return builder.create();
    }
}
