package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.gcm.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.mrsshimaa.mobileapp.R.id.listView;

public class ChattingPageUser extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,EmojiconsFragment.OnEmojiconBackspaceClickedListener  {

    TextView name;
    EditText message;
    ImageButton send;
    TextView chatconv;
    private DatabaseReference root;
    private String temp_key;

    String doctorname;
    String Username;
    String Gender;
    String Age;
    String Email;
    String Nationalid;
    String Bloodtype;
    String Phonenumber;
    String Lastdonationdate;
    String DoctorID;
    String GroupID;
    DatabaseReference groupref;
    DatabaseReference message_root;
    private FirebaseListAdapter<ChatMessage> adapter;
    ListView listOfMessages ;
    ArrayList<ChatMessage>messages;
    firebaseadapter FBA;
    String intentname;
    EmojiconEditText mEditEmojicon;
    PopupWindow pw;
    ListView patientslist;
    MembersGroupTherapyadapter MGTA;
    ArrayList<PatientsGroupTherapyDetails> Members;
    private String URL_ADD_DOCTOR_PATIENT="http://wellcare.atwebpages.com/android/adddoctorpatient.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_page_user);
        name = (TextView)findViewById(R.id.nametext);
        send = (ImageButton) findViewById(R.id.sendbtn);
        root=FirebaseDatabase.getInstance().getReference();
        listOfMessages= (ListView)findViewById(R.id.allmesaages);
        messages=new ArrayList<>();
        mEditEmojicon = (EmojiconEditText) findViewById(R.id.editemojicon);
        Members=new ArrayList<>();
        setEmojiconFragment(false);

        Bundle extras = getIntent().getExtras();

        intentname=GroupTherapyListUser.getSender();

        if(intentname .compareTo("Doctors")==0) {
            if (extras != null) {
                name.setText(extras.getString("doctorname"));
                DoctorID = extras.getString("doctorid");
                Username = extras.getString("Username");
                Gender = extras.getString("Sex");
                Age = extras.getString("Age");
                Email = extras.getString("Email");
                Nationalid = extras.getString("National_id");
                Bloodtype = extras.getString("Blood_type");
                Phonenumber = extras.getString("phonenumber");
                Lastdonationdate = extras.getString("lastdonationdate");

            }

            groupref = FirebaseDatabase.getInstance().getReference("test").child(Username + DoctorID);




        }
        else if(intentname .compareTo("Groups")==0)
        {


            if (extras != null) {
                name.setText(extras.getString("Group_name"));
                DoctorID = extras.getString("Doctor_ID");
                Username = extras.getString("Username");
                GroupID=extras.getString("ID");
            }



            groupref = FirebaseDatabase.getInstance().getReference("test").child(GroupID);
        }


