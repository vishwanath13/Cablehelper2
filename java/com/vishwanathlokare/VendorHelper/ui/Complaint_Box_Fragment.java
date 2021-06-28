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

import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.complaintAdapter;
import com.vishwanathlokare.VendorHelper.models.complaint_model;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Complaint_Box_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Complaint_Box_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Complaint_Box_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Complaint_Box_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Complaint_Box_Fragment newInstance(String param1, String param2) {
        Complaint_Box_Fragment fragment = new Complaint_Box_Fragment();
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
        // Inflate the layout for this fragment
         View v = inflater.inflate(R.layout.fragment_complaint__box_, container, false);


         Database_helper database_helper =  new Database_helper(getContext());
        ArrayList<complaint_model> list = (ArrayList<complaint_model>) database_helper.get_complaint();
        final complaintAdapter adapter = new complaintAdapter(list,getContext());
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.complait_group);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

         return v;
    }



}