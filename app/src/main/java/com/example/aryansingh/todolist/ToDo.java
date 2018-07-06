package com.example.aryansingh.todolist;

/**
 * Created by Aryan Singh on 7/1/2018.
 */

public class ToDo {

    String title;
    String description;
    long id;
    String time;
    String date;

    public ToDo(String title, String description, String time, String date) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
    }
}