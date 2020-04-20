package com.example.trafficroadworks.universalmodel;

import com.google.android.gms.maps.model.LatLng;

public class ModelMp {

    //declaring map model variable for showing data to the info window

    private String Map_road_name;
    private String Map_item_location;
    private String Map_road_s_date;
    private String Map_road_e_date;
    private String Map_road_des_information;
    private String Map_road_link;
    private String Map_road_author_name;
    private String Map_road_situation_comment;
    private String Map_road_publication_date;
    private LatLng Map_road_LatLng;

    //calling empty constructor
    public ModelMp() {
    }

    //calling all method constructor
    public ModelMp(String map_road_name, String map_item_location, String map_road_s_date, String map_road_e_date, String map_road_des_information, String map_road_link, String map_road_author_name, String map_road_situation_comment, String map_road_publication_date, LatLng map_road_LatLng) {
        Map_road_name = map_road_name;
        Map_item_location = map_item_location;
        Map_road_s_date = map_road_s_date;
        Map_road_e_date = map_road_e_date;
        Map_road_des_information = map_road_des_information;
        Map_road_link = map_road_link;
        Map_road_author_name = map_road_author_name;
        Map_road_situation_comment = map_road_situation_comment;
        Map_road_publication_date = map_road_publication_date;
        Map_road_LatLng = map_road_LatLng;
    }

    //setting getter and setter method to all the variable
    public String getMap_road_name() {
        return Map_road_name;
    }

    public void setMap_road_name(String map_road_name) {
        Map_road_name = map_road_name;
    }

    public String getMap_item_location() {
        return Map_item_location;
    }

    public void setMap_item_location(String map_item_location) {
        Map_item_location = map_item_location;
    }

    public String getMap_road_s_date() {
        return Map_road_s_date;
    }

    public void setMap_road_s_date(String map_road_s_date) {
        Map_road_s_date = map_road_s_date;
    }

    public String getMap_road_e_date() {
        return Map_road_e_date;
    }

    public void setMap_road_e_date(String map_road_e_date) {
        Map_road_e_date = map_road_e_date;
    }

    public String getMap_road_des_information() {
        return Map_road_des_information;
    }

    public void setMap_road_des_information(String map_road_des_information) {
        Map_road_des_information = map_road_des_information;
    }

    public String getMap_road_link() {
        return Map_road_link;
    }

    public void setMap_road_link(String map_road_link) {
        Map_road_link = map_road_link;
    }

    public String getMap_road_author_name() {
        return Map_road_author_name;
    }

    public void setMap_road_author_name(String map_road_author_name) {
        Map_road_author_name = map_road_author_name;
    }

    public String getMap_road_situation_comment() {
        return Map_road_situation_comment;
    }

    public void setMap_road_situation_comment(String map_road_situation_comment) {
        Map_road_situation_comment = map_road_situation_comment;
    }

    public String getMap_road_publication_date() {
        return Map_road_publication_date;
    }

    public void setMap_road_publication_date(String map_road_publication_date) {
        Map_road_publication_date = map_road_publication_date;
    }

    public LatLng getMap_road_LatLng() {
        return Map_road_LatLng;
    }

    public void setMap_road_LatLng(LatLng map_road_LatLng) {
        Map_road_LatLng = map_road_LatLng;
    }
}
