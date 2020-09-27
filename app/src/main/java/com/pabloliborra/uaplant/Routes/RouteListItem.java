package com.pabloliborra.uaplant.Routes;

import android.util.Log;

public class RouteListItem {
    private Route route;
    private String title;
    private String description;
    private int completeActivities;
    private int totalActivities;

    public RouteListItem(Route route) {
        this.route = route;
        this.title = route.getTitle();
        this.description = route.getDescription();
        this.completeActivities = 0;
        this.totalActivities = route.getActivities().size();
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

    public int getTotalActivities() {
        return this.totalActivities;
    }

    public Route getRoute() {
        return route;
    }
}
