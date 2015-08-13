package com.androidtmc.sales.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by minhc_000 on 13/08/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "Sales";
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1); // 1: version
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = ProductRepository.getCreateTableSQL();
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
