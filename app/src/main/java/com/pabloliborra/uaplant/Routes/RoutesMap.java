package com.pabloliborra.uaplant.Routes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.Constants;
import com.pabloliborra.uaplant.Utils.CustomInfoWindowAdapter;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class RoutesMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Route route;
    private GoogleMap mMap;

    LinkedHashMap<Marker,Activity> markers =  new LinkedHashMap<Marker,Activity>();


    private Marker tappedMarker;
    private Activity activityTapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_map);

        this.route = (Route) getIntent().getSerializableExtra(Constants.routeExtraTitle);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        setTitle(route.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        this.addMarkers();
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
        for(Activity activity:this.route.getActivities()) {
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
        if(this.markers.size() > 0) {
            Activity activity = null;
            Iterator<Marker> iterator = this.markers.keySet().iterator();
            if(iterator.hasNext()){
                activity = this.markers.get( iterator.next() );
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(activity.getLatitude(), activity.getLongitude()), 15));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.385750, -0.514250), 15));
        }
    }
}
