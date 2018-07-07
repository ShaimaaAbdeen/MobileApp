package com.example.mrsshimaa.mobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class DoctorProfilePublicUsers extends AppCompatActivity {
    String firstname;
    String lastname;
    String phonenumber;
    String email;
    String position;
    TextView doctorname;
    TextView doctoremail;
    TextView doctorphoneno;
    TextView doctorposition;
    String id;
    ImageView pp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_public_users);

        doctorname = (TextView)  findViewById(R.id.doctorname);
        doctoremail = (TextView)  findViewById(R.id.emailtext);
        doctorphoneno = (TextView)  findViewById(R.id.phonenotext);
        doctorposition = (TextView)  findViewById(R.id.positiontext);
        pp=(ImageView) findViewById(R.id.profilepic);
        Bundle extras = getIntent().getExtras();



        if (extras != null) {
            firstname=extras.getString("First_name");
            lastname=extras.getString("Last_name");
            phonenumber=extras.getString("Phonenumber");
            email=extras.getString("Email");
            position=extras.getString("Position");
            id=extras.getString("ID");

        }


        doctorname.setText(firstname+" "+lastname);
        doctoremail.setText(email);
        doctorphoneno.setText(phonenumber);
        doctorposition.setText(position);


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

                String add = "http://wellcare.atwebpages.com/android/getimagedoctor.php?Doctor_ID="+id;
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
