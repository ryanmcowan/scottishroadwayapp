package com.example.trafficroadworks.allfragmentmodel;

import android.os.AsyncTask;
import android.util.Xml;


import com.example.trafficroadworks.allinterfaces.ResponsePlannedWorkModel;
import com.example.trafficroadworks.universalmodel.DataModel;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlannedRoadWorkFragmentModel extends AsyncTask<String, Void, List<DataModel>> {


    //declaring variable for hold the data from xml parsing from api

    static final String ROAD_PUBLICATION_DATE = "pubDate";
    static final String ROAD_DESCRIPTION = "description";
    static final String ROAD_CHANNEL = "channel";
    static final String ROAD_LINK = "link";
    static final String ROAD_TITLE = "title";
    static final String ROAD_POINT = "point";
    static final String ROAD_AUTHOR = "author";
    static final String ROAD_COMMENT = "comment";
    static final String ROAD_ITEM = "item";

    //declaring list variable
    List<DataModel> dataModelList;

    //declaring incident interface for loading parsing data to fragment
    public ResponsePlannedWorkModel responsePlannedWork = null;

    @Override
    protected List<DataModel> doInBackground(String... strings) {

        //initializing list
        dataModelList = new ArrayList<>();

        //declaring xml pull parser
        XmlPullParser parser = Xml.newPullParser();

        //initializing input stream as null
        InputStream stream = null;

        try {

            // auto-detect the encoding from the stream and save to the variable
            stream = new URL(strings[0]).openConnection().getInputStream();
            parser.setInput(stream, null);
            int datatype = parser.getEventType();
            boolean complete_all = false;
            DataModel dataModel = null;
            while (datatype != XmlPullParser.END_DOCUMENT && !complete_all) {
                String FinalName = null;
                switch (datatype) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        FinalName = parser.getName();
                        if (FinalName.equalsIgnoreCase(ROAD_ITEM)) {
                            dataModel = new DataModel();
                        } else if (dataModel != null) {
                            if (FinalName.equalsIgnoreCase(ROAD_POINT)) {
                                dataModel.setRoad_latlng(parser.nextText().trim());
                            } else if (FinalName.equalsIgnoreCase(ROAD_LINK)) {
                                dataModel.setRoad_link(parser.nextText());
                            } else if (FinalName.equalsIgnoreCase(ROAD_DESCRIPTION)) {
                                dataModel.setRoad_description(parser.nextText().trim());
                            } else if (FinalName.equalsIgnoreCase(ROAD_PUBLICATION_DATE)) {
                                dataModel.setRoad_publication_date(parser.nextText());
                            } else if (FinalName.equalsIgnoreCase(ROAD_TITLE)) {
                                dataModel.setRoad_title_name(parser.nextText().trim());
                            } else if (FinalName.equalsIgnoreCase(ROAD_AUTHOR)) {
                                dataModel.setRoad_author_name(parser.nextText().trim());
                            } else if (FinalName.equalsIgnoreCase(ROAD_COMMENT)) {
                                dataModel.setRoad_situation_comment(parser.nextText().trim());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        FinalName = parser.getName();
                        if (FinalName.equalsIgnoreCase(ROAD_ITEM) && dataModel != null) {
                            dataModelList.add(dataModel);
                        } else if (FinalName.equalsIgnoreCase(ROAD_CHANNEL)) {
                            complete_all = true;
                        }
                        break;
                }
                datatype = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //passing data list
        return dataModelList;
    }


    @Override
    protected void onPostExecute(List<DataModel> dataModelsItems) {
        super.onPostExecute(dataModelsItems);

        //loading the data to interface method and pass the model list which have the all parsing data
        responsePlannedWork.onCompleteProcessPlannedWork(dataModelsItems);
    }
}
