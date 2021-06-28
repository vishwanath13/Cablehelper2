package com.vishwanathlokare.VendorHelper.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.LineAdapter;
import com.vishwanathlokare.VendorHelper.adapter.Line_adapter_dialog;
import com.vishwanathlokare.VendorHelper.adapter.Paper_adapter;

import com.vishwanathlokare.VendorHelper.models.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A fragment representing a list of Items.
 */
public class LineFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    RecyclerView recyclerView;
    RecyclerView recyclerView_real;
    Button btn_add;
    SearchView paper_search;
    Paper_adapter adapter_in;
    LineAdapter adapter;
    Line_adapter_dialog adapter_dialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LineFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LineFragment newInstance(int columnCount) {
        LineFragment fragment = new LineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.line_fragment, container, false);
        recyclerView_real = (RecyclerView) view.findViewById(R.id.recyclerView5);
        btn_add = (Button) view.findViewById(R.id.add_line);

        // Set the adapter
        Database_helper database_helper  =  new Database_helper(getContext());
        List<Line> lines = database_helper.get_Lines();
        adapter = new LineAdapter(lines,getContext(),getActivity());
        if (recyclerView_real instanceof RecyclerView) {
            Context context = view.getContext();

            if (mColumnCount <= 1) {
                recyclerView_real.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView_real.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView_real.setAdapter(adapter);
        }



        btn_add.setOnClickListener(new View.OnClickListener() {
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
                        final ArrayList<String> ALL_workers = (ArrayList) database_helper.get_workers_String();
                        recyclerView = v.findViewById(R.id.list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        adapter_dialog = new Line_adapter_dialog(ALL_workers,getContext());
                        recyclerView.setAdapter(adapter_dialog);
                        //making adapter clickable


                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
                        recyclerView.addItemDecoration(dividerItemDecoration);

                        //searchView section
                        paper_search = v.findViewById(R.id.paper_search);

                        paper_search.setQueryHint(Html.fromHtml("<font color = #00000>  Search </font>"));
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
                                            Toast.makeText(getContext(),"it is empty",Toast.LENGTH_SHORT).show();
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

                        if (!Mpackage.matches()) {

                            Toast.makeText(getContext(), "Line Name should Start with Character", Toast.LENGTH_SHORT).show();
                        } else if (amount_line.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Please Provide Line amount", Toast.LENGTH_SHORT).show();
                        } else {

                            Database_helper database_helper = new Database_helper(getContext());
                            Line line = new Line(line_name.getText().toString(), worker.getText().toString(),
                                    Integer.parseInt(amount_line.getText().toString()));
                            boolean done = database_helper.add_Line(line);

                            Toast.makeText(getContext(), "New Line Added Successfully" + done, Toast.LENGTH_SHORT).show();

                            adapter.Refresh(database_helper.get_Lines());


                        }
                    }
                }).create().show();
            }
        });





        return view;
    }
}