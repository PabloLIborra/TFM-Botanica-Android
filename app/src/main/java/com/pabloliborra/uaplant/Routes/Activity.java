package com.pabloliborra.uaplant.Routes;

import android.content.Context;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.DataConverter;
import com.pabloliborra.uaplant.Utils.StateConverter;
import com.pabloliborra.uaplant.Utils.State;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Activity implements Serializable, Comparable<Activity> {

    @PrimaryKey(autoGenerate = true)
    private long uid;
    private String title;
    private String subtitle;
    @TypeConverters(StateConverter.class)
    private State state;
    private Double latitude;
    private Double longitude;
    private String information;
    @TypeConverters(DataConverter.class)
    private Date date;

    private long routeId;

    public Activity(String title, String subtitle, State state, Double latitude, Double longitude, String information, Date date, long routeId) {
        this.title = title;
        this.subtitle = subtitle;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.information = information;
        this.date = date;
        this.routeId = routeId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public List<Question> getQuestions(final Context context) {
        return AppDatabase.getDatabaseMain(context).daoApp().loadQuestionsByActivityId(getUid());
    }

    public Plant getPlant(Context context) {
        return AppDatabase.getDatabaseMain(context).daoApp().loadPlantByActivityId(getUid());
    }

    @Override
    public int compareTo(Activity o) {
        if (date == null || o.date == null) {
            return 0;
        }
        return date.compareTo(o.date);
    }
}
