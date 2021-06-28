package com.vishwanathlokare.VendorHelper.ui.Pending_Renewal;

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
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.vishwanathlokare.VendorHelper.PdfUtills.LoadImage;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.Pending_RenewalAdapter;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;
import com.vishwanathlokare.VendorHelper.ui.Pending_Customer.GalleryFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private TextView customer ;
    MenuItem itemsign;
    TextView count;





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
            LoadImage loadImage = new LoadImage(itemsign,getContext());
            if(account.getPhotoUrl() != null) loadImage.execute(account.getPhotoUrl().toString());

        }
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);


        count = root.findViewById(R.id.Count_pending);

        RecyclerView recyclerView =  root.findViewById(R.id.recyclerView3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Database_helper database_helper = new Database_helper(getContext());
        List<DummyContent> everyone = database_helper.geteveryone();
        final List<DummyContent> paid_customer = new ArrayList<>(everyone);
        for(DummyContent item : everyone){
            if(item == null){
                continue;
            }
            else if (item.isPaid()){
                paid_customer.remove(item);
            }

        }


        final Pending_RenewalAdapter adapter = new Pending_RenewalAdapter(paid_customer,getContext(),count);
        recyclerView.setAdapter(adapter);

        adapter.check();

        SearchView searchView = root.findViewById(R.id.search_ss);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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





        return root;
    }




}