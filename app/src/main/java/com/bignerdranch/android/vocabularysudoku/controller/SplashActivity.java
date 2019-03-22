package com.bignerdranch.android.vocabularysudoku.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.WindowManager;
import com.bignerdranch.android.vocabularysudoku.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//hide state frame
        getSupportActionBar().hide();//hide title

        setContentView(R.layout.activity_splash);
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(3000);//let process sleep
                    Intent it = new Intent(getApplicationContext(), MenuActivity.class);//start MenuActivity
                    startActivity(it);
                    finish();//stop current activity
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
