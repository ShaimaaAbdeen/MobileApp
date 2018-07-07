package com.example.mrsshimaa.mobileapp;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

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

public class CreateGroup extends AppCompatActivity {
    String doctorid;
    private PopupWindow pw;
    CheckBox Sunday;
    CheckBox Monday;
    CheckBox Tuesday;
    CheckBox Wednesday;
    CheckBox Thursday;
    CheckBox Friday;
    CheckBox Saturday;

    TextView setbegintimesun;
    TextView setendtimesun;
    TextView setbegintimemon;
    TextView setendtimemon;
    TextView setbegintimetues;
    TextView setendtimetues;
    TextView setbegintimewed;
    TextView setendtimewed;
    TextView setbegintimethurs;
    TextView setendtimethurs;
    TextView setbegintimefri;
    TextView setendtimefri;
    TextView setbegintimesat;
    TextView setendtimesat;
    Button cancelButton;
    Button cancelButton_2;

    EditText groupname;
    //private String URL_GROUP_ID = "http://192.168.43.232/GarduationProjectV2/android/addgroupid.php";
    //private String URL_GROUP_TIME="http://192.168.43.232/GarduationProjectV2/android/addgrouptime.php";

    private String URL_GROUP_ID = "http://wellcare.atwebpages.com/android/addgroupid.php";
    private String URL_GROUP_TIME="http://wellcare.atwebpages.com/android/addgrouptime.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Sunday=(CheckBox) findViewById(R.id.sundaychkbx);
        Monday=(CheckBox) findViewById(R.id.mondaychkbx);
        Tuesday=(CheckBox) findViewById(R.id.tuesdaychkbx);
        Wednesday=(CheckBox) findViewById(R.id.wednesdaychkbx);
        Thursday=(CheckBox) findViewById(R.id.thursdaychkbx);
        Friday=(CheckBox) findViewById(R.id.fridaychkbx);
        Saturday=(CheckBox) findViewById(R.id.saturdaychkbx);

        groupname=(EditText) findViewById(R.id.groupnametext);


        Bundle extras = getIntent().getExtras();



