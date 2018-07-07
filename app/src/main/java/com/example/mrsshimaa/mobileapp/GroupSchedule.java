package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.content.DialogInterface;
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

public class GroupSchedule extends AppCompatActivity {
    ArrayList<GroupScheduleDetails> groupschedule;
    GroupScheduleAdapter GSA;
    ListView groupschedulelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_schedule);
        groupschedule= new ArrayList();
        groupschedulelist= (ListView) findViewById(R.id.groupschedulelist);
        //getJSON("http://192.168.43.232/GarduationProjectV2/android/getgroupidjoinedgrouptime.php");
        getJSON("http://wellcare.atwebpages.com/android/getgroupidjoinedgrouptime.php");

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
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(GroupSchedule.this).create();
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

            String[] doctorids = new String[jsonArray.length()];

            String[] groupnames = new String[jsonArray.length()];


            String[] groupids = new String[jsonArray.length()];

            String[] days = new String[jsonArray.length()];

            String[] begintimes = new String[jsonArray.length()];

            String[] endtimes = new String[jsonArray.length()];


            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array
                doctorids[i] = obj.getString("Doctor_ID");

                groupnames[i] = obj.getString("Group_name");

                groupids[i] = obj.getString("ID");

                days[i] = obj.getString("day");

                begintimes[i] = obj.getString("begin");

                endtimes[i] = obj.getString("end");


            }
            for (int i = 0; i < jsonArray.length(); i++) {

                GroupScheduleDetails GSD = new GroupScheduleDetails();

                GSD.setGroupid(groupids[i]);
                GSD.setGroupname(groupnames[i]);
                GSD.setDay(days[i]);
                GSD.setBegin(begintimes[i]);
                GSD.setEnd(endtimes[i]);


                groupschedule.add(GSD);


            }
            GSA = new GroupScheduleAdapter(GroupSchedule.this, R.layout.groupscheduleitem, groupschedule);
            groupschedulelist.setAdapter(GSA);

            GSA.notifyDataSetChanged();


        }


    }
    public  class GroupScheduleAdapter extends ArrayAdapter<GroupScheduleDetails> {
        Activity context;
        int layoutresource;
        ArrayList<GroupScheduleDetails> mData= new ArrayList<GroupScheduleDetails>();

        public GroupScheduleAdapter(Activity activity, int resource, ArrayList<GroupScheduleDetails> data) {
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
        public GroupScheduleDetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable GroupScheduleDetails item) {
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
            GroupSchedule.GroupScheduleAdapter.ViewHolder holder = null;

            if(row == null || row.getTag()==null){
                LayoutInflater inflater =LayoutInflater.from(context);
                row = inflater.inflate(layoutresource,null);
                holder= new GroupSchedule.GroupScheduleAdapter.ViewHolder();
                holder.mgroupname=(TextView)row.findViewById(R.id.groupname);
                holder.day=(TextView) row.findViewById(R.id.doctorname);
                holder.time=(TextView) row.findViewById(R.id.time);

                row.setTag(holder);
            }

            else
            {
                holder=(GroupSchedule.GroupScheduleAdapter.ViewHolder)row.getTag();
            }
            holder.GSD=getItem(position);
            holder.mgroupname.setText(holder.GSD.getGroupname());
            holder.day.setText(holder.GSD.getDay());
            holder.time.setText("From "+holder.GSD.getBegin()+" to "+holder.GSD.getEnd());


            final GroupSchedule.GroupScheduleAdapter.ViewHolder finalHolder = holder;


            return row;
        }


        class ViewHolder {
            GroupScheduleDetails GSD;
            TextView mgroupname;
            TextView day;
            TextView time;


        }
    }


}
