package com.vishwanathlokare.VendorHelper.ui;

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
import android.widget.SearchView;

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.groupApater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HIstory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HIstory extends Fragment {
    RecyclerView rv_group;

    ArrayList<Date> itemGroup = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HIstory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HIstory.
     */
    // TODO: Rename and change types and number of parameters
    public static HIstory newInstance(String param1, String param2) {
        HIstory fragment = new HIstory();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_h_istory,container,false);
        rv_group = (RecyclerView) v.findViewById(R.id.complait_group);
        Database_helper database_helper = new Database_helper(getContext());
        itemGroup = (ArrayList<Date>) database_helper.retrive_dates();


        final groupApater groupapater = new groupApater(getContext(),itemGroup);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_group.setLayoutManager(linearLayoutManager);
        rv_group.setAdapter(groupapater);
        Collections.sort(itemGroup);
        Collections.reverse(itemGroup);
        SearchView searchView = (SearchView) v.findViewById(R.id.history_search);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }


            public boolean onQueryTextChange(String s) {
                groupapater.getFilter().filter(s);
                return false;
            }
        });
        return v;
    }
}