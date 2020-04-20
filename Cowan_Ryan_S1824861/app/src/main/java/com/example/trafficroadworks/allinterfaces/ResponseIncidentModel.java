package com.example.trafficroadworks.allinterfaces;

import com.example.trafficroadworks.universalmodel.DataModel;

import java.util.List;

public interface ResponseIncidentModel {
    //response model which taken data from parsing model
    void onCompleteProcessIncident(List<DataModel> result);
}
