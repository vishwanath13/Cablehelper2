package com.vishwanathlokare.VendorHelper.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vishwanathlokare.VendorHelper.R;
import com.vishwanathlokare.VendorHelper.models.CustHistory;
import com.vishwanathlokare.VendorHelper.models.DummyContent;
import com.vishwanathlokare.VendorHelper.models.History_content;
import com.vishwanathlokare.VendorHelper.models.Line;
import com.vishwanathlokare.VendorHelper.models.Workers;
import com.vishwanathlokare.VendorHelper.models.complaint_model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database_helper extends SQLiteOpenHelper {
    Context c;
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String NAME1 = "NAME";
    public static final String NAME = NAME1;
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_" + NAME;
    public static final String COLUMN_CARD_NO = "CARD_NO";
    public static final String COLUMN_PHONE = "PHONE";
    public static final String COLUMN_PACKAGE = "PACKAGE";
    public static final String COLUMN_PAID = "PAID";
    public static final String COLUMN_ADDR = "ADDR";
    public static final String DATE = "DATE";
    public static final String COLUMN_DATE = DATE;
    public static final String HISTORY = "HISTORY";
    public static final String AMOUNT = "AMOUNT";
    public static final String TABLE_DATE = "DATES";
    public static final String SINGAL_DATE = "SINGAL_DATE";
    public static final String COMPLAINT = "COMPLAINT";
    public static final String TABLE_COMPLAINT = "TABLE_" + COMPLAINT;
    public static final String LINE_NAME = "LINE_NAME";
    public static final String WORKER = "WORKER";
    public static final String AMOUNT_LINE = "AMOUNT";
    public static final String LINES = "LINES";
    public static final String PAPERS = "PAPERS";
    public static final String PAPER_NAME = "PAPER_NAME";
    public static final String WEEKLY_AMOUNT = "WEEKLY_AMOUNT";
    public static final String WORKERS = "WORKERS";
    public static final String TOTAL_DAY = "TOTAL_DAY";

    public static final String PHONE = "PHONE";
    public static final String TABLE_STATE = "TABLE_STATE";


    public static final String PRESENT = "PRESENT";
    public static final String POSITION = "POSITION";
    public static final String STORE = "STORE";

    public static final String STORE_NAME = "STORE_NAME";

    public static final String USER_NAME = "USER_NAME";

    public static Long getlong(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }

    public Database_helper(@Nullable Context context) {

        super(context, "customer.db", null, 1);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String createSte = "CREATE TABLE " + CUSTOMER_TABLE + " ( " + COLUMN_CUSTOMER_NAME + " TEXT," + COLUMN_CARD_NO +
                " INTEGER," + COLUMN_PHONE + " INTEGER(10) PRIMARY KEY," + COLUMN_PACKAGE + " INTEGER," + COLUMN_PAID + " BOOLEAN NOT NULL " +
                "CHECK (PAID IN (0,1)),"
                + COLUMN_ADDR + " TEXT," + COLUMN_DATE + " INTEGER," + LINE_NAME + " TEXT, " + POSITION + " INTEGER );";

        String create_trigger = "CREATE TABLE " + HISTORY + " (" + NAME + " TEXT, " + AMOUNT + " INTEGER NOT NULL, " + DATE + " INTEGER);";

        String create_line = "CREATE TABLE " + LINES + " ( " + LINE_NAME + " TEXT PRIMARY KEY," + WORKER + " TEXT, " + AMOUNT_LINE + " INTEGER );";

        String create_paper = "CREATE TABLE " + PAPERS + " ( " + PAPER_NAME + " TEXT PRIMARY KEY," + WEEKLY_AMOUNT + " INTEGER );";


        String create_workers = "CREATE TABLE " + WORKERS + " ( " + WORKER + " TEXT PRIMARY KEY," + PHONE + " INTEGER, " + TOTAL_DAY + " INTEGER , "
                + PRESENT + " BOOLEAN NOT NULL);";

        String create_dates = "CREATE TABLE " + TABLE_DATE + "( " + SINGAL_DATE + " INTEGER PRIMARY KEY NOT NULL);";

        String create_complaint = "CREATE TABLE " + TABLE_COMPLAINT + "(" + COMPLAINT + " TEXT, " + NAME1 + " TEXT);";

        String create_Store = "CREATE TABLE " + STORE + " (" + STORE_NAME + " TEXT NOT NULL, " + USER_NAME + " TEXT) ";

        db.execSQL(create_line);
        db.execSQL(create_Store);
        db.execSQL(create_paper);
        db.execSQL(create_workers);
        db.execSQL(create_trigger);
        db.execSQL(create_dates);
        db.execSQL(createSte);
        db.execSQL(create_complaint);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINT);
        db.execSQL("DROP TABLE IF EXISTS " + LINES);
        db.execSQL("DROP TABLE IF EXISTS " + STORE);
        db.execSQL("DROP TABLE IF EXISTS " + WORKERS);
        db.execSQL("DROP TABLE IF EXISTS " + DATE);
        db.execSQL("DROP TABLE IF EXISTS " + PAPERS);
        db.close();
    }

    public boolean Delete_all() {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("DELETE FROM " + CUSTOMER_TABLE );
            db.execSQL("DELETE FROM " + TABLE_COMPLAINT );
            db.execSQL("DELETE FROM " + TABLE_DATE );
            db.execSQL("DELETE FROM " + HISTORY );
            db.execSQL("DELETE FROM " + LINES );
            db.execSQL("DELETE FROM " + WORKERS);
            db.execSQL("DELETE FROM " + STORE );
            db.execSQL("DELETE FROM " + PAPERS );
            db.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public boolean save_Store(String store, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(STORE_NAME, store);
        cv.put(USER_NAME, name);
        long insert = db.insert(STORE, null, cv);

        return insert != -1;
    }

    public boolean update_Store(String store, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete(STORE, null, null);
        cv.put(STORE_NAME, store);
        cv.put(USER_NAME, name);
        long insert = db.insert(STORE, null, cv);

        return insert != -1;
    }

    public ArrayList<String> get_Store() {
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<String> result = new ArrayList<>();
        String q = "SELECT * FROM " + STORE;
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            result.add(cursor.getString(0));
            result.add(cursor.getString(1));
        }
        return result;
    }


    // paper table  section

    public boolean add_Paper(String name, int amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PAPER_NAME, name);
        cv.put(WEEKLY_AMOUNT, amount);
        long insert = db.insert(PAPERS, null, cv);
        db.close();
        return insert != -1;
    }

    public void Delete_Paper(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "DELETE FROM " + PAPERS + " WHERE " + PAPER_NAME + " ='" + name + "';";
        db.execSQL(q);
        db.close();
    }

    public List<String> get_Papers() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DummyContent> returnList = new ArrayList<>();
        String Querry = "SELECT * FROM " + PAPERS;
        List<String> paperlist = new ArrayList<>();
        Cursor cursor = db.rawQuery(Querry, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                paperlist.add(name);

            } while (cursor.moveToNext());
        }
        db.close();
        return paperlist;
    }

    public ArrayList<Line> get_Paper() {
        SQLiteDatabase db = this.getReadableDatabase();

        String Querry = "SELECT * FROM " + PAPERS;
        ArrayList<Line> paperlist = new ArrayList<>();
        Cursor cursor = db.rawQuery(Querry, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                Integer am = cursor.getInt(1);
                paperlist.add(new Line(null, name, am));

            } while (cursor.moveToNext());
        }
        db.close();
        return paperlist;

    }


    //Worker section

    public boolean Replace_PresentsToTrue(ArrayList<String> names) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PRESENT, 1);
        for (int i = 0; i < names.size(); i++) {
            int d = db.update(WORKERS, cv, WORKER + "='" + names.get(i) + "'", null);
            if ((d != -1) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean Replace_PresentsToFalse(ArrayList<String> names) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PRESENT, false);
        for (int i = 0; i < names.size(); i++) {
            int d = db.update(WORKERS, cv, WORKER + "='" + names.get(i) + "'", null);
            if ((d != -1) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean add_Worker(Workers workers) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WORKER, workers.getName());
        cv.put(PRESENT, false);
        cv.put(TOTAL_DAY, workers.getCount());
        cv.put(PHONE, workers.getPhone());

        long insert = db.insert(WORKERS, null, cv);
        db.close();
        return insert != -1;
    }


    public boolean Delete_worker(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "DELETE FROM " + WORKERS + " WHERE " + WORKER + " ='" + name + "';";
        db.execSQL(q);
        ContentValues cv = new ContentValues();
        cv.put(WORKER, "");
        int done = db.update(LINES, cv, WORKER + " ='" + name + "'", null);
        db.close();
        return done != -1;
    }

    public List<Workers> get_workers() {
        SQLiteDatabase db = this.getReadableDatabase();

        String Querry = "SELECT * FROM " + WORKERS;
        List<Workers> workers = new ArrayList<>();
        Cursor cursor = db.rawQuery(Querry, null);
        if (cursor.moveToFirst()) {
            do {
                Workers worker = new Workers(cursor.getString(0), cursor.getLong(1), cursor.getInt(2),
                        cursor.getInt(3) == 1);


                workers.add(worker);

            } while (cursor.moveToNext());
        }
        db.close();
        return workers;
    }


    public boolean Refresh(String name) {
        SQLiteDatabase db = getWritableDatabase();
        Integer zero = 0;
        ContentValues cv = new ContentValues();
        cv.put(TOTAL_DAY, 0);
        int d = db.update(WORKERS, cv, WORKER + "='" + name + "'", null);
        return d != -1;
    }

    public boolean add_presenty(ArrayList<String> presents) {
        SQLiteDatabase db = getWritableDatabase();
        boolean yes = true;
        for (int i = 0; i < presents.size(); i++) {
            String q = "SELECT " + TOTAL_DAY + " FROM " + WORKERS + " WHERE " + WORKER + "='" + presents.get(i) + "';";

            Cursor cursor = db.rawQuery(q, null);
            if (cursor.moveToFirst()) {
                int amount = cursor.getInt(0);
                ContentValues cv = new ContentValues();
                cv.put(TOTAL_DAY, amount + 1);
                int done = db.update(WORKERS, cv, WORKER + "='" + presents.get(i) + "'", null);
                if (done == -1) {
                    yes = false;
                }
            }

        }
        return yes;
    }

    public boolean remove_presenty(ArrayList<String> presents) {
        SQLiteDatabase db = getWritableDatabase();
        boolean yes = true;
        for (int i = 0; i < presents.size(); i++) {
            String q = "SELECT " + TOTAL_DAY + " FROM " + WORKERS + " WHERE " + WORKER + "='" + presents.get(i) + "';";

            Cursor cursor = db.rawQuery(q, null);
            if (cursor.moveToFirst()) {
                int amount = cursor.getInt(0);
                ContentValues cv = new ContentValues();
                cv.put(TOTAL_DAY, amount - 1);
                int done = db.update(WORKERS, cv, WORKER + "='" + presents.get(i) + "'", null);
                if (done == -1) {
                    yes = false;
                }
            }

        }
        return yes;
    }


    public ArrayList<String> get_workers_String() {
        SQLiteDatabase db = this.getReadableDatabase();

        String Querry = "SELECT * FROM " + WORKERS;
        ArrayList<String> workers = new ArrayList<>();
        Cursor cursor = db.rawQuery(Querry, null);
        if (cursor.moveToFirst()) {
            do {
                String worker = cursor.getString(0);


                workers.add(worker);

            } while (cursor.moveToNext());
        }
        db.close();
        return workers;
    }


    public boolean update_worker(Workers workers, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WORKER, workers.getName());
        cv.put(PHONE, workers.getPhone());
        int up = db.update(WORKERS, cv, WORKER + " ='" + name + "'", null);
        cv.remove(PHONE);
        int so = db.update(LINES, cv, WORKER + " ='" + name + "'", null);
        db.close();
        return (up != -1 & (so != -1));
    }

    // Line Section


    public boolean add_Line(Line line) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LINE_NAME, line.getLine());
        cv.put(AMOUNT_LINE, line.getAmount());
        cv.put(WORKER, line.getWorker());

        long insert = db.insert(LINES, null, cv);
        db.close();
        return insert != -1;
    }

    public boolean update_Line(Line line, String Line) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(LINE_NAME, line.getLine());
        cv.put(AMOUNT_LINE, line.getAmount());
        cv.put(WORKER, line.getWorker());

        long insert = db.update(LINES, cv, LINE_NAME + "='" + Line + "'", null);
        cv.clear();
        cv.put(LINE_NAME, line.getLine());

        long in = db.update(CUSTOMER_TABLE, cv, LINE_NAME + " ='" + Line + "'", null);

        db.close();

        return in != -1 & insert != -1;
    }

    public void Delete_Line(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String q = "DELETE FROM " + LINES + " WHERE " + LINE_NAME + " ='" + name + "';";
        ContentValues cv = new ContentValues();
        cv.put(LINE_NAME, "");
        db.update(CUSTOMER_TABLE, cv, LINE_NAME + " ='" + name + "'", null);
        db.execSQL(q);
        db.close();

    }


    public List<Line> get_Lines() {
        SQLiteDatabase db = this.getReadableDatabase();

        String Querry = "SELECT * FROM " + LINES;
        List<Line> Lines = new ArrayList<>();
        Cursor cursor = db.rawQuery(Querry, null);
        if (cursor.moveToFirst()) {
            do {
                Line line = new Line(cursor.getString(0), cursor.getString(1), cursor.getInt(2));


                Lines.add(line);

            } while (cursor.moveToNext());
        }
        db.close();
        return Lines;
    }


    public List<String> get_Lines_name() {
        SQLiteDatabase db = this.getReadableDatabase();

        String Querry = "SELECT * FROM " + LINES;
        List<String> Lines = new ArrayList<>();
        Cursor cursor = db.rawQuery(Querry, null);
        if (cursor.moveToFirst()) {
            do {
                String line = cursor.getString(0);


                Lines.add(line);

            } while (cursor.moveToNext());
        }
        db.close();
        return Lines;
    }

    public List<String> get_Workers_Lines(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String Querry = "SELECT " + LINE_NAME + " FROM " + LINES + " WHERE " + WORKER + " ='" + name + "';";
        List<String> Lines = new ArrayList<>();
        Cursor cursor = db.rawQuery(Querry, null);
        if (cursor.moveToFirst()) {
            do {
                String line = cursor.getString(0);


                Lines.add(line);

            } while (cursor.moveToNext());
        }
        db.close();
        return Lines;
    }

    public Integer get_worker_amount(String toString) {
        SQLiteDatabase db = this.getReadableDatabase();
        String q = "SELECT " + AMOUNT_LINE + " FROM " + LINES + " WHERE " + WORKER + " ='" + toString + "';";
        Cursor cursor = db.rawQuery(q, null);
        Integer amount = 0;
        if (cursor.moveToFirst()) {
            do {
                amount = amount + cursor.getInt(0);

            } while (cursor.moveToNext());
            return amount;
        }
        return 0;
    }


    public boolean add_one(DummyContent dum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CUSTOMER_NAME, dum.getName1());
        cv.put(COLUMN_CARD_NO, dum.getCard());
        cv.put(COLUMN_PHONE, dum.getPhone());
        cv.put(COLUMN_PACKAGE, dum.getPackage_amo());
        cv.put(COLUMN_PAID, dum.isPaid());
        cv.put(COLUMN_ADDR, dum.getDetails());
        cv.put(LINE_NAME, dum.getLine());

        cv.put(COLUMN_DATE, getlong(dum.getRenewal()));
        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }


    }

    public List<DummyContent> get_Customer_of_Line(String line) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<DummyContent> dum = new ArrayList<>();
        String q = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + LINE_NAME + " ='" + line + "';";
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                Long card = cursor.getLong(1);
                Long phone = cursor.getLong(2);
                int amount = cursor.getInt(3);
                boolean paid = cursor.getInt(4) == 1;
                String details = cursor.getString(5);
                Long date = cursor.getLong(6);
                Date date1 = new Date(date);
                String line_name = cursor.getString(7);
                int pos = cursor.getInt(8);


                DummyContent dm = new DummyContent(name,
                        card,
                        phone,
                        paid,
                        amount,
                        details,
                        date1, pos, line_name);
                dum.add(dm);
            } while (cursor.moveToNext());
            return dum;
        }
        //DummyContent dumm = new DummyContent("hehab",910293L,2324L,true,453,
        //"fwiijebf",new Date(),"some other line");
        //dum.add(dumm);
        //dum.add(dumm);
        return dum;
    }

    public boolean add_one_Trigger(String name, int amount, Date date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        ContentValues cv1 = new ContentValues();
        String Q = "SELECT * FROM " + TABLE_DATE;


        Cursor cursor = db.rawQuery(Q, null);
        boolean flag = true;
        if (cursor.moveToFirst()) {
            do {
                if (date.compareTo(new Date(cursor.getLong(0))) == 0) {
                    flag = false;
                    break;
                }
            } while (cursor.moveToNext());
            if (flag) {
                cv1.put(SINGAL_DATE, getlong(date));
            }

        } else {
            cv1.put(SINGAL_DATE, getlong(date));
        }


        cv.put(NAME, name);
        cv.put(AMOUNT, amount);
        cv.put(DATE, getlong(date));
        long insert = db.insert(HISTORY, null, cv);
        long insert1 = db.insert(TABLE_DATE, null, cv1);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }


    }

    public List<DummyContent> geteveryone() {
        List<DummyContent> returnList = new ArrayList<>();
        String Querry = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(Querry, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                Long card = cursor.getLong(1);
                Long phone = cursor.getLong(2);
                int amount = cursor.getInt(3);
                boolean paid = cursor.getInt(4) == 1;
                String details = cursor.getString(5);
                Long date = cursor.getLong(6);
                Date date1 = new Date(date);
                String line = cursor.getString(7);
                int pos = cursor.getInt(8);


                DummyContent dm = new DummyContent(name,
                        card,
                        phone,
                        paid,
                        amount,
                        details,
                        date1, pos, line);
                returnList.add(dm);


            } while (cursor.moveToNext());
        } else {

        }
        db.close();

        return returnList;


    }

    public boolean updatePo(ArrayList<DummyContent> dm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        int i = 1;
        for (DummyContent dum : dm) {

            cv.put(POSITION, i);
            int rv = db.update(CUSTOMER_TABLE, cv, COLUMN_PHONE + " = " + dum.getPhone(), null);
            i = i + 1;
            if (rv == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean pay(int i, Long phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PACKAGE, i);
        cv.put(COLUMN_PAID, 1);
        int rv = db.update(CUSTOMER_TABLE, cv, COLUMN_PHONE + " = " + phone, null);
        return rv != -1;
    }

    public List<History_content> get_Trigger() {
        String Q = "SELECT * FROM " + HISTORY;
        ArrayList<History_content> contents = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(Q, null);
        if (cursor.moveToNext()) {
            do {
                String name = cursor.getString(0);
                int amount = cursor.getInt(1);
                Long date = cursor.getLong(2);
                Date date1 = new Date(date);
                History_content content = new History_content(name, amount, date1);
                contents.add(content);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return contents;

    }

    public boolean change_renewal(Date date, Long phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Long date_long = getlong(date);
        cv.put(COLUMN_DATE, date_long);
        int rv = db.update(CUSTOMER_TABLE, cv, COLUMN_PHONE + " = " + phone, null);
        return rv != -1;

    }

    public List<Date> retrive_dates() {
        ArrayList<Date> dates = new ArrayList<>();
        String Q = "SELECT * FROM " + TABLE_DATE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Q, null);
        if (cursor.moveToFirst()) {
            do {
                Date date = new Date(cursor.getLong(0));
                dates.add(date);
            } while (cursor.moveToNext());
        }
        return dates;
    }

    public List<CustHistory> retrive_info(String name) {
        ArrayList<CustHistory> dates = new ArrayList<>();
        String Q = "SELECT * FROM " + HISTORY + " WHERE " + NAME + " = '" + name + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Q, null);
        if (cursor.moveToFirst()) {
            do {
                Date date = new Date(cursor.getLong(2));
                int amount = cursor.getInt(1);
                CustHistory content = new CustHistory(date, amount);
                dates.add(content);
            } while (cursor.moveToNext());
        }
        return dates;
    }

    public List<complaint_model> get_complaint() {
        ArrayList<complaint_model> list = new ArrayList<>();
        String Q = "SELECT * FROM " + TABLE_COMPLAINT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Q, null);
        if (cursor.moveToFirst()) {
            do {
                complaint_model com = new complaint_model(cursor.getString(1), cursor.getString(0));
                list.add(com);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public boolean add_complaint(complaint_model com) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COMPLAINT, com.getComplaint());
        cv.put(NAME1, com.getName());
        Long insert = db.insert(TABLE_COMPLAINT, null, cv);
        return insert != -1;

    }

    public boolean update_complaint(String name, String complaint) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COMPLAINT, complaint);
        int g = db.update(TABLE_COMPLAINT, cv, NAME1 + " = '" + name + "'", null);

        return g != -1;

    }

    public boolean delete_everyone(Long hc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String phone = hc.toString();
        int d = db.delete(CUSTOMER_TABLE, COLUMN_PHONE + "=" + phone, null);
        return d != 0;
    }

    public void delete_complaint(complaint_model hc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String phone = hc.getComplaint();
        //String phone1 = hc.getName();
        //db.line_fragment(TABLE_COMPLAINT,COMPLAINT +"="+phone +" and "+NAME1+"="+phone1,null);
        String q = "DELETE FROM " + TABLE_COMPLAINT + " WHERE " + COMPLAINT + " ='" + phone + "';";
        db.execSQL(q);
        db.close();
    }

    public boolean update(Long phone, DummyContent dum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CUSTOMER_NAME, dum.getName1());
        cv.put(COLUMN_CARD_NO, dum.getCard());
        cv.put(COLUMN_PHONE, dum.getPhone());
        cv.put(COLUMN_PACKAGE, dum.getPackage_amo());
        cv.put(COLUMN_PAID, dum.isPaid());
        cv.put(COLUMN_ADDR, dum.getDetails());
        cv.put(COLUMN_DATE, getlong(dum.getRenewal()));
        cv.put(LINE_NAME, dum.getLine());
        int up = db.update(CUSTOMER_TABLE, cv, COLUMN_PHONE + " = " + phone, null);
        db.close();
        return up == 1;
    }

    public DummyContent getvalues(Long phone) {
        String Q = "SELECT * FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_PHONE + "= " + phone;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Q, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            Long card = cursor.getLong(1);
            Long phone1 = cursor.getLong(2);
            int amount = cursor.getInt(3);
            boolean paid = cursor.getInt(4) == 1;
            String details = cursor.getString(5);
            Long date = cursor.getLong(6);
            Date date1 = new Date(date);
            String line = cursor.getString(7);
            int pos = cursor.getInt(8);

            DummyContent dm = new DummyContent(name,
                    card,
                    phone1,
                    paid,
                    amount,
                    details,
                    date1, pos, line);
            return dm;
        }
        return null;
    }

    public boolean delete_paid() {
        SQLiteDatabase db = this.getWritableDatabase();
        //String Q = "UPDATE "+ CUSTOMER_TABLE+ " SET " + COLUMN_PAID + " = " + false;
        //db.execSQL(Q,null);
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PAID, false);
        int i = db.update(CUSTOMER_TABLE, cv, null, null);
        db.close();
        return i > 0;

    }

    public File createJson() throws IOException {
        File json = new File(new StringBuilder(getAppPath()).append("JsonStorage.json").toString());

        ArrayList<DummyContent> all = (ArrayList<DummyContent>) geteveryone();
        JsonArray jsonArray = new JsonArray();

        for (DummyContent dm : all) {
            JsonObject one = new JsonObject();

            one.addProperty(COLUMN_CUSTOMER_NAME, dm.getName1());
            one.addProperty(COLUMN_CARD_NO, dm.getCard());
            one.addProperty(COLUMN_PHONE, dm.getPhone());
            one.addProperty(COLUMN_PACKAGE, dm.getPackage_amo());
            one.addProperty(COLUMN_ADDR, dm.getDetails());
            one.addProperty(COLUMN_PAID, dm.isPaid());
            one.addProperty(LINE_NAME, dm.getLine());
            one.addProperty(POSITION, dm.getPo());
            one.addProperty(COLUMN_DATE, dm.getRenewal().getTime());
            JsonObject table = new JsonObject();
            table.addProperty("TABLES", CUSTOMER_TABLE);
            table.add(CUSTOMER_TABLE, one);
            jsonArray.add(table);
        }


        ArrayList<History_content> history = (ArrayList<History_content>) get_Trigger();
        for (History_content his : history) {
            JsonObject one = new JsonObject();
            one.addProperty(NAME, his.getName());
            one.addProperty(AMOUNT, his.getAmount());
            one.addProperty(DATE, his.getDate().getTime());

            JsonObject table = new JsonObject();
            table.addProperty("TABLES", HISTORY);
            table.add(HISTORY, one);
            jsonArray.add(table);
        }

        ArrayList<Line> lines = (ArrayList<Line>) get_Lines();
        for (Line his : lines) {
            JsonObject one = new JsonObject();
            one.addProperty(LINE_NAME, his.getLine());
            one.addProperty(AMOUNT_LINE, his.getAmount());
            one.addProperty(WORKER, his.getWorker());


            JsonObject table = new JsonObject();
            table.addProperty("TABLES", LINES);
            table.add(LINES, one);
            jsonArray.add(table);
        }

        ArrayList<Workers> workers = (ArrayList<Workers>) get_workers();
        for (Workers his : workers) {
            JsonObject one = new JsonObject();
            one.addProperty(WORKER, his.getName());
            one.addProperty(PHONE, his.getPhone());
            one.addProperty(TOTAL_DAY, his.getCount());
            one.addProperty(PRESENT, his.isPresent());

            JsonObject table = new JsonObject();
            table.addProperty("TABLES", WORKERS);
            table.add(WORKERS, one);
            jsonArray.add(table);

        }

        for (Line entry : get_Paper()) {

            JsonObject one = new JsonObject();
            one.addProperty(PAPER_NAME, entry.getWorker());
            one.addProperty(WEEKLY_AMOUNT, entry.getAmount());
            JsonObject table = new JsonObject();
            table.addProperty("TABLES", PAPERS);
            table.add(PAPERS, one);
            jsonArray.add(table);
        }


        for (complaint_model c : get_complaint()) {
            JsonObject one = new JsonObject();
            one.addProperty(NAME, c.getName());
            one.addProperty(COMPLAINT, c.getComplaint());
            JsonObject table = new JsonObject();
            table.addProperty("TABLES", TABLE_COMPLAINT);
            table.add(TABLE_COMPLAINT, one);
            jsonArray.add(table);
        }

        JsonObject st = new JsonObject();
        st.addProperty(STORE_NAME, get_Store().get(0));
        st.addProperty(USER_NAME, get_Store().get(1));
        JsonObject table1 = new JsonObject();
        table1.addProperty("TABLES", STORE);
        table1.add(STORE, st);
        jsonArray.add(table1);


        FileWriter fileWriter = new FileWriter(json);
        fileWriter.write(String.valueOf(jsonArray));
        fileWriter.close();
        return json;
    }

    public String getAppPath() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator
                + c.getResources().getString(R.string.app_name) + File.separator);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }

    public boolean readJsonfile(File file) throws FileNotFoundException {

        Boolean flg = true;
        if (Delete_all()) {



            FileReader fileReader = new FileReader(file);
            JsonParser ob = new JsonParser();
            JsonArray list = (JsonArray) ob.parse(fileReader);

            for (JsonElement j : list) {

                switch (j.getAsJsonObject().get("TABLES").getAsString()) {
                    case CUSTOMER_TABLE:
                        JsonElement dum = j.getAsJsonObject().get(CUSTOMER_TABLE);
                        DummyContent dm = new DummyContent(
                                dum.getAsJsonObject().get(COLUMN_CUSTOMER_NAME).getAsString(),
                                dum.getAsJsonObject().get(COLUMN_CARD_NO).getAsLong(),
                                dum.getAsJsonObject().get(COLUMN_PHONE).getAsLong(),
                                dum.getAsJsonObject().get(COLUMN_PAID).getAsBoolean(),
                                dum.getAsJsonObject().get(COLUMN_PACKAGE).getAsInt(),
                                dum.getAsJsonObject().get(COLUMN_ADDR).getAsString(),
                                new Date(dum.getAsJsonObject().get(COLUMN_DATE).getAsLong()),
                                dum.getAsJsonObject().get(POSITION).getAsInt(),
                                dum.getAsJsonObject().get(LINE_NAME).getAsString());
                        if (!add_one(dm)) {
                            flg = false;
                        }
                        break;
                    case HISTORY:
                        JsonElement hi = j.getAsJsonObject().get(HISTORY);
                        History_content history_content = new History_content(hi.getAsJsonObject().get(NAME1).getAsString(),
                                hi.getAsJsonObject().get(AMOUNT).getAsInt(),
                                new Date(hi.getAsJsonObject().get(DATE).getAsLong())
                        );
                        if (!add_one_Trigger(history_content.getName(), history_content.getAmount(), history_content.getDate())) {
                            flg = false;
                        }
                        break;
                    case LINES:
                        JsonElement line = j.getAsJsonObject().get(LINES);
                        Line line1 = new Line(line.getAsJsonObject().get(LINE_NAME).getAsString(),
                                line.getAsJsonObject().get(WORKER).getAsString(),
                                line.getAsJsonObject().get(AMOUNT_LINE).getAsInt());
                        if (!add_Line(line1)) {
                            flg = false;
                        }
                        break;


                    case WORKERS:
                        JsonElement worker = j.getAsJsonObject().get(WORKERS);
                        Workers workers = new Workers(worker.getAsJsonObject().get(WORKER).getAsString(),
                                worker.getAsJsonObject().get(PHONE).getAsLong(),
                                worker.getAsJsonObject().get(TOTAL_DAY).getAsInt(),
                                worker.getAsJsonObject().get(PRESENT).getAsBoolean());
                        if (!add_Worker(workers)) flg = false;

                        break;


                    case PAPERS:
                        JsonElement paper = j.getAsJsonObject().get(PAPERS);
                        if (!add_Paper(paper.getAsJsonObject().get(PAPER_NAME).getAsString(),
                                paper.getAsJsonObject().get(WEEKLY_AMOUNT).getAsInt())) flg = false;
                        break;
                    case TABLE_COMPLAINT:
                        JsonElement complaint = j.getAsJsonObject().get(TABLE_COMPLAINT);
                        complaint_model complaint_model = new complaint_model(complaint.getAsJsonObject().get(NAME1).getAsString(),
                                complaint.getAsJsonObject().get(COMPLAINT).getAsString());
                        if (!add_complaint(complaint_model)) flg = false;
                        break;
                    case STORE:
                        JsonElement store = j.getAsJsonObject().get(STORE);

                        if (!save_Store(store.getAsJsonObject().get(STORE_NAME).getAsString(),
                                store.getAsJsonObject().get(USER_NAME).getAsString())) flg = false;
                        break;
                }



            }
        }
        else {
            return false;
        }



        return flg;
    }



}

