package com.example.trafficroadworks.allActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.trafficroadworks.R;
import com.example.trafficroadworks.allActivity.fragmentViewPager.TrafficSecondPageViewPager;
import com.google.android.material.tabs.TabLayout;

public class TrafficSecondPage extends AppCompatActivity {

    //declaring view pager for second page
    private ViewPager eventDetailsViewPager;

    //declaring tab layout for second page activity
    private TabLayout eventDetailsTabLayout;

    //declaring second page view pager adapter for showing all the fragments
    private TrafficSecondPageViewPager trafficSecondPageViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_second_page);

        //declaring and initializing toolbar in second activity
        Toolbar toolbarSecondPage = findViewById(R.id.toolbar_second);
        setSupportActionBar(toolbarSecondPage);

        //initializing all view pager elements
        eventDetailsTabLayout = findViewById(R.id.secondPageTabLayout);
        eventDetailsViewPager = findViewById(R.id.ViewPager);
        trafficSecondPageViewPager = new TrafficSecondPageViewPager(getSupportFragmentManager());

        //setting adapter to the view pager
        eventDetailsViewPager.setAdapter(trafficSecondPageViewPager);

        //setting view pager to the tab layout
        eventDetailsTabLayout.setupWithViewPager(eventDetailsViewPager);
        eventDetailsViewPager.setOffscreenPageLimit(eventDetailsTabLayout.getTabCount());


        //set toolbar back click to return previous activity
        toolbarSecondPage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //show back arrow in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
