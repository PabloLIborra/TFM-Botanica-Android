package com.pabloliborra.uaplant.Routes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Relationships;
import com.pabloliborra.uaplant.Utils.StateConverter;
import com.pabloliborra.uaplant.Utils.State;

import java.io.Serializable;
import java.util.List;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Route implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long uid;
    private String title;
    private String description;
    @TypeConverters(StateConverter.class)
    private State state;

    public Route(String title, String description, State state) {
        this.title = title;
        this.description = description;
        this.state = state;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Activity> getActivities(final Context context) {
        return AppDatabase.getDatabaseMain(context).daoApp().loadActivityByRouteId(getUid());
    }
}
