package com.pabloliborra.uaplant.Utils;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.Routes.Activity;
import com.pabloliborra.uaplant.Routes.Question;
import com.pabloliborra.uaplant.Routes.Route;

import java.util.List;

@Dao
public interface DaoApp {
    @Query("SELECT * FROM route")
    List<Route> getAllRoutes();

    @Query("SELECT * FROM route WHERE title = :title")
    Route getRouteByTitle(String title);

    @Query("SELECT * FROM route WHERE uid = :id")
    Route loadRouteById(long id);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAllRoutes(Route... routes);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateRoute(Route route);

    @Delete
    void deleteRoute(Route route);

    @Query("DELETE FROM Route")
    void deleteAllRoutes();

    /////////

    @Query("SELECT * FROM activity")
    List<Activity> getAllActivities();

    @Query("SELECT * FROM activity WHERE uid = :id")
    Activity loadActivityById(long id);

    @Query("SELECT * FROM activity WHERE routeId = :routeId")
    List<Activity> loadActivityByRouteId(long routeId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAllActivities(Activity... activities);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateActivity(Activity activity);

    @Delete
    void deleteActivity(Activity activity);

    @Query("DELETE FROM Activity")
    void deleteAllActivities();

    /////////

    @Query("SELECT * FROM Plant")
    List<Plant> getAllPlants();

    @Query("SELECT * FROM plant WHERE uid = :id")
    Plant loadPlantById(long id);

    @Query("SELECT * FROM plant WHERE activityId = :activityId")
    Plant loadPlantByActivityId(long activityId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAllPlants(Plant... plants);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updatePlant(Plant plant);

    @Delete
    void deletePlant(Plant plant);

    @Query("DELETE FROM Plant")
    void deleteAllPlants();

    /////////

    @Query("SELECT * FROM Question")
    List<Question> getAllQuestions();

    @Query("SELECT * FROM question WHERE uid = :id")
    Question loadQuestionById(long id);

    @Query("SELECT * FROM question WHERE activityId = :activityId")
    List<Question> loadQuestionsByActivityId(long activityId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuestions(Question... questions);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateQuestion(Question question);

    @Delete
    void deleteQuestion(Question question);

    @Query("DELETE FROM Question")
    void deleteAllQuestions();

    /////////////////

    @Transaction
    @Query("SELECT * FROM Route WHERE uid = :id")
    public Relationships.RouteAndActivity getRoutesAndActivity(long id);

    @Transaction
    @Query("SELECT * FROM Activity WHERE uid = :id")
    public Relationships.ActivityAndPlant getActivitiesAndPlant(long id);

    @Transaction
    @Query("SELECT * FROM Activity WHERE uid = :id")
    public Relationships.ActivityAndQuestion getActivitiesAndQuestion(long id);
}

