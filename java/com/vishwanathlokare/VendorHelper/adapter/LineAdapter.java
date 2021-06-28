package com.vishwanathlokare.VendorHelper.adapter;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.Line;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LineAdapter extends RecyclerView.Adapter<LineAdapter.ViewHolder> {

    private final List<Line> mValues;
    Line_adapter_dialog adapter_in;
    Context context;

    Activity activity;

    public LineAdapter(List<Line> items, Context context,Activity activity)
    {
        this.context = context;
        mValues = items;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complaint, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mPhoneView.setText(mValues.get(position).getWorker());
        holder.mContentView.setText(mValues.get(position).getLine().toUpperCase());
        holder.amount.setText(mValues.get(position).getAmount() + "");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Line_customer freg = Line_customer.newInstance(mValues.get(position).getLine());
                //FragmentManager fr = ((FragmentActivity)context).getSupportFragmentManager();
                //FragmentTransaction frt = fr.beginTransaction();

                //frt.replace(R.id.drawer_layout,freg);
                Bundle args = new Bundle();

                args.putString("NAME",mValues.get(position).getLine() );

                //frt.addToBackStack(null).commit();
                final NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
                navController.navigate(R.id.nav_line_customer,args);



            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DO YOU REALLY WANT TO DELETE THIS LINE").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Database_helper database_helper = new Database_helper(context);
                        database_helper.Delete_Line(mValues.get(position).getLine());

                        Refresh(database_helper.get_Lines());
                        Toast.makeText(context,"Deleted Succesfully",Toast.LENGTH_SHORT).show();

                    }
                }).setNeutralButton("UPDATE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        final View v = LayoutInflater.from(context).inflate(R.layout.add_new_line_dialog,null,false);
                         final TextView worker = (TextView) v.findViewById(R.id.Worker_assigned);
                         final EditText line_name = (EditText) v.findViewById(R.id.Line_name);
                         final EditText amount_line = (EditText) v.findViewById(R.id.Amount_LINE);

                         worker.setText(mValues.get(position).getWorker());
                        line_name.setText(mValues.get(position).getLine());
                        amount_line.setText(mValues.get(position).getAmount() + "");


                        worker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {




                                final AlertDialog.Builder builder_in = new AlertDialog.Builder(context);
                                LayoutInflater inflater = LayoutInflater.from(context);
                                View v = inflater.inflate(R.layout.fragment_paper_view_list, null);
                                Database_helper database_helper = new Database_helper(context);
                                final ArrayList<String> ALL_workers = (ArrayList) database_helper.get_workers_String();
                                 RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.list);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                                adapter_in = new Line_adapter_dialog(ALL_workers,context);
                                recyclerView.setAdapter(adapter_in);
                                //making adapter clickable





                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
                                recyclerView.addItemDecoration(dividerItemDecoration);

                                //searchView section
                                 final SearchView paper_search = (SearchView) v.findViewById(R.id.paper_search);

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
                                        adapter_in.getFilter().filter(s);
                                        return false;
                                    }
                                });
                                // Set the adapter



                                builder_in.setView(v).setTitle("SELECT WORKER")
                                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String papers = adapter_in.getPapers();
                                                if (papers.isEmpty()) {
                                                    Toast.makeText(context,"it is empty",Toast.LENGTH_SHORT).show();
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







                        final Database_helper database_helper = new Database_helper(context);
                        builder1.setView(v).setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String regadd = "^(?![0-9_])[\\p{L}\\d .'-_]+$";
                                Pattern padd = Pattern.compile(regadd);
                                Matcher Mpackage = padd.matcher(line_name.getText().toString());

                                if (!Mpackage.matches()) {

                                    Toast.makeText(context, "Line Name should Start with Character", Toast.LENGTH_SHORT).show();
                                } else if (amount_line.getText().toString().equals("")) {
                                    Toast.makeText(context, "Please Provide Line amount", Toast.LENGTH_SHORT).show();
                                } else {
                                    Line w = new Line(line_name.getText().toString(),
                                            worker.getText().toString(),
                                            Integer.parseInt(amount_line.getText().toString()));

                                    boolean done = database_helper.update_Line(w, mValues.get(position).getLine());
                                    Toast.makeText(context, "Line updated Successfully", Toast.LENGTH_SHORT).show();
                                    Refresh((ArrayList) database_helper.get_Lines());
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setTitle("UPDATE").create().show();

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void Refresh(List<Line> lines) {
        mValues.clear();
        mValues.addAll(lines);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPhoneView;
        public final TextView mContentView;
        public final Button amount;
        public Line mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPhoneView = (TextView) view.findViewById(R.id.textView8);
            mContentView = (TextView) view.findViewById(R.id.textView7);
            amount = (Button) view.findViewById(R.id.button2);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}