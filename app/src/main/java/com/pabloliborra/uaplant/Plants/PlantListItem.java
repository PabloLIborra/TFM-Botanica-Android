package com.pabloliborra.uaplant.Plants;

import android.util.Log;

import com.pabloliborra.uaplant.Routes.Route;

public class PlantListItem {
    private Plant plant;
    private String title;

    public PlantListItem(Plant plant) {
        this.plant = plant;
        this.title = plant.getScientific_name();
    }

    public String getTitle() {
        return this.title;
    }

    public Plant getPlant() {
        return plant;
    }
}
