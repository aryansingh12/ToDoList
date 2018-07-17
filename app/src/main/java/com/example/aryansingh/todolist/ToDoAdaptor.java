package com.example.aryansingh.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;


public class ToDoAdaptor extends ArrayAdapter {

    ArrayList<ToDo> items;
    LayoutInflater layoutInflater;
    ToDoItemClickListener toDoItemClickListener;

    public ToDoAdaptor(@NonNull Context context, ArrayList<ToDo> items, ToDoItemClickListener listener) {
        super(context, 0, items);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.toDoItemClickListener = listener;
    }

    @NonNull
    @Override
    public int getCount() {
        return items.size();
    }

    //we'll have to create a view to show something
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View output = layoutInflater.inflate(R.layout.expense_row_layout,parent,false);

        TextView dateTextView = output.findViewById(R.id.dateTextView);
        TextView timeTextView = output.findViewById(R.id.timeTextView);
        TextView titleTextView = output.findViewById(R.id.titleTextView);
        ImageButton editButton = output.findViewById(R.id.editButton);
        CheckBox itemCheckbox = output.findViewById(R.id.itemCheckBox);
        ImageButton imageButton = output.findViewById(R.id.imageButton);
        TextView locationTextView = output.findViewById(R.id.locationTextView);


        ToDo toDo = items.get(position);
        titleTextView.setText(toDo.title);
        dateTextView.setText(toDo.date);
        timeTextView.setText(toDo.time);
       locationTextView.setText("Location: " + toDo.location);


        if(toDo.favorite == 1){
            imageButton.setImageDrawable(getContext().getResources().getDrawable(R.drawable.new_favorite));
        }


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toDoItemClickListener!=null)
                    toDoItemClickListener.rowButtonClicked(view,position);
            }
        });

        itemCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toDoItemClickListener!=null)
                    toDoItemClickListener.rowButtonClicked(v,position);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toDoItemClickListener!=null)
                    toDoItemClickListener.rowButtonClicked(v,position);
            }
        });

        return output;
    }

    public interface ToDoItemClickListener {

        void rowButtonClicked(View view, int position);
    }


}
