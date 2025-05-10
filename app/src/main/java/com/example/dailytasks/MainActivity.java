package com.example.dailytasks;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Task> tasks;
    private ListView tasksListView;
    private TaskAdapter adapter;
    private TaskDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView quoteText = findViewById(R.id.quoteText);
        new FetchQuoteTask(quoteText).execute();


        ImageButton signOutButton = findViewById(R.id.buttonSignOut);
        signOutButton.setOnClickListener(v -> signOut());

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

    private void signOut() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();  // Clear all session data
        editor.apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Prevent going back
        startActivity(intent);
        finish(); // Finish MainActivity
    }

    private static class FetchQuoteTask extends AsyncTask<Void, Void, String> {
        private final TextView textView;

        public FetchQuoteTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://zenquotes.io/api/random");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                JSONObject quoteObj = jsonArray.getJSONObject(0);
                return "\"" + quoteObj.getString("q") + "\"\n- " + quoteObj.getString("a");

            } catch (Exception e) {
                return "Could not fetch quote 😞";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }
    }


}