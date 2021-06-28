package com.vishwanathlokare.VendorHelper.PdfUtills;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.MenuItem;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.io.IOException;
import java.io.InputStream;

public class LoadImage extends AsyncTask<String,Void, Bitmap> {

    MenuItem item;
    Context context;
    public LoadImage(MenuItem useritem, Context context) {
        this.context = context;
        this.item = useritem;
    }


    @Override
    protected Bitmap doInBackground(String... strings) {
        String urlink = strings[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new java.net.URL(urlink).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(context.getResources(),
                resizeImage(bitmap,250,250));
        roundedBitmapDrawable1.setCornerRadius(50.0f);
        roundedBitmapDrawable1.setAntiAlias(true);

        item.setIcon(roundedBitmapDrawable1);
    }


    private Bitmap resizeImage(Bitmap roundedBitmapDrawable, int i, int i1) {
        int width = roundedBitmapDrawable.getWidth();
        int height = roundedBitmapDrawable.getHeight();
        int newWidth = i;
        int newHeight = i1;
        float scaleW = ((float) newWidth ) /width ;
        float scaleH = ((float) newHeight ) / height ;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW,scaleH);
        Bitmap resizedBitmap = Bitmap.createBitmap(roundedBitmapDrawable,0,0,width,height,matrix,true);
        return resizedBitmap;

    }
}
