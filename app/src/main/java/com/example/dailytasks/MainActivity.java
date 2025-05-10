package com.example.dailytasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_ADD_TASK = 1;

    private ArrayList<Task> tasks;
    private TaskAdapter adapter;
    private TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAddTask = findViewById(R.id.buttonAddTask);
        ListView listViewTasks = findViewById(R.id.listViewTasks);

        // Initialize database helper
        dbHelper = new TaskDatabaseHelper(this);

        // Load tasks from database
        tasks = new ArrayList<>(dbHelper.getAllTasks());

        // Set up adapter and list view
        adapter = new TaskAdapter(this, tasks, dbHelper);
        listViewTasks.setAdapter(adapter);

        // Add task button
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_TASK);
            }
        });
    }

    // Handle result from AddTaskActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_TASK && resultCode == RESULT_OK) {
            String newTaskDescription = data.getStringExtra("task");

            if (newTaskDescription != null && !newTaskDescription.trim().isEmpty()) {
                // Create and add new task to database
                Task newTask = new Task();
                newTask.setDescription(newTaskDescription);
                newTask.setStatus("incomplete"); // Default status

                dbHelper.addTask(newTask);

                // Reload task list
                tasks.clear();
                tasks.addAll(dbHelper.getAllTasks());
                adapter.notifyDataSetChanged();
            }
        }
    }
}
