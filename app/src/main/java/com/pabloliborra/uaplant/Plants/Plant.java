package com.pabloliborra.uaplant.Plants;

import java.io.Serializable;

public class Plant implements Serializable {

    private String scientific_name;
    private String family;
    private String information;
    private boolean unlock;

    public Plant(String scientific_name, String family, String information, boolean unlock) {
        this.scientific_name = scientific_name;
        this.family = family;
        this.information = information;
        this.unlock = unlock;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public String getFamily() {
        return family;
    }

    public String getInformation() {
        return information;
    }

    public boolean isUnlock() {
        return unlock;
    }

    public void setUnlock(boolean unlock) {
        this.unlock = unlock;
    }
}
