package com.example.dailytasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Initialize views
        descriptionEditText = findViewById(R.id.taskDescription);
        statusSpinner = findViewById(R.id.statusSpinner);
        addButton = findViewById(R.id.saveButton);

        // Initialize DatabaseHelper
        dbHelper = new TaskDatabaseHelper(this);

        // Set up the status spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // Set onClick listener for the add button
        addButton.setOnClickListener(v -> addTaskToDatabase());
    }

    private void addTaskToDatabase() {
        // Get input from user
        String description = descriptionEditText.getText().toString().trim();
        String status = statusSpinner.getSelectedItem().toString();

        // Check if description is empty
        if (description.isEmpty()) {
            descriptionEditText.setError("Please enter a description");
            return;
        }

        // Create Task object and add it to the database
        Task newTask = new Task(description, status);
        dbHelper.addTask(newTask);

        // Show a toast message to confirm task addition
        Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show();

        // Send the result back to MainActivity to refresh the list
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent); // Signal MainActivity to refresh the task list

        // Finish the activity and return to MainActivity
        finish();
    }
}
