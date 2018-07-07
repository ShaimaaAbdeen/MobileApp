package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class HospitalListBlood extends AppCompatActivity {
    ArrayList<HospitalBloodDetails> hospitalitems;
    ArrayList<HospitalBloodDetails> selectedhospitalitems;
    ArrayList<HospitalBloodDetails> similarhospitalitems;
    hospitalbloodadapter hba;
    ListView Hospitalslist;
    String Username;
    String Gender;
    String Age;
    String Email;
    String Nationalid;
    String Bloodtype;
    String Phonenumber;
    String Lastdonationdate;
    int notnumber;
    int messagecount;
    SharedPreferences sp;
    TextView notnumbertext;
    TextView messagecounttext;
    PopupWindow pw;
    ListView notificationslist;
    String URL_UPDATE_SEEN="http://wellcare.atwebpages.com/android/updateseen.php";

    ImageView profileicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list_blood);
        sp = getSharedPreferences("login",MODE_PRIVATE);

        notnumbertext = (TextView) findViewById(R.id.notnumber);
        messagecounttext=(TextView) findViewById(R.id.messagenumber);
        //getJSON("http://192.168.43.232/GarduationProjectV2/android/gethospitaldatabloodandroid.php");
        getJSON("http://wellcare.atwebpages.com/android/gethospitaldatabloodandroid.php");

        hospitalitems = new ArrayList<HospitalBloodDetails>();
        selectedhospitalitems = new ArrayList<HospitalBloodDetails>();
        similarhospitalitems = new ArrayList<HospitalBloodDetails>();
        profileicon=(ImageView) findViewById(R.id.profileicon);

        Hospitalslist = (ListView) findViewById(R.id.hospitallist);



        Hospitalslist.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {










            }


        });



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

    public  void showNotifications(View view)
    {
        LayoutInflater inflater = (LayoutInflater) HospitalListBlood.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notificationspopup, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        notificationslist= (ListView)layout.findViewById(R.id.notificationslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        NotificationsAdapter NA = new HospitalListBlood.NotificationsAdapter(HospitalListBlood.this, R.layout.notificationitem, HomeMod.notificationitems);

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
            HospitalListBlood.NotificationsAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new HospitalListBlood.NotificationsAdapter.ViewHolder();
                holder.mhospitalname = (TextView) row.findViewById(R.id.hospitalname);
                holder.mbloodtype = (TextView) row.findViewById(R.id.bloodtype);

                row.setTag(holder);
            } else {
                holder = (HospitalListBlood.NotificationsAdapter.ViewHolder) row.getTag();
            }
            holder.ND = getItem(position);
            holder.mhospitalname.setText(holder.ND.getHospitalname()+ " Urgently Needs Your Help");
            holder.mbloodtype.setText(holder.ND.getBloodType()+" Needed, Your blood type is " + Bloodtype+" so you can donate");


            final HospitalListBlood.NotificationsAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            holder.mhospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(HospitalListBlood.this,HospitalNotificationProfile.class);

                    intent.putExtra("name",finalHolder.ND.getHospitalname());
                    intent.putExtra("address",finalHolder.ND.getAddress());
                    intent.putExtra("contactnumber",finalHolder.ND.getContactNumber());
                    intent.putExtra("email",finalHolder.ND.getEmail());



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
        LayoutInflater inflater = (LayoutInflater) HospitalListBlood.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notificationspopup, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        notificationslist= (ListView)layout.findViewById(R.id.notificationslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        MessagesAdapter MA = new HospitalListBlood.MessagesAdapter(HospitalListBlood.this, R.layout.messagefileitem, HomeMod.messageitems);
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
            HospitalListBlood.MessagesAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new HospitalListBlood.MessagesAdapter.ViewHolder();
                holder.mhospitalname = (TextView) row.findViewById(R.id.hospitalname);

                row.setTag(holder);
            } else {
                holder = (HospitalListBlood.MessagesAdapter.ViewHolder) row.getTag();
            }
            holder.MD = getItem(position);
            holder.mhospitalname.setText(holder.MD.getHospitalname()+ " Sent You a File");


            final HospitalListBlood.MessagesAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            final ViewHolder finalHolder2 = holder;
            holder.mhospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Intent i = new Intent(HospitalListBlood.this,FileContent.class);
                    i.putExtra("File", finalHolder2.MD.getFile());
                    new AddNewEntry().execute("1",finalHolder2.MD.getID());

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
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };
    public  void Gotoprofile(View view )
    {
        Intent intent = new Intent(HospitalListBlood.this, UserProfilePrivate.class);
        intent.putExtra("Username",Username);
        intent.putExtra("National_id",Nationalid);
        intent.putExtra("Email",Email);
        intent.putExtra("Age", Age);
        intent.putExtra("Blood_type", Bloodtype);
        intent.putExtra("Sex", Gender);
        intent.putExtra("lastdonationdate",Lastdonationdate);
        intent.putExtra("phonenumber",Phonenumber);

        HospitalListBlood.this.finish();
        startActivity(intent);
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
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(HospitalListBlood.this).create();
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

            boolean count;
            int trace = 1;
            int index = 0;

            for (int i = 0; i < hospitalitems.size(); i += trace) {
                trace = 1;
                count = false;

                for (int j = i + 1; j < hospitalitems.size(); j++) {
                    if (hospitalitems.get(i).getName().toString().compareTo(hospitalitems.get(j).getName().toString()) == 0) {

                        hospitalitems.get(i).setAvailableBloodTypes(hospitalitems.get(i).getAvailableBloodTypes().toString() + "," + hospitalitems.get(j).getAvailableBloodTypes().toString());
                        hospitalitems.get(i).setNeededBloodTypes(hospitalitems.get(i).getNeededBloodTypes().toString() + "," + hospitalitems.get(j).getNeededBloodTypes().toString());
                        count = true;
                        trace++;


                        selectedhospitalitems.add(index, hospitalitems.get(i));


                    }


                }


                index++;

                if (count == false) {

                    selectedhospitalitems.add(hospitalitems.get(i));


                }
            }
            ArrayList<HospitalBloodDetails> unique = removeDuplicates(selectedhospitalitems);


            for (int i = 0; i < selectedhospitalitems.size(); i++) {
                String uniqueavailable = removeduplicatesstring(selectedhospitalitems.get(i).getAvailableBloodTypes());
                String uniqueneeded = removeduplicatesstring(selectedhospitalitems.get(i).getNeededBloodTypes());

                selectedhospitalitems.get(i).setAvailableBloodTypes(uniqueavailable);
                selectedhospitalitems.get(i).setNeededBloodTypes(uniqueneeded);

            }


            hba = new hospitalbloodadapter(HospitalListBlood.this, R.layout.hospitalistitemblood, unique);
            Hospitalslist.setAdapter(hba);

            hba.notifyDataSetChanged();
        }
    }

    static ArrayList<HospitalBloodDetails> removeDuplicates(ArrayList<HospitalBloodDetails> list) {

        // Store unique items in result.
        ArrayList<HospitalBloodDetails> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<HospitalBloodDetails> set = new HashSet<>();

        // Loop over argument list.
        for (HospitalBloodDetails item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);

                set.add(item);
            }
        }
        return result;
    }


    public String removeduplicatesstring(String string)
    {



        //String [] s=string.split(",");

        //char[] chars = string.toCharArray();
        //Set<String> charSet = new LinkedHashSet<String>();
        HashSet<String> test=new HashSet<String>(Arrays.asList(string.split(",")));



        StringBuilder builder = new StringBuilder();
        int i=test.size();
        for(String s :  test.toArray(new String[test.size()])) {
            i--;
            builder.append(s);
            if(i!=0) {
                builder.append(", ");
            }

        }
        String str = builder.toString();
        /*for (String x : test) {
            test.add(x);
        }

        StringBuilder sb = new StringBuilder();
        for (String character : test) {
            sb.append(character);
        }
        return  sb.toString();*/

        return str;

    }


    public  class hospitalbloodadapter extends ArrayAdapter<HospitalBloodDetails>
    {
        Activity context;
        int layoutresource;
        HospitalBloodDetails hd;
        ArrayList<HospitalBloodDetails> mData= new ArrayList<HospitalBloodDetails>();

        public hospitalbloodadapter( Activity activity,  int resource,ArrayList<HospitalBloodDetails> data) {
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
        public HospitalBloodDetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable HospitalBloodDetails item) {
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
            ViewHolder holder = null;

            if(row == null || row.getTag()==null){
                LayoutInflater inflater =LayoutInflater.from(context);
                row = inflater.inflate(layoutresource,null);
                holder= new ViewHolder();
                holder.mname=(TextView)row.findViewById(R.id.hospitalname);
                holder.mavailableblood=(TextView)row.findViewById(R.id.availablebloodtext);
                holder.mneededblood=(TextView)row.findViewById(R.id.neededbloodtext);

                row.setTag(holder);
            }

            else
            {
                holder=(ViewHolder)row.getTag();
            }
            holder.hd=getItem(position);
            holder.mname.setText(holder.hd.getName());
            if(holder.hd.getAvailableBloodTypes().compareTo("null")==0)
            {
                holder.mavailableblood.setText("No Available blood");

            }else {
                holder.mavailableblood.setText(holder.hd.getAvailableBloodTypes());
            }
            if(holder.hd.getNeededBloodTypes().equals("null"))
            {
                holder.mneededblood.setText("No Needed blood");

            }else {
                holder.mneededblood.setText(holder.hd.getNeededBloodTypes());
            }


            final ViewHolder finalHolder = holder;
           holder.mname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {






                    Intent intent = new Intent(HospitalListBlood.this,HospitalProfileBlood.class);

                    intent.putExtra("name",finalHolder.hd.getName().toString());
                    intent.putExtra("address",finalHolder.hd.getAddress().toString());
                    intent.putExtra("contactnumber",finalHolder.hd.getContactNumber().toString());
                    intent.putExtra("email",finalHolder.hd.getEmail().toString());
                    intent.putExtra("availablebloodtypes",finalHolder.hd.getAvailableBloodTypes().toString());
                    intent.putExtra("neededbloodtypes",finalHolder.hd.getNeededBloodTypes().toString());


                    startActivity(intent);

                }
            });

            return row;
        }

        class ViewHolder {
            HospitalBloodDetails hd;
            TextView mname;
            TextView mavailableblood;
            TextView mneededblood;


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
                Intent intent = new Intent(HospitalListBlood.this, FirstPage.class);
                startActivity(intent);
                FirstPage.sp.edit().putBoolean("logged",false).apply();
                HospitalListBlood.this.finish();


                return true;
            case R.id.home:
                intent = new Intent(HospitalListBlood.this, HomeMod.class);
                intent.putExtra("Username",Username);
                intent.putExtra("Sex",Gender);
                intent.putExtra("Age",Age);
                intent.putExtra("Email",Email);
                intent.putExtra("National_id",Nationalid);
                intent.putExtra("Blood_type",Bloodtype);
                intent.putExtra("phonenumber",Phonenumber);
                intent.putExtra("lastdonationdate",Lastdonationdate);
                startActivity(intent);


                return true;
            default:
                return super.onOptionsItemSelected(item);
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
