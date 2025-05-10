package com.example.dailytasks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddTaskActivity extends AppCompatActivity {

    private EditText descriptionEditText;
    private Spinner statusSpinner;
    private Button addButton;
    private TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        descriptionEditText = findViewById(R.id.taskDescription);
        statusSpinner = findViewById(R.id.statusSpinner);
        addButton = findViewById(R.id.saveButton);


        dbHelper = new TaskDatabaseHelper(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);


        addButton.setOnClickListener(v -> addTaskToDatabase());
    }

    private void addTaskToDatabase() {

        String description = descriptionEditText.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString();

        if (description.isEmpty()) {
            descriptionEditText.setError("Please enter a description");
            return;
        }

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "User session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Task newTask = new Task(description, status, userId);

        dbHelper.addTask(newTask);

        Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();

        setResult(RESULT_OK, new Intent());
        finish();
    }

}
