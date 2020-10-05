package com.pabloliborra.uaplant.Plants;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pabloliborra.uaplant.Routes.Activity;
import com.pabloliborra.uaplant.Utils.ListStringConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Plant implements Serializable, Comparable<Plant> {

    @PrimaryKey(autoGenerate = true)
    private long uid;
    private String scientific_name;
    private String family;
    private String information;
    private boolean unlock;
    @TypeConverters(ListStringConverter.class)
    private List<String> images;

    private long activityId;

    public Plant(String scientific_name, String family, String information, boolean unlock, long activityId) {
        this.scientific_name = scientific_name;
        this.family = family;
        this.information = information;
        this.unlock = unlock;
        this.activityId = activityId;
        this.images = new ArrayList<>();
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

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public void addImage(String image) {
        if(!images.equals("")) {
            this.images.add(image);
        }
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    @Override
    public int compareTo(Plant o) {
        if (scientific_name == null || o.scientific_name == null) {
            return 0;
        }
        return scientific_name.compareTo(o.scientific_name);
    }
}
