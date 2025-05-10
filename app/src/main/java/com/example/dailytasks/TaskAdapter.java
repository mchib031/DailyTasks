package com.example.dailytasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {

    private final Context context;
    private final ArrayList<Task> tasks;
    private final TaskDatabaseHelper dbHelper;

    public TaskAdapter(Context context, ArrayList<Task> tasks, TaskDatabaseHelper dbHelper) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
        this.dbHelper = dbHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false);
        }

        TextView textViewTask = convertView.findViewById(R.id.textViewTask);
        ImageView imageViewDelete = convertView.findViewById(R.id.imageViewDelete);

        textViewTask.setText(task.getDescription());

        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteTask(task.getId());        // Remove from database
                tasks.remove(position);                   // Remove from list
                notifyDataSetChanged();                   // Refresh the list view
            }
        });

        return convertView;
    }
}
