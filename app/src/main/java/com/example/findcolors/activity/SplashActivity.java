package com.example.findcolors.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.findcolors.R;

public class SplashActivity extends AppCompatActivity {

    /*
    الشاشة الاولي في التطبيق وتستمر لمدة ثانية ونصف ثم تغلق لفتح الشاشة الرئيسية
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },1500);
    }
}
