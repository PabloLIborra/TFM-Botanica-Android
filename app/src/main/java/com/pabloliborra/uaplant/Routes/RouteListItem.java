package com.pabloliborra.uaplant.Routes;

import android.content.Context;
import android.util.Log;

import com.pabloliborra.uaplant.Utils.AppDatabase;
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

        if(this.completeActivities == this.totalActivities && this.inProgressActivities == 0 && this.route.getState() != State.COMPLETE) {
            this.route.setState(State.COMPLETE);
            AppDatabase.getDatabaseMain(context).daoApp().updateRoute(this.route);
        } else if(this.inProgressActivities > 0 && this.route.getState() != State.IN_PROGRESS) {
            this.route.setState(State.IN_PROGRESS);
            AppDatabase.getDatabaseMain(context).daoApp().updateRoute(this.route);
        }

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
