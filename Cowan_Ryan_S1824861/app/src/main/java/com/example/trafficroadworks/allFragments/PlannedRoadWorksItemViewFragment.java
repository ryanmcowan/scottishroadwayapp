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

import com.example.trafficroadworks.alladapter.ItemViewPlannedWorkAdapter;

import com.example.trafficroadworks.allfragmentmodel.PlannedRoadWorkFragmentModel;

import com.example.trafficroadworks.allinterfaces.PlannedRoadWorkListenerAdapterInterface;

import com.example.trafficroadworks.allinterfaces.ResponsePlannedWorkModel;

import com.example.trafficroadworks.permission.PermissionLocation;
import com.example.trafficroadworks.trafficcommon.TrafficCommon;
import com.example.trafficroadworks.univarsalinterfaces.NearMeSelectItem;
import com.example.trafficroadworks.universalmodel.DataModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class PlannedRoadWorksItemViewFragment extends Fragment implements PlannedRoadWorkListenerAdapterInterface, ResponsePlannedWorkModel, NearMeSelectItem {

    //declaring variable in planned work fragment
    private View root;
    private RecyclerView planned_recyceler_view;
    private LinearLayoutManager layoutManagerPlanned;
    private ItemViewPlannedWorkAdapter itemViewplannedAdapter;
    private TextView nodatainfragment;

    //declaring list for holding data
    private List<DataModel> dataModelList;
    private List<DataModel> plannedClarifiedItems;
    private List<DataModel> nearMeEventListFixforPlanned;

    private List<DataModel> plnnednearMeEvent;

    private PlannedRoadWorkFragmentModel plannedRoadWorkFragmentModel;


    //declaring empty fragment constructor
    public PlannedRoadWorksItemViewFragment() {
        // Required empty public constructor
    }

    //this method show the view of the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_planned_road_works_item_view, container, false);

        //return the view
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //fetching Traffic first page instance from this fragment
        TrafficFirstPage.getInstanceTrafficFirstpage().nearMeSelect2 = this;

        //initialization of the variable
        nodatainfragment = root.findViewById(R.id.no_data_text_planedroadworks);
        planned_recyceler_view = root.findViewById(R.id.planed_road_works_recycler_view);
        layoutManagerPlanned = new LinearLayoutManager(getContext());

        //initialization list variable
        dataModelList = new ArrayList<>();
        plannedClarifiedItems = new ArrayList<>();
        nearMeEventListFixforPlanned = new ArrayList<>();

        //set recycler view in grid view where span is 2
        planned_recyceler_view.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //call incident adapter to pass the data list
        itemViewplannedAdapter = new ItemViewPlannedWorkAdapter(getContext(), dataModelList, PlannedRoadWorksItemViewFragment.this);
        planned_recyceler_view.setHasFixedSize(true);

        //setting the adapter to the recycler view for show
        planned_recyceler_view.setAdapter(itemViewplannedAdapter);
        plannedRoadWorkFragmentModel = new PlannedRoadWorkFragmentModel();
        plannedRoadWorkFragmentModel.responsePlannedWork = this;

        //executing the xml parsing model for incident item parsing from api
        plannedRoadWorkFragmentModel.execute("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");

    }


    //near me item click

    @Override
    public void onNearMeItemClickListener() {
        nearmeClarifydata();
    }

    //click listener for incident item when click done go to the second activity
    @Override
    public void onClickListenerForPlannedWork(DataModel model, int itemPosition) {
        //first check whether there is permission or not
        PermissionLocation permissionHelper = new PermissionLocation(getContext());
        if (!permissionHelper.checkPermission()) {
            permissionHelper.requestPermission();
        } else {

            //if permission ok then execute blew part
            Gson gson = new Gson();
            String jsonString = gson.toJson(dataModelList);
            Intent planned_intent = new Intent(getActivity(), TrafficSecondPage.class);
            planned_intent.putExtra("allitemlist", jsonString);
            planned_intent.putExtra("position", itemPosition);
            planned_intent.putExtra("plannedroadworkClick", "PlannedRoadWorksClick");
            startActivity(planned_intent);
        }
    }

    //complete the xml parsing from api and load data to list
    @Override
    public void onCompleteProcessPlannedWork(List<DataModel> result) {
        nodatainfragment.setVisibility(View.GONE);
        dataModelList.clear();
        dataModelList.addAll(result);
        plannedClarifiedItems.addAll(result);
        nearMeEventListFixforPlanned.addAll(result);
        itemViewplannedAdapter.notifyDataSetChanged();
        nearmeClarifydata();
    }

    //near me item filter data by measuring distance
    private void nearmeClarifydata() {

        // For near me data filter
        if (TrafficFirstPage.getInstanceTrafficFirstpage().sharedNearMePreference.getNearMeDialogValue() != 0) {
            getAllEventDistenceForNearMe();
        } else {
            dataModelList.clear();
            dataModelList.addAll(nearMeEventListFixforPlanned);
            itemViewplannedAdapter.notifyDataSetChanged();
        }
    }

    //measure the distance for every event happen and perform in near me item
    private void getAllEventDistenceForNearMe() {

        plnnednearMeEvent = new ArrayList<>();

        for (int i = 0; i < nearMeEventListFixforPlanned.size(); i++) {
            if (nearMeEventListFixforPlanned.get(i).getRoad_latlng() != null) {
                String latlng = nearMeEventListFixforPlanned.get(i).getRoad_latlng();
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
                    plnnednearMeEvent.add(nearMeEventListFixforPlanned.get(i));
                }
            }
        }
        dataModelList.clear();
        dataModelList.addAll(plnnednearMeEvent);
        itemViewplannedAdapter.notifyDataSetChanged();
    }

    //for degree to radius convert
    private double degTorad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    //for radius to degree convert
    private double radTodeg(double rad) {
        return (rad * 180.0 / Math.PI);
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
                itemViewplannedAdapter.notifyDataSetChanged();
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
                itemViewplannedAdapter.notifyDataSetChanged();
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
                searchTraffic(plannedClarifiedItems);
            }
        }
    }
}
