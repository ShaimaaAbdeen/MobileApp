package com.example.mrsshimaa.mobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class FileContent extends AppCompatActivity {

    String filecontent;
    TextView FC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_content);

        FC= (TextView) findViewById(R.id.filecontenttxt);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            filecontent=extras.getString("File");



        }

        FC.setText(filecontent);


    }
}
