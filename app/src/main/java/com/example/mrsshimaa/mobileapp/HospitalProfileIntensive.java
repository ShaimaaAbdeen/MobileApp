package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class HospitalProfileIntensive extends AppCompatActivity {
    TextView HospitalName;
    TextView HospitalAddress;
    TextView HospitalContactNumber;
    TextView HospitalEmail;
    TextView GeneralRoomsNumber;
    TextView NeonatalRoomsNumber;
    TextView PediatricRoomsNumber;
    TextView PsychiatricRoomsNumber;
    TextView CoronaryRoomsNumber;
    TextView NeurologicalRoomsNumber;
    TextView PostAnesthesiaRoomsNumber;
    ArrayList<String>RTs;
    ArrayList<Integer>RNs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_profile_intensive);
        HospitalName = (TextView) findViewById(R.id.hospitalname);
        HospitalAddress = (TextView) findViewById(R.id.hospitaladdresstext);
        HospitalContactNumber = (TextView) findViewById(R.id.hospitalcontacttext);
        HospitalEmail = (TextView) findViewById(R.id.hospitalemailtext);
        GeneralRoomsNumber = (TextView) findViewById(R.id.generalroomsnumber);
        NeonatalRoomsNumber = (TextView) findViewById(R.id.neonatalroomsnumber);
        PediatricRoomsNumber = (TextView) findViewById(R.id.pediatricroomsnumber);
        PsychiatricRoomsNumber = (TextView) findViewById(R.id.psychiatricroomsnumber);
        CoronaryRoomsNumber = (TextView) findViewById(R.id.coronaryroomsnumber);
        NeurologicalRoomsNumber = (TextView) findViewById(R.id.neurologicalroomsnumber);
        PostAnesthesiaRoomsNumber = (TextView) findViewById(R.id.postanesthesiaroomsnumber);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            RTs=extras.getStringArrayList("roomtypes");
            RNs=extras.getIntegerArrayList("roomnumbers");
            HospitalName.setText(extras.getString("name"));
            HospitalAddress.setText(extras.getString("address"));
            HospitalContactNumber.setText(extras.getString("contactnumber"));
            HospitalEmail.setText(extras.getString("email"));

            if(RTs.get(0).compareTo("empty")==0)
            {

                if(extras.getString("roomtype").compareTo("General/Surgical ICU")==0)
                {
                    GeneralRoomsNumber.setText(extras.getString("roomnumber"));
                    GeneralRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));
                }
                if(extras.getString("roomtype").compareTo("Neonatal ICU")==0)
                {
                    NeonatalRoomsNumber.setText(extras.getString("roomnumber"));
                    NeonatalRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));

                }
                if(extras.getString("roomtype").compareTo("Pediatric ICU")==0)
                {
                    PediatricRoomsNumber.setText(extras.getString("roomnumber"));
                    PediatricRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));

                }
                if(extras.getString("roomtype").compareTo("Coronary Care Unit")==0)
                {
                    CoronaryRoomsNumber.setText(extras.getString("roomnumber"));
                    CoronaryRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));

                }
                if(extras.getString("roomtype").compareTo("Neurological ICU")==0)
                {
                    NeurologicalRoomsNumber.setText(extras.getString("roomnumber"));
                    NeurologicalRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));

                }
                if(extras.getString("roomtype").compareTo("Post-anesthesia Care Unit")==0)
                {
                    PostAnesthesiaRoomsNumber.setText(extras.getString("roomnumber"));
                    PostAnesthesiaRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));

                }
                if(extras.getString("roomtype").compareTo("Psychiatric ICU")==0)
                {
                    PsychiatricRoomsNumber.setText(extras.getString("roomnumber"));
                    PsychiatricRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));

                }

            }
            else
            {
                int countgeneral =0;
                int countneonatal =0;
                int countpediatric =0;
                int countcoronary =0;
                int countneurological =0;
                int countpostanesthesia =0;
                int countpsychiatric =0;
                int X;

                for(int i=0;i<RTs.size();i++)
                {


                    if(RTs.get(i).toString().compareTo("General/Surgical ICU")==0)
                    {
                        countgeneral ++;
                        if(countgeneral>1)
                        {

                            X=Integer.parseInt(((TextView) findViewById(R.id.generalroomsnumber)).getText().toString());
                            X+=Integer.parseInt(RNs.get(i).toString());
                            GeneralRoomsNumber.setText(Integer.toString(X));
                            GeneralRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));


                        }
                        else {
                            GeneralRoomsNumber.setText(RNs.get(i).toString());
                            GeneralRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));
                        }

                    }
                    if(RTs.get(i).toString().compareTo("Neonatal ICU")==0)
                    {
                        countneonatal++;
                        if(countneonatal>1)
                        {

                            X=Integer.parseInt(((TextView) findViewById(R.id.neonatalroomsnumber)).getText().toString());
                            X+=Integer.parseInt(RNs.get(i).toString());
                            NeonatalRoomsNumber.setText(Integer.toString(X));
                            NeonatalRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));


                        }
                        else {
                            NeonatalRoomsNumber.setText(RNs.get(i).toString());
                            NeonatalRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));
                        }



                    }
                    if(RTs.get(i).toString().compareTo("Pediatric ICU")==0)
                    {
                        countpediatric++;
                        if(countpediatric>1)
                        {

                            X=Integer.parseInt(((TextView) findViewById(R.id.pediatricroomsnumber)).getText().toString());
                            X+=Integer.parseInt(RNs.get(i).toString());
                            PediatricRoomsNumber.setText(Integer.toString(X));
                            PediatricRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));


                        }
                        else {
                            PediatricRoomsNumber.setText(RNs.get(i).toString());
                            PediatricRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));
                        }



                    }
                    if(RTs.get(i).toString().compareTo("Coronary Care Unit")==0)
                    {
                        countcoronary++;
                        if(countcoronary>1)
                        {

                            X=Integer.parseInt(((TextView) findViewById(R.id.coronaryroomsnumber)).getText().toString());
                            X+=Integer.parseInt(RNs.get(i).toString());
                            CoronaryRoomsNumber.setText(Integer.toString(X));
                            CoronaryRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));


                        }
                        else {
                            CoronaryRoomsNumber.setText(RNs.get(i).toString());
                            CoronaryRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));
                        }



                    }
                    if(RTs.get(i).toString().compareTo("Neurological ICU")==0)
                    {
                        countneurological++;
                        if(countneurological>1)
                        {

                            X=Integer.parseInt(((TextView) findViewById(R.id.neurologicalroomsnumber)).getText().toString());
                            X+=Integer.parseInt(RNs.get(i).toString());
                            NeurologicalRoomsNumber.setText(Integer.toString(X));
                            NeurologicalRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));


                        }
                        else {
                            NeurologicalRoomsNumber.setText(RNs.get(i).toString());
                            NeurologicalRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));
                        }


                    }
                    if(RTs.get(i).toString().compareTo("Post-anesthesia Care Unit")==0)
                    {
                        countpostanesthesia++;
                        if(countpostanesthesia>1)
                        {

                            X=Integer.parseInt(((TextView) findViewById(R.id.postanesthesiaroomsnumber)).getText().toString());
                            X+=Integer.parseInt(RNs.get(i).toString());
                            PostAnesthesiaRoomsNumber.setText(Integer.toString(X));
                            PostAnesthesiaRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));


                        }
                        else {
                            PostAnesthesiaRoomsNumber.setText(RNs.get(i).toString());
                            PostAnesthesiaRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));
                        }



                    }
                    if(RTs.get(i).toString().compareTo("Psychiatric ICU")==0)
                    {
                        countpsychiatric++;
                        if(countpsychiatric>1)
                        {

                            X=Integer.parseInt(((TextView) findViewById(R.id.psychiatricroomsnumber)).getText().toString());
                            X+=Integer.parseInt(RNs.get(i).toString());
                            PsychiatricRoomsNumber.setText(Integer.toString(X));
                            PsychiatricRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));


                        }
                        else {
                            PsychiatricRoomsNumber.setText(RNs.get(i).toString());
                            PsychiatricRoomsNumber.setTextColor(Color.parseColor("#FF1B8F23"));
                        }


                    }

                }
            }

        }

    }










    public  void getLocation(View view)
    {
        Intent i = new Intent(HospitalProfileIntensive.this,HospitalLocation.class);
        i.putExtra("Origin Address","Current Position");
        i.putExtra("Destination Address",HospitalName.getText().toString());

        HospitalProfileIntensive.this.finish();

        startActivity(i);




    }



}
