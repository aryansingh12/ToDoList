package com.example.aryansingh.todolist;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aryan Singh on 7/2/2018.
 */

public class ToDoOpenHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "todos_db";
    public static final int VERSION = 1;

    private static ToDoOpenHelper instance;

    private ToDoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static ToDoOpenHelper getInstance(Context context) {
        if(instance == null)
            instance = new ToDoOpenHelper(context.getApplicationContext());
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String todo_table = "CREATE TABLE " + Contract.ToDo.TABLE_NAME +  " ( " +
                Contract.ToDo.COLUMN_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.ToDo.COLUMN_TITLE + " TEXT, "+
                Contract.ToDo.COLUMN_DESCRIPTION + " TEXT, " + Contract.ToDo.DATE + " TEXT, " + Contract.ToDo.TIME + " TEXT " + " )";

        String favorite_table = "CREATE TABLE " + Contract.Favorites.TABLE_NAME +  " ( " +
                Contract.Favorites.COLUMN_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Favorites.COLUMN_TITLE + " TEXT, "+
                Contract.Favorites.COLUMN_DESCRIPTION + " TEXT, " + Contract.Favorites.DATE + " TEXT, " + Contract.Favorites.TIME + " TEXT " + " )";

        db.execSQL(favorite_table);
        db.execSQL(todo_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
