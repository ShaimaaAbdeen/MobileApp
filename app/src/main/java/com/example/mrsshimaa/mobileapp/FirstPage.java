package com.example.mrsshimaa.mobileapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FirstPage extends AppCompatActivity {

    Intent i;
    public static SharedPreferences sp;
    public static SharedPreferences spD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        sp = getSharedPreferences("login",MODE_PRIVATE);


       if(sp.getBoolean("logged",false)){
            goToHome();


        }



    }
    public void Joinfamily(View view)
    {
        i=new Intent(FirstPage.this,SignUp.class);
        FirstPage.this.finish();
        startActivity(i);
    }

   public void Login(View view)
    {

        i=new Intent(FirstPage.this,Login.class);
        FirstPage.this.finish();

        startActivity(i);


    }
    public void goToHome(){
        Intent i = new Intent(this,HomeMod.class);
        FirstPage.this.finish();

        startActivity(i);
    }
}
