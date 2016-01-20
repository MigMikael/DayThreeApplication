package com.mig.cpsudev.daythreeapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ControlActivity extends AppCompatActivity {

    final String url = "http://192.168.43.54:3000/start";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        final EditText questionIdEditText = (EditText) findViewById(R.id.questionIdEditText);
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionId = questionIdEditText.getText().toString();
                PostQuestionTask task = new PostQuestionTask(ControlActivity.this, questionId);
                task.execute(url);
            }
        });

        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostQuestionTask task = new PostQuestionTask(ControlActivity.this, "-1");
                task.execute(url);
            }
        });
    }
}
