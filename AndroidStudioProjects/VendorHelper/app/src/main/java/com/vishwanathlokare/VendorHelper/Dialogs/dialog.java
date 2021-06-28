package com.vishwanathlokare.VendorHelper.Dialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.vishwanathlokare.VendorHelper.PdfUtills.Common;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.adapter.Pending_RenewalAdapter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.vishwanathlokare.VendorHelper.ui.Database_helper;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class dialog extends AppCompatDialogFragment {
    private EditText amount;

    Long amo1;
    Pending_RenewalAdapter madapter;
    int po;

    TextView count;
    String name;
    String papers;
    CheckBox checkBox;
   Integer rokda;



    public dialog(Long amo, Pending_RenewalAdapter adpter, int position,String name, String papers,Integer ro,TextView count){
        rokda = ro;
        amo1 = amo;
        madapter = adpter;
        po = position;
        this.name = name;
        this.papers = papers;
        this.count = count;
    }





    public Dialog onCreateDialog(Bundle savedInstanceState){





        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_fragment,null);
        checkBox =  view.findViewById(R.id.checkBox3);
        amount =  view.findViewById(R.id.amount_click);
        amount.setText(rokda + "");
        builder.setView(view);
        builder.setTitle("ADD_AMOUNT_TAKEN");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        builder.setPositiveButton("   Add_To_Paid", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            if(!amount.getText().toString().equals("")){
                int amo2 = Integer.parseInt(String.valueOf(amount.getText()));
                if(checkBox.isChecked()){
                    Toast.makeText(getContext(),"" + " is not enabled"
                            ,Toast.LENGTH_SHORT).show();

                    if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED) {


                        createPdfUtills(new StringBuilder(getAppPath()).append(name.toUpperCase() + ".pdf").toString());
                        Uri uri = Uri.parse(new StringBuilder(getAppPath()).append(name.toUpperCase()+".pdf").toString());
                        startActivity(new Intent(Intent.ACTION_VIEW)
                                .setDataAndType(uri,"application/pdf")
                                .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));

                    }
                    else  {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                    }

                }



                Database_helper database_helper = new Database_helper(getContext());
                boolean rv = database_helper.pay(amo2, (long) amo1);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE ,0);
                cal.set(Calendar.MILLISECOND,0);
                cal.set(Calendar.SECOND,0);
                madapter.notifyDataSetChanged();



                database_helper.add_one_Trigger(madapter.getname(po), amo2, cal.getTime());

                madapter.RemoveItem(po);
                madapter.check();
                madapter.notifyDataSetChanged();

                Toast.makeText(getActivity(), "Paid Successfully", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(getActivity(), "Please Provide Amount to be Taken", Toast.LENGTH_SHORT).show();
            }
        }});
        return builder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPdfUtills(String path) {
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


            BaseColor color = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN,36.0f,Font.NORMAL,BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.HELVETICA,fontSize,Font.NORMAL,color);
            Database_helper database_helper = new Database_helper(getContext());
            ArrayList<String> store = database_helper.get_Store();
            if(!store.isEmpty()) {
                Common.addNemItem(document, store.get(0).toUpperCase(), Element.ALIGN_LEFT, titleFont);
            }


           // BaseFont fontName = new BaseFont.createFont(R.font.amarante,"UTF -8",BaseFont.EMBEDDED);


            String string_date = DateFormat.getDateInstance(DateFormat.FULL).format(new Date());
            Common.addNemItem(document,string_date,Element.ALIGN_RIGHT,textFont);
            if(!store.isEmpty()) Common.addNemItem(document,store.get(1),Element.ALIGN_RIGHT,textFont);
            Common.addNemItem(document,"BILL RECEIPT", Element.ALIGN_CENTER,titleFont);

            Common.addLineSpace(document);
            Common.addLineSeparator(document);
            Common.addNemItem(document,"PAPERS ",Element.ALIGN_LEFT,textFont);

            StringBuilder sb = new StringBuilder();
            for(int i = 0; i<papers.length(); i++) {

                if(papers.charAt(i) == ',' | i == papers.length()-1){
                    if(i == papers.length()-1){
                        sb.append(papers.charAt(i));
                    }
                    String paper = sb.toString().trim();
                    Common.addLineSpace(document);
                    Common.addNemItem(document,paper.toUpperCase(),Element.ALIGN_RIGHT,textFont);
                    Common.addLineSpace(document);
                    sb = new StringBuilder();

                }
                else {
                    sb.append(papers.charAt(i));
                }
            }
            Common.addLineSeparator(document);
            Common.addNewItemWithLeftAndRight(document,"AMOUNT TAKEN ",amount.getText().toString() + " Rupees",
                    textFont,textFont);
            Common.addNewItemWithLeftAndRight(document,"FROM",name.toUpperCase(),
                    textFont,textFont);
            Common.addLineSeparator(document);




            if(new File(new StringBuilder(getAppPath()).append("Signature.jpg").toString()).exists()) {
            Common.addSignature(document,new StringBuilder(getAppPath()).append("Signature.jpg").toString());
                Toast.makeText(getContext(),"Hopefully done",Toast.LENGTH_SHORT).show();
            }
            Common.addNemItem(document,"Signature",Element.ALIGN_RIGHT,titleFont);
            document.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAppPath() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator
        +getResources().getString(R.string.app_name) + File.separator);
        if(!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != getActivity().RESULT_CANCELED) {
            Toast.makeText(getContext(),"not permissoned",Toast.LENGTH_SHORT).show();
            dismiss();
        }else{

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createPdfUtills(new StringBuilder(getAppPath()).append(name.toUpperCase() + ".pdf").toString());
                    Uri uri = Uri.parse(new StringBuilder(getAppPath()).append(name.toUpperCase()+".pdf").toString());
                    startActivity(new Intent(Intent.ACTION_VIEW)
                            .setDataAndType(uri,"application/pdf")
                            .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                }
                else {
                    Toast.makeText(getContext(),"Permission denied",Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
    }

