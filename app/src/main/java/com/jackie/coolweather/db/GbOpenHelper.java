package com.jackie.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/6/23.
 */
public class GbOpenHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;

    public static final String CREATE_GB = "create table gb(" +
            "id integer primary key autoincrement,"+
            "_id integer," +
            "province_name text," +
            "city_name text," +
            "district_name text)";

    public GbOpenHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