        if (extras != null) {
           // firstname=extras.getString("First_name");
           // lastname=extras.getString("Last_name");
            //phonenumber=extras.getString("phonenumber");
            //email=extras.getString("Email");
           // position=extras.getString("Position");
            doctorid=extras.getString("ID");
           // password=extras.getString("Password");
        }

    }

    public  void  addGroup(View view)
    {
        if(groupname.getText().toString().compareTo("")==0)
        {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(CreateGroup.this).create();
            alertDialog.setMessage("You must enter a group name");

            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        else if((!Sunday.isChecked())
                && (!Monday.isChecked())
                && (!Tuesday.isChecked())
                && (!Wednesday.isChecked())
                && (!Thursday.isChecked())
                && (!Friday.isChecked())
                && (!Saturday.isChecked())  )
        {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(CreateGroup.this).create();
            alertDialog.setMessage("You must choose at least one day");

            alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            initiateBloodPopupWindow(view);
        }
    }
    public  void timePicker(final View view)
    {


            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(CreateGroup.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    ((TextView) view).setText(selectedHour + ":" + selectedMinute);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

    }
    private void initiateBloodPopupWindow(View v) {
        try {



            LayoutInflater inflater = (LayoutInflater) CreateGroup.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popuptimetable, (ViewGroup) findViewById(R.id.popup_element));
            pw = new PopupWindow(layout,600, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pw.showAtLocation(v, Gravity.CENTER,0, 0);
            pw.setBackgroundDrawable(new BitmapDrawable());

            setbegintimesun=(TextView)layout.findViewById(R.id.setbegintimesun);
            setendtimesun=(TextView) layout.findViewById(R.id.settimeendsun);

            setbegintimemon=(TextView) layout.findViewById(R.id.setbegintimemon);
            setendtimemon=(TextView) layout.findViewById(R.id.setendtimemon);

            setbegintimetues=(TextView) layout.findViewById(R.id.setbegintimetues);
            setendtimetues=(TextView) layout.findViewById(R.id.setendtimetues);

            setbegintimewed=(TextView) layout.findViewById(R.id.setbegintimewed);
            setendtimewed=(TextView) layout.findViewById(R.id.setendtimewed);

            setbegintimethurs=(TextView)layout. findViewById(R.id.setbegintimethurs);
            setendtimethurs=(TextView) layout.findViewById(R.id.setendtimethurs);

            setbegintimefri=(TextView) layout.findViewById(R.id.setbegintimefri);
            setendtimefri=(TextView) layout.findViewById(R.id.setendtimefri);

            setbegintimesat=(TextView) layout.findViewById(R.id.setbegintimesat);
            setendtimesat=(TextView) layout.findViewById(R.id.setendtimesat);

             cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

            cancelButton_2 = (Button) layout.findViewById(R.id.end_data_send_button_2);
            cancelButton_2.setOnClickListener(cancel_button_click_listener_2);
            if(!Sunday.isChecked())
            {
                setbegintimesun.setEnabled(false);
                setendtimesun.setEnabled(false);

            }
            if(!Monday.isChecked())
            {
                setbegintimemon.setEnabled(false);
                setendtimemon.setEnabled(false);
            }
            if(!Tuesday.isChecked())
            {
                setbegintimetues.setEnabled(false);
                setendtimetues.setEnabled(false);
            }
            if(!Wednesday.isChecked())
            {
                setbegintimewed.setEnabled(false);
                setendtimewed.setEnabled(false);
            }
            if(!Thursday.isChecked())
            {
                setbegintimethurs.setEnabled(false);
                setendtimethurs.setEnabled(false);
            }
            if(!Friday.isChecked())
            {
                setbegintimefri.setEnabled(false);
                setendtimefri.setEnabled(false);
            }
            if(!Saturday.isChecked())
            {
                setbegintimesat.setEnabled(false);
                setendtimesat.setEnabled(false);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {




            new AddNewEntrygroupid().execute(doctorid,groupname.getText().toString());

          //  getJSON("http://192.168.43.232/GarduationProjectV2/android/getdatagroupidtable.php");
            getJSON("http://wellcare.atwebpages.com/android/getdatagroupidtable.php");

            pw.dismiss();

            Intent i = new Intent(CreateGroup.this,GroupTherapyListDoctor.class);
            i.putExtra("ID",doctorid);
            startActivity(i);


        }
    };

    private View.OnClickListener cancel_button_click_listener_2 = new View.OnClickListener() {
        public void onClick(View v) {





            pw.dismiss();



        }
    };
    private class AddNewEntrygroupid extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub


            String doctorid = arg[0];
            String groupname = arg[1];


            List<NameValuePair> params = new ArrayList<NameValuePair>();


            params.add(new BasicNameValuePair("Doctor_ID", doctorid));
            params.add(new BasicNameValuePair("Group_name", groupname));


            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_GROUP_ID,
                    ServiceHandler.POST, params);

            Log.d("Create  Request: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {
                        // new category created successfully
                        Log.e("Prediction added  ", "> " + jsonObj.getString("message"));
                    } else {
                        Log.e(" Prediction Error: ", "> " + jsonObj.getString("message"));
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


        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(CreateGroup.this).create();
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
            String[] IDS = new String[jsonArray.length()];

            String[] DoctorIDs = new String[jsonArray.length()];

            String[] Groupnames = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);


                DoctorIDs[i] = obj.getString("Doctor_ID");
                Groupnames[i] = obj.getString("Group_name");
                IDS[i] = obj.getString("ID");

            }

            for (int i = 0; i < jsonArray.length(); i++) {
                if (Groupnames[i].compareTo(groupname.getText().toString()) == 0) {
                    if (DoctorIDs[i].compareTo(doctorid) == 0) {
                        if (Sunday.isChecked()) {
                            new AddNewEntrygrouptime().execute(IDS[i], "Sunday", setbegintimesun.getText().toString(), setendtimesun.getText().toString());


                        }
                        if (Monday.isChecked()) {
                            new AddNewEntrygrouptime().execute(IDS[i], "Monday", setbegintimemon.getText().toString(), setendtimemon.getText().toString());

                        }
                        if (Tuesday.isChecked()) {
                            new AddNewEntrygrouptime().execute(IDS[i], "Tuesday", setbegintimetues.getText().toString(), setendtimetues.getText().toString());

                        }
                        if (Wednesday.isChecked()) {
                            new AddNewEntrygrouptime().execute(IDS[i], "Wednesday", setbegintimewed.getText().toString(), setendtimewed.getText().toString());

                        }
                        if (Thursday.isChecked()) {
                            new AddNewEntrygrouptime().execute(IDS[i], "Thursday", setbegintimethurs.getText().toString(), setendtimethurs.getText().toString());

                        }
                        if (Friday.isChecked()) {
                            new AddNewEntrygrouptime().execute(IDS[i], "Friday", setbegintimefri.getText().toString(), setendtimefri.getText().toString());

                        }
                        if (Saturday.isChecked()) {
                            new AddNewEntrygrouptime().execute(IDS[i], "Saturday", setbegintimesat.getText().toString(), setendtimesat.getText().toString());

                        }

                        break;

                    }
                }
            }


        }


    }
    private class AddNewEntrygrouptime extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub


            String groupid = arg[0];
            String day = arg[1];
            String begin = arg[2];
            String end = arg[3];


            List<NameValuePair> params = new ArrayList<NameValuePair>();


            params.add(new BasicNameValuePair("group_id", groupid));
            params.add(new BasicNameValuePair("day", day));
            params.add(new BasicNameValuePair("begin", begin));
            params.add(new BasicNameValuePair("end", end));

            for (int i = 0; i < params.size(); i++) {
                Log.i(params.get(i).getValue().toString(), "params:");
            }

            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_GROUP_TIME,
                    ServiceHandler.POST, params);

            Log.d("Create  Request: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {
                        // new category created successfully
                        Log.e("Prediction added  ", "> " + jsonObj.getString("message"));
                    } else {
                        Log.e(" Prediction Error: ", "> " + jsonObj.getString("message"));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.doctoractions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat:
                Intent intent = new Intent(CreateGroup.this, GroupTherapyListDoctor.class);
                startActivity(intent);
                CreateGroup.this.finish();


                return true;

            case R.id.logout:
                intent = new Intent(CreateGroup.this, FirstPage.class);
                startActivity(intent);
                CreateGroup.this.finish();

                // Green item was selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
