package com.example.mrsshimaa.mobileapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

import static com.example.mrsshimaa.mobileapp.R.id.destination;
import static com.example.mrsshimaa.mobileapp.R.id.edit_query;

public class HospitalListIntensive extends AppCompatActivity implements DirectionFinderListener {
    ArrayList<HospitalIntensiveDetails> hospitalitems;
    ArrayList<HospitalIntensiveDetails> selectedhospitalitems;
    hospitalintensiveadapter hba;
    ListView Hospitalslist;
    Spinner spinner;
    ArrayList<String> durationslist;
    ArrayList<String> distanceslist;
    ArrayList<String> destinationslist;
    ArrayList<HospitalIntensiveDetails> nearesthospitals;
    ProgressDialog processdialog;
    public Button getnearestbtn;
    Double min = 0.0;
    int minindex = 0;
    String Username;
    String Gender;
    String Age;
    String Email;
    String Nationalid;
    String Bloodtype;
    String Phonenumber;
    String Lastdonationdate;
    PopupWindow pw;
    ListView notificationslist;
    TextView notnumbertext;
    TextView messagecounttext;
    int notnumber;
    int messagecount;
    String URL_UPDATE_SEEN = "http://wellcare.atwebpages.com/android/updateseen.php";
    ImageView profileicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list_intensive);

        notnumbertext = (TextView) findViewById(R.id.notnumber);
        messagecounttext = (TextView) findViewById(R.id.messagenumber);
        profileicon=(ImageView) findViewById(R.id.profileicon);

        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            Username = extras.getString("Username");
            Gender = extras.getString("Sex");
            Age = extras.getString("Age");
            Email = extras.getString("Email");
            Nationalid = extras.getString("National_id");
            Bloodtype = extras.getString("Blood_type");
            Phonenumber = extras.getString("phonenumber");
            Lastdonationdate = extras.getString("lastdonationdate");
            notnumber = Integer.parseInt(extras.getString("notnumber"));
            messagecount = Integer.parseInt(extras.getString("messagenumber"));

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



        nearesthospitals = new ArrayList<>();
        getnearestbtn = (Button) (findViewById(R.id.getnearestbtn));
        getnearestbtn.setEnabled(true);

        getnearestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getnearestbtn.setEnabled(false);
                processdialog = ProgressDialog.show(HospitalListIntensive.this, "Please wait.",
                        "Searching..!", true);

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(HospitalListIntensive.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HospitalListIntensive.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                String originloc = "";

                // = etOrigin.getText().toString();
                Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geo.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.isEmpty()) {
                    originloc = "Waiting for Location";
                } else {
                    if (addresses.size() > 0) {
                        originloc = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                    }
                }

                for (int i = 0; i < selectedhospitalitems.size(); i++) {
                    destinationslist.add(selectedhospitalitems.get(i).getName());
                }
                for (int j = 0; j < destinationslist.size(); j++) {
                    try {
                          Log.i( destinationslist.get(j).toString(),"xx:");
                        new DirectionFinder(HospitalListIntensive.this, originloc, destinationslist.get(j)).execute();


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }




            }
        });
        durationslist = new ArrayList<>();
        distanceslist = new ArrayList<>();
        destinationslist = new ArrayList<>();
        List IntensiveTypes = new ArrayList<String>();

        IntensiveTypes.add("General/Surgical ICU");
        IntensiveTypes.add("Neonatal ICU");
        IntensiveTypes.add("Pediatric ICU");
        IntensiveTypes.add("Psychiatric ICU");
        IntensiveTypes.add("Coronary Care Unit");
        IntensiveTypes.add("Neurological ICU");
        IntensiveTypes.add("Post-anesthesia Care Unit");

        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<Integer>(
                this, android.R.layout.simple_spinner_item, IntensiveTypes);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);

        spinner = (Spinner) findViewById(R.id.spinnertypes);
        spinner.setAdapter(spinnerArrayAdapter);


        hospitalitems = new ArrayList<HospitalIntensiveDetails>();

        selectedhospitalitems = new ArrayList<HospitalIntensiveDetails>();

        Hospitalslist = (ListView) findViewById(R.id.hospitalslist);


       // getJSON("http://192.168.43.232/GarduationProjectV2/android/gethospitaldataintensiveandroid.php");
        getJSON("http://wellcare.atwebpages.com/android/gethospitaldataintensiveandroid.php");

    }




    public void Gotoprofile(View view) {
        Intent intent = new Intent(HospitalListIntensive.this, UserProfilePrivate.class);
        intent.putExtra("Username", Username);
        intent.putExtra("National_id", Nationalid);
        intent.putExtra("Email", Email);
        intent.putExtra("Age", Age);
        intent.putExtra("Blood_type", Bloodtype);
        intent.putExtra("Sex", Gender);
        intent.putExtra("lastdonationdate", Lastdonationdate);
        intent.putExtra("phonenumber", Phonenumber);

        startActivity(intent);
        HospitalListIntensive.this.finish();
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

    private void retrievedata(String json) throws JSONException {

        if(json==null) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(HospitalListIntensive.this).create();
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
            JSONArray jsonArray = new JSONArray(json);

            //creating a string array for listview
            String[] HospitalNames = new String[jsonArray.length()];

            String[] HospitalAddresses = new String[jsonArray.length()];

            String[] HospitalEmails = new String[jsonArray.length()];

            String[] HospitalContactNumbers = new String[jsonArray.length()];

            String[] HospitalLocations = new String[jsonArray.length()];

            int[] RowNumbers = new int[jsonArray.length()];

            int[] Rooms = new int[jsonArray.length()];

            String[] RoomTypes = new String[jsonArray.length()];

            //looping through all the elements in json array
            for (int i = 0; i < jsonArray.length(); i++) {

                //getting json object from the json array
                JSONObject obj = jsonArray.getJSONObject(i);

                //getting the name from the json object and putting it inside string array


                HospitalNames[i] = obj.getString("Hospital_name");
                HospitalAddresses[i] = obj.getString("Address");
                HospitalEmails[i] = obj.getString("Email");
                HospitalContactNumbers[i] = obj.getString("Contact_number");
                Rooms[i] = obj.getInt("Rooms");
                RoomTypes[i] = obj.getString("Type");

            }

            for (int i = 0; i < jsonArray.length(); i++) {

                HospitalIntensiveDetails hd = new HospitalIntensiveDetails();

                hd.setName(HospitalNames[i].toString());
                hd.setAddress(HospitalAddresses[i].toString());
                hd.setEmail(HospitalEmails[i].toString());
                hd.setContactNumber(HospitalContactNumbers[i].toString());

                hd.setRooms(Rooms[i]);
                hd.setRoomType(RoomTypes[i].toString());


                hospitalitems.add(hd);


            }

            int trace;
            int index = 0;

            for (int i = 0; i < hospitalitems.size(); i++) {
                Log.i(hospitalitems.get(i).getRoomType().toString(), "hi:");
            }
            for (int i = 0; i < hospitalitems.size(); i += trace) {
                ArrayList<String> RT = new ArrayList<String>();
                ArrayList<Integer> RN = new ArrayList<Integer>();


                trace = 1;
                int count = 0;

                for (int j = i + 1; j < hospitalitems.size(); j++) {
                    if (hospitalitems.get(i).getName().toString().compareTo(hospitalitems.get(j).getName().toString()) == 0) {

                        count++;


                        if (count > 1) {
                            selectedhospitalitems.get(index - 1).getRoomTypes().add(hospitalitems.get(j).getRoomType());
                            selectedhospitalitems.get(index - 1).getRoomnumbers().add(hospitalitems.get(j).getRooms());
                        } else {

                            RT.add(hospitalitems.get(i).getRoomType());
                            RT.add(hospitalitems.get(j).getRoomType());
                            RN.add(hospitalitems.get(i).getRooms());
                            RN.add(hospitalitems.get(j).getRooms());


                            hospitalitems.get(i).setRoomTypes(RT);
                            hospitalitems.get(i).setRoomnumbers(RN);
                            selectedhospitalitems.add(index, hospitalitems.get(i));

                            Log.i(Integer.toString(index), "index:");

                            index++;


                        }


                        trace++;


                    } else {

                        break;
                    }


                    Log.i(Integer.toString(i), "Trace:");


                }


                if (trace == 1) {

                    RT.add(hospitalitems.get(i).getRoomType());
                    RN.add(hospitalitems.get(i).getRooms());
                    hospitalitems.get(i).setRoomTypes(RT);
                    hospitalitems.get(i).setRoomnumbers(RN);
                    selectedhospitalitems.add(index, hospitalitems.get(i));

                    Log.i(Integer.toString(index), "index:");

                    index++;


                }


            }


            for (int i = 0; i < selectedhospitalitems.size(); i++) {
                Log.i(selectedhospitalitems.get(i).getName().toString(), "types:");


                for (int j = 0; j < selectedhospitalitems.get(i).getRoomTypes().size(); j++) {
                    Log.i(selectedhospitalitems.get(i).getRoomTypes().get(j).toString(), "types:");
                    Log.i(selectedhospitalitems.get(i).getRoomnumbers().get(j).toString(), "types:");

                }

            }


            hba = new hospitalintensiveadapter(HospitalListIntensive.this, R.layout.hospitallistitemintensive, selectedhospitalitems);
            Hospitalslist.setAdapter(hba);

            hba.notifyDataSetChanged();
        }
    }


    public class hospitalintensiveadapter extends ArrayAdapter<HospitalIntensiveDetails> {
        Activity context;
        int layoutresource;
        HospitalIntensiveDetails hd;
        ArrayList<HospitalIntensiveDetails> mData = new ArrayList<HospitalIntensiveDetails>();

        public hospitalintensiveadapter(Activity activity, int resource, ArrayList<HospitalIntensiveDetails> data) {
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
        public HospitalIntensiveDetails getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getPosition(@Nullable HospitalIntensiveDetails item) {
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
            HospitalListIntensive.hospitalintensiveadapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new HospitalListIntensive.hospitalintensiveadapter.ViewHolder();
                holder.mname = (TextView) row.findViewById(R.id.hospitalname);
                holder.mroomgeneral = (TextView) row.findViewById(R.id.generalroomsnumber);
                holder.mroomcoronary = (TextView) row.findViewById(R.id.coronaryroomsnumber);
                holder.mroomneonatal = (TextView) row.findViewById(R.id.neonatalroomsnumber);
                holder.mroomneurological = (TextView) row.findViewById(R.id.neurologicalroomsnumber);
                holder.mroompediatric = (TextView) row.findViewById(R.id.pediatricroomsnumber);
                holder.mroompostanesthesia = (TextView) row.findViewById(R.id.postanesthesiaroomsnumber);
                holder.mroompsychiatric = (TextView) row.findViewById(R.id.psychiatricroomsnumber);
               // holder.distance = (TextView) row.findViewById(R.id.dist);
               // holder.time = (TextView) row.findViewById(R.id.time);
              //  holder.distanceimage = (ImageView) row.findViewById(R.id.distimg);
              //  holder.timeimage = (ImageView) row.findViewById(R.id.clkimg);


                row.setTag(holder);
            } else {
                holder = (HospitalListIntensive.hospitalintensiveadapter.ViewHolder) row.getTag();
            }
            holder.hd = getItem(position);
            holder.mname.setText(holder.hd.getName());

            /*if( holder.hd.getTime().compareTo("")==0)
            {
                holder.time.setVisibility(View.INVISIBLE);
                holder.timeimage.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.time.setText(holder.hd.getTime());
                holder.time.setVisibility(View.VISIBLE);
                holder.timeimage.setVisibility(View.VISIBLE);
            }


            if(holder.hd.distance.compareTo("")==0)
            {
                holder.distance.setVisibility(View.INVISIBLE);
                holder.distanceimage.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.distance.setText(holder.hd.getDistance());
                holder.time.setVisibility(View.VISIBLE);
                holder.timeimage.setVisibility(View.VISIBLE);

            }*/
           // holder.time.setText(holder.hd.getTime());
            //holder.distance.setText(holder.hd.getDistance());

            Log.i(getItem(position).getName().toString(), "typeslist:");


            ((TextView) row.findViewById(R.id.generalroomsnumber)).setText("None");
            ((TextView) row.findViewById(R.id.generalroomsnumber)).setTextColor(Color.RED);

            ((TextView) row.findViewById(R.id.neonatalroomsnumber)).setText("None");
            ((TextView) row.findViewById(R.id.neonatalroomsnumber)).setTextColor(Color.RED);

            ((TextView) row.findViewById(R.id.neurologicalroomsnumber)).setText("None");
            ((TextView) row.findViewById(R.id.neurologicalroomsnumber)).setTextColor(Color.RED);

            ((TextView) row.findViewById(R.id.coronaryroomsnumber)).setText("None");
            ((TextView) row.findViewById(R.id.coronaryroomsnumber)).setTextColor(Color.RED);

            ((TextView) row.findViewById(R.id.psychiatricroomsnumber)).setText("None");
            ((TextView) row.findViewById(R.id.psychiatricroomsnumber)).setTextColor(Color.RED);

            ((TextView) row.findViewById(R.id.postanesthesiaroomsnumber)).setText("None");
            ((TextView) row.findViewById(R.id.postanesthesiaroomsnumber)).setTextColor(Color.RED);

            ((TextView) row.findViewById(R.id.pediatricroomsnumber)).setText("None");
            ((TextView) row.findViewById(R.id.pediatricroomsnumber)).setTextColor(Color.RED);


            for (holder.i = 0; holder.i < holder.hd.getRoomTypes().size(); holder.i++) {


                Log.i(getItem(position).getRoomTypes().get(holder.i).toString(), "typeslist:");
                Log.i(getItem(position).getRoomnumbers().get(holder.i).toString(), "numberslist:");


                if (holder.hd.getRoomTypes().get(holder.i).compareTo("General/Surgical ICU") == 0) {
                    holder.countgeneral++;

                    if (holder.countgeneral > 1) {

                        holder.X = Integer.parseInt(((TextView) row.findViewById(R.id.generalroomsnumber)).getText().toString());
                        holder.X += Integer.parseInt(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroomgeneral.setText(Integer.toString(holder.X));

                    } else {
                        holder.mroomgeneral.setText(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroomgeneral.setTextColor(Color.parseColor("#FF1B8F23"));
                    }

                } else if (holder.hd.getRoomTypes().get(holder.i).compareTo("Neonatal ICU") == 0) {
                    holder.countneonatal++;
                    if (holder.countneonatal > 1) {
                        holder.X = Integer.parseInt(((TextView) row.findViewById(R.id.neonatalroomsnumber)).getText().toString());
                        holder.X += Integer.parseInt(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroomneonatal.setText(Integer.toString(holder.X));


                    } else {
                        holder.mroomneonatal.setText(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroomneonatal.setTextColor(Color.parseColor("#FF1B8F23"));
                    }

                } else if (holder.hd.getRoomTypes().get(holder.i).compareTo("Pediatric ICU") == 0) {
                    holder.countpediatric++;
                    if (holder.countpediatric > 1) {
                        holder.X = Integer.parseInt(((TextView) row.findViewById(R.id.pediatricroomsnumber)).getText().toString());
                        holder.X += Integer.parseInt(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroompediatric.setText(Integer.toString(holder.X));

                    } else {
                        holder.mroompediatric.setText(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroompediatric.setTextColor(Color.parseColor("#FF1B8F23"));
                    }

                } else if (holder.hd.getRoomTypes().get(holder.i).compareTo("Coronary Care Unit") == 0) {
                    Log.i("yes", "hi:");
                    holder.countcoronary++;
                    if (holder.countcoronary > 1) {
                        holder.X = Integer.parseInt(((TextView) row.findViewById(R.id.coronaryroomsnumber)).getText().toString());
                        holder.X += Integer.parseInt(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroomcoronary.setText(Integer.toString(holder.X));

                    } else {
                        holder.mroomcoronary.setText(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroomcoronary.setTextColor(Color.parseColor("#FF1B8F23"));
                    }

                } else if (holder.hd.getRoomTypes().get(holder.i).compareTo("Neurological ICU") == 0) {
                    holder.countneurological++;
                    if (holder.countneurological > 1) {
                        holder.X = Integer.parseInt(((TextView) row.findViewById(R.id.neurologicalroomsnumber)).getText().toString());
                        holder.X += Integer.parseInt(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroomneurological.setText(Integer.toString(holder.X));

                    } else {
                        holder.mroomneurological.setText(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroomneurological.setTextColor(Color.parseColor("#FF1B8F23"));
                    }

                } else if (holder.hd.getRoomTypes().get(holder.i).compareTo("Post-anesthesia Care Unit") == 0) {


                    holder.countpostanesthesia++;
                    if (holder.countpostanesthesia > 1) {
                        holder.X = Integer.parseInt(((TextView) row.findViewById(R.id.postanesthesiaroomsnumber)).getText().toString());
                        holder.X += Integer.parseInt(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroompostanesthesia.setText(Integer.toString(holder.X));

                    } else {
                        holder.mroompostanesthesia.setText(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroompostanesthesia.setTextColor(Color.parseColor("#FF1B8F23"));
                    }

                } else if (holder.hd.getRoomTypes().get(holder.i).compareTo("Psychiatric ICU") == 0) {
                    holder.countpsychiatric++;
                    if (holder.countpsychiatric > 1) {
                        holder.X = Integer.parseInt(((TextView) row.findViewById(R.id.psychiatricroomsnumber)).getText().toString());
                        holder.X += Integer.parseInt(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroompsychiatric.setText(Integer.toString(holder.X));

                    } else {
                        holder.mroompsychiatric.setText(holder.hd.getRoomnumbers().get(holder.i).toString());
                        holder.mroompsychiatric.setTextColor(Color.parseColor("#FF1B8F23"));
                    }

                }


            }


            Log.i(holder.hd.getName().toString(), "counts:");

            Log.i(Integer.toString(holder.countgeneral), "counts:");
            Log.i(Integer.toString(holder.countcoronary), "counts:");
            Log.i(Integer.toString(holder.countneonatal), "counts:");
            Log.i(Integer.toString(holder.countneurological), "counts:");
            Log.i(Integer.toString(holder.countpediatric), "counts:");
            Log.i(Integer.toString(holder.countpsychiatric), "counts:");
            Log.i(Integer.toString(holder.countpostanesthesia), "counts:");


            holder.countgeneral = 0;
            holder.countcoronary = 0;
            holder.countneonatal = 0;
            holder.countneurological = 0;
            holder.countpediatric = 0;
            holder.countpostanesthesia = 0;
            holder.countpsychiatric = 0;
            holder.i = 0;


            final HospitalListIntensive.hospitalintensiveadapter.ViewHolder finalHolder = holder;
            holder.mname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(HospitalListIntensive.this, HospitalProfileIntensive.class);

                    intent.putExtra("name", finalHolder.hd.getName().toString());
                    intent.putExtra("address", finalHolder.hd.getAddress().toString());
                    intent.putExtra("contactnumber", finalHolder.hd.getContactNumber().toString());
                    intent.putExtra("email", finalHolder.hd.getEmail().toString());

                    intent.putExtra("rooms", Integer.toString(finalHolder.hd.getRooms()));
                    intent.putExtra("roomtype", finalHolder.hd.getRoomType().toString());
                    intent.putExtra("roomtypes", finalHolder.hd.getRoomTypes());
                    intent.putExtra("roomnumbers", finalHolder.hd.getRoomnumbers());


                    startActivity(intent);

                }
            });

            return row;
        }

        class ViewHolder {
            HospitalIntensiveDetails hd;
            TextView mname;
            TextView mroomgeneral;
            TextView mroomneonatal;
            TextView mroompediatric;
            TextView mroompsychiatric;
            TextView mroomcoronary;
            TextView mroomneurological;
            TextView mroompostanesthesia;
            TextView distance;
            TextView time;
            ImageView distanceimage;
            ImageView timeimage;
            int countgeneral = 0;
            int countneonatal = 0;
            int countpediatric = 0;
            int countcoronary = 0;
            int countneurological = 0;
            int countpostanesthesia = 0;
            int countpsychiatric = 0;
            int X;
            int i = 0;


        }
    }

    public void search(View view) {

        ArrayList<HospitalIntensiveDetails> searchlist = new ArrayList<HospitalIntensiveDetails>();
        for (int i = 0; i < selectedhospitalitems.size(); i++) {
            for (int j = 0; j < selectedhospitalitems.get(i).getRoomTypes().size(); j++) {
                if (selectedhospitalitems.get(i).getRoomTypes().get(j).compareTo(spinner.getSelectedItem().toString()) == 0) {
                    searchlist.add(selectedhospitalitems.get(i));
                    break;
                }
            }
        }

        hba = new hospitalintensiveadapter(HospitalListIntensive.this, R.layout.hospitallistitemintensive, searchlist);
        Hospitalslist.setAdapter(hba);

        hba.notifyDataSetChanged();


    }


    @Override
    public void onDirectionFinderStart() {

    }

    int index = 0;
    int count =0;
    ArrayList<HospitalIntensiveDetails> nearestlist = new ArrayList<HospitalIntensiveDetails>();

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        //Log.i("yes","chck:");



        for (Route route : routes) {
            Log.i(route.endAddress,"chck:");

            Log.i(Integer.toString(selectedhospitalitems.size()),"indx:");

            if (index < selectedhospitalitems.size()) {
                //Log.i("yes","chck:");



                selectedhospitalitems.get(index).setDistance(route.distance.text);

                selectedhospitalitems.get(index).setTime(route.duration.text);


                Log.i(Integer.toString(index),"indx:");
                Log.i( selectedhospitalitems.get(index).getName(),"indx:");
                Log.i( selectedhospitalitems.get(index).getTime(),"indx:");


                if (index == 0) {

                    nearestlist.add(selectedhospitalitems.get(index));
                    count=index;

                } else

                {
                        if (Double.parseDouble(nearestlist.get(index - 1).getTime().replace("mins", "").trim()) >= Double.parseDouble(selectedhospitalitems.get(index).getTime().replace("mins", "").trim())) {

                            Log.i(nearestlist.get(index - 1).getTime().replace("mins", "").trim(), "ix:");
                            Log.i((route.duration.text.replace("mins", "").trim()), "ix:");
                            Log.i(selectedhospitalitems.get(index).getName(), "ix:");

                            nearestlist.add(selectedhospitalitems.get(index));
                            count=index;


                        } else {

                            Log.i(nearestlist.get(index - 1).getTime().replace("mins", "").trim(), "ix:");
                            Log.i((route.duration.text.replace("mins", "").trim()), "ix:");

                            Log.i(selectedhospitalitems.get(index).getName(), "ix:");

                            //nearestlist.add(selectedhospitalitems.get(index-1));
                            nearestlist.add(selectedhospitalitems.get(count));


                        }



                }

            }
            index++;

        }



        ArrayList<HospitalIntensiveDetails> nearestlistmod = new ArrayList<HospitalIntensiveDetails>();

        nearestlistmod.add(nearestlist.get(nearestlist.size() - 1));
        hba = new hospitalintensiveadapter(HospitalListIntensive.this, R.layout.hospitallistitemintensivenearest, nearestlistmod);
        Hospitalslist.setAdapter(hba);


        hba.notifyDataSetChanged();


        if(index==selectedhospitalitems.size())
        {
            processdialog.dismiss();

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
                Intent intent = new Intent(HospitalListIntensive.this, FirstPage.class);
                startActivity(intent);
                FirstPage.sp.edit().putBoolean("logged",false).apply();
                HospitalListIntensive.this.finish();


                return true;
            case R.id.home:
                intent = new Intent(HospitalListIntensive.this, HomeMod.class);
                intent.putExtra("Username",Username);
                intent.putExtra("Sex",Gender);
                intent.putExtra("Age",Age);
                intent.putExtra("Email",Email);
                intent.putExtra("National_id",Nationalid);
                intent.putExtra("Blood_type",Bloodtype);
                intent.putExtra("phonenumber",Phonenumber);
                intent.putExtra("lastdonationdate",Lastdonationdate);
                HospitalListIntensive.this.finish();

                startActivity(intent);


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public  void showNotifications(View view)
    {
        LayoutInflater inflater = (LayoutInflater) HospitalListIntensive.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notificationspopup, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        notificationslist= (ListView)layout.findViewById(R.id.notificationslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        NotificationsAdapter NA = new HospitalListIntensive.NotificationsAdapter(HospitalListIntensive.this, R.layout.notificationitem, HomeMod.notificationitems);

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
            HospitalListIntensive.NotificationsAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new HospitalListIntensive.NotificationsAdapter.ViewHolder();
                holder.mhospitalname = (TextView) row.findViewById(R.id.hospitalname);
                holder.mbloodtype = (TextView) row.findViewById(R.id.bloodtype);

                row.setTag(holder);
            } else {
                holder = (HospitalListIntensive.NotificationsAdapter.ViewHolder) row.getTag();
            }
            holder.ND = getItem(position);
            holder.mhospitalname.setText(holder.ND.getHospitalname()+ " Urgently Needs Your Help");
            holder.mbloodtype.setText(holder.ND.getBloodType()+" Needed, Your blood type is " + Bloodtype+" so you can donate");


            final HospitalListIntensive.NotificationsAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            holder.mhospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(HospitalListIntensive.this,HospitalNotificationProfile.class);

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
        LayoutInflater inflater = (LayoutInflater) HospitalListIntensive.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.notificationspopup, (ViewGroup) findViewById(R.id.popup_element));
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pw.showAtLocation(view, Gravity.CENTER,0, 0);
        pw.setBackgroundDrawable(new BitmapDrawable());

        notificationslist= (ListView)layout.findViewById(R.id.notificationslist);
        Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
        cancelButton.setOnClickListener(cancel_button_click_listener);

        MessagesAdapter MA = new HospitalListIntensive.MessagesAdapter(HospitalListIntensive.this, R.layout.messagefileitem, HomeMod.messageitems);
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
            HospitalListIntensive.MessagesAdapter.ViewHolder holder = null;

            if (row == null || row.getTag() == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                row = inflater.inflate(layoutresource, null);
                holder = new HospitalListIntensive.MessagesAdapter.ViewHolder();
                holder.mhospitalname = (TextView) row.findViewById(R.id.hospitalname);

                row.setTag(holder);
            } else {
                holder = (HospitalListIntensive.MessagesAdapter.ViewHolder) row.getTag();
            }
            holder.MD = getItem(position);
            holder.mhospitalname.setText(holder.MD.getHospitalname()+ " Sent You a File");


            final HospitalListIntensive.MessagesAdapter.ViewHolder finalHolder = holder;
            final ViewHolder finalHolder1 = holder;
            final ViewHolder finalHolder2 = holder;
            holder.mhospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    Intent i = new Intent(HospitalListIntensive.this,FileContent.class);
                    i.putExtra("File", finalHolder2.MD.getFile());
                    new AddNewEntry().execute("1",finalHolder2.MD.getID());
                    HospitalListIntensive.this.finish();

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
