package com.example.ovs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.L;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(()->{
            startActivity(new Intent(MainActivity.this, SignUp.class));
        }, 3000);
    }
    //@Override
    //protected void onStart() {
        //super.onStart();
        //new Handler().postDelayed(()->{
            //startActivity(new Intent(MainActivity.this, SignUp.class));
        //}, 3000);
    //}

}