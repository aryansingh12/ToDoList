package com.example.aryansingh.todolist;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ToDoAdaptor.ToDoItemClickListener {

    ArrayList<ToDo> favs = new ArrayList<>();
    ListView listView;
    ToDoAdaptor adapter;

    public static final String TITLE_KEY = "title";
    public static final String ID_KEY = "id";
    public static final String DESCRIPTION_KEY = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listView = findViewById(R.id.listView_fav);

        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();
        Cursor cursor = database.query(Contract.ToDo.TABLE_NAME,null,null, null,null,null,null);

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(Contract.Favorites.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(Contract.Favorites.COLUMN_DESCRIPTION));
            String time = cursor.getString(cursor.getColumnIndex(Contract.ToDo.TIME));
            String date = cursor.getString(cursor.getColumnIndex(Contract.ToDo.DATE));

            long id1 = cursor.getLong(cursor.getColumnIndex(Contract.Favorites.COLUMN_ID));
            if (id1 > -1) {
                ToDo toDo = new ToDo(title, description, time, date);
                toDo.id = id1;
                favs.add(toDo);
            }
        }



        Intent intent = getIntent();
        String title = intent.getStringExtra("title_details");
        String descrip = intent.getStringExtra("descriptions_details");
        String time = intent.getStringExtra("time_details");
        String date = intent.getStringExtra("date_details");

        adapter = new ToDoAdaptor(this,favs,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        ToDo toDo = new ToDo(title, descrip,time,date);


        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Favorites.COLUMN_TITLE, toDo.title);
        contentValues.put(Contract.Favorites.COLUMN_DESCRIPTION, toDo.description);
        contentValues.put(Contract.Favorites.DATE, toDo.date);
        contentValues.put(Contract.Favorites.TIME, toDo.time);

        long id1 = database.insert(Contract.Favorites.TABLE_NAME, null, contentValues);
        if (id1 > -1) {
            toDo.id = id1;
            favs.add(0, toDo);
        }

        adapter.notifyDataSetChanged();


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DetailsActivity.class);

        String title = favs.get(position).title;
        String descrip = favs.get(position).description;

        intent.putExtra(TITLE_KEY,title);
        intent.putExtra(DESCRIPTION_KEY,descrip);

        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final ToDo toDo = favs.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("confirm delete");
        builder.setMessage("Do you really want to delete this expense?");
        builder.setCancelable(false);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(FavoritesActivity.this,"Item removed",Toast.LENGTH_LONG).show();

                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                long id = toDo.id;

                String[] id_identifier = {id+""};
                database.delete(Contract.Favorites.TABLE_NAME,Contract.Favorites.COLUMN_ID +" = ?", id_identifier);

                favs.remove(position);
                adapter.notifyDataSetChanged();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();// creates the dialog
        dialog.show();
        return true;
    }

    @Override
    public void rowButtonClicked(View view, int position) {

    }
}