        groupref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setEmojiconFragment(boolean useSystemDefault) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }
    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mEditEmojicon, emojicon);

    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mEditEmojicon);

    }

    public  void send(View view)
    {

        new AddNewEntry().execute(Username,DoctorID);

        EditText input = (EditText)findViewById(R.id.editemojicon);



        Map<String,Object> map = new HashMap<String, Object>();
        temp_key = groupref.push().getKey();
        groupref.updateChildren(map);

         message_root = groupref.child(temp_key);
        Map<String,Object> map2 = new HashMap<String, Object>();
        ChatMessage CM = new ChatMessage(input.getText().toString(),Username);
        map2.put("messageText",CM.getMessageText());
       // map2.put("messageTime",CM.getMessageTime());
        map2.put("messageUser",CM.getMessageUser());

        message_root.updateChildren(map2);





        input.setText("");

    }
    public  void showMembers(View view)
    {
        if(intentname.compareTo("Groups")==0) {
          //  getJSONallmembers("http://192.168.43.232/GarduationProjectV2/android/getdatagroupchat.php");
            getJSONallmembers("http://wellcare.atwebpages.com/android/getdatagroupchat.php");

            LayoutInflater inflater = (LayoutInflater) ChattingPageUser.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.showmemberspopup, (ViewGroup) findViewById(R.id.popup_element));
            pw = new PopupWindow(layout, 600, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pw.showAtLocation(view, Gravity.CENTER, 0, 0);
            pw.setBackgroundDrawable(new BitmapDrawable());

            patientslist = (ListView) layout.findViewById(R.id.patientslist);
            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);
        }
        if(intentname.compareTo("Doctors")==0)
        {
           // getJSONalldoctors("http://192.168.43.232/GarduationProjectV2/android/getdatadoctorslist.php");
            getJSONalldoctors("http://wellcare.atwebpages.com/android/getdatadoctorslist.php");

        }

    }
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {

            pw.dismiss();



        }
    };
    private String chat_msg,chat_user_name;
    private  Long chat_time;
    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            //chat_time = (Long) ((DataSnapshot)i.next()).getValue();
            chat_user_name= (String) ((DataSnapshot)i.next()).getValue();

            ChatMessage CM= new ChatMessage(chat_msg,chat_user_name);
            messages.add(CM);


        }
        listOfMessages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);


        FBA = new firebaseadapter(ChattingPageUser.this, R.layout.messageitemuser, messages);
        listOfMessages.setAdapter(FBA);
        FBA.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listOfMessages.setSelection(FBA.getCount() - 1);
            }
        });
        FBA.notifyDataSetChanged();
    }


    public  class firebaseadapter extends ArrayAdapter<ChatMessage> {
        Activity context;
        int layoutresource;
        ChatMessage CM;
        ArrayList<ChatMessage> mData= new ArrayList<ChatMessage>();

        public firebaseadapter(Activity activity, int resource, ArrayList<ChatMessage> data) {
            super(activity,resource,data);
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
        public ChatMessage getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable ChatMessage item) {
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
            ChattingPageUser.firebaseadapter.ViewHolder holder = null;

            if(row == null || row.getTag()==null){
                LayoutInflater inflater =LayoutInflater.from(context);
                row = inflater.inflate(layoutresource,null);
                holder= new ChattingPageUser.firebaseadapter.ViewHolder();
                holder.mmessgae=(TextView)row.findViewById(R.id.message_text);
               // holder.mtime=(TextView)row.findViewById(R.id.message_time);
                holder.muser=(TextView)row.findViewById(R.id.message_user);
                row.setTag(holder);
            }

            else
            {
                holder=(ChattingPageUser.firebaseadapter.ViewHolder)row.getTag();
            }
            holder.CM=getItem(position);
            holder.mmessgae.setText(holder.CM.getMessageText());
           // holder.mtime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", holder.CM.getMessageTime()));
            holder.muser.setText(holder.CM.getMessageUser());


            final ChattingPageUser.firebaseadapter.ViewHolder finalHolder = holder;

            return row;
        }


        class ViewHolder {
            ChatMessage CM;
            TextView mmessgae;
            TextView mtime;
            TextView muser;


        }
    }
    private void getJSONallmembers(final String urlWebService) {
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

                    retrievedataallmembers(s);


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
                        Log.i(json, "hi:");

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
    private void retrievedataallmembers(String json) throws JSONException {


        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(ChattingPageUser.this).create();
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
            Members.clear();
            JSONArray jsonArray = new JSONArray(json);


            String[] doctorids = new String[jsonArray.length()];

            String[] groupnames = new String[jsonArray.length()];

            String[] usernames = new String[jsonArray.length()];


            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array


                doctorids[i] = obj.getString("Doctor_ID");
                groupnames[i] = obj.getString("Group_name");
                usernames[i] = obj.getString("Username");

            }


            for (int i = 0; i < jsonArray.length(); i++) {

                if (doctorids[i].compareTo(DoctorID) == 0) {


                    if (groupnames[i].compareTo(name.getText().toString()) == 0) {
                        PatientsGroupTherapyDetails PGTD = new PatientsGroupTherapyDetails();

                        PGTD.setPatientname(usernames[i]);


                        Members.add(PGTD);
                    }
                }


            }


            MGTA = new ChattingPageUser.MembersGroupTherapyadapter(ChattingPageUser.this, R.layout.patientslistitemgrouptherapy, Members);
            patientslist.setAdapter(MGTA);

            MGTA.notifyDataSetChanged();
        }
    }
    public  class MembersGroupTherapyadapter extends ArrayAdapter<PatientsGroupTherapyDetails> {
        Activity context;
        int layoutresource;
        PatientsGroupTherapyDetails PGTD;
        ArrayList<PatientsGroupTherapyDetails> mData = new ArrayList<PatientsGroupTherapyDetails>();

        public MembersGroupTherapyadapter(Activity activity, int resource, ArrayList<PatientsGroupTherapyDetails> data) {
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
            ChattingPageUser.MembersGroupTherapyadapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new ChattingPageUser.MembersGroupTherapyadapter.ViewHolder();
                holder.mpatientname = (TextView) row.findViewById(R.id.patientname);

                row.setTag(holder);
            } else {
                holder = (ChattingPageUser.MembersGroupTherapyadapter.ViewHolder) row.getTag();
            }
            holder.PGTD = getItem(position);
            holder.mpatientname.setText(holder.PGTD.getPatientname());




            return row;
        }


        class ViewHolder {
            PatientsGroupTherapyDetails PGTD;
            TextView mpatientname;


        }
    }
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
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(ChattingPageUser.this).create();
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

            for (int i = 0; i < jsonArray.length(); i++) {
                if ((firstnames[i] + " " + lastnames[i]).compareTo(name.getText().toString()) == 0) {
                    Intent intent = new Intent(ChattingPageUser.this, DoctorProfilePublicUsers.class);
                    intent.putExtra("Email", emails[i]);
                    intent.putExtra("First_name", firstnames[i]);
                    intent.putExtra("Last_name", lastnames[i]);
                    intent.putExtra("Phonenumber", phonenumbers[i]);
                    intent.putExtra("Position", positions[i]);
                    intent.putExtra("ID",ids[i]);

                    startActivity(intent);
                    break;
                }
            }
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


            String P = arg[0];
            String DID = arg[1];




            List<NameValuePair> params = new ArrayList<NameValuePair>();



            params.add(new BasicNameValuePair("Patient", P));
            params.add(new BasicNameValuePair("Doctor_ID", DID));


            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_ADD_DOCTOR_PATIENT,
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
