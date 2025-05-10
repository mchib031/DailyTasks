package com.example.dailytasks;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
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

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info(tasks);", null);
        while (cursor.moveToNext()) {
            Log.d("DB_SCHEMA", "Column: " + cursor.getString(1));
        }
        cursor.close();


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

        Log.d("MainActivity", "Loaded tasks for userId " + userId + ": " + tasks.size() + " tasks");

        tasksListView.setOnItemClickListener((parent, view, position, id) -> {
        });
    }

    private void loadTasks() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) return;

        List<Task> taskList = dbHelper.getTasksByUserId(userId);
        Log.d("MainActivity", "Fetched tasks from DB for userId " + userId + ": " + taskList.size());


        tasks.clear();
        tasks.addAll(taskList);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadTasks();
        }
    }

    public void onAddTaskClicked(View view) {
        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivityForResult(intent, 1);
    }
}