package com.example.mrsshimaa.mobileapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

public class SignUp extends AppCompatActivity {
    Intent intent;
    //private String URL_SIGN_UP = "https://wellcare.000webhostapp.com/addandroidentry1.php";
  //  private String URL_SIGN_UP = "http://192.168.43.232/GarduationProjectV2/android/addandroidentry.php";
    private String URL_SIGN_UP="http://wellcare.atwebpages.com/android/addandroidentry.php";
    private Button JOIN;


    EditText FN ;
    EditText LN;
    EditText EM;
    EditText Pass;
    EditText NID;
    EditText UN;
    Spinner AG;
    Spinner BT;
    Spinner S;
    static boolean exists=false;
    boolean usernameexist=false;
    boolean nationalidexist=false;
    String firstname;
    String lastname;
    String username;
    String sex;
    String age;
    String email;
    String nationalid;
    String bloodtype;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        JOIN = (Button) findViewById(R.id.joinbtn);
        FN = (EditText) findViewById(R.id.firstnamentry);
        LN = (EditText) findViewById(R.id.lastnameentry);
        UN = (EditText) findViewById(R.id.usernameentry);
        EM = (EditText) findViewById(R.id.emailentry);
        Pass = (EditText) findViewById(R.id.passwordentry);
        NID = (EditText) findViewById(R.id.nationalidentry);
        AG = (Spinner) findViewById(R.id.spinnerage);
        BT = (Spinner) findViewById(R.id.spinnerbloodtype);
        S =(Spinner) findViewById(R.id.spinnersex);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            firstname = extras.getString("First_name");
            lastname = extras.getString("Last_name");
            username = extras.getString("User_name");
            email = extras.getString("Email");
            password = extras.getString("Password");
            nationalid = extras.getString("National_id");
            age = extras.getString("Age");
            bloodtype = extras.getString("Blood_type");
            sex = extras.getString("Sex");


            FN.setText(firstname);
            LN.setText(lastname);
            UN.setText(username);
            EM.setText(email);
            Pass.setText(password);
            NID.setText(nationalid);
           // AG.setSelection(Integer.parseInt(age));
           // BT.setSelection(Integer.parseInt(bloodtype));
           // S.setSelection(Integer.parseInt(sex));

        }



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



        JOIN.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
             //  getJSON("https://wellcare.000webhostapp.com/getdata2.php");
                //getJSON(" http://192.168.43.232/GarduationProjectV2/android/getdata.php");
              getJSON("http://wellcare.atwebpages.com/android/getdata.php");


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
            String UN = arg[2];
            String NID = arg[3];
            String AG = arg[4];
            String SX = arg[5];
            String EM = arg[6];
            String PASS = arg[7];
            String BT = arg[8];
            String PN = arg[9];
            String LDD = arg[10];



            List<NameValuePair> params = new ArrayList<NameValuePair>();



            params.add(new BasicNameValuePair("First_name", FN));
            params.add(new BasicNameValuePair("Last_name", LN));
            params.add(new BasicNameValuePair("Username", UN));
            params.add(new BasicNameValuePair("National_id", NID));
            params.add(new BasicNameValuePair("Age", AG));
            params.add(new BasicNameValuePair("Gender", SX));
            params.add(new BasicNameValuePair("Email", EM));
            params.add(new BasicNameValuePair("Password", PASS));
            params.add(new BasicNameValuePair("Blood_type", BT));
            params.add(new BasicNameValuePair("Phonenumber", PN));
            params.add(new BasicNameValuePair("lastdonationdate", LDD));

            for(int i=0;i<params.size();i++)
            {
                Log.i(params.get(i).getValue().toString(),"params:");
            }

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

        if (json == null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(SignUp.this).create();
            alertDialog.setMessage("Cannot connect to server, please check your internet connection!");

            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
        } else {
            JSONArray jsonArray = new JSONArray(json);

            String[] ems = new String[jsonArray.length()];
            String[] usernames = new String[jsonArray.length()];
            String[] nationalids = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);


                ems[i] = obj.getString("Email");
                usernames[i] = obj.getString("Username");
                nationalids[i] = obj.getString("National_id");


            }


            String Email = EM.getText().toString();
            String Username = UN.getText().toString();
            String NationalID = NID.getText().toString();


            for (int i = 0; i < ems.length; i++) {
                if (Email.equals(ems[i])) {


                    exists = true;


                    break;


                } else {
                    //Log.i("rett","F");

                    exists = false;
                }


            }

            for (int i = 0; i < usernames.length; i++) {
                if (Username.equals(usernames[i])) {


                    usernameexist = true;


                    break;


                } else {
                    //Log.i("rett","F");

                    usernameexist = false;
                }


            }

            for (int i = 0; i < nationalids.length; i++) {
                if (NationalID.equals(nationalids[i])) {


                    nationalidexist = true;


                    break;


                } else {

                    nationalidexist = false;
                }


            }
            Log.i(Boolean.toString(exists), "ex:");


            if (exists) {
                AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setMessage("Email Already Exists");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                if (!(SignUp.this.isFinishing())) {
                    alertDialog.show();
                }

            } else if (usernameexist) {
                AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setMessage("Username Already Exists");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                if (!(SignUp.this.isFinishing())) {
                    alertDialog.show();
                }
            } else if (nationalidexist) {
                AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setMessage("National ID Already Exists");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                if (!(SignUp.this.isFinishing())) {
                    alertDialog.show();
                }
            } else if (FN.getText().toString().compareTo("") == 0 ||
                    LN.getText().toString().compareTo("") == 0 ||
                    UN.getText().toString().compareTo("") == 0 ||
                    EM.getText().toString().compareTo("") == 0 ||
                    NID.getText().toString().compareTo("") == 0 ||
                    Pass.getText().toString().compareTo("") == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setMessage("Please Enter All Required data");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(EM.getText().toString()).matches()) {
                AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setMessage("Please Enter A Valid Email");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else if (NID.getText().toString().length() != 14) {
                AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setMessage("Enter a valid National ID");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else if(Pass.getText().toString().length()<7)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(SignUp.this).create();
                alertDialog.setMessage("Password is very weak");

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            else {

                new AddNewEntry().execute(FN.getText().toString(), LN.getText().toString(), UN.getText().toString(), NID.getText().toString(), AG.getSelectedItem().toString(), S.getSelectedItem().toString(), EM.getText().toString()
                        , Pass.getText().toString(), BT.getSelectedItem().toString(), "0", "0001-01-01");

                Intent intent = new Intent(SignUp.this, HomeMod.class);
                intent.putExtra("First_name", FN.getText().toString());
                intent.putExtra("Last_name", LN.getText().toString());
                intent.putExtra("Username", UN.getText().toString());
                intent.putExtra("National_id", NID.getText().toString());
                intent.putExtra("Email", EM.getText().toString());
                intent.putExtra("Age", AG.getSelectedItem().toString());
                intent.putExtra("Blood_type", BT.getSelectedItem().toString());
                intent.putExtra("Sex", S.getSelectedItem().toString());
                intent.putExtra("lastdonationdate", "0001-01-01");
                intent.putExtra("phonenumber", "0");

                startActivity(intent);
                SignUp.this.finish();

            }


        }
    }

}

