package com.example.pia.shedplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ROWID ="_id";
    static final String KEY_SHEDNBR = "shednbr";
    static final String KEY_TEMP = "temp";
    static final String KEY_HUM = "hum";
    static final String KEY_AMM = "amm";
    static final String KEY_TREAT = "treat";
    static final String KEY_DATE = "date";
    static final String KEY_LAT = "lat";
    static final String KEY_LONG = "longitude";
    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "ShedDB";
    static final String DATABASE_TABLE = "shedLogs";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "create table shedLogs (_id integer primary key autoincrement, "
            + "shednbr text not null, temp text not null, hum text not null, amm text not null, treat text not null, date text not null, lat text not null, longitude text not null);";
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context){
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            try{
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(TAG, "Upgrating database from version " + oldVersion +" to "
            + newVersion +", which will destroy all old data ");
            db.execSQL("DROP TABLE IF EXISTS shedLogs");
            onCreate(db);
        }
    }

    // opens the database
    public DBAdapter open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // closes the database
    public void close(){
        DBHelper.close();
    }

    // adds a log into the database
    public long insertShedLog(String shednbr, String temp, String hum, String amm, String treat, String date, String lat, String longitude){
        ContentValues initialValues =  new ContentValues();
        initialValues.put(KEY_SHEDNBR, shednbr);
        initialValues.put(KEY_HUM, hum);
        initialValues.put(KEY_TEMP, temp);
        initialValues.put(KEY_AMM, amm);
        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_TREAT, treat);
        initialValues.put(KEY_LAT, lat);
        initialValues.put(KEY_LONG,longitude);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // deletes a particular log
    public boolean deleteLog(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    // retrieves all the logs
    public Cursor getAllLogs()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SHEDNBR,
                KEY_TEMP, KEY_HUM, KEY_AMM, KEY_TREAT, KEY_DATE, KEY_LAT, KEY_LONG}, null, null, null, null, null);
    }

    // retrieves a particular log
    public Cursor getLog(long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,KEY_SHEDNBR,
                                KEY_TEMP, KEY_HUM, KEY_AMM, KEY_TREAT, KEY_DATE, KEY_LAT, KEY_LONG},
                        KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    // updates a log
    public boolean updateLog(long rowId, String shednbr, String temp, String hum, String amm, String treat, String date, String lat, String longitude)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_SHEDNBR, shednbr);
        args.put(KEY_HUM, hum);
        args.put(KEY_TEMP, temp);
        args.put(KEY_AMM, amm);
        args.put(KEY_TREAT, treat);
        args.put(KEY_DATE, date);
        args.put(KEY_LAT, lat);
        args.put(KEY_LONG, longitude);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

    // deletes all entries
    public boolean removeAll(){
        return db.delete(DATABASE_TABLE, null, null) > 0;
    }



}
