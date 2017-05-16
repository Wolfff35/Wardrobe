package com.wolff.wardrobe.localdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.wolff.wardrobe.localdb.DbSchema.WItemTable;

/**
 * Created by wolff on 11.04.2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "wItemBase.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ WItemTable.TABLE_NAME+"("+
        WItemTable.Cols.ID+" integer primary key autoincrement, "+
        WItemTable.Cols.UUID+", "+
        WItemTable.Cols.TITLE+", "+
                WItemTable.Cols.SEASON+", "+
                WItemTable.Cols.MIN_T+", "+
                WItemTable.Cols.MAX_T+
        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
