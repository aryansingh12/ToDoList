package com.example.aryansingh.todolist;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, AdapterView.OnItemLongClickListener, ToDoAdaptor.ToDoItemClickListener {

    ArrayList<ToDo> toDos = new ArrayList<>();

    ArrayList<String> favorite = new ArrayList<>();

    ToDoAdaptor adapter;

    ImageButton AddButton;



    ArrayList<String> checkBox;

    public static final String TITLE_KEY = "title";
    public static final String ID_KEY = "id";
    public static final String DESCRIPTION_KEY = "description";
    public static final String DATE_KEY = "date";
    public static final String TIME_KEY = "time";
    public static final String LOCATION_KEY = "location";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.mainListView);
        AddButton = findViewById(R.id.AddButton);
        checkBox = new ArrayList<>();


        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getReadableDatabase();

        Cursor cursor = database.query(Contract.ToDo.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
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
                toDos.add(toDo);
            }
        }

        adapter = new ToDoAdaptor(this, toDos,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);


        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                Log.d("Main ", "add button");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();

        inflater.inflate(R.menu.main_menu,menu);
        MenuItem delete =menu.findItem(R.id.del);
        if(checkBox.size()==0) {
            delete.setVisible(false);
        }
        else
            delete.setVisible(true);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.del) {

            if (checkBox.size() > 0) {

                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                for (String id : checkBox) {

                    String[] selectingId = {id + ""};
                    database.delete(Contract.ToDo.TABLE_NAME, Contract.ToDo.COLUMN_ID + " = ?", selectingId);

                }
                checkBox.clear();
                database.close();
                MainActivity.this.invalidateOptionsMenu();
                refresh();
                //  do this to create the list again

            }
        }
        else if(item.getItemId() == R.id.fav){
            Intent intent = new Intent(this,FavoritesActivity.class);
            startActivity(intent);
        }
                return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


        Intent intent = new Intent(this, DetailsActivity.class);

        String title = toDos.get(position).title;
        String description = toDos.get(position).description;
        String time = toDos.get(position).time;
        String date = toDos.get(position).date;
        String loc = toDos.get(position).location;
        long id1 = toDos.get(position).id;


        intent.putExtra("position", position);
        intent.putExtra(TITLE_KEY, title);
        intent.putExtra("id", id1);
        intent.putExtra(DESCRIPTION_KEY, description);
        intent.putExtra(TIME_KEY,time);
        intent.putExtra(DATE_KEY,date);
        intent.putExtra(LOCATION_KEY,loc);


        startActivityForResult(intent, 3);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        final ToDo toDo = toDos.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("confirm delete");
        builder.setMessage("Do you really want to delete this toDo?");
        builder.setCancelable(false);


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Item removed", Toast.LENGTH_LONG).show();

                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                long id = toDo.id;
                String[] selectingId = {id + ""};
                database.delete(Contract.ToDo.TABLE_NAME, Contract.ToDo.COLUMN_ID + " = ?", selectingId);

                toDos.remove(position);
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

        ToDo toDo = toDos.get(position);
        long id  = toDo.id;
        if(view.getId() == R.id.editButton){
            Intent intent = new Intent(this, EditActivity.class);

            String title = toDos.get(position).title;
            String description = toDos.get(position).description;
            String time = toDos.get(position).time;
            String date = toDos.get(position).date;
            String loc = toDos.get(position).location;
            long id1 = toDos.get(position).id;
            int fav = toDos.get(position).favorite;


            intent.putExtra("position", position);
            intent.putExtra(TITLE_KEY, title);
            intent.putExtra("id", id1);
            intent.putExtra(DESCRIPTION_KEY, description);
            intent.putExtra(TIME_KEY,time);
            intent.putExtra(DATE_KEY,date);
            intent.putExtra(LOCATION_KEY,loc);
            intent.putExtra("fav",fav);


            startActivityForResult(intent, 4);
        }

        else if(view.getId() == R.id.itemCheckBox){
            Boolean checked=((CheckBox)view).isChecked();
            if(checked){
                checkBox.add(toDos.get(position).id+"");
                this.invalidateOptionsMenu();
            }
            else
            {
                checkBox.remove(toDos.get(position).id+"");
                this.invalidateOptionsMenu();
            }
        }

        else if(view.getId() == R.id.imageButton){
            // how to take the id here.
            ImageButton ib = (ImageButton)view;

            ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(this);
            SQLiteDatabase database = openHelper.getWritableDatabase();


            if(toDos.get(position).favorite == 1){
                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.ToDo.COLUMN_TITLE, toDos.get(position).title);
                contentValues.put(Contract.ToDo.COLUMN_DESCRIPTION, toDos.get(position).description);
                contentValues.put(Contract.ToDo.DATE,toDos.get(position).date);
                contentValues.put(Contract.ToDo.TIME,toDos.get(position).time);
                contentValues.put(Contract.ToDo.FAVORITE,0);
                contentValues.put(Contract.ToDo.LOCATION,toDos.get(position).location);

                ib.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                Toast.makeText(this,"Removed from favorites",Toast.LENGTH_LONG).show();
                database.update(Contract.ToDo.TABLE_NAME,contentValues,Contract.ToDo.COLUMN_ID + " = " + id,null );
                toDos.get(position).favorite = 0;
                adapter.notifyDataSetChanged();
            }
            else if(toDos.get(position).favorite == 0){

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.ToDo.COLUMN_TITLE, toDos.get(position).title);
                contentValues.put(Contract.ToDo.COLUMN_DESCRIPTION, toDos.get(position).description);
                contentValues.put(Contract.ToDo.DATE,toDos.get(position).date);
                contentValues.put(Contract.ToDo.TIME,toDos.get(position).time);
                contentValues.put(Contract.ToDo.FAVORITE,1);
                contentValues.put(Contract.ToDo.LOCATION,toDos.get(position).location);

                ib.setImageDrawable(getResources().getDrawable(R.drawable.new_favorite));
                Toast.makeText(this,"Added to favorites",Toast.LENGTH_LONG).show();
                database.update(Contract.ToDo.TABLE_NAME,contentValues,Contract.ToDo.COLUMN_ID + " = " + id,null );
                toDos.get(position).favorite = 1;
                adapter.notifyDataSetChanged();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1){// add button
            if(resultCode == 8){

                // error if you press the back button
                String title = data.getStringExtra(AddActivity.TITLE_KEY);
                String description = data.getStringExtra(AddActivity.DESCRIPTION_KEY);
                String date = data.getStringExtra(AddActivity.DATE_KEY);
                String time = data.getStringExtra(AddActivity.TIME_KEY);
                String loc = data.getStringExtra(AddActivity.LOCATION_KEY);

                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.ToDo.COLUMN_TITLE, title);
                contentValues.put(Contract.ToDo.COLUMN_DESCRIPTION, description);
                contentValues.put(Contract.ToDo.DATE,date);
                contentValues.put(Contract.ToDo.TIME,time);
                contentValues.put(Contract.ToDo.LOCATION,loc);
                contentValues.put(Contract.ToDo.FAVORITE,0);


                long id1 = database.insert(Contract.ToDo.TABLE_NAME, null, contentValues);
                if (id1 > -1) {
                    ToDo toDo = new ToDo(title,description,time,date,loc,0);
                    toDo.id = id1;
                    toDos.add(toDo);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        if(requestCode == 3){//  removing from details page
            if(resultCode == 7){
                refresh();
                Toast.makeText(this,"Item removed", Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == 4){// edit activity
            if(resultCode == 9){
                String title = data.getStringExtra(EditActivity.TITLE_KEY);
                String description = data.getStringExtra(EditActivity.DESCRIPTION_KEY);
                String date = data.getStringExtra(EditActivity.DATE_KEY);
                String time = data.getStringExtra(EditActivity.TIME_KEY);
                String loc = data.getStringExtra(EditActivity.LOCATION_KEY);
                long id = data.getLongExtra("id",0);
                int position = data.getIntExtra("pos",0);
                int fav = data.getIntExtra("fav",0);

                ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
                SQLiteDatabase database = openHelper.getWritableDatabase();

                ToDo toDo = new ToDo(title,description,time,date,loc,fav);
                toDo.id = id;


                ContentValues contentValues = new ContentValues();
                contentValues.put(Contract.ToDo.COLUMN_TITLE, title);
                contentValues.put(Contract.ToDo.COLUMN_DESCRIPTION, description);
                contentValues.put(Contract.ToDo.DATE,date);
                contentValues.put(Contract.ToDo.TIME,time);
                contentValues.put(Contract.ToDo.LOCATION,loc);

                database.update(Contract.ToDo.TABLE_NAME,contentValues,Contract.ToDo.COLUMN_ID + " = " + id, null );

                toDos.set(position,toDo);
                adapter.notifyDataSetChanged();

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refresh(){
        String formatted="";
        ToDoOpenHelper toDoOpenHelper = ToDoOpenHelper.getInstance(this);
        SQLiteDatabase database=toDoOpenHelper.getReadableDatabase();
        toDos.clear();
        String addQuery="select * from "+Contract.ToDo.TABLE_NAME+" order by "+Contract.ToDo.COLUMN_ID+" asc;";
        Cursor cursor=database.rawQuery(addQuery,null);

        while(cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(Contract.ToDo.COLUMN_DESCRIPTION));
            String time = cursor.getString(cursor.getColumnIndex(Contract.ToDo.TIME));
            String loc = cursor.getString(cursor.getColumnIndex(Contract.ToDo.LOCATION));
            long id = cursor.getLong(cursor.getColumnIndex(Contract.ToDo.COLUMN_ID));
            int date = cursor.getInt(cursor.getColumnIndex(Contract.ToDo.DATE));
            int fav = cursor.getInt(cursor.getColumnIndex(Contract.ToDo.FAVORITE));

            if (date != 0) {
                Date d = new Date(date);
                DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                formatted = format.format(d);
            }
            ToDo toDo = new ToDo(title, description, time, formatted,loc ,fav);
            toDos.add(toDo);
        }
        adapter.notifyDataSetChanged();
        database.close();
        }
    }



/*

        AlarmManager  alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this,MyReceiver.class);
        PendingIntent pendingIntent  = PendingIntent.getBroadcast(this,1,intent,0);
        Calendar calendar = Calendar.getInstance();

        // take input type as hours , mins , date and all
        calendar,set(parameters);

        // do all this in the create alarm button.
        // set the title and the description, along w the time.

         */

