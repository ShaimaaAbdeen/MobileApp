package com.example.mrsshimaa.mobileapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;


public class Home extends AppCompatActivity {
    private PopupWindow pw;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
     intent = new Intent(Home.this, Login.class);
        startActivity(intent);

    }

    public  void GroupLogin(View view){
        intent = new Intent(Home.this, GroupLogin.class);
        startActivity(intent);
    }
    public  void BloodLogin(View view){
        intent = new Intent(Home.this, BloodLogin.class);
        startActivity(intent);
    }

    public void Joinfamily(View view){
        intent = new Intent(Home.this, SignUpBlood.class);

        startActivity(intent);
    }
    public void JoinGroupfamily(View view){
        intent = new Intent(Home.this, SignUpGroup.class);

        startActivity(intent);
    }
    public void checkHospitals(View view){
        intent = new Intent(Home.this, HospitalListIntensive.class);
        startActivity(intent);

    }


    public void showBlood(View view)
    {
        initiateBloodPopupWindow(view);

    }
    public void showIntensive(View view)
    {

        initiateIntensivePopupWindow(view);
    }
    public void showGroup(View view)
    {

        initiateGroupPopupWindow(view);
    }
    private void initiateBloodPopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) Home.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 600, 950, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER,0, 0);
            pw.setBackgroundDrawable(new BitmapDrawable());


            TextView mResultText = (TextView) layout.findViewById(R.id.bloodtextintro);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void initiateIntensivePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) Home.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popupintensive, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 600, 950, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER,0, 0);
            pw.setBackgroundDrawable(new BitmapDrawable());


            TextView mResultText = (TextView) layout.findViewById(R.id.intensivetextintro);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button_intensive);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void initiateGroupPopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) Home.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popupgroup, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 600, 950, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER,0, 0);


            TextView mResultText = (TextView) layout.findViewById(R.id.grouptextintro);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button_group);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };
}
