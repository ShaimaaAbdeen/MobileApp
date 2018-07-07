package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeMod extends AppCompatActivity {

    String name;
    String sex;
    String age;
    String email;
    String nationalid;
    String bloodtype;
    String phonenumber;
    String lastdonationdate;
    ImageView profileicon;
    private PopupWindow pw;
    Intent intent;
    ArrayList<HospitalBloodDetails> hospitalitems;
    ArrayList<HospitalBloodDetails> selectedhospitals;
    public static ArrayList<NotificationDetails> notificationitems;
    public static ArrayList<MessageDetails> messageitems;
    public  static  NotificationsAdapter NA;
    MessagesAdapter MA;
    int notcount=0;
    TextView notnumber;
    ArrayList <String> BloodNotification;
    String hospitalname="";
    String hospitalblood="";
    ListView notificationslist;
    ArrayList hospitalnamesnot;
    ArrayList hospitalbloodnot;
    int countmessages=0;
    TextView notmessages;
    Intent i;
    String URL_UPDATE_SEEN="http://wellcare.atwebpages.com/android/updateseen.php";
    String Email;
    String Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_mod);

        hospitalitems= new ArrayList<>();
        notnumber = (TextView) findViewById(R.id.notnumber);
        notmessages = (TextView) findViewById(R.id.messagenumber);

        profileicon=(ImageView) findViewById(R.id.profileicon);

        BloodNotification = new ArrayList<>();
        selectedhospitals = new ArrayList<>();
        notificationitems = new ArrayList<>();
        hospitalnamesnot = new ArrayList();
        hospitalbloodnot = new ArrayList();
        messageitems = new ArrayList<>();




        if(FirstPage.sp.getBoolean("logged",true))
        {
            loadPreferences();
            getJSONUsers("http://wellcare.atwebpages.com/android/getdata.php");
        }

        else {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                name = extras.getString("Username");
                sex = extras.getString("Sex");


                if (sex.compareTo("F") == 0||sex.compareTo("female") == 0 ) {
                    profileicon.setImageResource(R.drawable.userfemaleicon);
                }
                age = extras.getString("Age");
                email = extras.getString("Email");
                nationalid = extras.getString("National_id");
                bloodtype = extras.getString("Blood_type");
                phonenumber = extras.getString("phonenumber");
                lastdonationdate = extras.getString("lastdonationdate");


            }
             getJSON("http://wellcare.atwebpages.com/android/gethospitaldatabloodandroid.php");

             getJSONAllHospitals("http://wellcare.atwebpages.com/android/gethospitaldatabloodwithoutneeded.php");

        }







    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(Login.PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        Login.UnameValue = settings.getString(Login.PREF_UNAME, Login.DefaultUnameValue);
        Login.PasswordValue = settings.getString(Login.PREF_PASSWORD, Login.DefaultPasswordValue);
        Email=Login.UnameValue;
        Password=Login.PasswordValue;
        System.out.println("onResume load name: " + Login.UnameValue);
        System.out.println("onResume load password: " + Login.PasswordValue);
    }

    private void getJSONUsers(final String urlWebService) {

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

                    retrievedataUsers(s);

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

    private void retrievedataUsers(String json) throws Exception {
        //creating a json array from the json string

        Log.i(json,"hi:");
        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(HomeMod.this).create();
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

            String[] sexs = new String[jsonArray.length()];

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
                sexs[i] = obj.getString("Gender");
                usernames[i] = obj.getString("Username");
                nationalids[i] = obj.getString("National_id");
                images[i] = obj.getString("Profile_photo");
            }

            for (int i = 0; i < emails.length; i++) {
                Log.i(emails[i].toString(), "emails:");
            }


            for (int i = 0; i < jsonArray.length(); i++) {
                if (Email.compareTo(emails[i]) == 0) {

                    if (Password.compareTo(passwords[i]) == 0) {


                        name = usernames[i];
                        sex = sexs[i];
                        age = ages[i];
                        email = emails[i];
                        nationalid = nationalids[i];
                        bloodtype = bloodtypes[i];
                        phonenumber = phonenumbers[i];
                        lastdonationdate = donationdates[i];
                        Log.i(images[i], "img:");

                        getJSON("http://wellcare.atwebpages.com/android/gethospitaldatabloodandroid.php");

                        getJSONAllHospitals("http://wellcare.atwebpages.com/android/gethospitaldatabloodwithoutneeded.php");

                        break;


                    }
                } else if (Email.compareTo(emails[i]) != 0 && i == jsonArray.length() - 1) {

                    Intent intent = new Intent(this, GroupTherapyListDoctor.class);
                    startActivity(intent);
                    HomeMod.this.finish();
                }

            }

        }

    }




    public  void Gotoprofile(View view)
    {

        Intent intent = new Intent(HomeMod.this, UserProfilePrivate.class);
        intent.putExtra("Username",name);
        intent.putExtra("National_id",nationalid);
        intent.putExtra("Email",email);
        intent.putExtra("Age", age);
        intent.putExtra("Blood_type", bloodtype);
        intent.putExtra("Sex", sex);
        intent.putExtra("lastdonationdate",lastdonationdate);
        intent.putExtra("phonenumber",phonenumber);

        HomeMod.this.finish();
        startActivity(intent);
    }
    public void Gotobloodpage(View view){
        Intent intent = new Intent(HomeMod.this, HospitalListBlood.class);
        intent.putExtra("Username",name);
        intent.putExtra("National_id",nationalid);
        intent.putExtra("Email",email);
        intent.putExtra("Age", age);
        intent.putExtra("Blood_type", bloodtype);
        intent.putExtra("Sex", sex);
        intent.putExtra("lastdonationdate",lastdonationdate);
        intent.putExtra("phonenumber",phonenumber);
        intent.putExtra("notnumber",Integer.toString(notcount));
        intent.putExtra("messagenumber",Integer.toString(countmessages));
        HomeMod.this.finish();


        startActivity(intent);
    }
    public void checkHospitals(View view){
        intent = new Intent(HomeMod.this, HospitalListIntensive.class);
        intent.putExtra("Username",name);
        intent.putExtra("National_id",nationalid);
        intent.putExtra("Email",email);
        intent.putExtra("Age", age);
        intent.putExtra("Blood_type", bloodtype);
        intent.putExtra("Sex", sex);
        intent.putExtra("lastdonationdate",lastdonationdate);
        intent.putExtra("phonenumber",phonenumber);
        intent.putExtra("notnumber",Integer.toString(notcount));
        intent.putExtra("messagenumber",Integer.toString(countmessages));

        HomeMod.this.finish();

        startActivity(intent);
    }
    public void Gotogrouppage(View view){
        intent = new Intent(HomeMod.this, GroupTherapyListUser.class);
        intent.putExtra("Username",name);
        intent.putExtra("National_id",nationalid);
        intent.putExtra("Email",email);
        intent.putExtra("Age", age);
        intent.putExtra("Blood_type", bloodtype);
        intent.putExtra("Sex", sex);
        intent.putExtra("lastdonationdate",lastdonationdate);
        intent.putExtra("phonenumber",phonenumber);
        intent.putExtra("notnumber",Integer.toString(notcount));
        intent.putExtra("messagenumber",Integer.toString(countmessages));
        HomeMod.this.finish();
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
            LayoutInflater inflater = (LayoutInflater) HomeMod.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout,600, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER,0, 0);
            pw.setBackgroundDrawable(new BitmapDrawable());


            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void initiateIntensivePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) HomeMod.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popupintensive, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout,600, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER,0, 0);
            pw.setBackgroundDrawable(new BitmapDrawable());


            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button_intensive);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void initiateGroupPopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) HomeMod.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popupgroup, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout,600, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER,0, 0);


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homeactions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent intent = new Intent(HomeMod.this, FirstPage.class);
                startActivity(intent);
                FirstPage.sp.edit().putBoolean("logged",false).apply();

                HomeMod.this.finish();


                return true;

            default:
                return super.onOptionsItemSelected(item);
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

        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(HomeMod.this).create();
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
            JSONArray jsonArray = new JSONArray(json);

            //creating a string array for listview
            String[] HospitalNames = new String[jsonArray.length()];

            String[] HospitalAddresses = new String[jsonArray.length()];

            String[] HospitalEmails = new String[jsonArray.length()];

            String[] HospitalContactNumbers = new String[jsonArray.length()];

            String[] AvailableBloodTypes = new String[jsonArray.length()];

            String[] NeededBloodTypes = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array


                HospitalNames[i] = obj.getString("Hospital_name");
                HospitalAddresses[i] = obj.getString("Address");
                HospitalEmails[i] = obj.getString("Email");
                HospitalContactNumbers[i] = obj.getString("Contact_number");

                AvailableBloodTypes[i] = obj.getString("Availableblood");
                NeededBloodTypes[i] = obj.getString("Neededbloodtypes");

            }

            for (int i = 0; i < jsonArray.length(); i++) {

                HospitalBloodDetails hd = new HospitalBloodDetails();

                hd.setName(HospitalNames[i].toString());
                hd.setAddress(HospitalAddresses[i].toString());
                hd.setEmail(HospitalEmails[i].toString());
                hd.setContactNumber(HospitalContactNumbers[i].toString());
                hd.setNeededBloodTypes(NeededBloodTypes[i].toString());
                hd.setAvailableBloodTypes(AvailableBloodTypes[i].toString());


                hospitalitems.add(hd);


            }
            //  hospitalname=hospitalitems.get(0).getName();
            // hospitalblood=hospitalitems.get(0).getNeededBloodTypes();


            Date date = new Date();
            String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);


            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
            String inputString2 = lastdonationdate;


            // Log.i (modifiedDate,"Days:" );
            // Log.i (inputString2,"Days:" );

            long diff = 0;
            try {
                Date date1 = myFormat.parse(modifiedDate);
                Date date2 = myFormat.parse(inputString2);

                Log.i(date1.toString(), "Days:");
                Log.i(date2.toString(), "Days:");


                diff = date1.getTime() - date2.getTime();

                Log.i(Long.toString(date1.getTime()), "Days:");
                Log.i(Long.toString(date2.getTime()), "Days:");
                System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                Log.i(Long.toString(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)), "Days:");

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (lastdonationdate.compareTo("null") != 0 && lastdonationdate.compareTo("0001-01-01") != 0) {
                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 120) {
                    for (int i = 0; i < hospitalitems.size(); i++) {
                        if (bloodtype.compareTo("AB+") == 0) {

                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB+") == 0) {

                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                                //BloodNotification.add("AB+");
                                //notcount+=1;


                            }
                        } else if (bloodtype.compareTo("AB-") == 0) {

                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB+") == 0) {

                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                                //BloodNotification.add("AB-");

                                //notcount+=1;

                            }

                        } else if (bloodtype.compareTo("A+") == 0) {
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB+") == 0) {

                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                                //BloodNotification.add("AB+");

                                //notcount+=1;

                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB-") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                                //BloodNotification.add("AB-");

                                // notcount+=1;

                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("A+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }


                        } else if (bloodtype.compareTo("A-") == 0) {
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                                // BloodNotification.add("AB+");

                                // notcount+=1;

                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB-") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                                //BloodNotification.add("AB-");

                                // notcount+=1;

                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("A+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                                // BloodNotification.add("A+");

                                //notcount+=1;

                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("A-") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                                // BloodNotification.add("A-");
                                //  notcount+=1;

                            }


                        } else if (bloodtype.compareTo("B+") == 0) {


                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("B+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                        } else if (bloodtype.compareTo("B-") == 0) {

                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB-") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("B+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("B-") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }

                        } else if (bloodtype.compareTo("O+") == 0) {

                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("A+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("B+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("O+") == 0) {
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }

                        } else if (bloodtype.compareTo("O-") == 0) {

                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB+") == 0) {
                    /*if(!(hospitalitems.get(i).getName().compareTo(hospitalname)==0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalblood)==0))
                    {


                        notcount+=1;
                        hospitalname=hospitalitems.get(i).getName();
                        hospitalblood=hospitalitems.get(i).getNeededBloodTypes();
                        selectedhospitals.add(hospitalitems.get(i));

                        Log.i(hospitalname,"hn:");
                        Log.i(hospitalblood,"hn:");
                    }*/
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("AB-") == 0) {
                  /*  if(!(hospitalitems.get(i).getName().compareTo(hospitalname)==0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalblood)==0))
                    {
                        notcount+=1;
                        hospitalname=hospitalitems.get(i).getName();
                        hospitalblood=hospitalitems.get(i).getNeededBloodTypes();
                        selectedhospitals.add(hospitalitems.get(i));

                        Log.i(hospitalname,"hn:");
                        Log.i(hospitalblood,"hn:");
                    }*/

                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("B+") == 0) {
                   /* if(!(hospitalitems.get(i).getName().compareTo(hospitalname)==0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalblood)==0))
                    {
                        notcount+=1;
                        hospitalname=hospitalitems.get(i).getName();
                        hospitalblood=hospitalitems.get(i).getNeededBloodTypes();
                        selectedhospitals.add(hospitalitems.get(i));

                        Log.i(hospitalname,"hn:");
                        Log.i(hospitalblood,"hn:");
                    }
*/
                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("B-") == 0) {


                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("A+") == 0) {
                    /*if(!(hospitalitems.get(i).getName().compareTo(hospitalname)==0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalblood)==0))
                    {
                        notcount+=1;
                        hospitalname=hospitalitems.get(i).getName();
                        hospitalblood=hospitalitems.get(i).getNeededBloodTypes();
                        selectedhospitals.add(hospitalitems.get(i));

                        Log.i(hospitalname,"hn:");
                        Log.i(hospitalblood,"hn:");
                    }*/

                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("A-") == 0) {
                    /*if(!(hospitalitems.get(i).getName().compareTo(hospitalname)==0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalblood)==0))
                    {
                        notcount+=1;
                        hospitalname=hospitalitems.get(i).getName();
                        hospitalblood=hospitalitems.get(i).getNeededBloodTypes();
                        selectedhospitals.add(hospitalitems.get(i));

                        Log.i(hospitalname,"hn:");
                        Log.i(hospitalblood,"hn:");
                    }*/

                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("O+") == 0) {
                   /* if(!(hospitalitems.get(i).getName().compareTo(hospitalname)==0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalblood)==0))
                    {
                        notcount+=1;
                        hospitalname=hospitalitems.get(i).getName();
                        hospitalblood=hospitalitems.get(i).getNeededBloodTypes();
                        selectedhospitals.add(hospitalitems.get(i));

                        Log.i(hospitalname,"hn:");
                        Log.i(hospitalblood,"hn:");
                    }*/

                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }
                            if (hospitalitems.get(i).getNeededBloodTypes().compareTo("O-") == 0) {
                    /*if(!(hospitalitems.get(i).getName().compareTo(hospitalname)==0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalblood)==0))
                    {
                        notcount+=1;
                        hospitalname=hospitalitems.get(i).getName();
                        hospitalblood=hospitalitems.get(i).getNeededBloodTypes();
                        selectedhospitals.add(hospitalitems.get(i));

                        Log.i(hospitalname,"hn:");
                        Log.i(hospitalblood,"hn:");
                    }*/

                                if (hospitalbloodnot.size() == 0) {
                                    notcount++;
                                    hospitalnamesnot.add(hospitalitems.get(i).getName());
                                    hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                    selectedhospitals.add(hospitalitems.get(i));
                                } else {
                                    for (int j = 0; j < hospitalbloodnot.size(); j++) {


                                        if (!(hospitalitems.get(i).getName().compareTo(hospitalnamesnot.get(j).toString()) == 0 && hospitalitems.get(i).getNeededBloodTypes().compareTo(hospitalbloodnot.get(j).toString()) == 0)) {


                                            if (j == hospitalbloodnot.size() - 1) {
                                                notcount++;
                                                hospitalnamesnot.add(hospitalitems.get(i).getName());
                                                hospitalbloodnot.add(hospitalitems.get(i).getNeededBloodTypes());
                                                selectedhospitals.add(hospitalitems.get(i));
                                            }


                                        } else {
                                            break;
                                        }


                                    }
                                }


                            }

                        }
                    }
                }
            }


       /* HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(BloodNotification);
        BloodNotification.clear();
        BloodNotification.addAll(hashSet);

*/


            notnumber.setText(Integer.toString(notcount));

            if (notnumber.getText().toString().compareTo("0") == 0) {
                notnumber.setVisibility(View.INVISIBLE);
            } else {
                notnumber.setVisibility(View.VISIBLE);


            }

            for (int i = 0; i < selectedhospitals.size(); i++) {
                NotificationDetails ND = new NotificationDetails();

                ND.setHospitalname(selectedhospitals.get(i).getName());
                ND.setBloodType(selectedhospitals.get(i).getNeededBloodTypes());
                ND.setAddress(selectedhospitals.get(i).getAddress());
                ND.setContactNumber(selectedhospitals.get(i).getContactNumber());
                ND.setEmail(selectedhospitals.get(i).getEmail());


                notificationitems.add(ND);
            }

        }


    }


    public  void showNotifications(View view)
    {
        LayoutInflater inflater = (LayoutInflater) HomeMod.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notificationspopup, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        notificationslist= (ListView)layout.findViewById(R.id.notificationslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        NA = new HomeMod.NotificationsAdapter(HomeMod.this, R.layout.notificationitem, notificationitems);
        notificationslist.setAdapter(NA);

        NA.notifyDataSetChanged();



    }

    public class NotificationsAdapter extends ArrayAdapter<NotificationDetails> {
        Activity context;
        int layoutresource;

        ArrayList<NotificationDetails> mData = new ArrayList<NotificationDetails>();

        public NotificationsAdapter(Activity activity, int resource, ArrayList<NotificationDetails> data) {
            super(activity, resource, data);
            context = activity;
            layoutresource = resource;
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Nullable
        @Override
        public NotificationDetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable NotificationDetails item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            HomeMod.NotificationsAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new HomeMod.NotificationsAdapter.ViewHolder();
                holder.mhospitalname = (TextView) row.findViewById(R.id.hospitalname);
                holder.mbloodtype = (TextView) row.findViewById(R.id.bloodtype);

                row.setTag(holder);
            } else {
                holder = (HomeMod.NotificationsAdapter.ViewHolder) row.getTag();
            }
            holder.ND = getItem(position);
            holder.mhospitalname.setText(holder.ND.getHospitalname()+ " Urgently Needs Your Help");
            holder.mbloodtype.setText(holder.ND.getBloodType()+" Needed, Your blood type is " + bloodtype+" so you can donate");


            final HomeMod.NotificationsAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            holder.mhospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(HomeMod.this,HospitalNotificationProfile.class);

                    intent.putExtra("name",finalHolder.ND.getHospitalname());
                    intent.putExtra("address",finalHolder.ND.getAddress());
                    intent.putExtra("contactnumber",finalHolder.ND.getContactNumber());
                    intent.putExtra("email",finalHolder.ND.getEmail());


                    HomeMod.this.finish();
                    startActivity(intent);


                }
            });

            return row;
        }


        class ViewHolder {
            NotificationDetails ND;
            TextView mhospitalname;
            TextView mbloodtype;


        }
    }


    public  void showMessages(View view)
    {
        LayoutInflater inflater = (LayoutInflater) HomeMod.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notificationspopup, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        notificationslist= (ListView)layout.findViewById(R.id.notificationslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        MA = new HomeMod.MessagesAdapter(HomeMod.this, R.layout.messagefileitem, messageitems);
        notificationslist.setAdapter(MA);

        MA.notifyDataSetChanged();



    }

    public  class MessagesAdapter extends ArrayAdapter<MessageDetails> {
        Activity context;
        int layoutresource;

        ArrayList<MessageDetails> mData = new ArrayList<MessageDetails>();

        public MessagesAdapter(Activity activity, int resource, ArrayList<MessageDetails> data) {
            super(activity, resource, data);
            context = activity;
            layoutresource = resource;
            mData = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Nullable
        @Override
        public MessageDetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable MessageDetails item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }


        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            HomeMod.MessagesAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new HomeMod.MessagesAdapter.ViewHolder();
                holder.mhospitalname = (TextView) row.findViewById(R.id.hospitalname);

                row.setTag(holder);
            } else {
                holder = (HomeMod.MessagesAdapter.ViewHolder) row.getTag();
            }
            holder.MD = getItem(position);
            holder.mhospitalname.setText(holder.MD.getHospitalname()+ " Sent You a File");


            final HomeMod.MessagesAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            final ViewHolder finalHolder2 = holder;
            holder.mhospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {





                    i = new Intent(HomeMod.this,FileContent.class);
                    i.putExtra("File", finalHolder2.MD.getFile());
                    new AddNewEntry().execute("1",finalHolder2.MD.getID());
                    HomeMod.this.finish();
                    startActivity(i);



                }
            });

            return row;
        }


        class ViewHolder {
            MessageDetails MD;
            TextView mhospitalname;


        }
    }


    private void getJSONMessages(final String urlWebService, final String[] HNs, final String[]HIDs) {
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
                    retrievedataMessages(s,HNs,HIDs);
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
    private void retrievedataMessages(String json,String[] HNs,String[]HIDs) throws JSONException {
        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(HomeMod.this).create();
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
            JSONArray jsonArray = new JSONArray(json);

            //creating a string array for listview
            String[] HospitalIDs = new String[jsonArray.length()];

            String[] Files = new String[jsonArray.length()];

            String[] DonatorIDs = new String[jsonArray.length()];

            String[] Seen = new String[jsonArray.length()];


            String[] IDs = new String[jsonArray.length()];

            Log.i(Integer.toString(jsonArray.length()), "DID:");
            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array


                HospitalIDs[i] = obj.getString("Hospital_ID");
                // Log.i(HospitalIDs[i],"DID:");

                Files[i] = obj.getString("File");
                // Log.i(Files[i],"DID:");

                Seen[i] = obj.getString("Seen");
                // Log.i(Seen[i],"DID:");

                DonatorIDs[i] = obj.getString("Donator_ID");
                // Log.i(DonatorIDs[i],"DID:");

                IDs[i] = obj.getString("ID");


            }
            for (int i = 0; i < HospitalIDs.length; i++) {
                Log.i(HospitalIDs[i], "DID:");

            }


            for (int i = 0; i < jsonArray.length(); i++) {
                if (nationalid.compareTo(DonatorIDs[i]) == 0) {

                    if (Seen[i].compareTo("0") == 0) {
                        countmessages++;


                        for (int j = 0; j < HNs.length; j++) {

                            if (HospitalIDs[i].compareTo(HIDs[j]) == 0) {

                                MessageDetails MD = new MessageDetails();
                                MD.setHospitalname(HNs[j]);
                                MD.setFile(Files[i]);
                                MD.setID(IDs[i]);

                                messageitems.add(MD);
                                break;

                            }

                        }
                    } else if (Seen[i].compareTo("1") == 0) {
                        Log.i(Seen[i], "seen:");
                        continue;
                    }
                }
            }

            for (int i = 0; i < messageitems.size(); i++) {
                Log.i(messageitems.get(i).getHospitalname(), "HN:");


            }


            notmessages.setText(Integer.toString(countmessages));

            if (notmessages.getText().toString().compareTo("0") == 0) {
                notmessages.setVisibility(View.INVISIBLE);
            } else {
                notmessages.setVisibility(View.VISIBLE);


            }

        }

    }

    private void getJSONAllHospitals(final String urlWebService) {
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
                    retrievedataAllHospitals(s);
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
    private void retrievedataAllHospitals(String json) throws JSONException {
        //creating a json array from the json string

        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(HomeMod.this).create();
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
            JSONArray jsonArray = new JSONArray(json);

            //creating a string array for listview
            String[] HospitalIDs = new String[jsonArray.length()];

            String[] HospitalNames = new String[jsonArray.length()];


            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array


                HospitalIDs[i] = obj.getString("Hospital_ID");
                HospitalNames[i] = obj.getString("Hospital_name");


            }


            getJSONMessages("http://wellcare.atwebpages.com/android/getmessagefiles2.php", HospitalNames, HospitalIDs);

        }


    }

    private class AddNewEntry extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub


            String Seen = arg[0];
            String ID = arg[1];




            List<NameValuePair> params = new ArrayList<NameValuePair>();



            params.add(new BasicNameValuePair("Seen", Seen));
            params.add(new BasicNameValuePair("ID", ID));

            for(int i=0;i<params.size();i++)
            {
                Log.i(params.get(i).getValue().toString(),"params:");
            }

            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_UPDATE_SEEN,
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





}
