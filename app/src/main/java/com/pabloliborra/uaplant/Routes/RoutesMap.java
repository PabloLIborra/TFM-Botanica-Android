package com.pabloliborra.uaplant.Routes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;

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
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Constants;
import com.pabloliborra.uaplant.Utils.CustomInfoWindowAdapter;
import com.pabloliborra.uaplant.Utils.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RoutesMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, Serializable {

    private Route route;
    private GoogleMap mMap;

    LinkedHashMap<Marker,Activity> markers =  new LinkedHashMap<Marker,Activity>();

    private Marker tappedMarker;
    private Activity activityTapped;

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

        this.activities = this.route.getActivities(this);

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

    private void addMarkers() {
        if(this.activities != null) {
            for (Activity activity : this.route.getActivities(this)) {
                LatLng position = new LatLng(activity.getLatitude(), activity.getLongitude());
                MarkerOptions marker = new MarkerOptions().position(position);
                switch (activity.getState()) {
                    case IN_PROGRESS:
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        break;
                    case AVAILABLE:
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        break;
                    case COMPLETE:
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        break;
                    case INACTIVE:
                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                        break;
                }
                this.markers.put(mMap.addMarker(marker), activity);
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

    public void changeActivityState() {
        mMap.clear();
        this.route = AppDatabase.getDatabase(getApplicationContext()).daoApp().loadRouteById(this.route.getUid());
        this.activities = this.route.getActivities(this);
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
            AppDatabase.getDatabase(getApplicationContext()).daoApp().updateActivity(changedActivity);
        }

        if(this.route.getState() == State.AVAILABLE){
            for(Activity a:this.activities) {
                if(a.getState() == State.IN_PROGRESS || a.getState() == State.COMPLETE) {
                    this.route.setState(State.IN_PROGRESS);
                    AppDatabase.getDatabase(getApplicationContext()).daoApp().updateRoute(this.route);
                }
            }
        } else if(this.route.getState() == State.IN_PROGRESS) {
            int numActivitiesComplete = 0;
            for(Activity a:this.activities) {
                if(a.getState() == State.COMPLETE) {
                    numActivitiesComplete++;
                }
            }
            if(numActivitiesComplete == this.activities.size()) {
                this.route.setState(State.COMPLETE);
                AppDatabase.getDatabase(getApplicationContext()).daoApp().updateRoute(this.route);
            }
        }

        addMarkers();
    }
}
