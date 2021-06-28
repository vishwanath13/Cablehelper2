package com.vishwanathlokare.VendorHelper.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.text.Html;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.vishwanathlokare.VendorHelper.Dialogs.updateDialog;
import com.vishwanathlokare.VendorHelper.MainActivity;
import com.vishwanathlokare.VendorHelper.PdfUtills.LoadImage;
import com.vishwanathlokare.VendorHelper.adapter.MyItemRecyclerViewAdapter;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.Paper_adapter;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;
import com.vishwanathlokare.VendorHelper.Dialogs.showInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    public static final int DIALOG_FRAGMENT = 1;
    MyItemRecyclerViewAdapter adapter;
    MenuItem itemsign;
    TextView count;
    HashMap<String , Integer> paper_count;
    RecyclerView recyclerView1;
    Paper_adapter adapter_in;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);




    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        itemsign = menu.findItem(R.id.SignIn);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        MenuItem l = menu.findItem(R.id.Action_search);
        MenuItem u = menu.findItem(R.id.information);
        l.setVisible(true);
        u.setVisible(true);

        Database_helper database_helper = new Database_helper(getContext());
        if(!database_helper.get_Store().isEmpty()) ((MainActivity)getActivity()).setActionBarTitle(database_helper.get_Store().get(0).toUpperCase());
        else{
            ((MainActivity)getActivity()).setActionBarTitle("VendorHelper");
        }



        if(account == null){
        }
        else {

                    if (account.getPhotoUrl() != null) {


                        LoadImage loadImage = new LoadImage(itemsign, getContext());
                        loadImage.execute(account.getPhotoUrl().toString());
                    }
        }
    }




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);


        View root = inflater.inflate(R.layout.fragment_home,container,false);

        count = root.findViewById(R.id.Count_home);


        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder_i = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.fragment_paper_view_list, null);
                Database_helper database_helper = new Database_helper(getContext());

                final ArrayList<DummyContent> ALL_customers = (ArrayList) database_helper.geteveryone();
                paper_count = new HashMap<>();

                for(DummyContent dm : ALL_customers){
                    String papers = dm.getDetails();
                    StringBuilder sb = new StringBuilder();

                    for(int i=0; i<papers.length(); i++){


                        char t = papers.charAt(i);

                        if(t == ',' | i == papers.length()-1){

                            if(papers.length() == i+1){
                                sb.append(papers.charAt(i));
                            }

                            if(paper_count.containsKey(sb.toString().trim())) {
                                int value = paper_count.get(sb.toString().trim());
                                value = value + 1;
                                paper_count.remove(sb.toString().trim());
                                paper_count.put(sb.toString().trim(),value);
                            }
                            else {
                                paper_count.put(sb.toString().trim(),1);

                            }
                            sb = new StringBuilder();

                        }
                        else {
                            sb.append(papers.charAt(i));
                        }
                    }


                }


                ArrayList<String> last_count = new ArrayList<>();
                for(Map.Entry<String,Integer> entry : paper_count.entrySet()){
                    last_count.add(entry.getKey() + " > " + entry.getValue());

                }

                recyclerView1 = (RecyclerView) view.findViewById(R.id.list);
                recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));

                adapter_in = new Paper_adapter(last_count,getContext());
                recyclerView1.setAdapter(adapter_in);
                //making adapter clickable


                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView1.getContext(),DividerItemDecoration.VERTICAL);
                recyclerView1.addItemDecoration(dividerItemDecoration);

                //searchView section
                SearchView paper_search = view.findViewById(R.id.paper_search);

                paper_search.setQueryHint(Html.fromHtml("<font color = #000000>  Search </font>"));
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



                builder_i.setView(view).setTitle("total Packages Count".toUpperCase() )
                        .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                        .create()
                        .show();
            }
        });


        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Database_helper database_helper = new Database_helper(getContext());

        final List<DummyContent> everyone;
            everyone = database_helper.geteveryone();


            adapter = new MyItemRecyclerViewAdapter(everyone,getContext());
        recyclerView.setAdapter(adapter);
        count.setText(adapter.getItemCount() + " ");
        ArrayList<String> store = database_helper.get_Store();
        if(!store.isEmpty()){
            ((MainActivity)getActivity()).setActionBarTitle(store.get(0).toUpperCase());

        }

        SearchView searchView = (SearchView) root.findViewById(R.id.searchView);
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