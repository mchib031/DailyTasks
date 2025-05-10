package com.example.dailytasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Task> tasks;
    private ListView tasksListView;
    private TaskAdapter adapter;
    private TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        tasksListView = findViewById(R.id.tasksListView);
        dbHelper = new TaskDatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to login
            return;
        }


        tasks = new ArrayList<>(dbHelper.getTasksByUserId(userId));


        // Set up the adapter for ListView
        adapter = new TaskAdapter(this, tasks, dbHelper);
        tasksListView.setAdapter(adapter);

        // Load the tasks initially
        loadTasks();

        // Handle item click (you can add logic here to edit or delete a task)
        tasksListView.setOnItemClickListener((parent, view, position, id) -> {
            // Add logic to edit or delete task
        });
    }

    // Method to load tasks from the database
    private void loadTasks() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) return;

        List<Task> taskList = dbHelper.getTasksByUserId(userId);

        adapter.clear();
        tasks.clear();
        tasks.addAll(taskList);
        adapter.notifyDataSetChanged();
    }


    // Handle result from AddTaskActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is OK and from the AddTaskActivity
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Reload tasks from the database after task is added
            loadTasks();
        }
    }

    // Launch AddTaskActivity when the add button is clicked
    public void onAddTaskClicked(View view) {
        // Start AddTaskActivity and expect a result
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivityForResult(intent, 1); // Start AddTaskActivity and expect a result
    }
}