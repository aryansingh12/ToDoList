package com.example.aryansingh.todolist;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Aryan Singh on 7/2/2018.
 */

public class Contract {

    public class ToDo{
        public static final String TABLE_NAME = "todos";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ID = "id";
        public static final String TIME = "time";
        public static final String DATE = "date";

    }

    public class Favorites{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ID = "id";
        public static final String TIME = "time";
        public static final String DATE = "date";
    }
}
