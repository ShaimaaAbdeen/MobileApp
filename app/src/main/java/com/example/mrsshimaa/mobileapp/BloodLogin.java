package com.example.mrsshimaa.mobileapp;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mrsshimaa.mobileapp.app.Config;
import com.example.mrsshimaa.mobileapp.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BloodLogin extends AppCompatActivity {
    EditText emailf ;
    EditText passwordf;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
     String Email;
     String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_login);

        emailf= (EditText)findViewById(R.id.emailentry);
        passwordf=(EditText)findViewById(R.id.passwordentry);
    }

    // Fetches reg id from shared preferences
    // and displays on the screen



    public  void login (View view){
        //Get text from email and passord field

        getJSON("http://192.168.43.232/GraduationProject/getdata.php");

        // Initialize  AsyncLogin() class with email and password
      //  new AsyncLogin().execute(Email,Password);



    }
    private void getJSON(final String urlWebService) {
        /*
        * As fetching the json string is a network operation
        * And we cannot perform a network operation in main thread
        * so we need an AsyncTask
        * The constrains defined here are
        * Void -> We are not passing anything
        * Void -> Nothing at progress update as well
        * String -> After completion it should return a string and it will be the json string
        * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {

                super.onPostExecute(s);

               // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();


                try {
                    retrievedata(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {



                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.i(json,"hi:");

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();

    }
    private void retrievedata(String json) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        //creating a string array for listview
        String[] emails = new String[jsonArray.length()];

        String[] passwords = new String[jsonArray.length()];

        String[] firstnames = new String[jsonArray.length()];

        String[] lastnames = new String[jsonArray.length()];

        String[] ages = new String[jsonArray.length()];

        String[] bloodtypes = new String[jsonArray.length()];

        String[] donationdates = new String[jsonArray.length()];

        String[] phonenumbers = new String[jsonArray.length()];

        String[] sex = new String[jsonArray.length()];

        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++) {

            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i);

            //getting the name from the json object and putting it inside string array


            emails[i] = obj.getString("Email");
            passwords[i] = obj.getString("Password");
            firstnames[i] = obj.getString("First_name");
            lastnames[i] = obj.getString("Last_name");
            ages[i] = obj.getString("Age");
            bloodtypes[i] = obj.getString("Blood_type");
            donationdates[i] = obj.getString("lastdonationdate");
            phonenumbers[i] = obj.getString("Phonenumber");
            sex[i] = obj.getString("sex");
        }

        Email = emailf.getText().toString();
        Password = passwordf.getText().toString();

        if (Email.compareTo("") == 0 || Password.compareTo("") == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(BloodLogin.this).create();
            alertDialog.setMessage("Please Enter All Required data");

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        } else {
            for (int i = 0; i < jsonArray.length(); i++) {
                if (Email.compareTo(emails[i]) == 0) {

                    if (Password.compareTo(passwords[i]) == 0) {

                        Intent intent = new Intent(BloodLogin.this, UserProfilePageBlood.class);
                        intent.putExtra("First_name", firstnames[i]);
                        intent.putExtra("Last_name", lastnames[i]);
                        intent.putExtra("Age", ages[i]);
                        intent.putExtra("Blood_type", bloodtypes[i]);
                        intent.putExtra("lastdonationdate", donationdates[i]);
                        intent.putExtra("Phonenumber", phonenumbers[i]);
                        intent.putExtra("Email", emails[i]);
                        intent.putExtra("Sex", sex[i]);

                        startActivity(intent);
                        BloodLogin.this.finish();
                        break;




                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(BloodLogin.this).create();
                        alertDialog.setMessage("Wrong Email or Password");

                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        break;

                    }
                } else if (Email.compareTo(emails[i]) != 0 && i==jsonArray.length()-1) {
                    AlertDialog alertDialog = new AlertDialog.Builder(BloodLogin.this).create();
                    alertDialog.setMessage("Wrong Email or Password");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            }


        }
    }

}




