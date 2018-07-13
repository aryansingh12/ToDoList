package com.example.aryansingh.todolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    ArrayList<ToDo> favorites = new ArrayList<>();
    FavoriteAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ListView listView = findViewById(R.id.listView_fav);


        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();

        String arr[]={1+""};

        Cursor cursor = database.query(Contract.ToDo.TABLE_NAME,null,Contract.ToDo.FAVORITE+" = ?",arr,null,null,null);
        while(cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_DESCRIPTION));
            String time = cursor.getString(cursor.getColumnIndex(Contract.ToDo.TIME));
            String date = cursor.getString(cursor.getColumnIndex(Contract.ToDo.DATE));
            String location = cursor.getString(cursor.getColumnIndex(Contract.ToDo.LOCATION));
            int fav = cursor.getInt(cursor.getColumnIndex(Contract.ToDo.FAVORITE));

            long id1 = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_ID));
            if(id1>-1) {
                ToDo toDo = new ToDo(title, description, time, date,location,fav);
                toDo.id = id1;
                favorites.add(toDo);
            }
        }
        adapter = new FavoriteAdaptor(this, favorites);
        listView.setAdapter(adapter);



    }
}
