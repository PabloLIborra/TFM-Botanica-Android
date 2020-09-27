package com.pabloliborra.uaplant.Routes;

import com.pabloliborra.uaplant.Utils.State;

import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable {

    private String title;
    private String subtitle;
    private State state;
    private Double latitude;
    private Double longitude;
    private String information;
    private Date date;

    private Route route;

    public Activity(String title, String subtitle, State state, Double latitude, Double longitude, String information, Date date) {
        this.title = title;
        this.subtitle = subtitle;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.information = information;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public State getState() {
        return state;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getInformation() {
        return information;
    }

    public Date getDate() {
        return date;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }
}
