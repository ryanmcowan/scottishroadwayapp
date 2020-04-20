package com.example.trafficroadworks.allinterfaces;

import com.example.trafficroadworks.universalmodel.DataModel;

import java.util.List;

public interface ResponseRoadWorksModel {
    //response model which taken data from parsing model
    void onCompleteProcessRoadWork(List<DataModel> result);
}
