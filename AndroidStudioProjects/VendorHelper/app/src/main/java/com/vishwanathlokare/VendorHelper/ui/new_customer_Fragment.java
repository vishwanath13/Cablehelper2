package com.vishwanathlokare.VendorHelper.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.os.Build;
import  android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.Line_adapter_dialog;
import com.vishwanathlokare.VendorHelper.adapter.Paper_adapter;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.models.Line;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link new_customer_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class new_customer_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText edt_name;
    EditText edt_card;
    EditText edt_phone;
    TextView edt_addr;
    EditText edt_ammo;
    Switch paid;
    Button btx_add;
    Button btx_Line;
    TextView edt_line;
    Button btx_paper;
    RecyclerView recyclerView;
    TextView date;
    Date Actual_date;
    SearchView paper_search;
    EditText search;
    Paper_adapter adapter;
    Line_adapter_dialog adapter_dialog;
    Integer pos = 0;
    String line;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    public  static Date getDateFormat(DatePicker datePicker){
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        Calendar cal = Calendar.getInstance();
        cal.set(year,month,day);
        cal.add(Calendar.DAY_OF_MONTH, 30);
        return cal.getTime();
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public new_customer_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment new_customer_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static new_customer_Fragment newInstance(String param1, String param2) {
        new_customer_Fragment fragment = new new_customer_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

           setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem l = menu.findItem(R.id.Action_search);
        MenuItem u = menu.findItem(R.id.information);
        l.setVisible(false);
        u.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_customer_, container, false);
        edt_name = (EditText) v.findViewById(R.id.edt_name);
        edt_card = (EditText) v.findViewById(R.id.edt_card);
        edt_phone = (EditText) v.findViewById(R.id.edt_phone);
        edt_addr = (TextView) v.findViewById(R.id.edt_addr);
        edt_ammo = (EditText) v.findViewById(R.id.edt_amo);
        paid = (Switch) v.findViewById(R.id.paid);

        btx_add = (Button) v.findViewById((R.id.add));
        date = (TextView) v.findViewById(R.id.editTextDate2);
        btx_Line = (Button) v.findViewById(R.id.new_Line);
        btx_paper = (Button) v.findViewById(R.id.new_paper);
        edt_line = (TextView) v.findViewById(R.id.edit_Line);


        edt_addr.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View view) {
                final String alredy = edt_addr.getText().toString();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.fragment_paper_view_list, null);
                Database_helper database_helper = new Database_helper(getContext());
                final ArrayList<String> ALL_paper = (ArrayList) database_helper.get_Papers();
                recyclerView = (RecyclerView) v.findViewById(R.id.list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                adapter = new Paper_adapter(ALL_paper,getContext());
                recyclerView.setAdapter(adapter);
                //making adapter clickable



                adapter.setonLongClickListener(new Paper_adapter.onlongClickListener() {
                    @Override
                    public void onLongClick(final int po) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Arr you sure you want to delete this package".toUpperCase())
                                .setMessage("Note - Deleting Package will not Automatically" +
                                        " update all the customer which uses this package,You will have to manually update them")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Database_helper database_helper = new Database_helper(getActivity());
                                database_helper.Delete_Paper(adapter.getItem(po));
                                adapter.RemoveItem(po);
                            }
                        }).create().show();

                }});

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);

                //searchView section
                paper_search = (SearchView) v.findViewById(R.id.paper_search);

                paper_search.setQueryHint(Html.fromHtml("<font color = #ffffff>  Search </font>"));
                paper_search.setIconifiedByDefault(false);

                paper_search.onActionViewExpanded();
                paper_search.clearFocus();
                paper_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapter.getFilter().filter(s);
                        return false;
                    }
                });
                // Set the adapter



                builder.setView(v).setTitle("SELECT PAPER")
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ArrayList<String> papers = adapter.getPapers();
                        if (papers.isEmpty()) {
                            Toast.makeText(getContext(),"it is empty",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            StringBuilder sb = new StringBuilder();
                            for(i=0; i < papers.size(); i++){
                                if (sb.length() == 0){
                                    sb.append(papers.get(i));
                                }
                                else{
                                    sb.append(" , " + papers.get(i));
                                }
                            }
                            edt_addr.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            edt_addr.setText(sb.toString());
                            Toast.makeText(getContext(),"done he",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edt_addr.setText("");

                    }
                })
                .create().show();


            }

        });


        // LINE SECTION

        edt_line.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.fragment_paper_view_list, null);
                Database_helper database_helper = new Database_helper(getContext());
                final ArrayList<String > ALL_paper = (ArrayList) database_helper.get_Lines_name();
                recyclerView = v.findViewById(R.id.list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                adapter_dialog = new Line_adapter_dialog(ALL_paper,getContext());
                recyclerView.setAdapter(adapter_dialog);
                //making adapter clickable





                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
                recyclerView.addItemDecoration(dividerItemDecoration);

                //searchView section
                paper_search = (SearchView) v.findViewById(R.id.paper_search);

                paper_search.setQueryHint(Html.fromHtml("<font color = #0E228C>  Search </font>"));
                paper_search.setIconifiedByDefault(false);

                paper_search.onActionViewExpanded();
                paper_search.clearFocus();
                paper_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapter_dialog.getFilter().filter(s);
                        return false;
                    }
                });
                // Set the adapter



                builder.setView(v).setTitle("SELECT LINE")
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String papers = adapter_dialog.getPapers();
                                if (papers.isEmpty()) {
                                    Toast.makeText(getContext(),"You have not chosen anything",Toast.LENGTH_SHORT).show();
                                }
                                else{

                                    edt_line.setText(papers.toString());
                                }
                            }
                        }).setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edt_line.setText("");

                    }
                })
                        .create().show();


            }

        });



        //DATE SECTION


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog( getContext(),
                        android.R.style.Theme_Holo_Light_Dialog,
                        onDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar c =  Calendar.getInstance();
                c.set(Calendar.YEAR, i);
                c.set(Calendar.MONTH,i1);
                c.set(Calendar.DAY_OF_MONTH,i2);

                String string_date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                date.setText(string_date);
                datePicker.init(i,i1,i2,null);
                Actual_date = getDateFormat(datePicker);
            }
        };




        btx_Line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater =getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.add_new_line_dialog, null);

                final TextView worker = (TextView) v.findViewById(R.id.Worker_assigned);
                final EditText line_name = (EditText) v.findViewById(R.id.Line_name);
                final EditText amount_line = (EditText) v.findViewById(R.id.Amount_LINE);

                worker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final AlertDialog.Builder builder_in = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View v = inflater.inflate(R.layout.fragment_paper_view_list, null);
                        Database_helper database_helper = new Database_helper(getContext());
                        final ArrayList<String> ALL_paper = (ArrayList) database_helper.get_workers_String();
                        recyclerView = (RecyclerView) v.findViewById(R.id.list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        adapter_dialog = new Line_adapter_dialog(ALL_paper,getContext());
                        recyclerView.setAdapter(adapter_dialog);
                        //making adapter clickable


                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
                        recyclerView.addItemDecoration(dividerItemDecoration);

                        //searchView section
                        paper_search = (SearchView) v.findViewById(R.id.paper_search);

                        paper_search.setQueryHint(Html.fromHtml("<font color = #ffffff>  Search </font>"));
                        paper_search.setIconifiedByDefault(false);

                        paper_search.onActionViewExpanded();
                        paper_search.clearFocus();
                        paper_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String s) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                adapter_dialog.getFilter().filter(s);
                                return false;
                            }
                        });
                        // Set the adapter



                        builder_in.setView(v).setTitle("SELECT WORKER")
                                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String papers = adapter_dialog.getPapers();
                                        if (papers.isEmpty()) {
                                            Toast.makeText(getContext(),"Please provide Worker",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            worker.setText(papers);
                                        }
                                    }
                                }).setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                worker.setText("");

                            }
                        })
                                .create().show();


                    }

                });


                    builder.setView(v).setTitle("ADD NEW LINE").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    String regadd = "^(?![0-9_])[\\p{L}\\d .'-_]+$";
                    Pattern padd = Pattern.compile(regadd);
                    Matcher Mpackage = padd.matcher(line_name.getText().toString());

                    if(!Mpackage.matches()) {

                        Toast.makeText(getContext(), "Line Name should Start with Character", Toast.LENGTH_SHORT).show();
                    }
                    else if (amount_line.getText().toString().equals("")){
                        Toast.makeText(getContext(), "Please Provide Line amount", Toast.LENGTH_SHORT).show();
                    }
                    else{
                            Database_helper database_helper = new Database_helper(getContext());
                            Line line = new Line(line_name.getText().toString(), worker.getText().toString(),
                                    Integer.parseInt(amount_line.getText().toString()));
                            boolean done = database_helper.add_Line(line);

                            Toast.makeText(getContext(), "Line Added Successfully", Toast.LENGTH_SHORT).show();


                    }



                }
            }).create().show();
        }
    });









        btx_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater =getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.add_new_paper_dialog, null);
                final TextView paper = (TextView) v.findViewById(R.id.paper_name);
                final TextView weekly_amount = (TextView) v.findViewById((R.id.Weekly_prise));
                builder.setView(v).setTitle("ADD NEW PAPER")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String regname = "^(?![0-9_])[\\p{L}\\d .'-]+$";
                        Pattern pname = Pattern.compile(regname);
                        Matcher Mname = pname.matcher(paper.getText().toString());

                        if (!Mname.matches()) {
                            Toast.makeText(getContext(), "Package Name must start with Characters", Toast.LENGTH_SHORT).show();

                        } else if(weekly_amount.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Please Provide Weekly amount", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Database_helper database_helper = new Database_helper(getContext());
                            boolean done = database_helper.add_Paper(paper.getText().toString(), Integer.parseInt(weekly_amount.getText().toString()));
                            if (done) {
                                Toast.makeText(getContext(), "Added New Paper Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Something went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).create().show();
            }
        });





        btx_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent customer_one = null;
                try {
                    customer_one = new DummyContent(edt_name.getText().toString(),
                            Long.parseLong(edt_card.getText().toString()),
                            Long.parseLong(edt_phone.getText().toString()),
                            paid.isChecked(),
                            Integer.parseInt(edt_ammo.getText().toString()),
                            edt_addr.getText().toString(),
                            Actual_date,
                            null, edt_line.getText().toString());


                }
                catch (Exception e){
                    Toast w = Toast.makeText(getContext(),"Invalid Inputs",Toast.LENGTH_SHORT);
                    w.show();
                }
                String regname = "^(?![0-9_])[\\p{L}\\d .'-]+$";
                Pattern pname = Pattern.compile(regname);
                Matcher Mname = pname.matcher(edt_name.getText().toString());


                String regphone = "(0,91)?[7-9][0-9]{9}";
                Pattern Pphone = Pattern.compile(regphone);
                Matcher Mphone = Pphone.matcher(edt_phone.getText().toString());

                String regadd = "^(?![0-9_])[\\p{L}\\d .'-_]+$";
                Pattern padd = Pattern.compile(regadd);
                Matcher Mpackage = padd.matcher(edt_addr.getText().toString());
                if (!Mname.matches()) {
                    Toast.makeText(getContext(),"customer name is invalid could not add customer",Toast.LENGTH_SHORT).show();
                }
                else if(!Mphone.matches()){
                    Toast.makeText(getContext(),"phone number should of 10 digits in indian format",Toast.LENGTH_SHORT).show();
                }
                else if (!Mpackage.matches()){
                    Toast.makeText(getContext(),"Package name must start with Character",Toast.LENGTH_SHORT).show();

                }
                else{

                    try {
                        Database_helper database_helper = new Database_helper(getContext());
                        boolean success = database_helper.add_one(customer_one);
                        if (success == true) {
                            Toast.makeText(getContext(), "customer added succesfully = " + customer_one.getDetails().toString() + " " + success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Phone Number has already assigned to another customer", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Could not add customer", Toast.LENGTH_SHORT).show();
                    }
                }




              

            }
        });




        return v;

    }




}