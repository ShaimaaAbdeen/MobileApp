package com.example.mrsshimaa.mobileapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SignUpGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_group);

        List age = new ArrayList<Integer>();
        for (int i = 18; i <= 50; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        Spinner spinner = (Spinner)findViewById(R.id.spinnerage);
        spinner.setAdapter(spinnerArrayAdapter);

    }
}
