package com.example.trafficroadworks.allFragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.trafficroadworks.R;
import com.example.trafficroadworks.universalmodel.DataModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ItemDetailsFragment extends Fragment {

    //declaring variable in  ItemDetailsFragment fragment
    private View root;

    //declaring list variable
    private List<DataModel> itemsventdetails;
    private int position_item;

    //tool bar
    private String titletoolbar_event_details;


    //declaring initial variable for spliting data

    private String descriptionstartdate_details;
    private String descriptionenddate_details;
    private String descriptiondelayinformation_details;
    private String start_date_from_description_final;
    private String end_date_from_description_final;
    private String delay_information1_from_description_final;

    private String incident_reason_final_from_description;

    private String incident_description_from_final_status;
    private String incident_description_from_final_link;


    //set initial value fro these string
    private String Details_Incident = "demo_incident";
    private String Details_Roadworks = "demo_road";
    private String Details_PlanedWork = "demo_plan";

    //create empty constructor
    public ItemDetailsFragment() {
        // Required empty public constructor
    }

    //for view the data into the layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_item_details, container, false);


        //getting data from the intent
        if (getActivity().getIntent() != null) {
            String returnStr = getActivity().getIntent().getStringExtra("allitemlist");

            //getting model data through Gson way
            Gson gson = new Gson();
            Type listOfdoctorType = new TypeToken<List<DataModel>>() {
            }.getType();

            itemsventdetails = gson.fromJson(returnStr, listOfdoctorType);
        }


        //getting item position from intent

        if (getActivity().getIntent().getIntExtra("position", 0) >= 0) {
            position_item = getActivity().getIntent().getIntExtra("position", 0);

        }

        //getting which item click from incident
        if (getActivity().getIntent().getStringExtra("incidentitemClick") != null) {
            Details_Incident = getActivity().getIntent().getStringExtra("incidentitemClick");
        }
        if (getActivity().getIntent().getStringExtra("roadworksitemClick") != null) {
            Details_Roadworks = getActivity().getIntent().getStringExtra("roadworksitemClick");
        }
        if (getActivity().getIntent().getStringExtra("plannedroadworkClick") != null) {
            Details_PlanedWork = getActivity().getIntent().getStringExtra("plannedroadworkClick");
        }

        if (itemsventdetails.get(position_item).getRoad_title_name() != null) {

            String event_title = itemsventdetails.get(position_item).getRoad_title_name();
            String[] splited = event_title.split("-");
            titletoolbar_event_details = splited[0];

            Toolbar toolbardetails = (Toolbar) getActivity().findViewById(R.id.toolbar_second);
            toolbardetails.setTitle(titletoolbar_event_details);
        }


        //get description data from api and split it into a start date ,end date and delay information;

        if (itemsventdetails.get(position_item).getRoad_description() != null) {

            //spliting data from description in api xml parsing
            String description = itemsventdetails.get(position_item).getRoad_description();
            String[] splited_description = description.split("<br />");


            //check split
            if (splited_description.length < 2) {

                descriptionstartdate_details = splited_description[0];

                if (descriptionstartdate_details.startsWith("Start")) {
                    start_date_from_description_final = descriptionstartdate_details;
                } else {
                    start_date_from_description_final = "No Information";
                }
                descriptionenddate_details = "no information";
                descriptiondelayinformation_details = "no information";


                //check split
            } else if (splited_description.length < 3) {

                descriptionstartdate_details = splited_description[0];
                descriptionenddate_details = splited_description[1];


                if (descriptionstartdate_details.startsWith("Start")) {
                    start_date_from_description_final = descriptionstartdate_details;
                } else {
                    start_date_from_description_final = "No Information";
                }

                if (descriptionenddate_details.startsWith("End")) {
                    end_date_from_description_final = descriptionenddate_details;
                } else {
                    end_date_from_description_final = "No Information";
                }

                descriptiondelayinformation_details = "no information";

                //check split
            } else {

                descriptionstartdate_details = splited_description[0];
                descriptionenddate_details = splited_description[1];
                descriptiondelayinformation_details = splited_description[2];

                if (descriptionstartdate_details.startsWith("Start")) {
                    start_date_from_description_final = descriptionstartdate_details;
                } else {
                    start_date_from_description_final = "No Information";
                }

                if (descriptionenddate_details.startsWith("End")) {
                    end_date_from_description_final = descriptionenddate_details;
                } else {
                    end_date_from_description_final = "No Information";
                }
                if (descriptiondelayinformation_details.startsWith("Delay")) {
                    delay_information1_from_description_final = descriptiondelayinformation_details;
                } else {
                    delay_information1_from_description_final = "No Information";
                }

            }
            //check split
        } else {
            descriptionstartdate_details = "no information";
            descriptionenddate_details = "no information";
            descriptiondelayinformation_details = "no information";
        }

        //get description data from api and split it into a reason,status and link information;

        if (itemsventdetails.get(position_item).getRoad_description() != null) {

            incident_reason_final_from_description = itemsventdetails.get(position_item).getRoad_description();
            incident_description_from_final_status = itemsventdetails.get(position_item).getRoad_description();
            incident_description_from_final_link = itemsventdetails.get(position_item).getRoad_link();
        }


        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //initialize all variable for details

        TextView Location = root.findViewById(R.id.details_Location);
        TextView Location_details_title = root.findViewById(R.id.details_Location_title);
        TextView Reason = root.findViewById(R.id.reason_details);
        TextView Reason_details_title = root.findViewById(R.id.reason_details_title);
        TextView Status = root.findViewById(R.id.status_details);
        TextView Status_details_title = root.findViewById(R.id.status_details_title);
        TextView Link = root.findViewById(R.id.link_details);
        TextView Link_details_title = root.findViewById(R.id.link_details_title);


        //for roadworks item details

        if (Details_Roadworks.equals("Roadworksitemclick")) {

            Location.setText("Location");
            Reason.setText("Start Date");
            Status.setText("End Date");
            Link.setText("Delay Information");


            //set data for click item

            Location_details_title.setText(itemsventdetails.get(position_item).getRoad_title_name());
            Reason_details_title.setText(start_date_from_description_final);
            Status_details_title.setText(end_date_from_description_final);
            Link_details_title.setText(descriptiondelayinformation_details);
        }

        //for incident item details

        if (Details_Incident.equals("Incidentitemclick")) {
            Location.setText("Location");
            Reason.setText("Reason");
            Status.setText("Status");
            Link.setText("Link");


            //set data for click item

            Location_details_title.setText(itemsventdetails.get(position_item).getRoad_title_name());
            Reason_details_title.setText(incident_description_from_final_status);
            Status_details_title.setText(incident_description_from_final_status);
            Link_details_title.setText(incident_description_from_final_link);

        }
        //for planed work item details

        if (Details_PlanedWork.equals("PlannedRoadWorksClick")) {
            Location.setText("Location");
            Reason.setText("Start Date");
            Status.setText("End Date");
            Link.setText("Description");


            //set data for click item

            Location_details_title.setText(itemsventdetails.get(position_item).getRoad_title_name());
            Reason_details_title.setText(start_date_from_description_final);
            Status_details_title.setText(end_date_from_description_final);
            Link_details_title.setText(descriptiondelayinformation_details);

        }
    }
}
