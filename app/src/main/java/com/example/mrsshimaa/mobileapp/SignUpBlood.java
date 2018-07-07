package com.example.mrsshimaa.mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUpBlood extends AppCompatActivity {
    Intent intent;
   private String URL_SIGN_UP = "http://192.168.43.232/GraduationProject/addandroidentry.php";
    private Button JOIN;

    EditText FN ;
    EditText LN;
    EditText EM;
    EditText Pass;
    EditText LDD;
    Spinner AG;
    Spinner BT;
    Spinner S;
   static boolean exists=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_blood);

        List age = new ArrayList<Integer>();
        for (int i = 18; i <= 50; i++) {
            age.add(Integer.toString(i));
        }
        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, age);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item );

        Spinner spinner = (Spinner)findViewById(R.id.spinnerage);
        spinner.setAdapter(spinnerArrayAdapter);

        List bloodtypes = new ArrayList<String>();
        bloodtypes.add("O-");
        bloodtypes.add("O+");
        bloodtypes.add("AB+");
        bloodtypes.add("AB-");
        bloodtypes.add("A-");
        bloodtypes.add("A+");
        bloodtypes.add("B-");
        bloodtypes.add("B+");


        ArrayAdapter<String> spinnerArrayAdapterbloodtypes = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, bloodtypes);
        spinnerArrayAdapterbloodtypes.setDropDownViewResource( R.layout.spinner_item );

        Spinner spinnerbloodtypes = (Spinner)findViewById(R.id.spinnerbloodtype);
        spinnerbloodtypes.setAdapter(spinnerArrayAdapterbloodtypes);

        List sexlist = new ArrayList<String>();
        sexlist.add("F");
        sexlist.add("M");


        ArrayAdapter<String> spinnerArrayAdaptersex = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, sexlist);
        spinnerArrayAdaptersex.setDropDownViewResource( R.layout.spinner_item );

        Spinner spinnersex = (Spinner)findViewById(R.id.spinnersex);
        spinnersex.setAdapter(spinnerArrayAdaptersex);

        JOIN = (Button) findViewById(R.id.joinbtn);
        FN = (EditText) findViewById(R.id.firstnamentry);
        LN = (EditText) findViewById(R.id.lastnameentry);
        EM = (EditText) findViewById(R.id.emailentry);
        Pass = (EditText) findViewById(R.id.passwordentry);
        LDD = (EditText) findViewById(R.id.date);
        AG = (Spinner) findViewById(R.id.spinnerage);
        BT = (Spinner) findViewById(R.id.spinnerbloodtype);
        S =(Spinner) findViewById(R.id.spinnersex);


        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "YYYYMMDD";


            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise

                        int year = Integer.parseInt(clean.substring(0,4));
                        int mon  = Integer.parseInt(clean.substring(4,6));
                        int day  = Integer.parseInt(clean.substring(6,8));
                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",year,mon,day );
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 4),
                            clean.substring(4, 6),
                            clean.substring(6, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    LDD.setText(current);
                    LDD.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };


        LDD.addTextChangedListener(tw);

        JOIN.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                getJSON("http://192.168.43.232/GraduationProject/getdata.php");


               if(exists){
                    Log.i("hghjgjhghkgjh","araaaaaaaaaaaaaaaaaaaaaaaaaaaaaaf");
                    AlertDialog alertDialog = new AlertDialog.Builder(SignUpBlood.this).create();
                    alertDialog.setMessage("Email Already Exists");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();


                }
                 else if(FN.getText().toString().compareTo("")==0 ||
                        LN.getText().toString().compareTo("")==0||
                        EM.getText().toString().compareTo("")==0 ||
                        Pass.getText().toString().compareTo("")==0 )
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(SignUpBlood.this).create();
                    alertDialog.setMessage("Please Enter All Required data");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else if   (!android.util.Patterns.EMAIL_ADDRESS.matcher(EM.getText().toString()).matches()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(SignUpBlood.this).create();
                    alertDialog.setMessage("Please Enter A Valid Email");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else {

                       if(LDD.getText().toString().compareTo("")==0){
                            new AddNewEntry().execute(FN.getText().toString(), LN.getText().toString(), EM.getText().toString()
                                    , Pass.getText().toString(),"0001-01-01", AG.getSelectedItem().toString(), BT.getSelectedItem().toString(),S.getSelectedItem().toString());

                            Intent intent = new Intent(SignUpBlood.this, UserProfilePageBlood.class);
                            intent.putExtra("First_name", FN.getText().toString());
                            intent.putExtra("Last_name", LN.getText().toString());
                            intent.putExtra("Age", AG.getSelectedItem().toString());
                            intent.putExtra("Blood_type", BT.getSelectedItem().toString());
                            intent.putExtra("lastdonationdate","null");
                            intent.putExtra("Email", EM.getText().toString());
                            intent.putExtra("Sex", S.getSelectedItem().toString());


                           startActivity(intent);
                            SignUpBlood.this.finish();
                        }
                        else {

                        new AddNewEntry().execute(FN.getText().toString(), LN.getText().toString(), EM.getText().toString()
                                , Pass.getText().toString(), LDD.getText().toString(), AG.getSelectedItem().toString(), BT.getSelectedItem().toString(),S.getSelectedItem().toString());

                        Intent intent = new Intent(SignUpBlood.this, UserProfilePageBlood.class);
                        intent.putExtra("First_name", FN.getText().toString());
                        intent.putExtra("Last_name", LN.getText().toString());
                        intent.putExtra("Age", AG.getSelectedItem().toString());
                        intent.putExtra("Blood_type", BT.getSelectedItem().toString());
                        intent.putExtra("lastdonationdate", LDD.getText().toString());
                        intent.putExtra("Email", EM.getText().toString());
                        intent.putExtra("Sex", S.getSelectedItem().toString());

                        startActivity(intent);
                        SignUpBlood.this.finish();
                    }
               }

            }
        });

    }



    private class AddNewEntry extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub
            String FN = arg[0];
            String LN = arg[1];
            String EM = arg[2];
            String Pass = arg[3];
            String LDD = arg[4];
            String AG = arg[5];
            String BT = arg[6];
            String S = arg[7];
            List<NameValuePair> params = new ArrayList<NameValuePair>();



                params.add(new BasicNameValuePair("First_name", FN));
                params.add(new BasicNameValuePair("Last_name", LN));
                params.add(new BasicNameValuePair("Email", EM));
                params.add(new BasicNameValuePair("Password", Pass));
                params.add(new BasicNameValuePair("lastdonationdate", LDD));
                params.add(new BasicNameValuePair("Age", AG));
                params.add(new BasicNameValuePair("Blood_type", BT));
                params.add(new BasicNameValuePair("sex", S));


            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_SIGN_UP,
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

                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();


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

    public void retrievedata(String json) throws JSONException {
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        //creating a string array for listview
        String[] emails = new String[jsonArray.length()];

        String Email;

        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++) {

            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i);

            //getting the name from the json object and putting it inside string array


            emails[i] = obj.getString("Email");

        }

        Email = EM.getText().toString();


        for (int i = 0; i < jsonArray.length(); i++) {
            if (Email.compareTo(emails[i]) == 0) {




                exists=true;


                break;


            }
            else{
                //Log.i("rett","F");

                exists=false;
            }



        }


    }
}
