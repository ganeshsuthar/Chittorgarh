package com.m.chittorgarh.View.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.m.chittorgarh.R;

public class SplashScreen extends AppCompatActivity {
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getBoolean("userStatus",false)){
                    startActivity(new Intent(SplashScreen.this,Home.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreen.this,Login.class));
                    finish();
                }

            }
        },2000);
    }
}
