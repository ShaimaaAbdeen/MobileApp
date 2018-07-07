package com.example.mrsshimaa.mobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static android.R.attr.id;

public class Login extends AppCompatActivity {
    EditText emailf ;
    EditText passwordf;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    String Email;
    String Password;
    public static final String PREFS_NAME = "preferences";
    public static final String PREF_UNAME = "Username";
    public static final String PREF_PASSWORD = "Password";

    public static final String DefaultUnameValue = "";
    public static String UnameValue;

    public static final String DefaultPasswordValue = "";
    public static String PasswordValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailf= (EditText)findViewById(R.id.emailentry);
        passwordf=(EditText)findViewById(R.id.passwordentry);




    }
    @Override
    public void onPause() {
        super.onPause();
        savePreferences();

    }


    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = emailf.getText().toString();
        PasswordValue = passwordf.getText().toString();
        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }


    public  void login (View view) {

       // getJSON("https://wellcare.000webhostapp.com/getdata2.php");
       // getJSON(" http://192.168.43.232/GarduationProjectV2/android/getdata.php");
       getJSON("http://wellcare.atwebpages.com/android/getdata.php");




    }
    private void getJSON(final String urlWebService) {

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
                    Log.i(s,"j:");

                        retrievedata(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
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

    private void retrievedata(String json) throws Exception {

        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(Login.this).create();
            alertDialog.setMessage("Cannot connect to server, please check your internet connection!");

            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
        }
        else {
            //creating a json array from the json string

            Log.i(json, "hi:");
            JSONArray jsonArray = new JSONArray(json);


            String[] emails = new String[jsonArray.length()];

            String[] passwords = new String[jsonArray.length()];

            String[] firstnames = new String[jsonArray.length()];

            String[] lastnames = new String[jsonArray.length()];

            String[] usernames = new String[jsonArray.length()];

            String[] nationalids = new String[jsonArray.length()];

            String[] ages = new String[jsonArray.length()];

            String[] bloodtypes = new String[jsonArray.length()];

            String[] donationdates = new String[jsonArray.length()];

            String[] phonenumbers = new String[jsonArray.length()];

            String[] sex = new String[jsonArray.length()];

            String[] images = new String[jsonArray.length()];

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
                sex[i] = obj.getString("Gender");
                usernames[i] = obj.getString("Username");
                nationalids[i] = obj.getString("National_id");
                images[i] = obj.getString("Profile_photo");
            }

            for (int i = 0; i < emails.length; i++) {
                Log.i(emails[i].toString(), "emails:");
            }
            Email = emailf.getText().toString();
            Password = passwordf.getText().toString();

            if (Email.compareTo("") == 0 || Password.compareTo("") == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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


                            FirstPage.sp.edit().putBoolean("logged", true).apply();


                            Intent intent = new Intent(Login.this, HomeMod.class);
                            intent.putExtra("First_name", firstnames[i]);
                            intent.putExtra("Last_name", lastnames[i]);
                            intent.putExtra("Username", usernames[i]);

                            intent.putExtra("Age", ages[i]);
                            intent.putExtra("Blood_type", bloodtypes[i]);
                            intent.putExtra("lastdonationdate", donationdates[i]);
                            intent.putExtra("phonenumber", phonenumbers[i]);
                            intent.putExtra("Email", emails[i]);
                            intent.putExtra("Sex", sex[i]);
                            intent.putExtra("National_id", nationalids[i]);
                            intent.putExtra("Profile_photo", images[i]);

                            Log.i(images[i], "img:");
                            startActivity(intent);
                            Login.this.finish();
                            break;


                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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
                    } else if (Email.compareTo(emails[i]) != 0 && i == jsonArray.length() - 1) {
                        CheckDoctor();
                    }
                /*else if (Email.compareTo(emails[i]) != 0 && i==jsonArray.length()-1) {
                    AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                    alertDialog.setMessage("Wrong Email or Password");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }*/
                }


            }
        }
    }

    public  void CheckDoctor()
    {
       //getJSONDoctors("http://192.168.43.232/GarduationProjectV2/android/getdatadoctorslist.php");
        getJSONDoctors("http://wellcare.atwebpages.com/android/getdatadoctorslist.php");

    }


    private void getJSONDoctors(final String urlWebService) {
        /*
        * As fetching the json string is a network operation
        * And we cannot perform a network operation in main thread
        * so we need an AsyncTask
        * The constrains defined here are
        * Void -> We are not passing anything
        * Void -> Nothing at progress update as well
        * String -> After completion it should return a string and it will be the json string
        * */
        class GetJSONDoctors extends AsyncTask<Void, Void, String> {

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
                    Log.i(s,"j:");

                    retrievedataDoctors(s);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
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



        GetJSONDoctors getJSON = new GetJSONDoctors();
        getJSON.execute();

    }


    private void retrievedataDoctors(String json) throws Exception {
        //creating a json array from the json string
        if (json == null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(Login.this).create();
            alertDialog.setMessage("Cannot connect to server, please check your internet connection!");

            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
        } else {
            Log.i(json, "hi:");
            JSONArray jsonArray = new JSONArray(json);


            String[] emails = new String[jsonArray.length()];

            String[] passwords = new String[jsonArray.length()];

            String[] firstnames = new String[jsonArray.length()];

            String[] lastnames = new String[jsonArray.length()];

            String[] ids = new String[jsonArray.length()];

            String[] phonenumbers = new String[jsonArray.length()];

            String[] positions = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array

                emails[i] = obj.getString("Email");
                passwords[i] = obj.getString("Password");
                firstnames[i] = obj.getString("First_name");
                lastnames[i] = obj.getString("Last_name");
                positions[i] = obj.getString("Position");
                phonenumbers[i] = obj.getString("Phonenumber");
                ids[i] = obj.getString("ID");
            }

            for (int i = 0; i < emails.length; i++) {
                Log.i(emails[i].toString(), "emails:");
            }
            Email = emailf.getText().toString();
            Password = passwordf.getText().toString();

            if (Email.compareTo("") == 0 || Password.compareTo("") == 0) {
                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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


                            FirstPage.sp.edit().putBoolean("logged", true).apply();

                            Intent intent = new Intent(Login.this, GroupTherapyListDoctor.class);
                            intent.putExtra("First_name", firstnames[i]);
                            intent.putExtra("Last_name", lastnames[i]);
                            intent.putExtra("phonenumber", phonenumbers[i]);
                            intent.putExtra("Email", emails[i]);
                            intent.putExtra("Position", positions[i]);
                            intent.putExtra("ID", ids[i]);
                            intent.putExtra("Password", passwords[i]);


                            startActivity(intent);
                            Login.this.finish();
                            break;


                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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
                    } else if (Email.compareTo(emails[i]) != 0 && i == jsonArray.length() - 1) {
                        AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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
}
