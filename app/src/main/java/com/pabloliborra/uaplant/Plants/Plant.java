package com.pabloliborra.uaplant.Plants;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Plant implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long uid;
    private String scientific_name;
    private String family;
    private String information;
    private boolean unlock;

    private long activityId;

    public Plant(String scientific_name, String family, String information, boolean unlock, long activityId) {
        this.scientific_name = scientific_name;
        this.family = family;
        this.information = information;
        this.unlock = unlock;
        this.activityId = activityId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean isUnlock() {
        return unlock;
    }

    public void setUnlock(boolean unlock) {
        this.unlock = unlock;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }
}
