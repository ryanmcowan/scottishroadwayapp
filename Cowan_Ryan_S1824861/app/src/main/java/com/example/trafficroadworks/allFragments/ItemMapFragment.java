package com.example.trafficroadworks.allFragments;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trafficroadworks.R;
import com.example.trafficroadworks.universalmodel.DataModel;
import com.example.trafficroadworks.universalmodel.ModelMp;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemMapFragment extends Fragment {


    //declaring variable in map fragment
    View root;

    private TextView eventName_inMapInfoWindow;
    private TextView location__inMapInfoWindow;
    private TextView startDate_inMapInfoWindow;
    private TextView endDate_inMapInfoWindow;
    private TextView delayInformation_inMapInfoWindow;
    private TextView link_inMapInfoWindow;
    private TextView pubDate_inMapInfoWindow;


    public int Positionmaps;

    //declaring list variable
    public List<DataModel> getDataList;
    public List<LatLng> latLngListforMapShow;

    //for view click item in map
    public LatLng FirstLocationShowInTheMaps;
    //

    //location

    public LatLng Latitude_longitude;

    private GoogleMap mMap;


    //for info_dialog in map marker

    private String start_date_from_description_for_mapWindow;
    private String end_date_from_description_for_mapWindow;
    private List<ModelMp> even_name_inMap_Show;
    private String delay_information1;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {


            //initializing variable
            mMap = googleMap;
            for (int i = 0; i < latLngListforMapShow.size(); i++) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLngListforMapShow.get(i))
                        .icon(bitmapDescriptorFromVector(getContext(), R.drawable.location_map)));

            }
            //for showing info dialog

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    //for creating custom info dialog

                    View v = getLayoutInflater().inflate(R.layout.info_window_for_map, null);
                    eventName_inMapInfoWindow = (TextView) v.findViewById(R.id.Event_name_In_Map_Info_Window);
                    location__inMapInfoWindow = (TextView) v.findViewById(R.id.location_In_Map_Info_Window);
                    startDate_inMapInfoWindow = (TextView) v.findViewById(R.id.start_date_In_Map_Info_Window);
                    endDate_inMapInfoWindow = (TextView) v.findViewById(R.id.end_date_In_Map_Info_Window);
                    link_inMapInfoWindow = (TextView) v.findViewById(R.id.link_In_Map_Info_Window);
                    delayInformation_inMapInfoWindow = (TextView) v.findViewById(R.id.delay_information_In_Map_Info_Window);
                    pubDate_inMapInfoWindow = (TextView) v.findViewById(R.id.publication_date_In_Map_Info_Window);


                    for (int i = 0; i < even_name_inMap_Show.size(); i++) {
                        ModelMp InfoWindowItem = even_name_inMap_Show.get(i);
                        if (InfoWindowItem.getMap_road_LatLng().equals(marker.getPosition())) {

                            //setting custom info dialog data for each element

                            eventName_inMapInfoWindow.setText(InfoWindowItem.getMap_road_name());
                            location__inMapInfoWindow.setText("Location: " + InfoWindowItem.getMap_item_location());
                            startDate_inMapInfoWindow.setText(InfoWindowItem.getMap_road_s_date());
                            endDate_inMapInfoWindow.setText(InfoWindowItem.getMap_road_e_date());
                            delayInformation_inMapInfoWindow.setText(InfoWindowItem.getMap_road_des_information());
                            link_inMapInfoWindow.setText("Link: " + InfoWindowItem.getMap_road_link());
                            pubDate_inMapInfoWindow.setText("Publication Date: " + InfoWindowItem.getMap_road_publication_date());
                        } else {
                        }
                    }
                    return v;
                }
            });


            //show first time info window and location hen map open

            LatLng latLng = FirstLocationShowInTheMaps;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(bitmapDescriptorFromVector(getContext(), R.drawable.location_map)));
            marker.showInfoWindow();
        }
    };

    //for view data to fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_item_map, container, false);

        //initializing list
        latLngListforMapShow = new ArrayList<>();

        //getting intent data from fist page activity

        if (getActivity().getIntent() != null) {

            String returnData = getActivity().getIntent().getStringExtra("allitemlist");

            //getting model data through the Gson way
            Gson gson = new Gson();
            Type listOfdataType = new TypeToken<List<DataModel>>() {
            }.getType();
            getDataList = gson.fromJson(returnData, listOfdataType);
        }

        //getting item click position
        if (getActivity().getIntent().getIntExtra("position", 0) >= 0) {
            Positionmaps = getActivity().getIntent().getIntExtra("position", 0);
        }

        //for listing data
        even_name_inMap_Show = new ArrayList<ModelMp>();

        for (int i = 0; i < getDataList.size(); i++) {

            //get location data from api and split it into a latitude and longitude


            if (getDataList.get(i).getRoad_latlng() != null) {

                //split latitude and longitude from geo location in api
                String latlng = getDataList.get(i).getRoad_latlng();
                String[] splited = latlng.split("\\s+");
                double latitude = Double.parseDouble(splited[0]);
                double longitude = Double.parseDouble(splited[1]);

                Latitude_longitude = new LatLng(latitude, longitude);

                latLngListforMapShow.add(Latitude_longitude);

            } else {

                Latitude_longitude = new LatLng(0, 0);
            }

            //get description data from api and split it into a start date ,end date and delay information;

            if (getDataList.get(i).getRoad_latlng() != null) {
                String description = getDataList.get(i).getRoad_description();
                String[] splited_description_map = description.split("<br />");


                //split data check
                if (splited_description_map.length < 2) {
                    start_date_from_description_for_mapWindow = splited_description_map[0];
                    end_date_from_description_for_mapWindow = "no information";
                    delay_information1 = "no information";

                    //split data check
                } else if (splited_description_map.length < 3) {

                    start_date_from_description_for_mapWindow = splited_description_map[0];
                    end_date_from_description_for_mapWindow = splited_description_map[1];
                    delay_information1 = "no information";

                    //split data check
                } else {
                    start_date_from_description_for_mapWindow = splited_description_map[0];
                    end_date_from_description_for_mapWindow = splited_description_map[1];
                    delay_information1 = splited_description_map[2];
                }
            } else {
                start_date_from_description_for_mapWindow = "no information";
                end_date_from_description_for_mapWindow = "no information";
                delay_information1 = "no information";
            }


            //setting split data to map model for show data in info window

            ModelMp modelmap = new ModelMp(getDataList.get(i).getRoad_title_name(), getDataList.get(i).getRoad_title_name(), start_date_from_description_for_mapWindow,
                    end_date_from_description_for_mapWindow, delay_information1, getDataList.get(i).getRoad_link()
                    , getDataList.get(i).getRoad_author_name(), getDataList.get(i).getRoad_situation_comment(),
                    getDataList.get(i).getRoad_publication_date(), Latitude_longitude);

            even_name_inMap_Show.add(modelmap);
        }


        //for click item view in map

        DataModel mapitem = getDataList.get(Positionmaps);
        if (mapitem.getRoad_latlng() != null) {
            //split data from geo location in api
            String ViewLocation_atFirst_in_map = mapitem.getRoad_latlng();
            String[] splitedgeorss = ViewLocation_atFirst_in_map.split("\\s+");
            double latitude_first_view_In_map = Double.parseDouble(splitedgeorss[0]);
            double longitude_first_view_In_map = Double.parseDouble(splitedgeorss[1]);

            FirstLocationShowInTheMaps = new LatLng(latitude_first_view_In_map, longitude_first_view_In_map);
        } else {
            FirstLocationShowInTheMaps = Latitude_longitude;
        }

        return root;
    }

    //using it for use and set custom location indicator
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.location_map);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //using for map callback view
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
