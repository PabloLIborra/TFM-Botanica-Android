package com.pabloliborra.uaplant.Routes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.TextViewCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Constants;
import com.pabloliborra.uaplant.Utils.CustomInfoWindowAdapter;
import com.pabloliborra.uaplant.Utils.MessageEvent;
import com.pabloliborra.uaplant.Utils.State;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoutesMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Serializable {
    private String ACTIVITY_TAG = "Mapa Actividad";
    private Route route;
    private GoogleMap mMap;

    LinkedHashMap<Marker,Activity> markers =  new LinkedHashMap<Marker,Activity>();

    private Marker tappedMarker;
    private Activity activityTapped;

    private AlertDialog.Builder builder;
    private ImageButton informationButton;

    private List<Activity> activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_map);

        this.route = (Route) getIntent().getSerializableExtra(Constants.routeExtraTitle);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        setTitle("Itinerarios");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.informationButton = findViewById(R.id.infoButton);
        this.informationButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                showInformationAlert();
            }
        });

        final ImageButton reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailReport();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        changeActivityState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmailReport() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:?subject=" + "Reportar error en " + '"' + ACTIVITY_TAG + '"' + "&to=" + "uaplant.app@gmail.com"));

        startActivity(Intent.createChooser(i, "Enviar email"));
    }

    private void showInformationAlert() {
        builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_info_map, viewGroup, false);

        TextView titleAlert = dialogView.findViewById(R.id.nameAlert);
        TextView subtitleAlert = dialogView.findViewById(R.id.informationAlert);
        Button closeButtonAlert = dialogView.findViewById(R.id.buttonCloseAlert);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        titleAlert.setText(this.route.getTitle());
        subtitleAlert.setText(this.route.getDescription());
        closeButtonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.map_style_json));

        if (!success) {
            Log.e("MAP", "Style parsing failed.");
        }
        changeActivityState();
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(RoutesMap.this, ActivityDetailActivity.class);
                intent.putExtra(Constants.activityExtraTitle, activityTapped);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        this.tappedMarker = marker;
        this.activityTapped = this.markers.get(marker);

        switch (activityTapped.getState()) {
            case IN_PROGRESS:
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(this), activityTapped));
                return false;
            case AVAILABLE:
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(this), activityTapped));
                return false;
            case COMPLETE:
                mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(this), activityTapped));
                return false;
            case INACTIVE:
                return true;
            default:
                return true;
        }
    }

    public void changeActivityState() {
        mMap.clear();
        this.route = AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().loadRouteById(this.route.getUid());
        this.activities = this.route.getActivities(this);
        Collections.sort(this.activities);
        Activity changedActivity = null;
        boolean changeActivity = false;
        for(Activity a:this.activities) {
            if(a.getState() != State.COMPLETE) {
                if(a.getState() != State.IN_PROGRESS) {
                    if(a.getState() != State.AVAILABLE) {
                        if(a.getState() == State.INACTIVE) {
                            a.setState(State.AVAILABLE);
                            changedActivity = a;
                            changeActivity = true;
                        }
                    }
                }
                break;
            }
        }

        if(changeActivity == true){
            AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().updateActivity(changedActivity);
        }

        addMarkers();
    }

    private void addMarkers() {
        if(this.activities != null) {
            boolean showInfoAlert = true;
            for (Activity activity : this.activities) {
                LatLng position = new LatLng(activity.getLatitude(), activity.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(position);
                switch (activity.getState()) {
                    case IN_PROGRESS:
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        showInfoAlert = false;
                        break;
                    case AVAILABLE:
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        break;
                    case COMPLETE:
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        showInfoAlert = false;
                        break;
                    case INACTIVE:
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                        break;
                }
                this.markers.put(mMap.addMarker(marker), activity);
            }
            if(showInfoAlert == true) {
                this.showInformationAlert();
            }
        }
        if (this.markers.size() > 0) {
            Activity activity = null;
            Iterator<Marker> iterator = this.markers.keySet().iterator();
            if (iterator.hasNext()) {
                activity = this.markers.get(iterator.next());
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(activity.getLatitude(), activity.getLongitude()), 15));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.385750, -0.514250), 15));
        }
    }
}
