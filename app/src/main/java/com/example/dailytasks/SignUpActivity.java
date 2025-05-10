package com.example.dailytasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonSignUp, buttonLogin;
    private TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);  // Ensure your layout file is named correctly

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonLogin = findViewById(R.id.buttonLogin);  // Ensure this button exists in the XML
        dbHelper = new TaskDatabaseHelper(this);

        // Sign-up button click listener
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validation checks
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (username.length() < 4) {  // Minimum username length check
                    Toast.makeText(SignUpActivity.this, "Username must be at least 4 characters", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {  // Minimum password length check
                    Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else if (dbHelper.isUsernameTaken(username)) {
                    Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Register the user in the database
                    dbHelper.registerUser(username, password);
                    Toast.makeText(SignUpActivity.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();

                    // Redirect to Login Activity
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });

        // Login button click listener to go back to login screen
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Login Activity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();  // Optionally finish the sign-up activity to avoid returning to it
            }
        });
    }
}
