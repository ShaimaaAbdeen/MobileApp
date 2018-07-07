package com.example.mrsshimaa.mobileapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class DoctorProfilePrivate extends AppCompatActivity {
    String firstname;
    String lastname;
    String phonenumber;
    String email;
    String position;
    String id;
    String password;
    TextView doctorname;
    TextView doctorid;
    TextView doctoremail;
    TextView doctorphoneno;
    TextView doctorposition;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    ImageView pp;

    public static final String UPLOAD_URL = "http://wellcare.atwebpages.com/android/uploadimagedoctor.php";
    static final String UPLOAD_KEY = "Profile_photo";
    public static final String UPLOAD_ID = "Doctor_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile_private);

        doctorname = (TextView)  findViewById(R.id.doctorname);
        doctorid = (TextView)  findViewById(R.id.idtext);
        doctoremail = (TextView)  findViewById(R.id.emailtext);
        doctorphoneno = (TextView)  findViewById(R.id.phonenotext);
        doctorposition = (TextView)  findViewById(R.id.positiontext);
        pp=(ImageView) findViewById(R.id.profilepic);

        Bundle extras = getIntent().getExtras();



        if (extras != null) {
            firstname=extras.getString("First_name");
            lastname=extras.getString("Last_name");
            phonenumber=extras.getString("phonenumber");
            email=extras.getString("Email");
            position=extras.getString("Position");
            id=extras.getString("ID");
            password=extras.getString("Password");
        }


        doctorname.setText(firstname+" "+lastname);
        doctorid.setText(id);
        doctoremail.setText(email);
        doctorphoneno.setText(phonenumber);
        doctorposition.setText(position);

        getImage(id);
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
                Intent intent = new Intent(DoctorProfilePrivate.this, GroupTherapyListDoctor.class);
                startActivity(intent);
                DoctorProfilePrivate.this.finish();


                return true;

            case R.id.logout:
                intent = new Intent(DoctorProfilePrivate.this, FirstPage.class);
                startActivity(intent);
                DoctorProfilePrivate.this.finish();

                // Green item was selected
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                loading = ProgressDialog.show(DoctorProfilePrivate.this, "Uploading...", null,true,true);
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
                data.put(UPLOAD_ID, id);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
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
