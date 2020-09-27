package com.pabloliborra.uaplant.Routes;

import android.media.Image;

import java.util.List;

public class RoutesSection {

    private int sectionImage;
    private String sectionTitle;
    private List<RouteListItem> routesList;

    public RoutesSection(int sectionImage, String sectionTitle, List<RouteListItem> routesList) {
        this.sectionImage = sectionImage;
        this.sectionTitle = sectionTitle;
        this.routesList = routesList;
    }

    public int getSectionImage() {
        return sectionImage;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public List<RouteListItem> getRoutesList() {
        return routesList;
    }
}
