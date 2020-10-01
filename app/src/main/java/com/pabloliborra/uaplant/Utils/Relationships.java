package com.pabloliborra.uaplant.Utils;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.Routes.Activity;
import com.pabloliborra.uaplant.Routes.Question;
import com.pabloliborra.uaplant.Routes.Route;

import java.util.List;

public class Relationships {
    public static class RouteAndActivity {
        @Embedded
        public Route route;
        @Relation(
                parentColumn = "uid",
                entityColumn = "routeId"
        )
        public List<Activity> activities;
    }

    public static class ActivityAndPlant {
        @Embedded
        public Activity activity;
        @Relation(
                parentColumn = "uid",
                entityColumn = "activityId"
        )
        public Plant plant;
    }

    public static class ActivityAndQuestion {
        @Embedded
        public Activity activity;
        @Relation(
                parentColumn = "uid",
                entityColumn = "activityId"
        )
        public List<Question> questions;
    }
}
