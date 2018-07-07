package com.example.mrsshimaa.mobileapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HospitalProfileBlood extends AppCompatActivity {


    TextView HospitalName;
    TextView HospitalAddress;
    TextView HospitalContactNumber;
    TextView HospitalEmail;
    TextView HospitalAvailableBlood;
    TextView HospitalNeededBlood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile_blood);


        HospitalName = (TextView) findViewById(R.id.hospitalname);
        HospitalAddress = (TextView) findViewById(R.id.hospitaladdresstext);
        HospitalContactNumber = (TextView) findViewById(R.id.hospitalcontacttext);
        HospitalEmail = (TextView) findViewById(R.id.hospitalemailtext);
        HospitalAvailableBlood = (TextView) findViewById(R.id.availablebloodtypestext);
        HospitalNeededBlood = (TextView) findViewById(R.id.neededbloodtypestext);


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            HospitalName.setText(extras.getString("name"));

            if(extras.getString("address").compareTo("null")==0) {
                HospitalAddress.setText("No address Available");
                HospitalAddress.setTextColor(Color.parseColor("#FF64B5F6"));
            }
            else
            {
                HospitalAddress.setText(extras.getString("address"));

            }
            if(extras.getString("contactnumber").compareTo("null")==0) {
                HospitalContactNumber.setText("No Contact Number Available");
                HospitalContactNumber.setTextColor(Color.parseColor("#FF64B5F6"));
            }
            else
            {
                HospitalContactNumber.setText(extras.getString("contactnumber"));

            }

            if(extras.getString("email").compareTo("null")==0) {
                HospitalEmail.setText("No Email Available");
                HospitalEmail.setTextColor(Color.parseColor("#FF64B5F6"));

            }
            else {
                HospitalEmail.setText(extras.getString("email"));
            }
            if(extras.getString("availablebloodtypes").compareTo("null")==0) {
                HospitalAvailableBlood.setText("No Available blood types");
                HospitalAvailableBlood.setTextColor(Color.parseColor("#FF64B5F6"));


            }
            else {
                HospitalAvailableBlood.setText(extras.getString("availablebloodtypes"));
            }
            if(extras.getString("neededbloodtypes").compareTo("null")==0) {
                HospitalNeededBlood.setText("No Needed blood types");
                HospitalNeededBlood.setTextColor(Color.parseColor("#FF64B5F6"));

            }
            else {
                HospitalNeededBlood.setText(extras.getString("neededbloodtypes"));
            }


        }
    }


    public  void getLocation(View view)
    {
        Intent i = new Intent(HospitalProfileBlood.this,HospitalLocation.class);
        i.putExtra("Origin Address","Current Position");
        i.putExtra("Destination Address",HospitalName.getText().toString());
        HospitalProfileBlood.this.finish();

        startActivity(i);




    }


}
