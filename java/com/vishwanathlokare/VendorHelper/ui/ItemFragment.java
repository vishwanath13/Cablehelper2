package com.vishwanathlokare.VendorHelper.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.vishwanathlokare.VendorHelper.PdfUtills.LoadImage;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.MyItemRecyclerViewAdapter;
import com.vishwanathlokare.VendorHelper.adapter.Paper_adapter;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.adapter.ItemAdapter;
import com.vishwanathlokare.VendorHelper.Dialogs.calenderDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {
    private DatePickerDialog.OnDateSetListener onDateSetListener;


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private DatePicker datePicker_real ;

    MenuItem itemsign;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        itemsign = menu.findItem(R.id.SignIn);
        MenuItem l = menu.findItem(R.id.Action_search);
        MenuItem u = menu.findItem(R.id.information);
        l.setVisible(false);
        u.setVisible(false);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());

        if(account == null){
        }
        else {
                    /*InputStream is = new java.net.URL(personPhoto.toString()).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    userImage.setImageBitmap(bitmap);*/
            LoadImage loadImage = new LoadImage(itemsign,getContext());
            if(account.getPhotoUrl() != null) loadImage.execute(account.getPhotoUrl().toString());

        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Action_search:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("DO YOU REALLY WANT TO RESET ALL CUSTOMER").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Database_helper database_helper = new Database_helper(getContext());
                        boolean b = database_helper.delete_paid();
                        if(b) {
                            Toast.makeText(getContext(), "All customers have been reset", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();

                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

            recyclerView.setLayoutManager(new LinearLayoutManager(context));



            final Database_helper database_helper = new Database_helper(getContext());
        List<DummyContent> everyone= database_helper.geteveryone();


        final List<DummyContent> paid_customer = new ArrayList<>(everyone);


        ArrayList<DummyContent> paid_customer1  = new ArrayList<>();
        paid_customer1.addAll(paid_customer);
        for(int i=0; i<paid_customer.size();i++){
            Date date = paid_customer1.get(i).getRenewal();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String date_string =  format.format(date);
            paid_customer.get(i).name = paid_customer1.get(i).getName1() + "  >  " +date_string;
        }

        Collections.sort(paid_customer,DummyContent.CompareDate);

        final ItemAdapter adapter = new ItemAdapter((ArrayList) paid_customer);

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                calenderDialog dialog = new calenderDialog(adapter,position,paid_customer.get(position).getPhone()
                ,paid_customer.get(position).getRenewal());
                dialog.show(getFragmentManager(),"calender_dialog");
            }
        });



        return view;
    }
}