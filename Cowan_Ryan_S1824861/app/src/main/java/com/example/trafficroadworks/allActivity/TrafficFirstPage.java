package com.example.trafficroadworks.allActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.trafficroadworks.R;
import com.example.trafficroadworks.allActivity.fragmentViewPager.TrafficFirstPageViewPager;
import com.example.trafficroadworks.sharedpreferencefornearme.SharedNearMePreference;
import com.example.trafficroadworks.trafficcommon.TrafficCommon;
import com.example.trafficroadworks.univarsalinterfaces.NearMeSelectItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.threetenabp.AndroidThreeTen;
import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class TrafficFirstPage extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, NearMeSelectItem {


    //for showing near me click item,initialize it
    public int menu = 1;

    // for Searching variable
    public SearchView searchElement;

    //creating instance from this activity for accessing data from fragment
    public static TrafficFirstPage firstinstance;

    //declaring Near me Layout variable
    public LinearLayout nearmelayoutshow;

    //declaring near me text variable in top
    public TextView near_me_text_show;

    //declaring shared preference for getting near me value
    public SharedNearMePreference sharedNearMePreference;

    //declaring Action bar toggle for navigation drawer
    public ActionBarDrawerToggle mDrawerToggle;

    //declaring near me select item for incident,roadworks,planned roadworks fragment
    public NearMeSelectItem nearMeSelect1;
    public NearMeSelectItem nearMeSelect2;
    public NearMeSelectItem nearMeSelect3;

    //declaring View pager for holding fragments in this activity
    private ViewPager firstPageViewPager;

    //declaring tab layout for holding view pager and show each fragment in each tab
    private TabLayout firstPageTabLayout;

    //viewpager adapter for showing all fragment in this activity
    private TrafficFirstPageViewPager firstPageAdapter;

    //declaring drawer layout
    public DrawerLayout drawer;


    //declaring location element

    public Double lat;
    public Double lng;
    public LocationRequest locationRequest;
    public LocationCallback locationCallback;
    public FusedLocationProviderClient fusedLocationProviderClient;
    public Location currentLocation;

    public boolean permissionAccessCoarseLocationApproved;
    public boolean permissionAccessFineLocationApproved;

    //this is the navigation view declare

    public NavigationView navigationView;
    public LinearLayout nearmelin;


    //for showing current state of near me event
    public int wantEventMiles = 0;


    //declaring radio group for near me
    public RadioGroup near_me_radio_group;

    //for showing current mile state in activity
    public int mile;

    //declaring radio button
    public RadioButton fivemiles;
    public RadioButton tenmiles;
    public RadioButton fiftenmiles;
    public RadioButton twentymiles;
    public RadioButton twentyfivemiles;
    public RadioButton fiftymiles;
    public RadioButton hundredmiles;
    public RadioButton allevent;

    //declaring custom menu item in navigation drawer

    LinearLayout navIncident;
    LinearLayout navRoadworks;
    LinearLayout navPlannedWorks;
    LinearLayout nav_nearMe;

    //initialization of this page instance
    public static TrafficFirstPage getInstanceTrafficFirstpage() {
        return firstinstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initializing android three ten for convert time date day month and year.
        AndroidThreeTen.init(this);
        firstinstance = this;
        setContentView(R.layout.activity_traffic_first_page);

        //initializing toolbar in first page activity
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        //initializing shared preference for saving data initially
        sharedNearMePreference = new SharedNearMePreference(this);

        //initializing variable
        nearmelin = findViewById(R.id.nearmelin);
        nearmelayoutshow = findViewById(R.id.near_me_selected_view);
        near_me_text_show = findViewById(R.id.text_nearme);


        //for navigation drawer initializing menu item

        nav_nearMe = findViewById(R.id.nv_near_me);
        navIncident = findViewById(R.id.nav_incident);
        navPlannedWorks = findViewById(R.id.nav_plannedRoadWork);
        navRoadworks = findViewById(R.id.nav_roadworks);

        //initializing all radio button variable

        fivemiles = (RadioButton) findViewById(R.id.five_miles);
        tenmiles = (RadioButton) findViewById(R.id.ten_miles);
        fiftenmiles = (RadioButton) findViewById(R.id.fifteen_miles);
        twentymiles = (RadioButton) findViewById(R.id.twenty_miles);
        twentyfivemiles = (RadioButton) findViewById(R.id.twenty_five_miles);
        fiftymiles = (RadioButton) findViewById(R.id.fifty_miles);
        hundredmiles = (RadioButton) findViewById(R.id.hundred_miles);
        allevent = (RadioButton) findViewById(R.id.all_events);

        //initializing radio group

        near_me_radio_group = findViewById(R.id.nearme_radio_group);

        //initializing search event

        searchElement = findViewById(R.id.search_view_traffic);

        //get save data from shared preference and show it to activity if near me is other than all event then showing near me text in top bar data otherwise its gone its layout visibility

        if (sharedNearMePreference.getNearMeDialogValue() == 0) {
            nearmelayoutshow.setVisibility(View.GONE);
        } else {
            nearmelayoutshow.setVisibility(View.VISIBLE);
            mile = sharedNearMePreference.getNearMeDialogValue();
            near_me_text_show.setText(String.valueOf(mile));
        }

        //initializing pager variable

        firstPageTabLayout = findViewById(R.id.firstPageTabLayout);
        firstPageViewPager = findViewById(R.id.firstpageviewpager);
        firstPageAdapter = new TrafficFirstPageViewPager(getSupportFragmentManager());

        //set adapter to the view pager for showing fragment
        firstPageViewPager.setAdapter(firstPageAdapter);

        //set view pager in tab layout
        firstPageTabLayout.setupWithViewPager(firstPageViewPager);
        firstPageViewPager.setOffscreenPageLimit(firstPageTabLayout.getTabCount());

        //initializing custom tab layout Header for tab title

        //this is incident header layout
        View incidents_header = LayoutInflater.from(this).inflate(R.layout.incident_custome_tab_header, null);
        incidents_header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //this is roadworks header layout
        View roadworks_header = LayoutInflater.from(this).inflate(R.layout.roadworks_custom_tab_header, null);
        roadworks_header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //this is planned roadworks header layout
        View plannedw_orks_header = LayoutInflater.from(this).inflate(R.layout.plannedworks_custom_tab_header, null);
        plannedw_orks_header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        //setting custom header in each particular position
        firstPageTabLayout.getTabAt(0).setCustomView(incidents_header);
        firstPageTabLayout.getTabAt(1).setCustomView(roadworks_header);
        firstPageTabLayout.getTabAt(2).setCustomView(plannedw_orks_header);

        //initializing navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //initializing navigation drawer
        drawer = findViewById(R.id.drawer_layout);

        //setting drawer toggle in the first page activity
        mDrawerToggle = new ActionBarDrawerToggle(
                TrafficFirstPage.this,
                drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        mDrawerToggle.setDrawerIndicatorEnabled(false);

        //set custom navigation indicator icon
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.menuicon, getTheme());
        mDrawerToggle.setHomeAsUpIndicator(drawable);


        //setting navigation indicator click handler for open and close the drawer
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        mDrawerToggle.syncState();

        //set navigation item clickable
        navigationView.setNavigationItemSelectedListener(this);

        //set all the navigation item menu clickable
        nav_nearMe.setOnClickListener(this);
        navIncident.setOnClickListener(this);
        navRoadworks.setOnClickListener(this);
        navPlannedWorks.setOnClickListener(this);
    }


    //for assuming the current location of the user
    private void deviceLocation() {

        //create location request for the user
        buildLocationRequest();

        //for current location
        buildLocationCallback();

        //fused location provider client use for performing to fetch and take the device location from the device
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(TrafficFirstPage.this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        //providing permission for fine and coarse location access
        permissionAccessCoarseLocationApproved =
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
        permissionAccessFineLocationApproved =
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;

        if (permissionAccessCoarseLocationApproved && permissionAccessFineLocationApproved) {
            fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TrafficFirstPage.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //for set all the location of the event
                    lat = task.getResult().getLatitude();
                    lng = task.getResult().getLongitude();

                    //save the current location into a common class
                    TrafficCommon.Current_device_latitude = lat;
                    TrafficCommon.Current_device_longitude = lng;

                }

            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }


    //create current location request to the user
    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setSmallestDisplacement(10f);
    }

    //for current location find
    private void buildLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.traffic_first_page, menu);
        return true;
    }


    //navigation item menu selected method but as we use custom menu item we declare previously and implement it.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
        }
        return true;
    }

    //on option item selected user for item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;

        // return super.onOptionsItemSelected(item);
    }

    //this android on start life cycle method for using request to the user for location permission
    @Override
    protected void onStart() {
        super.onStart();
        if (permissionAccessCoarseLocationApproved && permissionAccessFineLocationApproved) {
            deviceLocation();
        }
    }

    //this is implement method of near me interfaces
    @Override
    public void onNearMeItemClickListener() {
        //nothing here to be implemented
    }


    //this is the implementation of all item menu in navigation drawer and near me event as well
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            //this is near me event
            case R.id.nv_near_me:

                //check if near me click or not
                if (menu == 1) {
                    nearmelin.setVisibility(View.VISIBLE);
                    //radio group clickable for near me event
                    near_me_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {

                            View radioButton = near_me_radio_group.findViewById(checkedId);

                            //taken radio group index from click
                            int index = near_me_radio_group.indexOfChild(radioButton);


                            //check which radio button is clicked for near me event
                            switch (index) {
                                case 0:
                                    menu = 2;
                                    wantEventMiles = 5;

                                    //set near me value to the common class
                                    TrafficCommon.Select_Near_Me_Value = wantEventMiles;

                                    //set near me value to the shared preference
                                    sharedNearMePreference.setNearMeDialogValue(wantEventMiles);

                                    //notify click event to all 3 fragment
                                    nearMeSelect1.onNearMeItemClickListener();
                                    nearMeSelect2.onNearMeItemClickListener();
                                    nearMeSelect3.onNearMeItemClickListener();


                                    //save click state for calling method
                                    setRadioButtonChecked(wantEventMiles);

                                    //set text for top bar in activity
                                    nearmelayoutshow.setVisibility(View.VISIBLE);
                                    mile = sharedNearMePreference.getNearMeDialogValue();
                                    near_me_text_show.setText(String.valueOf(mile).toString());

                                    //perform close the drawer after click the radio button
                                    drawer = findViewById(R.id.drawer_layout);
                                    drawer.closeDrawer(GravityCompat.END);
                                    nearmelin.setVisibility(View.GONE);
                                    break;
                                case 1:
                                    menu = 2;
                                    wantEventMiles = 10;

                                    //set near me value to the common class
                                    TrafficCommon.Select_Near_Me_Value = wantEventMiles;

                                    //set near me value to the shared preference
                                    sharedNearMePreference.setNearMeDialogValue(wantEventMiles);

                                    //notify click event to all 3 fragment
                                    nearMeSelect1.onNearMeItemClickListener();
                                    nearMeSelect2.onNearMeItemClickListener();
                                    nearMeSelect3.onNearMeItemClickListener();

                                    //save click state for calling method
                                    setRadioButtonChecked(wantEventMiles);

                                    //set text for top bar in activity
                                    nearmelayoutshow.setVisibility(View.VISIBLE);
                                    mile = sharedNearMePreference.getNearMeDialogValue();
                                    near_me_text_show.setText(String.valueOf(mile).toString());

                                    //perform close the drawer after click the radio button
                                    drawer.closeDrawer(GravityCompat.END);
                                    nearmelin.setVisibility(View.GONE);
                                    break;
                                case 2:
                                    menu = 2;
                                    wantEventMiles = 15;

                                    //set near me value to the common class
                                    TrafficCommon.Select_Near_Me_Value = wantEventMiles;

                                    //set near me value to the shared preference
                                    sharedNearMePreference.setNearMeDialogValue(wantEventMiles);

                                    //notify click event to all 3 fragment
                                    nearMeSelect1.onNearMeItemClickListener();
                                    nearMeSelect2.onNearMeItemClickListener();
                                    nearMeSelect3.onNearMeItemClickListener();

                                    //save click state for calling method
                                    setRadioButtonChecked(wantEventMiles);

                                    //set text for top bar in activity
                                    nearmelayoutshow.setVisibility(View.VISIBLE);
                                    mile = sharedNearMePreference.getNearMeDialogValue();
                                    near_me_text_show.setText(String.valueOf(mile).toString());

                                    //perform close the drawer after click the radio button
                                    drawer.closeDrawer(GravityCompat.END);
                                    nearmelin.setVisibility(View.GONE);
                                    break;
                                case 3:
                                    menu = 2;
                                    wantEventMiles = 20;

                                    //set near me value to the common class
                                    TrafficCommon.Select_Near_Me_Value = wantEventMiles;

                                    //set near me value to the shared preference
                                    sharedNearMePreference.setNearMeDialogValue(wantEventMiles);

                                    //notify click event to all 3 fragment
                                    nearMeSelect1.onNearMeItemClickListener();
                                    nearMeSelect2.onNearMeItemClickListener();
                                    nearMeSelect3.onNearMeItemClickListener();

                                    //save click state for calling method
                                    setRadioButtonChecked(wantEventMiles);

                                    //set text for top bar in activity
                                    nearmelayoutshow.setVisibility(View.VISIBLE);
                                    mile = sharedNearMePreference.getNearMeDialogValue();
                                    near_me_text_show.setText(String.valueOf(mile).toString());

                                    //perform close the drawer after click the radio button
                                    drawer.closeDrawer(GravityCompat.END);
                                    nearmelin.setVisibility(View.GONE);
                                    break;
                                case 4:
                                    menu = 2;
                                    wantEventMiles = 25;

                                    //set near me value to the common class
                                    TrafficCommon.Select_Near_Me_Value = wantEventMiles;

                                    //set near me value to the shared preference
                                    sharedNearMePreference.setNearMeDialogValue(wantEventMiles);

                                    //notify click event to all 3 fragment
                                    nearMeSelect1.onNearMeItemClickListener();
                                    nearMeSelect2.onNearMeItemClickListener();
                                    nearMeSelect3.onNearMeItemClickListener();

                                    //save click state for calling method
                                    setRadioButtonChecked(wantEventMiles);

                                    //set text for top bar in activity
                                    nearmelayoutshow.setVisibility(View.VISIBLE);
                                    mile = sharedNearMePreference.getNearMeDialogValue();
                                    near_me_text_show.setText(String.valueOf(mile).toString());

                                    //perform close the drawer after click the radio button
                                    drawer.closeDrawer(GravityCompat.END);
                                    nearmelin.setVisibility(View.GONE);
                                    break;
                                case 5:
                                    menu = 2;
                                    wantEventMiles = 50;

                                    //set near me value to the common class
                                    TrafficCommon.Select_Near_Me_Value = wantEventMiles;

                                    //set near me value to the shared preference
                                    sharedNearMePreference.setNearMeDialogValue(wantEventMiles);

                                    //notify click event to all 3 fragment
                                    nearMeSelect1.onNearMeItemClickListener();
                                    nearMeSelect2.onNearMeItemClickListener();
                                    nearMeSelect3.onNearMeItemClickListener();

                                    //save click state for calling method
                                    setRadioButtonChecked(wantEventMiles);

                                    //set text for top bar in activity
                                    nearmelayoutshow.setVisibility(View.VISIBLE);
                                    mile = sharedNearMePreference.getNearMeDialogValue();
                                    near_me_text_show.setText(String.valueOf(mile).toString());

                                    //perform close the drawer after click the radio button
                                    drawer.closeDrawer(GravityCompat.END);
                                    nearmelin.setVisibility(View.GONE);
                                    break;
                                case 6:
                                    menu = 2;
                                    wantEventMiles = 100;

                                    //set near me value to the common class
                                    TrafficCommon.Select_Near_Me_Value = wantEventMiles;

                                    //set near me value to the shared preference
                                    sharedNearMePreference.setNearMeDialogValue(wantEventMiles);

                                    //notify click event to all 3 fragment
                                    nearMeSelect1.onNearMeItemClickListener();
                                    nearMeSelect2.onNearMeItemClickListener();
                                    nearMeSelect3.onNearMeItemClickListener();

                                    //save click state for calling method
                                    setRadioButtonChecked(wantEventMiles);

                                    //set text for top bar in activity
                                    nearmelayoutshow.setVisibility(View.VISIBLE);
                                    mile = sharedNearMePreference.getNearMeDialogValue();
                                    near_me_text_show.setText(String.valueOf(mile).toString());

                                    //perform close the drawer after click the radio button
                                    drawer.closeDrawer(GravityCompat.END);
                                    nearmelin.setVisibility(View.GONE);
                                    break;
                                case 7:
                                    menu = 2;
                                    wantEventMiles = 0;

                                    //set near me value to the common class
                                    TrafficCommon.Select_Near_Me_Value = wantEventMiles;

                                    //set near me value to the shared preference
                                    sharedNearMePreference.setNearMeDialogValue(wantEventMiles);

                                    //notify click event to all 3 fragment
                                    nearMeSelect1.onNearMeItemClickListener();
                                    nearMeSelect2.onNearMeItemClickListener();
                                    nearMeSelect3.onNearMeItemClickListener();

                                    //save click state for calling method
                                    setRadioButtonChecked(wantEventMiles);

                                    //gone the layout fot all event in near me,no text set in top bar
                                    nearmelayoutshow.setVisibility(View.GONE);

                                    //perform close the drawer after click the radio button
                                    drawer.closeDrawer(GravityCompat.END);
                                    nearmelin.setVisibility(View.GONE);
                                    break;
                            }
                        }


                    });
                    //changing variable value
                    menu = 2;
                } else {
                    //gone the near me item click layout
                    nearmelin.setVisibility(View.GONE);
                    //changing the variable value
                    menu = 1;
                }
                break;

            //this is perform for incident menu item click
            case R.id.nav_incident:

                //when click incident from navigation item it open incident fragment in view pager
                firstPageViewPager.setCurrentItem(0);

                //after performing click handel close the navigation drawer
                drawer.closeDrawer(GravityCompat.END);
                break;

            //this is perform for Roadworks menu item click
            case R.id.nav_roadworks:

                //when click incident from navigation item it open roadworks fragment in view pager
                firstPageViewPager.setCurrentItem(1);

                //after performing click handel close the navigation drawer
                drawer.closeDrawer(GravityCompat.END);
                break;

            //this is perform for planned roadworks menu item click
            case R.id.nav_plannedRoadWork:

                //when click incident from navigation item it open roadworks fragment in view pager
                firstPageViewPager.setCurrentItem(2);

                //after performing click handle close the navigation drawer
                drawer.closeDrawer(GravityCompat.END);
                break;

        }
    }


    private void setRadioButtonChecked(int value) {


        if (value == 5) {
            fivemiles.setChecked(true);
        }
        if (value == 10) {
            tenmiles.setChecked(true);
        }
        if (value == 15) {
            fiftenmiles.setChecked(true);
        }
        if (value == 20) {
            twentymiles.setChecked(true);
        }
        if (value == 25) {
            twentyfivemiles.setChecked(true);
        }
        if (value == 50) {
            fiftymiles.setChecked(true);
        }
        if (value == 100) {
            hundredmiles.setChecked(true);
        }
        if (value == 0) {
            allevent.setChecked(true);
        }
    }
}
