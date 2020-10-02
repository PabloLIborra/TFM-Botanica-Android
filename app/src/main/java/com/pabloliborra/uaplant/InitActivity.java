package com.pabloliborra.uaplant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.Routes.Activity;
import com.pabloliborra.uaplant.Routes.Question;
import com.pabloliborra.uaplant.Routes.Route;
import com.pabloliborra.uaplant.Routes.RoutesSection;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Relationships;
import com.pabloliborra.uaplant.Utils.State;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InitActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        Button initButton = findViewById(R.id.initButton);
        initButton.setBackgroundResource(R.drawable.rounded_button_primary);
        initButton.setTextSize(18);
        initButton.setTypeface(null, Typeface.BOLD);
        initButton.setText(R.string.init_button);
        initButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton();
            }
        });

        createData();
    }

    private void startButton() {
        Intent intent = new Intent(this, TabBarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createData() {
        List<Route> routes = new ArrayList<>();

        List<String> answers = new ArrayList<>();
        answers.add("Prueba");
        answers.add("Arbol");
        answers.add("Planta");

        Route route = new Route("Gimnospermas", "Información de prueba", State.AVAILABLE);
        routes.add(route);

        for(Route r:routes) {
            if(AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().getRouteByTitle(r.getTitle()) == null) {
                AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().insertAllRoutes(r);

                routes = AppDatabase.getDatabase(getApplicationContext()).daoApp().getAllRoutes();

                List<Activity> activities = new ArrayList<>();
                Activity activity = new Activity("Actividad", "Subtitulo", State.AVAILABLE, 38.3864202198746, -0.514705561099236, "Informacion de la actividad.", new Date(), routes.get(0).getUid());
                activities.add(activity);
                Activity activity2 = new Activity("Prueba2", "Subtitulo", State.INACTIVE, 38.38642023, -0.52, "Informacion", new Date(), routes.get(0).getUid());
                activities.add(activity2);

                for(Activity a:activities) {
                    AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().insertAllActivities(a);
                }

                activities = AppDatabase.getDatabase(getApplicationContext()).daoApp().getAllActivities();

                final List<Plant> plants = new ArrayList<>();
                Plant plant = new Plant("Cupressus sempervirens", "Cupressaceae", "Informacion", false, activities.get(0).getUid());
                plants.add(plant);
                Plant plant2 = new Plant("Cupressus sempervirens2", "Cupressaceae", "Informacion", false, activities.get(1).getUid());
                plants.add(plant2);

                for(Plant p:plants) {
                    AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().insertAllPlants(p);
                }

                final List<Question> questions = new ArrayList<>();
                Question question = new Question("Hábito", answers, "Arbol", activities.get(0).getUid());
                questions.add(question);
                Question question2 = new Question("Hábito2", answers, "Arbol", activities.get(1).getUid());
                questions.add(question2);

                for(Question q:questions) {
                    AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().insertAllQuestions(q);
                }
            }
        }
    }
}
