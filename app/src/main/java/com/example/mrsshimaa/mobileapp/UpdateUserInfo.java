package com.example.mrsshimaa.mobileapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdateUserInfo extends AppCompatActivity {
    static EditText phonenoedit;
    public static EditText donationdatedit;
    static Spinner spinnerbloodtypes;
    Button Done;
    //String URL_UPDATE = "http://192.168.43.232/GarduationProjectV2/android/update.php";
    String URL_UPDATE = "http://wellcare.atwebpages.com/android/update.php";
    Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        Done = (Button) findViewById(R.id.donebtn);
        phonenoedit = (EditText) findViewById(R.id.phonenoedit);
        donationdatedit = (EditText) findViewById(R.id.donationdateedit);

        final List bloodtypes = new ArrayList<String>();
        bloodtypes.add("O-");
        bloodtypes.add("O+");
        bloodtypes.add("AB+");
        bloodtypes.add("AB-");
        bloodtypes.add("A-");
        bloodtypes.add("A+");
        bloodtypes.add("B-");
        bloodtypes.add("B+");


        final ArrayAdapter<String> spinnerArrayAdapterbloodtypes = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, bloodtypes);
        spinnerArrayAdapterbloodtypes.setDropDownViewResource( R.layout.spinner_item );

        spinnerbloodtypes = (Spinner)findViewById(R.id.spinnerbloodtype);
        spinnerbloodtypes.setAdapter(spinnerArrayAdapterbloodtypes);


        if(UserProfilePrivate.GetUpdateClicker().compareTo("PN")==0)
        {
            donationdatedit.setEnabled(false);
            spinnerbloodtypes.setEnabled(false);
        }
        if(UserProfilePrivate.GetUpdateClicker().compareTo("DD")==0)
        {
            phonenoedit.setEnabled(false);
            spinnerbloodtypes.setEnabled(false);

        }
        if(UserProfilePrivate.GetUpdateClicker().compareTo("BT")==0)
        {
            phonenoedit.setEnabled(false);
            donationdatedit.setEnabled(false);

        }




        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        donationdatedit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(UpdateUserInfo.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Done.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();

                if (extras != null) {

                    String Username = extras.getString("Username");
                    String Age = extras.getString("Age");
                    String Email = extras.getString("Email");
                    String NationalID = extras.getString("National_id");
                    String PhoneNumber = extras.getString("phonenumber");
                    String LastDonationDate = extras.getString("lastdonationdate");
                    String BloodType = extras.getString("Blood_type");
                    String Sex = extras.getString("Sex");


                    // Log.i(NationalID,"ND:");
                    //Log.i(PhoneNumber,"ND:");
                    //Log.i(LastDonationDate,"ND:");
                    //Log.i(BloodType,"ND:");
                    // Log.i(NationalID,"ND:");
                    // Log.i(NationalID,"ND:");


                    Log.i(phonenoedit.getText().toString(), "PN:");

                    if (!phonenoedit.isEnabled() && !donationdatedit.isEnabled()) {
                        if (PhoneNumber.equals("add phonenumber?") && !LastDonationDate.equals("add last donationdate?")) {
                            new AddNewEntry().execute("0", LastDonationDate, spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);

                            i.putExtra("phonenumber", "0");
                            i.putExtra("lastdonationdate", LastDonationDate);
                            i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        } else if (!PhoneNumber.equals("add phonenumber?") && LastDonationDate.equals("add last donationdate?")) {
                            new AddNewEntry().execute(PhoneNumber, "0001-01-01", spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);

                            i.putExtra("phonenumber", PhoneNumber);
                            i.putExtra("lastdonationdate", "0001-01-01");
                            i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        } else if (PhoneNumber.equals("add phonenumber?") && LastDonationDate.equals("add last donationdate?")) {
                            new AddNewEntry().execute("0", "0001-01-01", spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);

                            i.putExtra("phonenumber", "0");
                            i.putExtra("lastdonationdate", "0001-01-01");
                            i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        } else if (!PhoneNumber.equals("add phonenumber?") && !LastDonationDate.equals("add last donationdate?")) {
                            new AddNewEntry().execute(PhoneNumber, LastDonationDate, spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);

                            i.putExtra("phonenumber", PhoneNumber);
                            i.putExtra("lastdonationdate", LastDonationDate);
                            i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        }
                    } else if (!phonenoedit.isEnabled() && !spinnerbloodtypes.isEnabled()) {
                        if (PhoneNumber.equals("add phonenumber?")) {
                            new AddNewEntry().execute("0", donationdatedit.getText().toString(), BloodType, NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);

                            i.putExtra("phonenumber", "0");
                            i.putExtra("lastdonationdate", donationdatedit.getText().toString());
                            i.putExtra("Blood_type", BloodType);
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        } else {
                            new AddNewEntry().execute(PhoneNumber, donationdatedit.getText().toString(), BloodType, NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);

                            i.putExtra("phonenumber", PhoneNumber);
                            i.putExtra("lastdonationdate", donationdatedit.getText().toString());
                            i.putExtra("Blood_type", BloodType);
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        }

                    } else if (!donationdatedit.isEnabled() && !spinnerbloodtypes.isEnabled()) {                          // Log.i("yes","ND:");
                        // Log.i(LastDonationDate,"ND:");

                        if (LastDonationDate.equals("add last donationdate?")) {
                            //Log.i("yes","ND:");
                            new AddNewEntry().execute(phonenoedit.getText().toString(), "0001-01-01", BloodType, NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);

                            i.putExtra("phonenumber", phonenoedit.getText().toString());
                            i.putExtra("lastdonationdate", "0001-01-01");
                            i.putExtra("Blood_type", BloodType);
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        } else {
                            new AddNewEntry().execute(phonenoedit.getText().toString(), LastDonationDate, BloodType, NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);
                            i.putExtra("phonenumber", phonenoedit.getText().toString());
                            i.putExtra("lastdonationdate", LastDonationDate);
                            i.putExtra("Blood_type", BloodType);
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        }

                    } else if (donationdatedit.isEnabled() && spinnerbloodtypes.isEnabled() && phonenoedit.isEnabled()) {


                        if (donationdatedit.getText().toString().equals("")&& !phonenoedit.getText().toString().equals("") ) {
                            if (LastDonationDate.equals("add last donationdate?")) {
                                new AddNewEntry().execute(phonenoedit.getText().toString(), "0001-01-01", spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                                Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                                i.putExtra("Username", Username);
                                i.putExtra("Age", Age);
                                i.putExtra("Email", Email);
                                i.putExtra("National_id", NationalID);

                                i.putExtra("phonenumber", phonenoedit.getText().toString());
                                i.putExtra("lastdonationdate", "0001-01-01");
                                i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                                i.putExtra("Sex", Sex);

                                UpdateUserInfo.this.finish();
                                startActivity(i);
                            } else {
                                new AddNewEntry().execute(phonenoedit.getText().toString(), LastDonationDate, spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                                Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                                i.putExtra("Username", Username);
                                i.putExtra("Age", Age);
                                i.putExtra("Email", Email);
                                i.putExtra("National_id", NationalID);

                                i.putExtra("phonenumber",phonenoedit.getText().toString());
                                i.putExtra("lastdonationdate", LastDonationDate);
                                i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                                i.putExtra("Sex", Sex);

                                UpdateUserInfo.this.finish();
                                startActivity(i);
                            }
                        } else if (phonenoedit.getText().toString().equals("") && !donationdatedit.getText().toString().equals("")) {
                            if (PhoneNumber.equals("add phonenumber?")) {
                                new AddNewEntry().execute("0", donationdatedit.getText().toString(), spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                                Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                                i.putExtra("Username", Username);
                                i.putExtra("Age", Age);
                                i.putExtra("Email", Email);
                                i.putExtra("National_id", NationalID);

                                i.putExtra("phonenumber", "0");
                                i.putExtra("lastdonationdate", donationdatedit.getText().toString());
                                i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                                i.putExtra("Sex", Sex);

                                UpdateUserInfo.this.finish();
                                startActivity(i);
                            }
                            else {
                                new AddNewEntry().execute(PhoneNumber, donationdatedit.getText().toString(), spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                                Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                                i.putExtra("Username", Username);
                                i.putExtra("Age", Age);
                                i.putExtra("Email", Email);
                                i.putExtra("National_id", NationalID);

                                i.putExtra("phonenumber", PhoneNumber);
                                i.putExtra("lastdonationdate", donationdatedit.getText().toString());
                                i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                                i.putExtra("Sex", Sex);

                                UpdateUserInfo.this.finish();
                                startActivity(i);
                            }
                        }
                        else if(donationdatedit.getText().toString().equals("")&& phonenoedit.getText().toString().equals(""))
                        {
                            if (PhoneNumber.equals("add phonenumber?") && !LastDonationDate.equals("add last donationdate?")) {
                                new AddNewEntry().execute("0", LastDonationDate, spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                                Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                                i.putExtra("Username", Username);
                                i.putExtra("Age", Age);
                                i.putExtra("Email", Email);
                                i.putExtra("National_id", NationalID);

                                i.putExtra("phonenumber", "0");
                                i.putExtra("lastdonationdate", LastDonationDate);
                                i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                                i.putExtra("Sex", Sex);

                                UpdateUserInfo.this.finish();
                                startActivity(i);
                            }

                            else if (!PhoneNumber.equals("add phonenumber?") && LastDonationDate.equals("add last donationdate?")) {
                                new AddNewEntry().execute(PhoneNumber,"0001-01-01", spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                                Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                                i.putExtra("Username", Username);
                                i.putExtra("Age", Age);
                                i.putExtra("Email", Email);
                                i.putExtra("National_id", NationalID);

                                i.putExtra("phonenumber", PhoneNumber);
                                i.putExtra("lastdonationdate", "0001-01-01");
                                i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                                i.putExtra("Sex", Sex);

                                UpdateUserInfo.this.finish();
                                startActivity(i);
                            }
                            else if (PhoneNumber.equals("add phonenumber?") && LastDonationDate.equals("add last donationdate?")) {
                                new AddNewEntry().execute("0","0001-01-01", spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                                Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                                i.putExtra("Username", Username);
                                i.putExtra("Age", Age);
                                i.putExtra("Email", Email);
                                i.putExtra("National_id", NationalID);

                                i.putExtra("phonenumber", "0");
                                i.putExtra("lastdonationdate", "0001-01-01");
                                i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                                i.putExtra("Sex", Sex);

                                UpdateUserInfo.this.finish();
                                startActivity(i);
                            }
                            else if (!PhoneNumber.equals("add phonenumber?") && !LastDonationDate.equals("add last donationdate?")) {
                                new AddNewEntry().execute(PhoneNumber,LastDonationDate, spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                                Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                                i.putExtra("Username", Username);
                                i.putExtra("Age", Age);
                                i.putExtra("Email", Email);
                                i.putExtra("National_id", NationalID);

                                i.putExtra("phonenumber", PhoneNumber);
                                i.putExtra("lastdonationdate", LastDonationDate);
                                i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                                i.putExtra("Sex", Sex);

                                UpdateUserInfo.this.finish();
                                startActivity(i);
                            }
                        }
                        else{
                            new AddNewEntry().execute(phonenoedit.getText().toString(), donationdatedit.getText().toString(), spinnerbloodtypes.getSelectedItem().toString(), NationalID);
                            Intent i = new Intent(UpdateUserInfo.this, UserProfilePrivate.class);

                            i.putExtra("Username", Username);
                            i.putExtra("Age", Age);
                            i.putExtra("Email", Email);
                            i.putExtra("National_id", NationalID);

                            i.putExtra("phonenumber", phonenoedit.getText().toString());
                            i.putExtra("lastdonationdate", donationdatedit.getText().toString());
                            i.putExtra("Blood_type", spinnerbloodtypes.getSelectedItem().toString());
                            i.putExtra("Sex", Sex);

                            UpdateUserInfo.this.finish();
                            startActivity(i);
                        }

                    }
                }
            }
        });
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        donationdatedit.setText(sdf.format(myCalendar.getTime()));
    }

    private class AddNewEntry extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub


            String PN = arg[0];
            String DT = arg[1];
            String BT = arg[2];
            String NID = arg[3];



            List<NameValuePair> params = new ArrayList<NameValuePair>();



            params.add(new BasicNameValuePair("Phonenumber", PN));
            params.add(new BasicNameValuePair("lastdonationdate", DT));
            params.add(new BasicNameValuePair("Blood_type", BT));
            params.add(new BasicNameValuePair("National_id", NID));



            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_UPDATE,
                    ServiceHandler.POST, params);

            Log.d("Create  Request: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {
                        // new category created successfully
                        Log.e("Prediction added  ","> " + jsonObj.getString("message"));
                    } else {
                        Log.e(" Prediction Error: ","> " + jsonObj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "JSON data error!");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }
    public void datePicker(View view)
    {
    }

}
