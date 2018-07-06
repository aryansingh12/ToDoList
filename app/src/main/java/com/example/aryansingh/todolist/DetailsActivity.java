package com.example.aryansingh.todolist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {



    TextView detail_title;
    TextView detail_description;
    TextView time_text;
    TextView date_text;

    public static final String DATE_KEY = "date";
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String TIME_KEY = "time";
    public static final String ID_KEY = "id";

//    public static final String TITLE_DETAILS = "title";


    Button taskCompletedButton;
    Button alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        detail_title = (TextView) findViewById(R.id.detail_title);
        detail_description = (TextView) findViewById(R.id.detail_description);
        time_text = (TextView) findViewById(R.id.time_text);
        date_text = (TextView) findViewById(R.id.date_text);
        taskCompletedButton = (Button) findViewById(R.id.taskCompletedButton);

        Intent intent = getIntent();

        String title = intent.getStringExtra(MainActivity.TITLE_KEY);
        String description = intent.getStringExtra(MainActivity.DESCRIPTION_KEY);
        String date = intent.getStringExtra(MainActivity.DATE_KEY);
        String time = intent.getStringExtra(MainActivity.TIME_KEY);

        final long id1 = intent.getLongExtra("id",0);

        final int position = intent.getIntExtra("position",0);

        detail_title.setText(title);
        detail_description.setText(description);
        date_text.setText(date);
        time_text.setText(time);

        final ToDo toDo = new ToDo(title,description,time,date);
        toDo.id = id1;

        taskCompletedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailsActivity.this);
                builder.setTitle("Task Finished");
                builder.setCancelable(false);
                builder.setMessage("Have you finished the task "+detail_title.getText().toString()+" ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ToDoOpenHelper openHelper = ToDoOpenHelper.getInstance(getApplicationContext());
                        SQLiteDatabase database = openHelper.getWritableDatabase();

                        String[] selectingId = {id1 + ""};
                        database.delete(Contract.ToDo.TABLE_NAME, Contract.ToDo.COLUMN_ID + " = ?", selectingId);

                        Intent i = new Intent();
                        Toast.makeText(DetailsActivity.this,"Task "+detail_title.getText().toString()+" Finished",Toast.LENGTH_SHORT).show();
                        setResult(0, i);
                        finish();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.favorite){

            // add to favorites

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
    }

    public void favorites(View view){
        Intent intent = new Intent(this,FavoritesActivity.class);

        String title = detail_title.getText().toString();
        String descrip = detail_description.getText().toString();

        intent.putExtra("title_details",title);
        intent.putExtra("descriptions_details",descrip);
        startActivity(intent);

        Toast.makeText(this,"Added to favorites",Toast.LENGTH_LONG).show();
    }

//    public void delete(View view){
//        Intent intent = new Intent();
//        // this takes us to the main activity, and deletes the item with the id provided
//        // we are giving the id to the main activity
//
//        intent.putExtra("position",position);
//        setResult(5,intent);
//        finish();
//    }

    public void alarm(View view) {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SET_ALARM) ==
                PackageManager.PERMISSION_GRANTED){
            setAlarm("Done",4,40);
        }

        // this is for the dialogue, request permission
        else{
            String[] permissions = {Manifest.permission.SET_ALARM};
            ActivityCompat.requestPermissions(this,permissions,1);
        }
    }
    public void setAlarm(String message, int hours, int mins){

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hours)
                .putExtra(AlarmClock.EXTRA_MINUTES, mins);


         if (intent.resolveActivity(getPackageManager()) != null) {
        startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
