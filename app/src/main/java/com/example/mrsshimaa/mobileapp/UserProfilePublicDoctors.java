package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class UserProfilePublicDoctors extends AppCompatActivity {

    PopupWindow pw;
    ListView groupslist;
    TextView username;
    TextView age;
    TextView email;
    ImageView pp;
    ArrayList <groupenrolledindetails>groupsenrolledin;
    GroupsEnrolledInAdapter GEIA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_public_doctors);
        username = (TextView)findViewById(R.id.username);
        age = (TextView) findViewById(R.id.agetext);
        email = (TextView) findViewById(R.id.emailtext);
        pp = (ImageView) findViewById(R.id.profilepic);
        groupsenrolledin= new ArrayList();
        String id="";
        Bundle extras = getIntent().getExtras();

            if (extras != null) {
                username.setText(extras.getString("Username"));
                age.setText(extras.getString("Age"));
                email.setText(extras.getString("Email"));

                if(extras.getString("Sex").compareTo("female")==0|| extras.getString("Sex").compareTo("F")==0)
                {
                    pp.setImageResource(R.drawable.userfemaleblue);

                }

                 id = extras.getString("National_id");

            }


        getImage(id);
    }



    private void getImage(final String ID) {
        class GetImage extends AsyncTask<String,Void,Bitmap>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(UserProfilePrivate.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                // loading.dismiss();

//                Log.i(b.toString(),"img:");
                pp.setImageBitmap(b);
                // pp.setImageDrawable(b);
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                Log.i((ID),"img:");

                String add = "http://wellcare.atwebpages.com/android/getimage.php?National_id="+ID;
                //String add = "http://wellcare.atwebpages.com/android/uploads/"+ID+".png";

                URL url = null;
                Bitmap image = null;
                Drawable imagee=null;
                try {




                    url = new URL(add);
                    // byte[] byteArray = IOUtils.toByteArray(url.openConnection().getInputStream());


                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Log.i(url.openConnection().getInputStream().toString(),"img:");


                    // Log.i(Integer.toString(byteArray.length),"img:");
                    // image = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);





                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Log.i(image.toString(),"img:");

                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(ID);
    }
    public  void showEnrolledGroups(View view)
    {

        LayoutInflater inflater = (LayoutInflater) UserProfilePublicDoctors.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popupgroupsenrolledin, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout,600, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        groupslist= (ListView)layout.findViewById(R.id.groupslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        //getJSON("http://192.168.43.232/GarduationProjectV2/android/getdatadoctorjoinedgroupchat.php");
        getJSON("http://wellcare.atwebpages.com/android/getdatadoctorjoinedgroupchat.php");

    }
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {

            pw.dismiss();



        }
    };


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

        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(UserProfilePublicDoctors.this).create();
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


            String[] firstnames = new String[jsonArray.length()];

            String[] lastnames = new String[jsonArray.length()];

            String[] doctorids = new String[jsonArray.length()];

            String[] groupnames = new String[jsonArray.length()];

            String[] usernames = new String[jsonArray.length()];


            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array

                firstnames[i] = obj.getString("First_name");

                lastnames[i] = obj.getString("Last_name");

                doctorids[i] = obj.getString("Doctor_ID");

                groupnames[i] = obj.getString("Group_name");

                usernames[i] = obj.getString("Username");

            }

            for (int i = 0; i < jsonArray.length(); i++) {
                if (username.getText().toString().compareTo(usernames[i]) == 0) {
                    groupenrolledindetails GIID = new groupenrolledindetails();

                    GIID.setGroupname(groupnames[i]);
                    GIID.setDoctorname(firstnames[i] + " " + lastnames[i]);


                    groupsenrolledin.add(GIID);

                }
            }

            GEIA = new UserProfilePublicDoctors.GroupsEnrolledInAdapter(UserProfilePublicDoctors.this, R.layout.groupenrolledinitem, groupsenrolledin);
            groupslist.setAdapter(GEIA);

            GEIA.notifyDataSetChanged();


        }

    }
    public  class GroupsEnrolledInAdapter extends ArrayAdapter<groupenrolledindetails> {
        Activity context;
        int layoutresource;
        ArrayList<groupenrolledindetails> mData = new ArrayList<groupenrolledindetails>();

        public GroupsEnrolledInAdapter(Activity activity, int resource, ArrayList<groupenrolledindetails> data) {
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
        public groupenrolledindetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable groupenrolledindetails item) {
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
            UserProfilePublicDoctors.GroupsEnrolledInAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new UserProfilePublicDoctors.GroupsEnrolledInAdapter.ViewHolder();
                holder.groupname = (TextView) row.findViewById(R.id.groupname);
                holder.doctorname = (TextView) row.findViewById(R.id.doctorname);

                row.setTag(holder);
            } else {
                holder = (UserProfilePublicDoctors.GroupsEnrolledInAdapter.ViewHolder) row.getTag();
            }
            holder.GIID = getItem(position);
            holder.groupname.setText(holder.GIID.getGroupname());
            holder.doctorname.setText(holder.GIID.getDoctorname());


            final UserProfilePublicDoctors.GroupsEnrolledInAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder2 = holder;

            return row;
        }


        class ViewHolder {
            groupenrolledindetails GIID;
            TextView groupname;
            TextView doctorname;


        }
    }



}
