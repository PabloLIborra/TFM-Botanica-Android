package com.pabloliborra.uaplant.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Routes.Question;
import com.pabloliborra.uaplant.Routes.Route;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class JSONDownload {

    public enum TypeClass {
        Init,
        Class
    }

    Activity contextActivity;
    TypeClass type;
    AlertDialog.Builder builder;
    TextView titleAlert;
    AlertDialog alertDialog;

    public JSONDownload(Activity contextActivity, TypeClass type) {
        this.contextActivity = contextActivity;
        this.type = type;
        new DownloadDataFromServer().execute();
    }

    private class DownloadDataFromServer extends AsyncTask<Void, Void, Void> {
        String urlServer = "http://jtech.ua.es/uaplant/";
        String data;
        LinkedHashMap<com.pabloliborra.uaplant.Routes.Activity,String> imagesLocalizationToDownload = new LinkedHashMap<>();
        LinkedHashMap<Plant, List<String>> imagesPlantsToDownload = new LinkedHashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createDownloadDialog();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(urlServer + "index.txt");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                data = "";
                while(line != null) {
                    line = bufferedReader.readLine();
                    if(line != null) {
                        data = data + line + ";";
                    }
                }
                if(data != "") {
                    String[] routesTxt = data.split(";");
                    for(String routeTxt:routesTxt) {
                        url = new URL(urlServer + routeTxt + "/" + routeTxt + ".json");

                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        inputStream = httpURLConnection.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        line = "";
                        data = "";
                        while(line != null) {
                            line = bufferedReader.readLine();
                            if(line != null) {
                                data = data + line;
                            }
                        }


                        JSONObject JO = new JSONObject(data);

                        if(AppDatabase.getDatabaseMain(contextActivity).daoApp().getRouteByTitle(JO.get("itinerario").toString()) == null) {
                            Route route = new Route(JO.get("itinerario").toString(),JO.get("informacion_itinerario").toString(),State.AVAILABLE);
                            AppDatabase.getDatabaseMain(contextActivity).daoApp().insertAllRoutes(route);
                            route = AppDatabase.getDatabaseMain(contextActivity).daoApp().getRouteByTitle(JO.get("itinerario").toString());

                            JSONArray arrayPlants = JO.getJSONArray("plantas");

                            State state = State.AVAILABLE;

                            for(int i = 0; i < arrayPlants.length(); i++) {
                                JO = arrayPlants.getJSONObject(i);
                                com.pabloliborra.uaplant.Routes.Activity activity = new com.pabloliborra.uaplant.Routes.Activity(
                                        JO.get("titulo_actividad").toString(), JO.get("subtitulo_actividad").toString(), state,
                                        Double.parseDouble(JO.get("lat").toString()), Double.parseDouble(JO.get("lng").toString()),
                                        JO.get("informacion_actividad").toString(), new Date(), route.getUid());
                                state = State.INACTIVE;
                                AppDatabase.getDatabaseMain(contextActivity).daoApp().insertAllActivities(activity);
                                List<com.pabloliborra.uaplant.Routes.Activity> activities = AppDatabase.getDatabaseMain(contextActivity).daoApp().loadActivityByRouteId(route.getUid());
                                for(com.pabloliborra.uaplant.Routes.Activity a:activities) {
                                    if(a.getTitle().equalsIgnoreCase(activity.getTitle())) {
                                        activity = a;
                                        break;
                                    }
                                }
                                ////////
                                // AÑADIR IMAGEN DE LA ACTIVIDAD (ANTES O DESPUES)
                                ////////

                                Plant plant = new Plant(JO.get("nombre_cientifico").toString(), JO.get("familia").toString(),
                                        JO.get("descripcion_planta").toString(), false, activity.getUid());
                                AppDatabase.getDatabaseMain(contextActivity).daoApp().insertAllPlants(plant);
                                plant = AppDatabase.getDatabaseMain(contextActivity).daoApp().loadPlantByActivityId(activity.getUid());

                                ////////
                                // AÑADIR IMAGEN DEL CARRUSEL DE LA PLANTA (ANTES O DESPUES)
                                ////////

                                JSONArray arrayQuestions = JO.getJSONArray("preguntas");
                                for (int x = 0; x < arrayQuestions.length(); x++) {
                                    JO = arrayQuestions.getJSONObject(x);
                                    List<String> listAnswers = new ArrayList<>();
                                    String[] answers = JO.get("respuestas").toString().split(";");
                                    for(String answer:answers) {
                                        listAnswers.add(answer);
                                    }
                                    listAnswers.add(JO.get("respuestac").toString());
                                    Question question = new Question(JO.get("titulo_pregunta").toString(), listAnswers, JO.get("respuestac").toString(), activity.getUid());
                                    AppDatabase.getDatabaseMain(contextActivity).daoApp().insertAllQuestions(question);
                                }

                            }

                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(type == TypeClass.Class) {
                EventBus.getDefault().post(new MessageEvent(true));
            }
            alertDialog.dismiss();
        }
    }

    private void createDownloadDialog() {
        builder = new AlertDialog.Builder(contextActivity, R.style.DownloadDialogTheme);
        ViewGroup viewGroup = contextActivity.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(contextActivity).inflate(R.layout.download_init, viewGroup, false);
        dialogView.setBackgroundColor(Color.TRANSPARENT);
        Rect displayRectangle = new Rect();
        contextActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialogView.setMinimumWidth((int)(displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));
        titleAlert = dialogView.findViewById(R.id.textDownloadInit);


        builder.setView(dialogView);
        alertDialog = builder.create();
        titleAlert.setText("Comprobando archivos");
        alertDialog.show();
    }
}
