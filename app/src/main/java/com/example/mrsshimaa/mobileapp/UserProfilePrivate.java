package com.example.mrsshimaa.mobileapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrsshimaa.mobileapp.app.Config;
import com.example.mrsshimaa.mobileapp.util.NotificationUtils;
import com.google.android.gms.nearby.messages.internal.Update;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProfilePrivate extends AppCompatActivity {
    TextView name;
    TextView age;
    TextView email;
    TextView phonenumber;
    TextView lastdonationdate;
    TextView bloodtype;
    TextView nationalid;
    static  String updateclicker;
   // TextView notificationnumber;
    ImageView pp;
    public static final String UPLOAD_URL = "http://wellcare.atwebpages.com/android/uploadimage.php";
    static final String UPLOAD_KEY = "Profile_photo";
    public static final String UPLOAD_ID = "National_id";


    //public static final String UPLOAD_URL = "http://wellcare.atwebpages.com/android/uploadimageurl.php";
    //public static final String UPLOAD_KEY = "image";



    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    SharedPreferences sp;
    PopupWindow pw;
    String Sex;

    String nID;

    private RequestHandler requestHandler;

    String Lastdonationdate;
    //private String URL_UPLOAD_IMAGE="http://wellcare.atwebpages.com/android/uploadimage.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_private);
        name = (TextView) findViewById(R.id.username);
        pp = (ImageView) findViewById(R.id.profilepic);
        age = (TextView) findViewById(R.id.agetext);
        email = (TextView) findViewById(R.id.emailtext);
        phonenumber = (TextView) findViewById(R.id.phonenotext);
        lastdonationdate = (TextView) findViewById(R.id.datetext);
        bloodtype = (TextView) findViewById(R.id.bloodtypetext);
        nationalid = (TextView) findViewById(R.id.nationalidtext);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        requestHandler = new RequestHandler();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Lastdonationdate = extras.getString("lastdonationdate");
            Sex = extras.getString("Sex");
            name.setText(extras.getString("Username"));

            //name.setText(extras.getString("First_name") + " " + extras.getString("Last_name"));

            if (extras.getString("Sex").compareTo("F") == 0 || extras.getString("Sex").compareTo("female") == 0) {
                pp.setImageResource(R.drawable.userfemaleblue);
            }

            age.setText(extras.getString("Age"));
            email.setText(extras.getString("Email"));
            nationalid.setText(extras.getString("National_id"));
            nID = extras.getString("National_id");
            bloodtype.setText(extras.getString("Blood_type"));
            if (extras.getString("Blood_type").compareTo("null") == 0) {
                bloodtype.setText("add blood type?");
                bloodtype.setTextColor(Color.parseColor("#FF64B5F6"));
                bloodtype.setOnClickListener(addbloodtype);
            } else {
                phonenumber.setText(extras.getString("phonenumber"));
            }
            if (extras.getString("phonenumber").compareTo("0") == 0 || extras.getString("phonenumber").compareTo("null") == 0) {
                phonenumber.setText("add phonenumber?");
                phonenumber.setTextColor(Color.parseColor("#FF64B5F6"));
                phonenumber.setOnClickListener(addPhonenumber);
            } else {
                phonenumber.setText(extras.getString("phonenumber"));
            }

            if (extras.getString("lastdonationdate").compareTo("0001-01-01") == 0 || extras.getString("lastdonationdate").compareTo("null") == 0) {
                Log.i("yes", "ND:");
                lastdonationdate.setText("add last donationdate?");
                lastdonationdate.setTextColor(Color.parseColor("#FF64B5F6"));
                lastdonationdate.setOnClickListener(addDonationdate);
            } else {
                lastdonationdate.setText(extras.getString("lastdonationdate"));

            }
            // if(extras.getString("Profile_photo").compareTo("null")!=0 )  {
            Log.i("yes", "ND:");

            //byte[] decodedString = Base64.decode(extras.getString("Profile_photo"), Base64.DEFAULT);
            // Bitmap imgBitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            //pp.setImageBitmap(imgBitMap);


            //}

        }


        String id = extras.getString("National_id");

        getImage(id);

        // getJSONImage("http://wellcare.atwebpages.com/android/getimage.php?Nationalid="+id);
      //  new DownloadImageTask(pp)
               // .execute("http://wellcare.atwebpages.com/android/uploads/"+id+".png");
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    private View.OnClickListener addPhonenumber = new View.OnClickListener() {
        public void onClick(View v) {


            SetUpdateClicker("PN");
            Intent intent = new Intent(UserProfilePrivate.this, UpdateUserInfo.class);
            intent.putExtra("Username",name.getText().toString());
            intent.putExtra("Age",age.getText().toString());
            intent.putExtra("Email",email.getText().toString());
            intent.putExtra("National_id",nationalid.getText());
            intent.putExtra("phonenumber",phonenumber.getText());
            intent.putExtra("lastdonationdate",lastdonationdate.getText());
            intent.putExtra("Blood_type",bloodtype.getText());
            intent.putExtra("Sex",Sex);

            startActivity(intent);
        }
    };


    private View.OnClickListener addDonationdate = new View.OnClickListener() {
        public void onClick(View v) {

            SetUpdateClicker("DD");
            Intent intent = new Intent(UserProfilePrivate.this, UpdateUserInfo.class);
            intent.putExtra("Username",name.getText().toString());
            intent.putExtra("Age",age.getText().toString());
            intent.putExtra("Email",email.getText().toString());
            intent.putExtra("National_id",nationalid.getText());
            intent.putExtra("phonenumber",phonenumber.getText());
            intent.putExtra("lastdonationdate",lastdonationdate.getText());
            intent.putExtra("Blood_type",bloodtype.getText());
            intent.putExtra("Sex",Sex);


            startActivity(intent);
        }
    };
    private View.OnClickListener addbloodtype = new View.OnClickListener() {
        public void onClick(View v) {

            SetUpdateClicker("BT");
            Intent intent = new Intent(UserProfilePrivate.this, UpdateUserInfo.class);
            intent.putExtra("Username",name.getText().toString());
            intent.putExtra("Age",age.getText().toString());
            intent.putExtra("Email",email.getText().toString());
            intent.putExtra("National_id",nationalid.getText());
            intent.putExtra("phonenumber",phonenumber.getText());
            intent.putExtra("lastdonationdate",lastdonationdate.getText());
            intent.putExtra("Blood_type",bloodtype.getText());
            intent.putExtra("Sex",Sex);

            startActivity(intent);
        }
    };

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



        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {



                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
               // Log.i(bitmap.toString(),"img:");
                //Log.i(pp.getDrawable().getConstantState().toString(),"img:");

                pp.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Bitmap bm=((BitmapDrawable)pp.getDrawable()).getBitmap();

            //String ppImage = getStringImage(bitmap);
           // new AddNewEntry().execute(nationalid.getText().toString(),ppImage);


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
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserProfilePrivate.this, "Uploading...", null,true,true);
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
                data.put(UPLOAD_ID, nID);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
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
            case R.id.home:
                Intent intent = new Intent(UserProfilePrivate.this, HomeMod.class);
                startActivity(intent);


                return true;

            case R.id.logout:
                intent = new Intent(UserProfilePrivate.this, FirstPage.class);
                intent.putExtra("lastdonationdate",Lastdonationdate);

                startActivity(intent);
                FirstPage.sp.edit().putBoolean("logged",false).apply();
                UserProfilePrivate.this.finish();

                // Green item was selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public  static  void SetUpdateClicker(String s)
    {
        updateclicker=s;
    }
    public  static  String GetUpdateClicker()
    {
        return  updateclicker;

    }
    public  void updateProfile(View view)
    {
        UserProfilePrivate.SetUpdateClicker("");
        Intent intent = new Intent(UserProfilePrivate.this, UpdateUserInfo.class);
        intent.putExtra("Username",name.getText().toString());
        intent.putExtra("Age",age.getText().toString());
        intent.putExtra("Email",email.getText().toString());
        intent.putExtra("National_id",nationalid.getText());
        intent.putExtra("phonenumber",phonenumber.getText());
        intent.putExtra("lastdonationdate",lastdonationdate.getText());
        intent.putExtra("Blood_type",bloodtype.getText());
        intent.putExtra("Sex",Sex);

        startActivity(intent);
    }


 /*   private class AddNewEntry extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... arg) {
            // TODO Auto-generated method stub


            String NID = arg[0];
            String PPIMG = arg[1];




            List<NameValuePair> params = new ArrayList<NameValuePair>();



            params.add(new BasicNameValuePair("National_id", NID));
            params.add(new BasicNameValuePair("Profile_Picture", PPIMG));


            for(int i=0;i<params.size();i++)
            {
                Log.i(params.get(i).getValue().toString(),"params:");
            }

            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_UPLOAD_IMAGE,
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

    }*/


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


}



