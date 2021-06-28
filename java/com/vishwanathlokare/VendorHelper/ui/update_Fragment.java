package com.vishwanathlokare.VendorHelper.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

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
import com.vishwanathlokare.VendorHelper.adapter.Paper_adapter;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.models.Line;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link update_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class update_Fragment extends Fragment {
      public static final int PICKER =1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM2 = "param2";
    Long phone_;
    // TODO: Rename and change types of parameters
    private Long mParam1;


    Switch paid;
    Button update;
    Date actual_date;


    SearchView paper_search;

    EditText edt_name;
    EditText edt_card;
    EditText edt_phone;
    TextView edt_addr;
    EditText edt_ammo;

    Button btx_add;
    Button btx_Line;
    Button btx_paper;
    TextView edt_line;

    RecyclerView recyclerView;
    TextView date;

    Paper_adapter adapter;

    public update_Fragment() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment update_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static update_Fragment newInstance(Long param2) {
        update_Fragment fragment = new update_Fragment();
        Bundle args = new Bundle();

        args.putLong(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phone_ = getArguments().getLong(ARG_PARAM2);


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
        edt_line = (TextView) v.findViewById(R.id.edit_Line);
        final TextView date = (TextView) v.findViewById(R.id.editTextDate2);

        btx_Line = (Button)  v.findViewById(R.id.new_Line);
        btx_paper = (Button)  v.findViewById(R.id.new_paper);
        paid = v.findViewById(R.id.paid);
        update = (Button) v.findViewById(R.id.add);
        update.setText("UPDATE");


        //papers_Section
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
                    public void onLongClick(int po) {
                        Toast.makeText(getContext(),"pressed long one",Toast.LENGTH_SHORT).show();
                    }
                });

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


        //Line Section

        edt_line.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View view) {
                final String alredy = edt_line.getText().toString();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.fragment_paper_view_list, null);
                Database_helper database_helper = new Database_helper(getContext());
                final ArrayList<String > ALL_paper = (ArrayList) database_helper.get_Lines_name();
                recyclerView = (RecyclerView) v.findViewById(R.id.list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                adapter = new Paper_adapter(ALL_paper,getContext());
                recyclerView.setAdapter(adapter);
                //making adapter clickable



                adapter.setonLongClickListener(new Paper_adapter.onlongClickListener() {
                    @Override
                    public void onLongClick(int po) {
                        Toast.makeText(getContext(),"pressed long one",Toast.LENGTH_SHORT).show();
                    }
                });

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

                                    edt_line.setText(sb.toString());
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


        //btn_ section
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



                        final String alredy = edt_addr.getText().toString();
                        final AlertDialog.Builder builder_in = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View v = inflater.inflate(R.layout.fragment_paper_view_list, null);
                        Database_helper database_helper = new Database_helper(getContext());
                        final ArrayList<String> ALL_paper = (ArrayList) database_helper.get_workers_String();
                        recyclerView = (RecyclerView) v.findViewById(R.id.list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        adapter = new Paper_adapter(ALL_paper,getContext());
                        recyclerView.setAdapter(adapter);
                        //making adapter clickable



                        adapter.setonLongClickListener(new Paper_adapter.onlongClickListener() {
                            @Override
                            public void onLongClick(int po) {
                                Toast.makeText(getContext(),"pressed long one",Toast.LENGTH_SHORT).show();
                            }
                        });

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



                        builder_in.setView(v).setTitle("SELECT WORKER")
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

                                            worker.setText(sb.toString());
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

                        Database_helper database_helper = new Database_helper(getContext());
                        Line line = new Line(line_name.getText().toString(),worker.getText().toString(),
                                Integer.parseInt(amount_line.getText().toString()));
                        boolean done = database_helper.add_Line(line);

                        Toast.makeText(getContext(),"done" +done , Toast.LENGTH_SHORT).show();


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
                        Database_helper database_helper = new Database_helper(getContext());
                        boolean done = database_helper.add_Paper(paper.getText().toString(),Integer.parseInt(weekly_amount.getText().toString()));
                        if (done) {
                            Toast.makeText(getContext(),"" + done,Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "not done" ,Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create().show();
            }
        });




        final Database_helper database_helper = new Database_helper(getContext());
        final DummyContent dum = database_helper.getvalues(phone_);


        edt_name.setText(dum.getName1());
        edt_card.setText(dum.getCard()+ "");
        edt_phone.setText(dum.getPhone()+"");
        edt_ammo.setText(dum.getPackage_amo()+"");
        edt_addr.setText(dum.getDetails());
        DateFormat df = DateFormat.getInstance();
        date.setText(android.icu.text.DateFormat.getDateInstance(DateFormat.FULL).format(dum.getRenewal()));
        paid.setChecked(dum.isPaid());
        actual_date = dum.getRenewal();
        edt_line.setText(dum.getLine());




                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

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
                                Toast.makeText(getContext(), "customer name is invalid could not add customer", Toast.LENGTH_SHORT).show();
                            } else if (!Mphone.matches()) {
                                Toast.makeText(getContext(), "phone number should of 10 digits in indian format", Toast.LENGTH_SHORT).show();
                            } else if (!Mpackage.matches()) {
                                Toast.makeText(getContext(), "Package name must start with Character", Toast.LENGTH_SHORT).show();

                            } else {
                                DummyContent dm = new DummyContent(edt_name.getText().toString(),
                                        Long.parseLong(edt_card.getText().toString()),
                                        Long.parseLong(edt_phone.getText().toString()),
                                        paid.isChecked(),
                                        Integer.parseInt(edt_ammo.getText().toString()),
                                        edt_addr.getText().toString(), actual_date,
                                        dum.getPo(), edt_line.getText().toString());
                                boolean update = database_helper.update(phone_, dm);
                                Toast.makeText(getContext(), "updated successfully = " + update, Toast.LENGTH_SHORT).show();

                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }

                    });





        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dum.getRenewal());
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog df = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        datePicker.init(i,i1,i2,null);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, datePicker.getYear());
                        cal.set(Calendar.MONTH, datePicker.getYear());
                        cal.set(Calendar.DAY_OF_MONTH, datePicker.getYear());
                        actual_date = cal.getTime();
                        date.setText(android.icu.text.DateFormat.getDateInstance(DateFormat.FULL).format(cal.getTime()));

                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                df.show();
            }
        });

        return v;
    }
}