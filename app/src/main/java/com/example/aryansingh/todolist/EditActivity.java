package com.example.aryansingh.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    EditText titleEditText;
    EditText descriptionEditText;
    EditText timeEditText;
    EditText dateEditText;
    EditText locationEditText;
    Button finishButton;

    public static final String DATE_KEY = "date";
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String TIME_KEY = "time";
    public static final String LOCATION_KEY = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        finishButton = (Button) findViewById(R.id.finishButton);
        locationEditText = (EditText) findViewById(R.id.locationEditText);

        Intent intent = getIntent();

        String title = intent.getStringExtra(MainActivity.TITLE_KEY);
        String description = intent.getStringExtra(MainActivity.DESCRIPTION_KEY);
        String date = intent.getStringExtra(MainActivity.DATE_KEY);
        String time = intent.getStringExtra(MainActivity.TIME_KEY);
        String location = intent.getStringExtra(MainActivity.LOCATION_KEY);
        final int fav = intent.getIntExtra("fav",0);

        final long id1 = intent.getLongExtra("id",0);
        final int position = intent.getIntExtra("position",0);



            titleEditText.setText(title);
            descriptionEditText.setText(description);
            timeEditText.setText(time);
            dateEditText.setText(date);
            locationEditText.setText(location);


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String loc = locationEditText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(DATE_KEY, date);
                intent.putExtra(TIME_KEY, time);
                intent.putExtra(TITLE_KEY, title);
                intent.putExtra(DESCRIPTION_KEY, description);
                intent.putExtra(LOCATION_KEY,loc);
                intent.putExtra("id", id1);
                intent.putExtra("pos", position);
                intent.putExtra("fav",fav);
                setResult(9, intent);
                finish();
            }
        });



    }
}
