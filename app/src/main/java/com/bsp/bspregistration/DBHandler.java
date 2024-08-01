package com.bsp.bspregistration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static  String DB_NAME = "";
    private static final int DB_VERSION = 1;


    private static final String TABLE_NAME_forUpload = "tbl_forupload";
    private static final String id = "id";
    private static final String json = "json";
    private static final String formnumber = "formnumber";
    private static final String tempref = "tempref";
    private static final String amount = "amount";
    private static final String isuploaded = "isuploaded";


    private static final String tbl_adultposition = "tbl_adultposition";
    private static final String id_pos = "id";
    private static final String adultpos = "adultpos";
    private static final String price = "price";
    private static final String type = "type";
    private static final String archive = "archive";

    public DBHandler(Context context , String db_namex) {
        super(context, db_namex, null, DB_VERSION);
        DB_NAME = db_namex;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME_forUpload + " ("
                + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + json + " TEXT,"
                + formnumber + " TEXT,"
                + tempref + " TEXT,"
                + amount + " TEXT,"
                + isuploaded + " TEXT)";
        db.execSQL(query);

        String query1 = "CREATE TABLE " + tbl_adultposition + " ("
                + id_pos + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + adultpos + " TEXT,"
                + price + " TEXT,"
                + type + " TEXT,"
                + archive + " TEXT)";
        db.execSQL(query1);

    }

    public void addNewposition(String posix, String pricex, String typex, String arv) {
        boolean isok = selectifExistingInpos(posix);

        if (!isok)
        {
            try
            {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(adultpos, posix);
                values.put(price, pricex);
                values.put(type, typex);
                values.put(archive, arv);
                db.insertOrThrow(tbl_adultposition, null, values);
                //db.close();
            }catch (Exception e){
                String ex = e.getMessage();
                String exx = e.getMessage();
            }
        }
    }
    public String[] selectpos()
    {
        String[] ret = new String[]{"INSTITUTIONAL SCOUTING  REPRESENTATIVE (ISR) ' 100"};

                String txtt = "";
                SQLiteDatabase db = this.getReadableDatabase();
                String selectQuery = "select * from " + tbl_adultposition + " where " + archive  + " = ?  GROUP by " + adultpos;
                String[] selectionArgs = {"0"};
                Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
                if (cursor != null)
                {
                    while (cursor.moveToNext())
                    {
                        @SuppressLint("Range") String jsox = cursor.getString(cursor.getColumnIndex(adultpos));
                txtt += jsox + ",";
            }
            cursor.close();
        }
        ret = txtt.split(",");
        return ret;
    }
    public String selectprice(String posxxx)
    {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + tbl_adultposition + " where " + adultpos  + " = ?";
        String[] selectionArgs = {posxxx};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String jsox = cursor.getString(cursor.getColumnIndex(price));
              ret = jsox;
            }
            cursor.close();
        }
        return ret;
    }
    public boolean selectifExistingInpos(String ansx)
    {
        boolean ret = false;
        try
        {
            SQLiteDatabase db = this.getReadableDatabase();
            String[] projection = {adultpos};
            String selection = adultpos + " = ? ";
            String[] selectionArgs = {ansx};
            Cursor cursor = db.query(tbl_adultposition, projection, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(adultpos));
                    ret = true;
                }
                cursor.close();
            }
            else {
                ret = false;
            }
        }
        catch (Exception exception)
        {
            Log.e("getStackTrace", exception.getStackTrace().toString());
            Log.e("getMessage", exception.getMessage());
        }
        return  ret;
    }
    public void addNewData(String jsonx, String formnumberx, String temprefx, String amo, String upload) {

        boolean isok = selectifExistingInAns(jsonx);

        if (!isok)
            {
                try
                {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(json, jsonx);
                values.put(formnumber, formnumberx);
                    values.put(tempref, temprefx);
                    values.put(amount, amo);
                    values.put(isuploaded, upload);
                db.insertOrThrow(TABLE_NAME_forUpload, null, values);
                //db.close();
                }catch (Exception e){
                    String ex = e.getMessage();
                    String exx = e.getMessage();
                }
            }
    }
    public void updateAnswetoUploaded(String que_id, String cur, String isdup){

        if (!isdup.equals("")){
            // Convert the string to a JSONArray
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(que_id);


            // Iterate through each object in the array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Update the value of "overwrite" field
                jsonObject.put("overwrite", "");
            }
                que_id = jsonArray.toString();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(isuploaded, "1");
        values.put(tempref, cur);
        String selection = json + " = ?";
        String[] selectionArgs = { que_id };
        int rowsUpdated = db.update(TABLE_NAME_forUpload, values, selection, selectionArgs);
        Log.e("das ", String.valueOf(rowsUpdated));
    }

    public ArrayList<String> selectUps()
    {
        ArrayList<String> ret = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + TABLE_NAME_forUpload + " where " + isuploaded  + " = ?";
        String[] selectionArgs = {"0"};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String jsox = cursor.getString(cursor.getColumnIndex(json));
                ret.add(jsox);
            }
            cursor.close();
        }
        return ret;
    }

    public String selecttomain(String temp)
    {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + TABLE_NAME_forUpload + " where " + tempref  + " = ?";
        String[] selectionArgs = {temp};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String jsox = cursor.getString(cursor.getColumnIndex(json));
                ret = jsox;
            }
            cursor.close();
        }
        return ret;
    }
    public String selectamount(String temp)
    {
        String ret = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + TABLE_NAME_forUpload + " where " + tempref  + " = ?";
        String[] selectionArgs = {temp};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String jsox = cursor.getString(cursor.getColumnIndex(amount));
                ret = jsox;
            }
            cursor.close();
        }
        return ret;
    }
    public ArrayList<HistoryList> selecthis()
    {
        ArrayList<HistoryList> ret = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + TABLE_NAME_forUpload + " where " + isuploaded  + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String jsox = cursor.getString(cursor.getColumnIndex(tempref));
                @SuppressLint("Range") String jsox1 = cursor.getString(cursor.getColumnIndex(amount));

                HistoryList up = new HistoryList(jsox,jsox1);
                ret.add(up);
            }
            cursor.close();
        }
        return ret;
    }
    public ArrayList<UploadList> selectUpx()
    {
        ArrayList<UploadList> ret = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from " + TABLE_NAME_forUpload + " where " + isuploaded  + " = ?";
        String[] selectionArgs = {"0"};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String jsox = cursor.getString(cursor.getColumnIndex(tempref));

                UploadList up = new UploadList(jsox);
                ret.add(up);
            }
            cursor.close();
        }
        return ret;
    }

    public boolean selectifExistingInAns(String ansx)
    {
        boolean ret = false;
        try
        {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {json};
        String selection = json + " = ? ";
        String[] selectionArgs = {ansx};
        Cursor cursor = db.query(TABLE_NAME_forUpload, projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(json));
                ret = true;
            }
            cursor.close();
        }
        else {
            ret = false;
        }
        }
        catch (Exception exception)
        {
            Log.e("getStackTrace", exception.getStackTrace().toString());
            Log.e("getMessage", exception.getMessage());
        }
        return  ret;
    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_forUpload);
        onCreate(db);
    }
}
