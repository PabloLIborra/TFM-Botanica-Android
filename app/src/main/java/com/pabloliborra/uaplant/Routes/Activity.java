package com.pabloliborra.uaplant.Routes;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.Utils.State;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Activity implements Serializable, Comparable<Activity> {

    private Route route;

    private String title;
    private String subtitle;
    private State state;
    private Double latitude;
    private Double longitude;
    private String information;
    private Date date;
    private List<Question> questions;
    private Plant plant;

    public Activity(String title, String subtitle, State state, Double latitude, Double longitude, String information, Date date, List<Question> questions, Plant plant) {
        this.title = title;
        this.subtitle = subtitle;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.information = information;
        this.date = date;
        this.questions = questions;
        this.plant = plant;
    }

    public Route getRoute() {
        return route;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setState(State state) {
        this.state = state;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public Plant getPlant() {
        return plant;
    }

    @Override
    public int compareTo(Activity o) {
        if (getDate() == null || o.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(o.getDate());
    }
}
