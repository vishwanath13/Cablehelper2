package com.vishwanathlokare.VendorHelper.ui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.vishwanathlokare.VendorHelper.MainActivity;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.worker_adapter;
import com.vishwanathlokare.VendorHelper.models.Workers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A fragment representing a list of Items.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class Paper_fragment extends Fragment{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView recyclerView;
    Button btx_add;
    worker_adapter adapter;
    TextView submit;
    CheckBox selectAll;
    ArrayList<Workers> presents;
    ArrayList<Workers> absent = new ArrayList<>();
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Paper_fragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Paper_fragment newInstance(int columnCount) {
        Paper_fragment fragment = new Paper_fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);


        String string_date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(new Date());
        ((MainActivity)getActivity()).setActionBarTitle(string_date);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem l = menu.findItem(R.id.Action_search);
        MenuItem u = menu.findItem(R.id.information);
        l.setVisible(false);
        u.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_paper, container, false);
        final Database_helper database_helper = new Database_helper(getContext());
        submit = view.findViewById(R.id.Submit);
        selectAll = (CheckBox) view.findViewById(R.id.checkBox2);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isfirst = prefs.getBoolean("First", true);
        if(isfirst){
            prefs.edit().putLong("Date",new Date().getTime()).putBoolean("First",false).commit();
        }
        final SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(getContext());

        long worker_Date = pref1.getLong("Date", new Date().getTime());

        boolean isfirstRun = prefs.getBoolean("Submit", true);



        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        LocalDate d1 = LocalDate.parse(dateFormat.format(new Date()), DateTimeFormatter.ISO_LOCAL_DATE);

        LocalDate d2 = LocalDate.parse(dateFormat.format(worker_Date), DateTimeFormatter.ISO_LOCAL_DATE);



            Duration diff = Duration.between(d2.atStartOfDay(), d1.atStartOfDay());

            long diffDays = diff.toDays();

            if(diffDays > 0) {
                if(isfirstRun) {

                    for(int i =0; i<diffDays; i++) {
                        database_helper.add_presenty(database_helper.get_workers_String());

                    }

                    ZonedDateTime zdt = ZonedDateTime.of(d1.atStartOfDay(), ZoneId.systemDefault());
                    long date2 = zdt.toInstant().toEpochMilli();
                   SharedPreferences.Editor editor = prefs.edit();
                   editor.putLong("Date",date2);

                   editor.commit();

                }
                else {
                    if(diffDays > 1) {
                        for (int i = 0; i < diffDays - 1; i++) {
                            database_helper.add_presenty(database_helper.get_workers_String());
                        }
                    }
                    ZonedDateTime zdt = ZonedDateTime.of(d1.atStartOfDay(), ZoneId.systemDefault());
                    long date2 = zdt.toInstant().toEpochMilli();

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("Submit",true);
                    editor.putLong("Date",date2);
                    editor.commit();
                }
                Toast.makeText(getContext(),"Updating present list for " + diffDays + " days " + d2 ,Toast.LENGTH_LONG).show();
            }
            else if (diffDays < 0) {
                Toast.makeText(getContext(),"Something wrong Plz Reinstall Your Application ",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(),"Presenty Already Updated for Today's session " + d2 ,Toast.LENGTH_LONG).show();
            }



        isfirstRun = prefs.getBoolean("Submit", true);
        if (isfirstRun){
            submit.setText(R.string.submit);
            database_helper.Replace_PresentsToFalse(database_helper.get_workers_String());
        }
        else {
            submit.setText("Undo");
        }




        adapter = new worker_adapter((ArrayList) database_helper.get_workers(),getContext());

        // Set the adapter
        recyclerView = view.findViewById(R.id.recyclerView4);


        Context context = view.getContext();


        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(adapter);

        btx_add = (Button) view.findViewById(R.id.add_worker);
        btx_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View v = LayoutInflater.from(getContext()).inflate(R.layout.add_new_worker,null,false);
                final EditText name = (EditText) v.findViewById(R.id.worker_name);
                final EditText phone = (EditText) v.findViewById(R.id.phone_worker);

                builder.setView(v).setTitle("ADD NEW WORKER").setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String regphone = "(0,91)?[7-9][0-9]{9}";
                        Pattern Pphone = Pattern.compile(regphone);
                        Matcher Mphone = Pphone.matcher(phone.getText().toString());

                        String regadd = "^(?![0-9_])[\\p{L}\\d .'-_]+$";
                        Pattern padd = Pattern.compile(regadd);
                        Matcher Mpackage = padd.matcher(name.getText().toString());


                        if(!Mpackage.matches()){
                            Toast.makeText(getContext(), "Worker name should not start with a character", Toast.LENGTH_SHORT).show();
                        }
                        else  if (!Mphone.matches()) {

                            Toast.makeText(getContext(), "Please Provide Valid Phone Number ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Database_helper db = new Database_helper(getContext());

                            boolean done = db.add_Worker(new Workers(name.getText().toString(),
                                    Long.parseLong(phone.getText().toString())
                                    , 0, false));
                            adapter.refresh((ArrayList) database_helper.get_workers());

                            if (done) {
                                Toast.makeText(getContext(), "done ", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(), "Something Went Wrong while adding new Worker ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
        });

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(selectAll.isChecked()){


                    ArrayList<Workers> all =  (ArrayList) database_helper.get_workers();
                    for (int i=0; i<all.size();i++){
                        all.get(i).setPresent(true);
                    }
                    adapter.refresh(all);
                    adapter.set_all_present();

                }
                else{
                    ArrayList<Workers> all =  (ArrayList) database_helper.get_workers();
                    for (int i=0; i<all.size();i++){
                        all.get(i).setPresent(false);
                    }
                    adapter.refresh(all);
                    adapter.set_all_absent();

                }
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(submit.getText().equals("Submit")) {
                 presents = adapter.getPresenty();
                if(presents.size() == 0) {
                    Toast.makeText(getContext(),"no one is present",Toast.LENGTH_SHORT).show();
                }
                else {
                    ArrayList<String> present = new ArrayList<>();
                    for(int i=0; i<presents.size(); i++){
                        present.add(presents.get(i).getName());
                    }


                    boolean done = database_helper.add_presenty(present);


                    if(done) {
                        boolean yes = database_helper.Replace_PresentsToTrue(present);
                        if(yes) {
                            Toast.makeText(getContext(),  "Presenty Added succcefully", Toast.LENGTH_SHORT).show();
                            prefs.edit().putBoolean("Submit",false).commit();
                            submit.setText("undo");
                            adapter.refresh((ArrayList)database_helper.get_workers());
                        }

                    }

                }
            }
                else{
                    absent = (ArrayList) database_helper.get_workers();
                    ArrayList<String> absents = new ArrayList<>();
                    for(int i=0; i< absent.size(); i++){
                        if(absent.get(i).isPresent()){
                            absents.add(absent.get(i).getName());
                        }
                    }
                    if(absents.size() == 0) {
                        Toast.makeText(getContext(),"nothing changed",Toast.LENGTH_SHORT).show();
                        prefs.edit().putBoolean("Submit",true).commit();

                        submit.setText("Submit");
                    }
                    else {

                        boolean done = database_helper.remove_presenty(absents);
                        Toast.makeText(getContext(),done + "",Toast.LENGTH_SHORT).show();
                        if(done) {
                            boolean yes = database_helper.Replace_PresentsToFalse((ArrayList)database_helper.get_workers_String());
                            if (yes) {
                                submit.setText(R.string.submit);
                                prefs.edit().putBoolean("Submit",true).commit();
                                adapter.refresh((ArrayList<Workers>) database_helper.get_workers());
                            }
                        }

                    }

                }
        } });







        return view;
    }




}
