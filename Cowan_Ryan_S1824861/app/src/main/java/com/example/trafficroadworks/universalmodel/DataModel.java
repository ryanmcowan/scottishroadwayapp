package com.example.trafficroadworks.universalmodel;

public class DataModel {

    //declaring the data which fetch from the xml data parsing
    private String road_publication_date;
    private String road_description;
    private String road_link;
    private String road_title_name;
    private String road_latlng;
    private String road_author_name;
    private String road_situation_comment;

    //calling empty constructor
    public DataModel() {
    }

    //calling constructor again
    public DataModel(String road_link, String road_title_name) {
        this.road_link = road_link;
        this.road_title_name = road_title_name;
    }


    //setting all variable getter and setter method for data fetch.
    public String getRoad_publication_date() {
        return road_publication_date;
    }

    public void setRoad_publication_date(String road_publication_date) {
        this.road_publication_date = road_publication_date;
    }

    public String getRoad_description() {
        return road_description;
    }

    public void setRoad_description(String road_description) {
        this.road_description = road_description;
    }

    public String getRoad_link() {
        return road_link;
    }

    public void setRoad_link(String road_link) {
        this.road_link = road_link;
    }

    public String getRoad_title_name() {
        return road_title_name;
    }

    public void setRoad_title_name(String road_title_name) {
        this.road_title_name = road_title_name;
    }

    public String getRoad_latlng() {
        return road_latlng;
    }

    public void setRoad_latlng(String road_latlng) {
        this.road_latlng = road_latlng;
    }

    public String getRoad_author_name() {
        return road_author_name;
    }

    public void setRoad_author_name(String road_author_name) {
        this.road_author_name = road_author_name;
    }

    public String getRoad_situation_comment() {
        return road_situation_comment;
    }

    public void setRoad_situation_comment(String road_situation_comment) {
        this.road_situation_comment = road_situation_comment;
    }
}
