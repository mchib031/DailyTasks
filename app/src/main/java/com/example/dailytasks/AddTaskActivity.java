package com.example.dailytasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTaskActivity extends Activity {

    private EditText editTextTask;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editTextTask = findViewById(R.id.editTextTask);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = editTextTask.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("task", task);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
