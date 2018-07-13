package com.example.aryansingh.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aryan Singh on 7/11/2018.
 */

public class FavoriteAdaptor extends ArrayAdapter {


    ArrayList<ToDo> items;
    LayoutInflater layoutInflater;

    public FavoriteAdaptor(@NonNull Context context, ArrayList<ToDo> items) {
        super(context, 0, items);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View output = layoutInflater.inflate(R.layout.favorite_row_layout,parent,false);

        TextView dateTextView = output.findViewById(R.id.dateTextView);
        TextView timeTextView = output.findViewById(R.id.timeTextView);
        TextView titleTextView = output.findViewById(R.id.titleTextView);
        TextView locationTextView = output.findViewById(R.id.locationTextView);

        ToDo toDo = items.get(position);
        titleTextView.setText(toDo.title);
        dateTextView.setText(toDo.date);
        timeTextView.setText(toDo.time);
        locationTextView.setText(locationTextView.getText() + ": " + toDo.location);

        return output;
    }


}
