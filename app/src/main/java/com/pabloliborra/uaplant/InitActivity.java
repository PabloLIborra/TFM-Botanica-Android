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
        answers.add("arbusto con aspecto de palmera");
        answers.add("Arbol");
        answers.add("Planta");

        Route route = new Route("Gimnospermas", "Itinerario común", State.AVAILABLE);
        routes.add(route);

        for(Route r:routes) {
            if(AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().getRouteByTitle(r.getTitle()) == null) {
                AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().insertAllRoutes(r);

                routes = AppDatabase.getDatabase(getApplicationContext()).daoApp().getAllRoutes();

                List<Activity> activities = new ArrayList<>();
                Activity activity = new Activity("Cupressus sempervirens L.", "Actividad situada situada entre el edifico del Rectorado y la Biblioteca General", State.AVAILABLE, 38.3839611, -0.5116916666666667, "Esta actividad se va a desarrollar en una zona ajardinada situada entre el edifico del Rectorado y la Biblioteca General. Nuestro objetivo es descubrir una de las gimnospermas con mayor significado en nuestra cultura mediterránea, a través de unas preguntas sobre caracteres morfológicos visibles, que nos llevarán al nombre de la especie.\n", new Date(), routes.get(0).getUid());
                activities.add(activity);
                //Activity activity2 = new Activity("Prueba2", "Subtitulo", State.INACTIVE, 38.38642023, -0.52, "Informacion", new Date(), routes.get(0).getUid());
                //activities.add(activity2);

                for(Activity a:activities) {
                    AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().insertAllActivities(a);
                }

                activities = AppDatabase.getDatabase(getApplicationContext()).daoApp().getAllActivities();

                final List<Plant> plants = new ArrayList<>();
                Plant plant = new Plant("Cupressus sempervirens", "Cupressaceae", "Nombre común: Ciprés\n\nEtimología. El nombre del género Cupressus, parece que deriva del nombre latino “Cyprus” (Chipre), lugar donde es nativo y crece silvestre. El epíteto específico sempervirens, hace referencia a que las hojas son perennes y la planta siempre se muestra verde.\n\nOrigen y distribución. Es originario del Mediterráneo oriental. Pero desde la antigüedad ha sido cultivado en jardines y cementerios en gran parte de la cuenca mediterránea, e incluso se ha utilizado como setos o barrera vegetal para proteger cultivos.\n\nDescripción. Árbol que puede alcanzar hasta 30 m de altura, con ramas cortas y aplicadas al tronco, de porte columnar (foto 2). Corteza grisácea, con fisuras longitudinales que no se exfolian. Hojas pequeñas, escuamiformes, de un verde obscuro, con una franja blanco-hialina en el margen, de ápice obtuso, sin glándulas resiníferas, e imbricadas sobre las ramas finas, cubriéndolas por completo (foto 3). Son árboles monoicos, es decir, con estructuras reproductoras masculinas y femeninas en un mismo individuo. Conos masculinos pequeños y efímeros, terminales, de color amarillento; los femeninos, globosos, de 2-3,5 cm de diámetro, formados por 1-14 escamas, de un color gris-marrón en la madurez (foto 4).\n\nObservaciones. Tiene un significado etnobotánico muy importante en la cultura greco-latina, ya que se ha relacionado con la muerte y la vida eterna. Sus hojas, siempre verdes (incluso en lo más seco y cálido del verano) y su particular forma de crecimiento (esbelto, apuntando hacia al cielo), han hecho de ella una planta ceremonial, solemne, que vemos asiduamente en nuestros cementerios y monumentos funerarios.\n\nProtección. Está calificada como LC (“preocupación menor”) por la UICN.", false, activities.get(0).getUid());
                plants.add(plant);
                //Plant plant2 = new Plant("Cupressus sempervirens2", "Cupressaceae", "Informacion", false, activities.get(1).getUid());
                //plants.add(plant2);

                for(Plant p:plants) {
                    AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().insertAllPlants(p);
                }

                final List<Question> questions = new ArrayList<>();
                Question question = new Question("Hábito", answers, "Arbol", activities.get(0).getUid());
                questions.add(question);
                //Question question2 = new Question("Hábito2", answers, "Arbol", activities.get(1).getUid());
                //questions.add(question2);

                for(Question q:questions) {
                    AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().insertAllQuestions(q);
                }
            }
        }
    }
}
