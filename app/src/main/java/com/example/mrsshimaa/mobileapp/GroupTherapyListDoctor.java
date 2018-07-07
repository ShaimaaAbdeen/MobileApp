package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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

public class GroupTherapyListDoctor extends AppCompatActivity {
    FrameLayout Patientsframe;
    FrameLayout Groupsframe;
    ListView Patientslist;
    Boolean PatientsClicked=false;
    Boolean GroupsClicked=false;
    String firstname;
    String lastname;
    String phonenumber;
    String email;
    String position;
    String id;
    String password;
    ArrayList<PatientsGroupTherapyDetails> Patients;
    ArrayList<GroupsGroupTherapyDetails> Groups;

    PatientsGroupTherapyadapter PGTA;
    GroupsGroupTherapyadapter GGTA;
    static  String Sender;

    String Email;
    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_therapy_list_doctor);
        //getJSON("http://192.168.43.232/GarduationProjectV2/android/getpatientslist.php");



        Patients=new ArrayList<>();
        Groups=new ArrayList<>();
        Patientslist= (ListView)findViewById(R.id.patientslist);
        Patientsframe=(FrameLayout)findViewById(R.id.patientsbtn);
        Groupsframe=(FrameLayout)findViewById(R.id.groupsbtn);

        Patientsframe.setBackgroundColor(Color.parseColor("#00838F"));




        if(FirstPage.sp.getBoolean("logged",true))
        {
           loadPreferences();
            getJSONDoctors("http://wellcare.atwebpages.com/android/getdatadoctorslist.php");
        }
        else {

            Bundle extras = getIntent().getExtras();


            if (extras != null) {
                firstname = extras.getString("First_name");
                lastname = extras.getString("Last_name");
                phonenumber = extras.getString("phonenumber");
                email = extras.getString("Email");
                position = extras.getString("Position");
                id = extras.getString("ID");
                password = extras.getString("Password");
            }
        }


        getJSON("http://wellcare.atwebpages.com/android/getpatientslist.php");


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

        Log.i(json,"hi:");


        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(GroupTherapyListDoctor.this).create();
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


            for (int i = 0; i < jsonArray.length(); i++) {
                if (Email.compareTo(emails[i]) == 0) {

                    if (Password.compareTo(passwords[i]) == 0) {


                        firstname = firstnames[i];
                        lastname = lastnames[i];
                        phonenumber = phonenumbers[i];
                        email = emails[i];
                        position = positions[i];
                        id = ids[i];
                        password = passwords[i];

                        break;


                    }
                }


            }


        }
    }


    public  void Gotoprofile(View view)
    {
        Intent intent = new Intent(GroupTherapyListDoctor.this, DoctorProfilePrivate.class);
        intent.putExtra("First_name", firstname);
        intent.putExtra("Last_name", lastname);
        intent.putExtra("phonenumber", phonenumber);
        intent.putExtra("Email", email);
        intent.putExtra("Position",position);
        intent.putExtra("ID", id);
        intent.putExtra("Password",password );


        startActivity(intent);
        GroupTherapyListDoctor.this.finish();


    }
    public  void showPatientslist(View view)
    {
        PatientsClicked=true;
        Patientsframe.setBackgroundColor(Color.parseColor("#00838F"));
        Groupsframe.setBackgroundColor(Color.parseColor("#00ACC1"));
        GroupsClicked=false;
        //getJSON("http://192.168.43.232/GarduationProjectV2/android/getpatientslist.php");
        getJSON("http://wellcare.atwebpages.com/android/getpatientslist.php");
    }
    public  void showGroupslist(View view)
    {
        GroupsClicked=true;
        Groupsframe.setBackgroundColor(Color.parseColor("#00838F"));
        Patientsframe.setBackgroundColor(Color.parseColor("#00ACC1"));
        PatientsClicked=false;
       // getJSON("http://192.168.43.232/GarduationProjectV2/android/getdatagroupslistdoctor.php");
        //getJSON(" http://192.168.43.232/GarduationProjectV2/android/getdatagroupslistdoctorjoineddoctor.php");
        getJSON("http://wellcare.atwebpages.com/android/getdatagroupslistdoctorjoineddoctor.php");

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


    private void retrievedata(String json) throws JSONException
    {
        //if(json.compareTo("http://192.168.43.232/GarduationProjectV2/android/getdatadoctorslist.php")==0) {


        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(GroupTherapyListDoctor.this).create();
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
            if (PatientsClicked == true || (PatientsClicked == false && GroupsClicked == false)) {
                Patients.clear();

                JSONArray jsonArray = new JSONArray(json);

                if (jsonArray != null) {
                    String[] patientnames = new String[jsonArray.length()];

                    String[] doctorids = new String[jsonArray.length()];


                    //looping through all the elements in json array
                    for (int i = 0; i < jsonArray.length(); i++) {

                        //getting json object from the json array
                        JSONObject obj = jsonArray.getJSONObject(i);

                        //getting the name from the json object and putting it inside string array


                        patientnames[i] = obj.getString("Patient");
                        doctorids[i] = obj.getString("Doctor_ID");

                    }
                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (doctorids[i].compareTo(id) == 0) {
                            PatientsGroupTherapyDetails PGTD = new PatientsGroupTherapyDetails();

                            PGTD.setPatientname(patientnames[i]);


                            Patients.add(PGTD);
                        }


                    }
                }
                PGTA = new GroupTherapyListDoctor.PatientsGroupTherapyadapter(GroupTherapyListDoctor.this, R.layout.patientslistitemgrouptherapy, Patients);
                Patientslist.setAdapter(PGTA);

                PGTA.notifyDataSetChanged();
                //}

            } else if (GroupsClicked == true) {
                Groups.clear();
                JSONArray jsonArray = new JSONArray(json);

                if (jsonArray != null) {
                    String[] Firstnames = new String[jsonArray.length()];

                    String[] Lastnames = new String[jsonArray.length()];

                    String[] Doctorsids = new String[jsonArray.length()];

                    String[] Groupsnames = new String[jsonArray.length()];

                    String[] ID = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);

                        Firstnames[i] = obj.getString("First_name");

                        Lastnames[i] = obj.getString("Last_name");

                        Doctorsids[i] = obj.getString("Doctor_ID");

                        Groupsnames[i] = obj.getString("Group_name");

                        ID[i] = obj.getString("ID");


                    }
                    for (int i = 0; i < jsonArray.length(); i++) {


                        if (Doctorsids[i].compareTo(id) == 0) {
                            GroupsGroupTherapyDetails GGTD = new GroupsGroupTherapyDetails();

                            GGTD.setDoctorid(Doctorsids[i]);
                            GGTD.setGroupname(Groupsnames[i]);
                            GGTD.setID(ID[i]);
                            GGTD.setDoctorFirstname(Firstnames[i]);
                            GGTD.setDoctorLastname(Lastnames[i]);
                            Groups.add(GGTD);

                        }


                    }

                }
                GGTA = new GroupTherapyListDoctor.GroupsGroupTherapyadapter(GroupTherapyListDoctor.this, R.layout.groupslistitemgrouptherapy, Groups);
                Patientslist.setAdapter(GGTA);

                GGTA.notifyDataSetChanged();

            }
        }
    }


    public  class PatientsGroupTherapyadapter extends ArrayAdapter<PatientsGroupTherapyDetails> {
        Activity context;
        int layoutresource;
        PatientsGroupTherapyDetails PGTD;
        ArrayList<PatientsGroupTherapyDetails> mData= new ArrayList<PatientsGroupTherapyDetails>();

        public PatientsGroupTherapyadapter(Activity activity, int resource, ArrayList<PatientsGroupTherapyDetails> data) {
            super(activity, resource, data);
            context=activity;
            layoutresource=resource;
            mData=data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Nullable
        @Override
        public PatientsGroupTherapyDetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable PatientsGroupTherapyDetails item) {
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
            GroupTherapyListDoctor.PatientsGroupTherapyadapter.ViewHolder holder = null;

            if(row == null || row.getTag()==null){
                LayoutInflater inflater =LayoutInflater.from(context);
                row = inflater.inflate(layoutresource,null);
                holder= new GroupTherapyListDoctor.PatientsGroupTherapyadapter.ViewHolder();
                holder.mpatientname=(TextView)row.findViewById(R.id.patientname);

                row.setTag(holder);
            }

            else
            {
                holder=(GroupTherapyListDoctor.PatientsGroupTherapyadapter.ViewHolder)row.getTag();
            }
            holder.PGTD=getItem(position);
            holder.mpatientname.setText(holder.PGTD.getPatientname());


            final GroupTherapyListDoctor.PatientsGroupTherapyadapter.ViewHolder finalHolder = holder;
            holder.mpatientname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    setSender("Patients");




                    Intent intent = new Intent(GroupTherapyListDoctor.this,ChattingPageDoctor.class);

                    intent.putExtra("patientname",finalHolder.PGTD.getPatientname().toString());

                    intent.putExtra("First_name", firstname);
                    intent.putExtra("Last_name", lastname);
                    intent.putExtra("phonenumber", phonenumber);
                    intent.putExtra("Email", email);
                    intent.putExtra("Position", position);
                    intent.putExtra("ID", id);
                    intent.putExtra("Password", password);

                    startActivity(intent);


                }
            });

            return row;
        }


        class ViewHolder {
            PatientsGroupTherapyDetails PGTD;
            TextView mpatientname;


        }
    }


    public  class GroupsGroupTherapyadapter extends ArrayAdapter<GroupsGroupTherapyDetails> {
        Activity context;
        int layoutresource;
        GroupsGroupTherapyDetails GGTD;
        ArrayList<GroupsGroupTherapyDetails> mData= new ArrayList<GroupsGroupTherapyDetails>();

        public GroupsGroupTherapyadapter(Activity activity, int resource, ArrayList<GroupsGroupTherapyDetails> data) {
            super(activity, resource, data);
            context=activity;
            layoutresource=resource;
            mData=data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Nullable
        @Override
        public GroupsGroupTherapyDetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable GroupsGroupTherapyDetails item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            GroupTherapyListDoctor.GroupsGroupTherapyadapter.ViewHolder holder = null;

            if(row == null || row.getTag()==null){
                LayoutInflater inflater =LayoutInflater.from(context);
                row = inflater.inflate(layoutresource,null);
                holder= new GroupTherapyListDoctor.GroupsGroupTherapyadapter.ViewHolder();
                holder.mgroupname=(TextView)row.findViewById(R.id.groupname);

                row.setTag(holder);
            }

            else
            {
                holder=(GroupTherapyListDoctor.GroupsGroupTherapyadapter.ViewHolder)row.getTag();
            }
            holder.GGTD=getItem(position);
            holder.mgroupname.setText(holder.GGTD.getGroupname());


            final GroupTherapyListDoctor.GroupsGroupTherapyadapter.ViewHolder finalHolder = holder;
            holder.mgroupname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setSender("Groups");



                    Intent intent = new Intent(GroupTherapyListDoctor.this,ChattingPageDoctor.class);


                    intent.putExtra("Doctor_ID", finalHolder.GGTD.getDoctorid());
                    intent.putExtra("Group_name", finalHolder.GGTD.getGroupname());
                    intent.putExtra("ID", finalHolder.GGTD.getID());
                    intent.putExtra("DoctorFirstname",finalHolder.GGTD.getDoctorFirstname());
                    intent.putExtra("DoctorLastname",finalHolder.GGTD.getDoctorLastname());


                    startActivity(intent);





                }
            });

            return row;
        }


        class ViewHolder {
            GroupsGroupTherapyDetails GGTD;
            TextView mgroupname;


        }
    }

    public static   void setSender(String sender)
    {
        Sender=sender;

    }
    public static String getSender()
    {
        return  Sender;
    }

    public  void createGroup(View view)
    {
        Intent intent = new Intent(GroupTherapyListDoctor.this, CreateGroup.class);
       // intent.putExtra("First_name", firstname);
       // intent.putExtra("Last_name", lastname);
        //intent.putExtra("phonenumber", phonenumber);
        //intent.putExtra("Email", email);
        //intent.putExtra("Position",position);
        intent.putExtra("ID", id);
        //intent.putExtra("Password",password );


        startActivity(intent);
        GroupTherapyListDoctor.this.finish();


    }
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
                Intent intent = new Intent(GroupTherapyListDoctor.this, FirstPage.class);
                startActivity(intent);
                FirstPage.sp.edit().putBoolean("logged",false).apply();
                GroupTherapyListDoctor.this.finish();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
