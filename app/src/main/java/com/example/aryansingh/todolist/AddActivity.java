package com.example.aryansingh.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{



    EditText titleEditText;
    EditText timeEditText;
    EditText dateEditText;
    EditText descriptionEditText;
    Button finishButton;
    TextView addTask;

    public static final String DATE_KEY = "date";
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String TIME_KEY = "time";

    int year,month,day;
    int year1,month1,day1;
    int hour,mins;
    int hour1 = 0,mins1 = 0;

    long epochTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addTask=(TextView)findViewById(R.id.addTask);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        timeEditText = (EditText) findViewById(R.id.timeEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        finishButton=(Button)findViewById(R.id.finishButton);

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                int month = newCalendar.get(Calendar.MONTH);
                int year = newCalendar.get(Calendar.YEAR);
                int currentDay=newCalendar.get(Calendar.DAY_OF_MONTH);
                showDatePicker(AddActivity.this, year, month, currentDay);
            }
        });

        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar=Calendar.getInstance();
                int hour= calendar.get(Calendar.HOUR_OF_DAY);
                int minute=calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        int mHour=hourOfDay;
                        int mMinute=minute;
                        if(mHour<10&&mMinute<10){
                            timeEditText.setText("0"+mHour+":"+"0"+mMinute);
                        }
                        else
                        if(mHour<10&&mMinute>10){
                            timeEditText.setText("0"+mHour+":"+mMinute);
                        }
                        else
                        if(mHour>10&&mMinute<10){
                            timeEditText.setText(mHour+":"+"0"+mMinute);
                        }
                        else
                            timeEditText.setText(mHour+":"+mMinute);

                    }
                },hour,minute,true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra(DATE_KEY, date);
                intent.putExtra(TIME_KEY, time);
                intent.putExtra(TITLE_KEY, title);
                intent.putExtra(DESCRIPTION_KEY, description);

                setResult(0, intent);
                finish();
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        year1 = year;
        month1 = month+1;
        day1 = dayOfMonth;

        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mins = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, AddActivity.this, hour, mins, true);
        timePickerDialog.show();

        dateEditText.setText(day1 + " / " + month1 + " / " + year1);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        hour1 = hourOfDay;
        mins1 = minute;



    }

    public void showDatePicker(Context context, int initialYear, int initialMonth, int initialDay) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        epochTime = calendar.getTime().getTime();
                        dateEditText.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, initialYear, initialMonth, initialDay);
        datePickerDialog.setTitle("Select Date");
        datePickerDialog.show();

    }
}
