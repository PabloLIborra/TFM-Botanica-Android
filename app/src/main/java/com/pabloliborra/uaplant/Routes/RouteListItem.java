package com.pabloliborra.uaplant.Routes;

import android.content.Context;
import android.util.Log;

import com.pabloliborra.uaplant.Utils.State;

public class RouteListItem {
    private Route route;
    private String title;
    private String description;
    private int completeActivities;
    private int inProgressActivities;
    private int totalActivities;

    public RouteListItem(Route route, Context context, int numActivities) {
        this.route = route;
        this.title = route.getTitle();
        this.description = route.getDescription();
        this.completeActivities = 0;
        for(Activity a:route.getActivities(context)) {
            if(a.getState() == State.COMPLETE) {
                this.completeActivities++;
            } else if(a.getState() == State.IN_PROGRESS) {
                this.inProgressActivities++;
            }
        }
        this.totalActivities = numActivities;
        Log.d("Actividades", String.valueOf(this.totalActivities));
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCompleteActivities() {
        return this.completeActivities;
    }
    
    public int getInProgressActivities() {
        return this.inProgressActivities;
    }

    public int getTotalActivities() {
        return this.totalActivities;
    }

    public Route getRoute() {
        return route;
    }
}
