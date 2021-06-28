package com.vishwanathlokare.VendorHelper.ui.Pending_Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.vishwanathlokare.VendorHelper.PdfUtills.LoadImage;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.Pending_CutomerAdapter;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

   private TextView size;
   private TextView amount;
    MenuItem itemsign;







    public GalleryFragment() {
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



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_pending_customer, container, false);


        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final Database_helper database_helper = new Database_helper(getContext());
        List<DummyContent> everyone = database_helper.geteveryone();
        final List<DummyContent> paid_customer = new ArrayList<>(everyone);
        for(DummyContent item : everyone){
            if(item == null){
                continue;
               }
            else if (!item.isPaid()){
                   paid_customer.remove(item);
                    }
              }
        final Pending_CutomerAdapter madapter = new Pending_CutomerAdapter(paid_customer,getContext());
        recyclerView.setAdapter(madapter);
        size = (TextView) root.findViewById(R.id.size);
        amount = (TextView) root.findViewById(R.id.amount);
        size.setText(madapter.getItemCount() + "");
        int amo = 0;
        for(DummyContent item : paid_customer){
            amo = amo + item.package_amo;
        }
        amount.setText(amo +"");
        

        SearchView searchView = (SearchView) root.findViewById(R.id.Search_pc);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }


            public boolean onQueryTextChange(String s) {
                madapter.getFilter().filter(s);
                return false;
            }
        });





        return root;

    }




}