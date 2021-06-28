package com.vishwanathlokare.VendorHelper.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vishwanathlokare.VendorHelper.MainActivity;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.CustHistory;
import com.vishwanathlokare.VendorHelper.adapter.CusthistoryAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A fragment representing a list of Items.
 */
public class Customer_History extends Fragment {

    // TODO: Customize parameter argument names
    // TODO: Customize parameters

    private static final String NAME = "NAME";

    public String name = "";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Customer_History() {
    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Customer_History newInstance(String name) {
        Customer_History fragment = new Customer_History();

        Bundle args = new Bundle();

        args.putString(NAME, name);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {

            name = getArguments().getString(NAME);
        }
        setHasOptionsMenu(true);




    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem l = menu.findItem(R.id.Action_search);
        MenuItem u = menu.findItem(R.id.information);
        l.setVisible(false);
        u.setVisible(false);
        ((MainActivity)getActivity()).setActionBarTitle(name);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_customer__history_list, container, false);

        // Set the adapter
        RecyclerView view = view1.findViewById(R.id.customer_History);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            Database_helper db = new Database_helper(getContext());
            ArrayList<CustHistory> histories =(ArrayList<CustHistory>) db.retrive_info(name);
            Collections.sort(histories,CustHistory.CompareDate);
            Collections.reverse(histories);
            recyclerView.setAdapter(new CusthistoryAdapter(histories));

        }
        return view1;
    }
}