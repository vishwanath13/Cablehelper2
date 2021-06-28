package com.vishwanathlokare.VendorHelper.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.vishwanathlokare.VendorHelper.MainActivity;
import com.vishwanathlokare.VendorHelper.PdfUtills.Common;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.Line_customer_adapter;
import com.vishwanathlokare.VendorHelper.adapter.Paper_adapter;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Line_customer extends Fragment {

    TextView size;
    SearchView searchView;
    RecyclerView recyclerView;
    Button btn;
    Line_customer_adapter adapter;
    ArrayList<DummyContent> customer;
    Paper_adapter adapter_in;

    private static final String NAME = "NAME";

    public String name = "";

    public Line_customer() {
    }
    public static Line_customer newInstance(String name) {
        Line_customer fragment = new Line_customer();

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

        ((MainActivity)getActivity()).setActionBarTitle(name.toUpperCase());
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

    View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_line_customer, container,false);

    size = (TextView) v.findViewById(R.id.Count_customer);
    recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_customer);
    btn = (Button) v.findViewById(R.id.download);
    searchView = (SearchView) v.findViewById(R.id.searchView_customer);

    final Database_helper database_helper = new Database_helper(getContext());


    if (name.equals("")){
        Toast.makeText(getContext(),"We Could not find any data here",Toast.LENGTH_SHORT).show();
    }
    else {


        customer = (ArrayList) database_helper.get_Customer_of_Line(name);
        adapter = new Line_customer_adapter(customer, getContext());
        size.setText(adapter.getItemCount() + "");
        boolean flag = true;
        for(DummyContent dm : customer) {
            if(dm.getPo() == null) {
                flag = false;
                break;
            }
        }
        if(flag) {
            adapter.swaped();

        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
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

        size.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

            }

        });

        btn.setText("DOWNLOAD");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) {

                    try {
                        createPdfUtills(new StringBuilder(getAppPath()).append(name.toUpperCase()+".pdf").toString());

                        Uri uri = Uri.parse(new StringBuilder(getAppPath()).append(name.toUpperCase()+".pdf").toString());
                        startActivity(new Intent(Intent.ACTION_VIEW)
                                .setDataAndType(uri,"application/pdf")
                                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        Toast.makeText(getContext(),"Pdf is created with name - " + name.toUpperCase(),
                                Toast.LENGTH_SHORT).show();

                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }

                }
                else  {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);

                }
            }
        });



       return v;
    }

    private void createPdfUtills(String path) throws DocumentException {

        if(new File(path).exists()){
            new File(path).delete();

        }
        try {
            Document document = new com.itextpdf.text.Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            document.setPageSize(PageSize.A1);
            document.addAuthor("Vishwanath Lokare");
            document.addCreationDate();
            document.addCreator("CableHelper");


            BaseColor color = new BaseColor(0, 153, 204, 255);
            BaseColor color1 = new BaseColor(100, 100, 100, 255);


            // BaseFont fontName = new BaseFont.createFont(R.font.amarante,"UTF -8",BaseFont.EMBEDDED);

            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 36.0f, Font.NORMAL, BaseColor.BLACK);
            Font titleFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLACK);
            Common.addNemItem(document, name.toUpperCase(), Element.ALIGN_CENTER, titleFont);
            Common.addLineSpace(document);
            Common.addNewItemWithLeftAndRight(document,"Customers","Papers",titleFont2,titleFont2);
            Common.addLineSeparator(document);
            int i = 1;
            for(DummyContent dum : customer ) {
                Common.addNewItemWithLeftAndRight(document, i + ") " + dum.getName1(),dum.getDetails()
                        ,textFont, textFont);
                Common.addLineSpace(document);
                i++;
            }
            document.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
            | ItemTouchHelper.DOWN
            | ItemTouchHelper.START
            | ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int from = viewHolder.getAdapterPosition();
            int to = target.getAdapterPosition();
            Collections.swap(customer,from,to);
            recyclerView.getAdapter().notifyItemMoved(from,to);
            recyclerView.getAdapter().notifyDataSetChanged();
            Database_helper database_helper = new Database_helper(getContext());
            boolean v = database_helper.updatePo(customer);
            adapter.notifyDataSetChanged();
            if(adapter.getItemCount() == 0) {
                Toast.makeText(getContext(),"There is nothing to be saved",Toast.LENGTH_SHORT).show();
            }
            else if (v){
                Toast.makeText(getContext(),"LINE ARRANGEMNT IS SAVED",Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

    };


    private String getAppPath() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator
                +getResources().getString(R.string.app_name) + File.separator);
        if(!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        createPdfUtills(new StringBuilder(getAppPath()).append(name.toUpperCase() + ".pdf").toString());
                        Uri uri = Uri.parse(new StringBuilder(getAppPath()).append(name.toUpperCase()+".pdf").toString());
                        startActivity(new Intent(Intent.ACTION_VIEW)
                                .setDataAndType(uri,"application/pdf")
                                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getContext(),"Permission denied",Toast.LENGTH_SHORT).show();
                }

        }
    }

}
