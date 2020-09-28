package com.pabloliborra.uaplant.Routes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pabloliborra.uaplant.Utils.State;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Route implements Serializable {

    private String title;
    private String description;
    private State state;
    private List<Activity> activities;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Route(String title, String description, State state, List<Activity> activities) {
        this.title = title;
        this.description = description;
        this.state = state;
        this.activities = activities;
        Collections.sort(this.activities);
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public State getState() {
        return this.state;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
