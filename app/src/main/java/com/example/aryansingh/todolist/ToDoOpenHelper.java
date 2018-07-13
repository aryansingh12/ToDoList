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
        // add a column for the favorite, a boolean one
        String todo_table = "CREATE TABLE " + Contract.ToDo.TABLE_NAME +  " ( " +
                Contract.ToDo.COLUMN_ID +  " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.ToDo.COLUMN_TITLE + " TEXT, " + Contract.ToDo.FAVORITE + " INTEGER DEFAULT 0, " +
        Contract.ToDo.COLUMN_DESCRIPTION + " TEXT, " + Contract.ToDo.LOCATION + " TEXT, "  + Contract.ToDo.DATE + " TEXT, " + Contract.ToDo.TIME + " TEXT " + " )";

        db.execSQL(todo_table);
    }

//    + Contract.ToDo.FAVORITE + "  flag INTEGER DEFAULT 0,"

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
