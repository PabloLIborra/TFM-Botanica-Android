package com.pabloliborra.uaplant.Plants;

import com.pabloliborra.uaplant.Routes.RouteListItem;

import java.util.List;

public class PlantsSection {

    private String sectionTitle;
    private List<PlantListItem> plantListItems;

    public PlantsSection(String sectionTitle, List<PlantListItem> routesList) {
        this.sectionTitle = sectionTitle;
        this.plantListItems = routesList;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public List<PlantListItem> getPlantsList() {
        return plantListItems;
    }
}
