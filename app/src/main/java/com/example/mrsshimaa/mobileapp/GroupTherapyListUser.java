package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.FrameLayout;
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
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class GroupTherapyListUser extends AppCompatActivity {
    ArrayList<DoctorsGroupTherapyDetails> Doctors;
    ArrayList<GroupsGroupTherapyDetails> Groups;

    DoctorsGroupTherapyadapter DGTA;
    GroupsGroupTherapyadapter GGTA;
    FrameLayout Doctorsframe;
    FrameLayout Groupsframe;
    ListView Doctorslist;
    Boolean DoctorsClicked=false;
    Boolean GroupsClicked=false;
    String Username;
    String Gender;
    String Age;
    String Email;
    String Nationalid;
    String Bloodtype;
    String Phonenumber;
    String Lastdonationdate;
    static String Sender="Doctors";
    ArrayList<Integer>GroupsIDs;
    ArrayList<DoctorsGroupTherapyDetails> AllDoctors;
    PopupWindow pw;
    ListView AllDoctorsList;
    TextView notnumbertext;
    TextView messagecounttext;
    int notnumber;
    int messagecount;
    ListView notificationslist;
    String URL_UPDATE_SEEN="http://wellcare.atwebpages.com/android/updateseen.php";

    ImageView profileicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_therapy_list_user);

        profileicon=(ImageView) findViewById(R.id.profileicon);

        notnumbertext = (TextView) findViewById(R.id.notnumber);
        messagecounttext=(TextView) findViewById(R.id.messagenumber);
       //getJSON("http://192.168.43.232/GarduationProjectV2/android/getdatadoctorslist.php");
        getJSON("http://wellcare.atwebpages.com/android/getdatadoctorsjoineddoctorspatient.php");
       // getJSON("wellcare.atwebpages.com/android/getdatadoctorslist.php");

        Doctors= new ArrayList<DoctorsGroupTherapyDetails>();
        Groups= new ArrayList<GroupsGroupTherapyDetails>();
        AllDoctors= new ArrayList<DoctorsGroupTherapyDetails>();

        GroupsIDs= new ArrayList<Integer>();
        Doctorslist= (ListView)findViewById(R.id.doctorslist);
        Doctorsframe=(FrameLayout)findViewById(R.id.doctorsbtn);
        Groupsframe=(FrameLayout)findViewById(R.id.groupsbtn);

        Doctorsframe.setBackgroundColor(Color.parseColor("#00838F"));


        Bundle extras = getIntent().getExtras();



        if (extras != null) {
            Username=extras.getString("Username");
            Gender=extras.getString("Sex");
            Age=extras.getString("Age");
            Email=extras.getString("Email");
            Nationalid=extras.getString("National_id");
            Bloodtype=extras.getString("Blood_type");
            Phonenumber=extras.getString("phonenumber");
            Lastdonationdate=extras.getString("lastdonationdate");
            notnumber=Integer.parseInt(extras.getString("notnumber"));
            messagecount=Integer.parseInt(extras.getString("messagenumber"));

        }


        notnumbertext.setText(Integer.toString(notnumber));
        messagecounttext.setText(Integer.toString(messagecount));


        if (notnumbertext.getText().toString().compareTo("0") == 0) {
            notnumbertext.setVisibility(View.INVISIBLE);
        } else {
            notnumbertext.setVisibility(View.VISIBLE);


        }



        if (messagecounttext.getText().toString().compareTo("0") == 0) {
            messagecounttext.setVisibility(View.INVISIBLE);
        } else {
            messagecounttext.setVisibility(View.VISIBLE);


        }

    }


    public  void Gotoprofile(View view )
    {
        Intent intent = new Intent(GroupTherapyListUser.this, UserProfilePrivate.class);
        intent.putExtra("Username",Username);
        intent.putExtra("National_id",Nationalid);
        intent.putExtra("Email",Email);
        intent.putExtra("Age", Age);
        intent.putExtra("Blood_type", Bloodtype);
        intent.putExtra("Sex", Gender);
        intent.putExtra("lastdonationdate",Lastdonationdate);
        intent.putExtra("phonenumber",Phonenumber);

        startActivity(intent);
        GroupTherapyListUser.this.finish();
    }
    public  void showDoctorslist(View view)
    {
        DoctorsClicked=true;
        Doctorsframe.setBackgroundColor(Color.parseColor("#00838F"));
        Groupsframe.setBackgroundColor(Color.parseColor("#00ACC1"));
        GroupsClicked=false;
        //getJSON("http://192.168.43.232/GarduationProjectV2/android/getdatadoctorslist.php");
        getJSON("http://wellcare.atwebpages.com/android/getdatadoctorsjoineddoctorspatient.php");

        //getJSON("wellcare.atwebpages.com/android/getdatadoctorslist.php");

    }

    public  void showGroupslist(View view)
    {
        GroupsClicked=true;
        Groupsframe.setBackgroundColor(Color.parseColor("#00838F"));
        Doctorsframe.setBackgroundColor(Color.parseColor("#00ACC1"));
        DoctorsClicked=false;
      //  getJSON("http://192.168.43.232/GarduationProjectV2/android/getdatagroupslistuser.php");
        //getJSON("http://192.168.43.232/GarduationProjectV2/android/getdatagroupid.php");

        getJSON("http://wellcare.atwebpages.com/android/getdatagroupid.php");


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


        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(GroupTherapyListUser.this).create();
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

            if (DoctorsClicked == true || (DoctorsClicked == false && GroupsClicked == false)) {
                Doctors.clear();

                Log.i(json, "DGTD:");
                JSONArray jsonArray = new JSONArray(json);


                String[] Firstnames = new String[jsonArray.length()];

                String[] Lastnames = new String[jsonArray.length()];

                String[] IDs = new String[jsonArray.length()];

                String[] Emails = new String[jsonArray.length()];

                String[] Passwords = new String[jsonArray.length()];

                String[] Phonenumbers = new String[jsonArray.length()];

                String[] Positions = new String[jsonArray.length()];

                String[] Patients = new String[jsonArray.length()];


                for (int i = 0; i < jsonArray.length(); i++) {

                    //getting json object from the json array
                    JSONObject obj = jsonArray.getJSONObject(i);

                    //getting the name from the json object and putting it inside string array

                    Firstnames[i] = obj.getString("First_name");

                    Lastnames[i] = obj.getString("Last_name");

                    IDs[i] = obj.getString("ID");

                    Emails[i] = obj.getString("Email");

                    Passwords[i] = obj.getString("Password");

                    Phonenumbers[i] = obj.getString("Phonenumber");

                    Positions[i] = obj.getString("Position");

                    Patients[i] = obj.getString("Patient");

                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    //Log.i(Patients[i],"DGTD:");
                    Log.i(Username, "DGTD:");

                    if (Patients[i].compareTo(Username) == 0) {
                        DoctorsGroupTherapyDetails DGTD = new DoctorsGroupTherapyDetails();

                        DGTD.setName(Firstnames[i] + " " + Lastnames[i]);

                        DGTD.setID(IDs[i]);

                        DGTD.setEmail(Emails[i]);

                        DGTD.setPassword(Passwords[i]);

                        DGTD.setPhonenumber(Phonenumbers[i]);

                        DGTD.setPosition(Positions[i]);

                        //DGTD.setPatient(Patients[i]);


                        Doctors.add(DGTD);
                    }


                }


                DGTA = new DoctorsGroupTherapyadapter(GroupTherapyListUser.this, R.layout.doctorslistitemgrouptherapy, Doctors);
                Doctorslist.setAdapter(DGTA);

                DGTA.notifyDataSetChanged();


                //}

            } else if (GroupsClicked == true) {

                Groups.clear();
                JSONArray jsonArray = new JSONArray(json);

                if (jsonArray != null) {
                    String[] Doctorsids = new String[jsonArray.length()];

                    String[] Groupsnames = new String[jsonArray.length()];

                    String[] Usernames = new String[jsonArray.length()];

                    String[] IDs = new String[jsonArray.length()];


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);


                        Doctorsids[i] = obj.getString("Doctor_ID");

                        Groupsnames[i] = obj.getString("Group_name");

                        Usernames[i] = obj.getString("Username");

                        IDs[i] = obj.getString("ID");


                    }
                    for (int i = 0; i < jsonArray.length(); i++) {


                        if (Usernames[i].compareTo(Username) == 0) {
                            GroupsGroupTherapyDetails GGTD = new GroupsGroupTherapyDetails();

                            GGTD.setDoctorid(Doctorsids[i]);
                            GGTD.setGroupname(Groupsnames[i]);
                            GGTD.setUsername(Usernames[i]);
                            GGTD.setID(IDs[i]);
                            Groups.add(GGTD);

                        }


                    }
                }

                GGTA = new GroupsGroupTherapyadapter(GroupTherapyListUser.this, R.layout.groupslistitemgrouptherapy, Groups);
                Doctorslist.setAdapter(GGTA);

                GGTA.notifyDataSetChanged();

            }


        }
    }

    public  class DoctorsGroupTherapyadapter extends ArrayAdapter<DoctorsGroupTherapyDetails> {
        Activity context;
        int layoutresource;
        DoctorsGroupTherapyDetails DGTD;
        ArrayList<DoctorsGroupTherapyDetails> mData= new ArrayList<DoctorsGroupTherapyDetails>();

        public DoctorsGroupTherapyadapter(Activity activity, int resource, ArrayList<DoctorsGroupTherapyDetails> data) {
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
        public DoctorsGroupTherapyDetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable DoctorsGroupTherapyDetails item) {
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
            GroupTherapyListUser.DoctorsGroupTherapyadapter.ViewHolder holder = null;

            if(row == null || row.getTag()==null){
                LayoutInflater inflater =LayoutInflater.from(context);
                row = inflater.inflate(layoutresource,null);
                holder= new GroupTherapyListUser.DoctorsGroupTherapyadapter.ViewHolder();
                holder.mname=(TextView)row.findViewById(R.id.doctorname);
                holder.mspecialization=(TextView)row.findViewById(R.id.doctorspecialization);

                row.setTag(holder);
            }

            else
            {
                holder=(GroupTherapyListUser.DoctorsGroupTherapyadapter.ViewHolder)row.getTag();
            }
            holder.DGTD=getItem(position);
            holder.mname.setText(holder.DGTD.getName());
            holder.mspecialization.setText(holder.DGTD.getPosition());


            final GroupTherapyListUser.DoctorsGroupTherapyadapter.ViewHolder finalHolder = holder;
            holder.mname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    setSender("Doctors");



                    Intent intent = new Intent(GroupTherapyListUser.this,ChattingPageUser.class);

                    intent.putExtra("doctorname",finalHolder.DGTD.getName().toString());
                    intent.putExtra("doctorid",finalHolder.DGTD.getID().toString());

                    intent.putExtra("Username",Username);
                    intent.putExtra("National_id",Nationalid);
                    intent.putExtra("Email",Email);
                    intent.putExtra("Age", Age);
                    intent.putExtra("Blood_type", Bloodtype);
                    intent.putExtra("Sex", Gender);
                    intent.putExtra("lastdonationdate",Lastdonationdate);
                    intent.putExtra("phonenumber",Phonenumber);

                    startActivity(intent);


                }
            });

            return row;
        }


        class ViewHolder {
            DoctorsGroupTherapyDetails DGTD;
            TextView mname;
            TextView mspecialization;


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
            GroupTherapyListUser.GroupsGroupTherapyadapter.ViewHolder holder = null;

            if(row == null || row.getTag()==null){
                LayoutInflater inflater =LayoutInflater.from(context);
                row = inflater.inflate(layoutresource,null);
                holder= new GroupTherapyListUser.GroupsGroupTherapyadapter.ViewHolder();
                holder.mgroupname=(TextView)row.findViewById(R.id.groupname);

                row.setTag(holder);
            }

            else
            {
                holder=(GroupTherapyListUser.GroupsGroupTherapyadapter.ViewHolder)row.getTag();
            }
            holder.GGTD=getItem(position);
            holder.mgroupname.setText(holder.GGTD.getGroupname());


            final GroupTherapyListUser.GroupsGroupTherapyadapter.ViewHolder finalHolder = holder;
            holder.mgroupname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                  //  getJSONgrouptime("http://192.168.43.232/GarduationProjectV2/android/getdatagrouptime.php",finalHolder.GGTD.getDoctorid(),finalHolder.GGTD.getGroupname(),finalHolder.GGTD.getUsername(),finalHolder.GGTD.getID());
                    getJSONgrouptime("http://wellcare.atwebpages.com/android/getdatagrouptime.php",finalHolder.GGTD.getDoctorid(),finalHolder.GGTD.getGroupname(),finalHolder.GGTD.getUsername(),finalHolder.GGTD.getID());







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

    private void getJSONgrouptime(final String urlWebService, final  String DoctorID, final  String GroupName,final String Username,final String GroupID) {
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
                    retrievedatagrouptime(s,DoctorID,GroupName,Username,GroupID);
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
    private void retrievedatagrouptime(String json,final  String DoctorID, final  String GroupName,final String Username,final String GroupID) throws JSONException
    {


        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(GroupTherapyListUser.this).create();
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
            TimeZone tz = TimeZone.getTimeZone("GMT+2");
            Calendar c = Calendar.getInstance(tz);
            String time = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.format("%02d", c.get(Calendar.MINUTE)) + ":"
                    + String.format("%02d", c.get(Calendar.SECOND)) + ":";


            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);


            JSONArray jsonArray = new JSONArray(json);

            String[] groupids = new String[jsonArray.length()];

            String[] days = new String[jsonArray.length()];

            String[] begintimes = new String[jsonArray.length()];

            String[] endtimes = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);


                groupids[i] = obj.getString("group_id");

                days[i] = obj.getString("day");

                begintimes[i] = obj.getString("begin");

                endtimes[i] = obj.getString("end");


            }


            DateFormat time1 = null;
            String begintime;
            String endtime;
            java.sql.Time timeValue1 = null;
            java.sql.Time timeValue2 = null;
            java.sql.Time timeValue3 = null;


            for (int i = 0; i < jsonArray.length(); i++) {

                if (groupids[i].compareTo(GroupID) == 0) {

                    if (days[i].compareTo(dayOfTheWeek) == 0) {
                        try {


                            begintime = begintimes[i];
                            endtime = endtimes[i];

                            time1 = new SimpleDateFormat("HH:mm:ss");


                            timeValue1 = new java.sql.Time(time1.parse(begintime).getTime());
                            timeValue2 = new java.sql.Time(time1.parse(endtime).getTime());
                            timeValue3 = new java.sql.Time(time1.parse(time).getTime());


                            Log.i(timeValue1.toString(), "y:");
                            Log.i(timeValue2.toString(), "y:");
                            Log.i(timeValue3.toString(), "y:");


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (begintimes[i].compareTo(time) == 0) {
                            setSender("Groups");


                            Intent intent = new Intent(GroupTherapyListUser.this, ChattingPageUser.class);

                            intent.putExtra("Doctor_ID", DoctorID);

                            intent.putExtra("Group_name", GroupName);

                            intent.putExtra("Username", Username);

                            intent.putExtra("ID", GroupID);

                            startActivity(intent);

                        } else if (timeValue3.after(timeValue1) && timeValue3.before(timeValue2)) {
                            setSender("Groups");

                            Intent intent = new Intent(GroupTherapyListUser.this, ChattingPageUser.class);

                            intent.putExtra("Doctor_ID", DoctorID);

                            intent.putExtra("Group_name", GroupName);

                            intent.putExtra("Username", Username);

                            intent.putExtra("ID", GroupID);

                            startActivity(intent);
                            System.out.println(true);
                        } else {

                            Log.i("yes", "y:");
                            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(GroupTherapyListUser.this).create();


                            alertDialog.setMessage("This Group doesn't begin now, please check the group's schedule.");

                            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {


                                            dialog.dismiss();
                                        }
                                    });

                            alertDialog.show();


                        }
                    } else {



                        //if(i==(jsonArray.length()-1)) {
                            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(GroupTherapyListUser.this).create();
                            alertDialog.setMessage("This Group doesn't begin now, please check the group's schedule.");

                            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {


                                            dialog.dismiss();
                                        }
                                    });

                            alertDialog.show();
                       // }


                    }
                }


            }


        }

    }

    public  void showSchedule(View view)
    {
        Intent i = new Intent(GroupTherapyListUser.this,GroupSchedule.class);
        startActivity(i);

    }
    public  void showAllDoctors(View view)
    {
       // getJSONalldoctors("http://192.168.43.232/GarduationProjectV2/android/getdatadoctorslist.php");
        getJSONalldoctors("http://wellcare.atwebpages.com/android/getdatadoctorslist.php");
        LayoutInflater inflater = (LayoutInflater) GroupTherapyListUser.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popupshowalldoctors, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, 600, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        AllDoctorsList= (ListView) layout.findViewById(R.id.alldoctorslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

    }
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {

            pw.dismiss();



        }
    };
    private void getJSONalldoctors(final String urlWebService) {
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


                    retrievedataalldoctors(s);



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
    private void retrievedataalldoctors(String json) throws JSONException {


        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(GroupTherapyListUser.this).create();
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
            AllDoctors.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                DoctorsGroupTherapyDetails DGTD = new DoctorsGroupTherapyDetails();
                DGTD.setName(firstnames[i] + "" + lastnames[i]);
                DGTD.setEmail(emails[i]);
                DGTD.setID(ids[i]);
                DGTD.setPhonenumber(phonenumbers[i]);
                DGTD.setPassword(passwords[i]);
                DGTD.setPosition(positions[i]);
                AllDoctors.add(DGTD);

            }


            DGTA = new DoctorsGroupTherapyadapter(GroupTherapyListUser.this, R.layout.doctorslistitemgrouptherapy, AllDoctors);
            AllDoctorsList.setAdapter(DGTA);

            DGTA.notifyDataSetChanged();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listhospitalactions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Intent intent = new Intent(GroupTherapyListUser.this, FirstPage.class);
                startActivity(intent);
                FirstPage.sp.edit().putBoolean("logged",false).apply();
                GroupTherapyListUser.this.finish();


                return true;
            case R.id.home:
                intent = new Intent(GroupTherapyListUser.this, HomeMod.class);
                intent.putExtra("Username",Username);
                intent.putExtra("Sex",Gender);
                intent.putExtra("Age",Age);
                intent.putExtra("Email",Email);
                intent.putExtra("National_id",Nationalid);
                intent.putExtra("Blood_type",Bloodtype);
                intent.putExtra("phonenumber",Phonenumber);
                intent.putExtra("lastdonationdate",Lastdonationdate);

                GroupTherapyListUser.this.finish();

                startActivity(intent);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public  void showNotifications(View view)
    {
        LayoutInflater inflater = (LayoutInflater) GroupTherapyListUser.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notificationspopup, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        notificationslist= (ListView)layout.findViewById(R.id.notificationslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        NotificationsAdapter NA = new GroupTherapyListUser.NotificationsAdapter(GroupTherapyListUser.this, R.layout.notificationitem, HomeMod.notificationitems);

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
            GroupTherapyListUser.NotificationsAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new GroupTherapyListUser.NotificationsAdapter.ViewHolder();
                holder.mhospitalname = (TextView) row.findViewById(R.id.hospitalname);
                holder.mbloodtype = (TextView) row.findViewById(R.id.bloodtype);

                row.setTag(holder);
            } else {
                holder = (GroupTherapyListUser.NotificationsAdapter.ViewHolder) row.getTag();
            }
            holder.ND = getItem(position);
            holder.mhospitalname.setText(holder.ND.getHospitalname()+ " Urgently Needs Your Help");
            holder.mbloodtype.setText(holder.ND.getBloodType()+" Needed, Your blood type is " + Bloodtype+" so you can donate");


            final GroupTherapyListUser.NotificationsAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            holder.mhospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(GroupTherapyListUser.this,HospitalNotificationProfile.class);

                    intent.putExtra("name",finalHolder.ND.getHospitalname());
                    intent.putExtra("address",finalHolder.ND.getAddress());
                    intent.putExtra("contactnumber",finalHolder.ND.getContactNumber());
                    intent.putExtra("email",finalHolder.ND.getEmail());


                    GroupTherapyListUser.this.finish();

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
        LayoutInflater inflater = (LayoutInflater) GroupTherapyListUser.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notificationspopup, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        notificationslist= (ListView)layout.findViewById(R.id.notificationslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        MessagesAdapter MA = new GroupTherapyListUser.MessagesAdapter(GroupTherapyListUser.this, R.layout.messagefileitem, HomeMod.messageitems);
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
            GroupTherapyListUser.MessagesAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new GroupTherapyListUser.MessagesAdapter.ViewHolder();
                holder.mhospitalname = (TextView) row.findViewById(R.id.hospitalname);

                row.setTag(holder);
            } else {
                holder = (GroupTherapyListUser.MessagesAdapter.ViewHolder) row.getTag();
            }
            holder.MD = getItem(position);
            holder.mhospitalname.setText(holder.MD.getHospitalname()+ " Sent You a File");


            final GroupTherapyListUser.MessagesAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            final ViewHolder finalHolder2 = holder;
            holder.mhospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Intent i = new Intent(GroupTherapyListUser.this,FileContent.class);
                    i.putExtra("File", finalHolder2.MD.getFile());
                    new AddNewEntry().execute("1",finalHolder2.MD.getID());

                    GroupTherapyListUser.this.finish();
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
