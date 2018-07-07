package com.example.mrsshimaa.mobileapp;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mrsshimaa.mobileapp.app.Config;
import com.example.mrsshimaa.mobileapp.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;

import static com.example.mrsshimaa.mobileapp.R.layout.popupbasic;

public class UserProfilePageBlood extends AppCompatActivity  {
    private PopupWindow pw;
    TextView name;
    TextView notificationnumber;
    ImageView pp;
    public static final String UPLOAD_URL = "http://192.168.43.232/GraduationProject/uploadppandroid.php";
    public static final String UPLOAD_KEY = "image";
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page_blood);
        name = (TextView) findViewById(R.id.username);
        pp=(ImageView) findViewById(R.id.profilepic);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            name.setText(extras.getString("Username"));

            //name.setText(extras.getString("First_name") + " " + extras.getString("Last_name"));

            if(extras.getString("Sex").compareTo("F")==0){
                pp.setImageResource(R.drawable.userf);
            }
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received



                }
            }
        };

        displayFirebaseRegId();

    }
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

    }
    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    public void basicInfo(View view) {
        initiatebasicInformationPopupWindow(view);
    }

    public void bloodInfo(View view) {
        initiatebloodinformationPopupWindow(view);
    }

    public void other(View view) {
        initiateotherPopupWindow(view);
    }

    private void initiatebasicInformationPopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) UserProfilePageBlood.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(popupbasic, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 600, 950, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER, 0, 0);

            //TextView name = (TextView) findViewById(R.id.username);
            // TextView birthday = (TextView) findViewById(R.id.birthdaydatetext);
            TextView age = (TextView) layout.findViewById(R.id.agetext);
            TextView email = (TextView) layout.findViewById(R.id.emailtext);
            TextView phonenumber = (TextView) layout.findViewById(R.id.phonenotext);

            Bundle extras = getIntent().getExtras();

            if (extras != null) {

                age.setText(extras.getString("Age"));
                email.setText(extras.getString("Email"));
                    phonenumber.setText("add phonenumber?");
                    phonenumber.setTextColor(Color.parseColor("#FF26A69A"));
                    phonenumber.setOnClickListener(addPhonenumber);





            }


            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private View.OnClickListener addPhonenumber = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(UserProfilePageBlood.this, SignUpBlood.class);
            startActivity(intent);
        }
    };


    private void initiatebloodinformationPopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) UserProfilePageBlood.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popupbloodinfo, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 600, 950, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER,0, 0);

            TextView donationdate = (TextView) layout.findViewById(R.id.datetext);
            TextView bloodtype = (TextView)layout.findViewById(R.id.bloodtypetext);

            Bundle extras =getIntent().getExtras();

            if(extras!=null) {
                if(extras.getString("lastdonationdate").compareTo("null")==0 || extras.getString("lastdonationdate").compareTo("")==0)  {
                    donationdate.setText("add last donationdate?");
                    donationdate.setTextColor(Color.parseColor("#FF26A69A"));
                    donationdate.setOnClickListener(addDonationdate);
                } else {
                    donationdate.setText(extras.getString("lastdonationdate"));

                }
                bloodtype.setText(extras.getString("Blood_type"));

            }

            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private View.OnClickListener addDonationdate = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(UserProfilePageBlood.this, SignUpBlood.class);
            startActivity(intent);
        }
    };
    private void initiateotherPopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) UserProfilePageBlood.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popupother, (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 600, 950, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.CENTER,0, 0);


            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void choosePhoto(View view )
    {
        //Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       // startActivityForResult(i,1);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if(requestCode == 1 && resultCode== RESULT_OK && data !=null){

            Uri selectedimage=data.getData();
            try {
                Bitmap bitmapimage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedimage);
                ImageView pp= (ImageView) findViewById(R.id.profilepic);
                pp.setImageBitmap(bitmapimage);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }*/

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                pp.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        uploadImage();
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserProfilePageBlood.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put(UPLOAD_KEY, uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }



    private void viewImage() {
        //startActivity(new Intent(this, ImageListView.class));
    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };

  /*  public void showMenu(View view)
    {

        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.show();

    }*/




}
