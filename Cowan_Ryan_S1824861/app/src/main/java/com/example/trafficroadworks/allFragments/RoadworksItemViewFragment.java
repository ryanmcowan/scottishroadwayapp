package com.example.trafficroadworks.allFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.trafficroadworks.R;
import com.example.trafficroadworks.allActivity.TrafficFirstPage;
import com.example.trafficroadworks.allActivity.TrafficSecondPage;

import com.example.trafficroadworks.alladapter.ItemViewRoadWorksAdapter;

import com.example.trafficroadworks.allfragmentmodel.RoadWorkFragmentModel;
import com.example.trafficroadworks.allinterfaces.ResponseRoadWorksModel;
import com.example.trafficroadworks.allinterfaces.RoadWorkListenerAdapterInterface;

import com.example.trafficroadworks.permission.PermissionLocation;
import com.example.trafficroadworks.trafficcommon.TrafficCommon;
import com.example.trafficroadworks.univarsalinterfaces.NearMeSelectItem;

import com.example.trafficroadworks.universalmodel.DataModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class RoadworksItemViewFragment extends Fragment implements RoadWorkListenerAdapterInterface, ResponseRoadWorksModel, NearMeSelectItem {

    //declaring variable in Roadwork work fragment
    public View root;
    private RecyclerView roadworks_recyceler_view;
    private LinearLayoutManager layoutManagerRoadworks;
    private ItemViewRoadWorksAdapter itemViewRoadworksAdapter;
    private TextView nodatainfragment;

    //declaring list for holding data
    private List<DataModel> dataModelList;
    private List<DataModel> roadworksClarifiedItems;
    private List<DataModel> nearMeEventListFixforRoadworks;

    private List<DataModel> roadworksnearMeEvent;

    private RoadWorkFragmentModel roadWorkFragmentModel;


    //declaring empty fragment constructor
    public RoadworksItemViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_roadworks_item_view, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //fetching Traffic first page instance from this fragment
        TrafficFirstPage.getInstanceTrafficFirstpage().nearMeSelect3 = this;

        //initialization of the variable
        nodatainfragment = root.findViewById(R.id.no_data_road_works);
        roadworks_recyceler_view = root.findViewById(R.id.road_works_recycler_view);
        layoutManagerRoadworks = new LinearLayoutManager(getContext());

        //initialization list variable
        dataModelList = new ArrayList<>();
        roadworksClarifiedItems = new ArrayList<>();
        nearMeEventListFixforRoadworks = new ArrayList<>();

        //set recycler view in grid view where span is 2
        roadworks_recyceler_view.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //call incident adapter to pass the data list
        itemViewRoadworksAdapter = new ItemViewRoadWorksAdapter(getContext(), dataModelList, RoadworksItemViewFragment.this);
        roadworks_recyceler_view.setHasFixedSize(true);

        //setting the adapter to the recycler view for show
        roadworks_recyceler_view.setAdapter(itemViewRoadworksAdapter);
        roadWorkFragmentModel = new RoadWorkFragmentModel();
        roadWorkFragmentModel.responseRoadWork = this;

        //executing the xml parsing model for incident item parsing from api
        roadWorkFragmentModel.execute("https://trafficscotland.org/rss/feeds/roadworks.aspx");

    }

    //complete the xml parsing from api and load data to list
    @Override
    public void onCompleteProcessRoadWork(List<DataModel> result) {

        nodatainfragment.setVisibility(View.GONE);
        dataModelList.clear();
        dataModelList.addAll(result);
        roadworksClarifiedItems.addAll(result);
        nearMeEventListFixforRoadworks.addAll(result);
        itemViewRoadworksAdapter.notifyDataSetChanged();
        nearmeClarifydata();
    }

    //near me item filter data by measuring distance
    private void nearmeClarifydata() {
        // For near me data filter
        if (TrafficFirstPage.getInstanceTrafficFirstpage().sharedNearMePreference.getNearMeDialogValue() != 0) {
            getAllEventDistenceForNearMe();
        } else {
            dataModelList.clear();
            dataModelList.addAll(nearMeEventListFixforRoadworks);
            itemViewRoadworksAdapter.notifyDataSetChanged();
        }
    }


    //measure the distance for every event happen and perform in near me item
    private void getAllEventDistenceForNearMe() {
        roadworksnearMeEvent = new ArrayList<>();

        for (int i = 0; i < nearMeEventListFixforRoadworks.size(); i++) {
            if (nearMeEventListFixforRoadworks.get(i).getRoad_latlng() != null) {
                String latlng = nearMeEventListFixforRoadworks.get(i).getRoad_latlng();
                String[] splitedincident = latlng.split("\\s+");
                double Incidentlatitude = Double.parseDouble(splitedincident[0]);
                double incidentlongitude = Double.parseDouble(splitedincident[1]);

                double theta = TrafficCommon.Current_device_longitude - incidentlongitude;

                double destination = Math.sin(degTorad(TrafficCommon.Current_device_latitude))
                        * Math.sin(degTorad(Incidentlatitude))
                        + Math.cos(degTorad(TrafficCommon.Current_device_latitude))
                        * Math.cos(degTorad(Incidentlatitude))
                        * Math.cos(degTorad(theta));

                destination = Math.acos(destination);
                destination = radTodeg(destination);
                destination = destination * 60 * 1.1515;

                if (destination <= TrafficFirstPage.getInstanceTrafficFirstpage().sharedNearMePreference.getNearMeDialogValue()) {
                    roadworksnearMeEvent.add(nearMeEventListFixforRoadworks.get(i));
                }
            }
        }
        dataModelList.clear();
        dataModelList.addAll(roadworksnearMeEvent);
        itemViewRoadworksAdapter.notifyDataSetChanged();
    }

    //for degree to radius convert
    private double degTorad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //for radius to degree convert
    private double radTodeg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //click listener for incident item when click done go to the second activity
    @Override
    public void onClickListenerForRoadWorks(DataModel model, int itemPosition) {
        //first check whether there is permission or not
        PermissionLocation permissionHelper = new PermissionLocation(getContext());
        if (!permissionHelper.checkPermission()) {
            permissionHelper.requestPermission();
        } else {

            //if permission ok then execute blew part
            Gson gson = new Gson();
            String jsonString = gson.toJson(dataModelList);
            Intent roadworks_intent = new Intent(getActivity(), TrafficSecondPage.class);
            roadworks_intent.putExtra("allitemlist", jsonString);
            roadworks_intent.putExtra("position", itemPosition);
            roadworks_intent.putExtra("roadworksitemClick", "Roadworksitemclick");
            startActivity(roadworks_intent);
        }
    }

    @Override
    public void onNearMeItemClickListener() {
        nearmeClarifydata();
    }


    //searching option for this fragment
    private void searchTraffic(final List<DataModel> searchList) {

        TrafficFirstPage.getInstanceTrafficFirstpage().searchElement.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<DataModel> filteredList = new ArrayList<>();
                if (query.isEmpty()) {
                    filteredList = searchList;
                } else {
                    for (DataModel item : searchList) {
                        if (item.getRoad_title_name().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }
                dataModelList.clear();
                dataModelList.addAll(filteredList);
                itemViewRoadworksAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<DataModel> filteredList = new ArrayList<>();
                if (newText.isEmpty() || newText.equals(" ")) {
                    filteredList = searchList;
                } else {

                    for (DataModel searchItem : searchList) {
                        if (searchItem.getRoad_title_name() != null) {
                            if (searchItem.getRoad_title_name().toLowerCase().contains(newText.toLowerCase())) {
                                filteredList.add(searchItem);
                            }
                        }
                    }
                }
                dataModelList.clear();
                dataModelList.addAll(filteredList);
                itemViewRoadworksAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    //for finding search data when load the data initially
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            if (dataModelList != null) {
                searchTraffic(roadworksClarifiedItems);
            }
        }
    }
}
