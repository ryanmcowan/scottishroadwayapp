package com.example.trafficroadworks.allActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.trafficroadworks.R;

public class SplashScreen extends AppCompatActivity {

    //created this activity for showing splash screen in the opening of the app and also this is the starting activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //creating intent for going Traffic first page activity after showing splash automatically

        final Intent splashScreenIntent = new Intent(this, TrafficFirstPage.class);

        //using one thread for showing splash at particular given time

        Thread splashTimer = new Thread() {
            public void run() {
                try {

                    //time to stay splash screen awake

                    sleep(2500);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    //after complete the awake time, start new activity through intent

                    startActivity(splashScreenIntent);

                    //afterwards calling finish for ending all work of this activity

                    finish();

                }

            }

        };
        splashTimer.start();
    }

}

