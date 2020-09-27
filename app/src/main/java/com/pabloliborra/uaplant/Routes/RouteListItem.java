package com.pabloliborra.uaplant.Routes;

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
        this.completeActivities = 2;
        this.totalActivities = 4;
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
}
