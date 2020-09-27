package com.pabloliborra.uaplant.Routes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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
import com.pabloliborra.uaplant.Utils.State;

import java.util.ArrayList;
import java.util.List;

public class RoutesMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private Toolbar mTopToolbar;
    private Route route;
    private GoogleMap mMap;

    private List<MarkerOptions> markers = new ArrayList<>();

    private Marker tappedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_map);

        mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        setTitle("Mapa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.route = (Route) getIntent().getSerializableExtra(Constants.routeExtraTitle);
        Log.d("dfd", route.getTitle());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        this.tappedMarker = marker;
        switch (marker.getSnippet()) {
            case "IN_PROGRESS":
                return false;
            case "AVAILABLE":
                return false;
            case "COMPLETE":
                return false;
            case "INACTIVE":
                return true;
            default:
                return true;
        }
    }

    private void addMarkers() {
        for(Activity activity:this.route.getActivities()) {
            LatLng position = new LatLng(activity.getLatitude(), activity.getLongitude());
            MarkerOptions marker = new MarkerOptions().position(position).title(activity.getTitle());
            switch (activity.getState()) {
                case IN_PROGRESS:
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    marker.snippet(State.IN_PROGRESS.toString());
                    break;
                case AVAILABLE:
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    marker.snippet(State.AVAILABLE.toString());
                    break;
                case COMPLETE:
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    marker.snippet(State.COMPLETE.toString());
                    break;
                case INACTIVE:
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    marker.snippet(State.INACTIVE.toString());
                    break;
            }
            mMap.addMarker(marker);
            this.markers.add(marker);
        }
        if(this.markers.size() > 0) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.markers.get(0).getPosition(), 15));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.385750, -0.514250), 15));
        }
    }
}
