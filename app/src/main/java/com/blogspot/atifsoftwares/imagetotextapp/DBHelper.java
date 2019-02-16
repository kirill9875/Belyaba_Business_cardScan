package com.blogspot.atifsoftwares.imagetotextapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLDataException;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "[DBcards]";
    public static final String TABLE_NAME = "[Cards]";

    public static final String NAME = "[name]";
    public static final String COMPANY = "[company]";
    public static final String EMAIL = "[email]";
    public static final String TELEPHONE = "[telephone]";
    public static final String URL = "[URL]";


    public DBHelper(Context context){
        super(context,DB_NAME,null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.print("Создание бд");
        String zp = "CREATE TABLE " + TABLE_NAME + " (_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NAME + " text, " +
                COMPANY + " text, " +
                EMAIL + " text, " +
                TELEPHONE + " text, " +
                URL + " text)";

        db.execSQL(zp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DB_NAME);
        onCreate(db);
    }
}
