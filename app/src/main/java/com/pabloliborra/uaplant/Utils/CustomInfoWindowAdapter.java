package com.pabloliborra.uaplant.Utils;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Routes.Activity;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;

    private Activity activity;

    public CustomInfoWindowAdapter(LayoutInflater inflater, Activity activity){
        this.inflater = inflater;
        this.activity = activity;
    }

    private View rederInfoWindow(Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.info_window_map, null);

        ((TextView)v.findViewById(R.id.titleInfoWindow)).setText(activity.getTitle());
        TextView subtitle = ((TextView)v.findViewById(R.id.subtitleInfpoWindow));
        subtitle.setText(activity.getSubtitle());

        return v;
    }

    @Override
    public View getInfoContents(final Marker m) {
        return rederInfoWindow(m);
    }

    @Override
    public View getInfoWindow(Marker m) {
        return rederInfoWindow(m);
    }

}
