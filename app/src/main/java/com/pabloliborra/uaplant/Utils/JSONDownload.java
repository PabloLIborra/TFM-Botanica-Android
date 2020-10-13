package com.pabloliborra.uaplant.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Routes.Question;
import com.pabloliborra.uaplant.Routes.Route;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import java.util.Map;

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
    int totalImagesDownload = 0;
    int numImagesDownload = 0;
    List<Target> targetActivity = new ArrayList<>();
    List<Target> targetPlant = new ArrayList<>();

    LinkedHashMap<com.pabloliborra.uaplant.Routes.Activity,String> imagesLocalizationToDownload = new LinkedHashMap<>();
    LinkedHashMap<Plant, List<String>> imagesPlantsToDownload = new LinkedHashMap<>();

    String[] parseUrl;

    public JSONDownload(Activity contextActivity, TypeClass type) {
        this.contextActivity = contextActivity;
        this.type = type;
        new DownloadDataFromServer().execute();
    }

    private class DownloadDataFromServer extends AsyncTask<Void, Void, Void> {
        String urlServer = "http://jtech.ua.es/uaplant/";
        String data;
        boolean checkDownloadImages = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            createDownloadDialog();
            imagesPlantsToDownload.clear();
            imagesLocalizationToDownload.clear();
            checkDownloadImages = false;
            totalImagesDownload = 0;
            numImagesDownload = 0;
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
                            checkDownloadImages = true;
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
                                String plantName = JO.get("nombre_cientifico").toString().replace(" ","_");
                                String nameLocationImage = JO.get("foto_localizacion").toString().replace(" ","_");
                                imagesLocalizationToDownload.put(activity, route.getTitle() + ";" + plantName + ";" + JO.get("foto_localizacion").toString() + ";" + urlServer + routeTxt + "/" + plantName + "/" + nameLocationImage);

                                Plant plant = new Plant(JO.get("nombre_cientifico").toString(), JO.get("familia").toString(),
                                        JO.get("descripcion_planta").toString(), false, activity.getUid());
                                AppDatabase.getDatabaseMain(contextActivity).daoApp().insertAllPlants(plant);
                                plant = AppDatabase.getDatabaseMain(contextActivity).daoApp().loadPlantByActivityId(activity.getUid());

                                List<String> images = new ArrayList<>();
                                String[] imagesPlant = JO.get("fotos_carrusel").toString().split(";");
                                for(String image:imagesPlant) {
                                    String nameImage = JO.get("fotos_carrusel").toString().replace(" ","_");
                                    images.add(route.getTitle() + ";" + plantName + ";" + image + ";"  + urlServer + routeTxt + "/" + plantName + "/" + nameImage);
                                }
                                imagesPlantsToDownload.put(plant, images);

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
            if(checkDownloadImages == true) {
                for (Map.Entry<com.pabloliborra.uaplant.Routes.Activity, String> entry : imagesLocalizationToDownload.entrySet()) {
                    totalImagesDownload++;
                }
                for (Map.Entry<Plant, List<String>> entry : imagesPlantsToDownload.entrySet()) {
                    List<String> values = entry.getValue();

                    for(String value:values) {
                        totalImagesDownload++;
                    }
                }

                titleAlert.setText("Descargando archivos 0/" + totalImagesDownload);
                Handler mainHandler = new Handler(contextActivity.getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        downloadImagesLocation();
                        downloadImagesPlant();
                    }
                };
                mainHandler.post(myRunnable);
            } else {
                alertDialog.dismiss();
            }
        }
    }

    private void createDownloadDialog() {
        builder = new AlertDialog.Builder(contextActivity, R.style.DownloadDialogTheme);
        ViewGroup viewGroup = contextActivity.findViewById(android.R.id.content);
        View dialogView;
        if(type == TypeClass.Class) {
            dialogView = LayoutInflater.from(contextActivity).inflate(R.layout.download_class, viewGroup, false);
        } else {
            dialogView = LayoutInflater.from(contextActivity).inflate(R.layout.download_init, viewGroup, false);
        }
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

    private void downloadImagesLocation() {
        for (Map.Entry<com.pabloliborra.uaplant.Routes.Activity, String> entry : imagesLocalizationToDownload.entrySet()) {
            com.pabloliborra.uaplant.Routes.Activity key = entry.getKey();
            String value = entry.getValue();

            System.out.println("For actividad " + key + value);
            downloadActivityImage(key, value);
        }
    }

    private void downloadImagesPlant() {
        for (Map.Entry<Plant, List<String>> entry : imagesPlantsToDownload.entrySet()) {
            Plant key = entry.getKey();
            List<String> values = entry.getValue();

            for(String value:values) {
                System.out.println("For planta " + key + value);
                downloadPlantImage(key, value);
            }
        }
    }

    public void downloadActivityImage(final com.pabloliborra.uaplant.Routes.Activity activity, String url) {
        final String[] parseUrl = url.split(";");

        System.out.println("Actividad entras");
        targetActivity.add(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                System.out.println("Actividad guardada");
                numImagesDownload++;
                titleAlert.setText("Descargando archivos " + numImagesDownload + "/" + totalImagesDownload);
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        String path = saveToInternalStorage(bitmap, parseUrl[0], parseUrl[1], parseUrl[2]);
                        com.pabloliborra.uaplant.Routes.Activity newActivity = AppDatabase.getDatabaseMain(contextActivity).daoApp().loadActivityById(activity.getUid());
                        newActivity.setLocationImage(path);
                        AppDatabase.getDatabaseMain(contextActivity).daoApp().updateActivity(newActivity);
                        if (numImagesDownload == totalImagesDownload) {
                            new Handler(contextActivity.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if(alertDialog.isShowing()) {
                                        alertDialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                System.out.println("Actividad failed");
                numImagesDownload++;
                titleAlert.setText("Descargando archivos " + numImagesDownload + "/" + totalImagesDownload);
                if(numImagesDownload == totalImagesDownload) {
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        Picasso.get().load(parseUrl[3]).into(targetActivity.get(targetActivity.size()-1));
    }

    public void downloadPlantImage(final Plant plant, String url) {
        final String[] parseUrl = url.split(";");

        System.out.println("Planta entras");
        targetPlant.add(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                System.out.println("Guardada planta");
                numImagesDownload++;
                titleAlert.setText("Descargando archivos " + numImagesDownload + "/" + totalImagesDownload);

                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        String path = saveToInternalStorage(bitmap, parseUrl[0], parseUrl[1], parseUrl[2]);
                        Plant newPlant = AppDatabase.getDatabaseMain(contextActivity).daoApp().loadPlantById(plant.getUid());
                        newPlant.addImage(path);
                        AppDatabase.getDatabaseMain(contextActivity).daoApp().updatePlant(newPlant);
                        if (numImagesDownload == totalImagesDownload) {
                            new Handler(contextActivity.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if(alertDialog.isShowing()) {
                                        alertDialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                System.out.println("Failed planta");
                numImagesDownload++;
                titleAlert.setText("Descargando archivos " + numImagesDownload + "/" + totalImagesDownload);
                if(numImagesDownload == totalImagesDownload) {
                    alertDialog.dismiss();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

        Picasso.get().load(parseUrl[3]).into(targetPlant.get(targetPlant.size()-1));
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String nameRoute, String namePlant, String nameImage){
        ContextWrapper cw = new ContextWrapper(this.contextActivity);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(nameRoute, Context.MODE_PRIVATE);
        // Create imageDir
        String nameFile = namePlant + "_" + nameImage;
        File mypath = new File(directory, nameFile);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().post(new MessageEvent());
        return "app_" + nameRoute + "/" + nameFile;
    }
}
