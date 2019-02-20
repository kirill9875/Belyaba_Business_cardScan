package com.blogspot.atifsoftwares.imagetotextapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLDataException;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "[DBcards]";
    public static final String TABLE_NAME = "[Cards]";

    public static final String NAME = "[name]";
    public static final String SUBJECT = "[subject]";
    public static final String COMPANY = "[company]";
    public static final String EMAIL = "[email]";
    public static final String TELEPHONE = "[telephone]";
    public static final String URL = "[URL]";
    public static final String IMAGE_PATH = "[img_path]";
    public static final String IMAGE_NAME = "[img_name]";
    public static final String DESCRIPTION = "[description]";
    public static final String DATE = "[date]";


    public DBHelper(Context context){
        super(context,DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String zp = "CREATE TABLE " + TABLE_NAME + " (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NAME + " text, " +
                SUBJECT + " text, " +
                COMPANY + " text, " +
                EMAIL + " text, " +
                TELEPHONE + " text, " +
                URL + " text, " +
                DESCRIPTION + " text, " +
                DATE + " text, " +
                IMAGE_NAME + " text, " +
                IMAGE_PATH + " text)";

        db.execSQL(zp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
}
