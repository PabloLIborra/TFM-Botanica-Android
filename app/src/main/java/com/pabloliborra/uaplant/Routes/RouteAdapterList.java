package com.pabloliborra.uaplant.Routes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.Constants;

import java.util.List;

public class RouteAdapterList extends RecyclerView.Adapter<RouteAdapterList.RecyclerRouteHolder> implements RoutesChildAdapterList.OnRouteClickListener {
    Activity activity;

    private List<RoutesSection> sections;
    private List<RouteListItem> routes;
    private int selectedPosition = -1;

    public RouteAdapterList(Activity activity, List<RoutesSection> sections, List<RouteListItem> routes) {
        this.activity = activity;
        this.sections = sections;
        this.routes = routes;
    }

    @NonNull
    @Override
    public RecyclerRouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_route, parent, false);
        return new RecyclerRouteHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerRouteHolder holder, int position) {
        RoutesSection section = this.sections.get(position);
        List<RouteListItem> routes = section.getRoutesList();

        holder.imageSection.setImageResource(section.getSectionImage());
        holder.titleSection.setText(section.getSectionTitle());
        holder.numActivitiesSection.setText(String.valueOf(section.getRoutesList().size()));

        RoutesChildAdapterList childAdapter = new RoutesChildAdapterList(this.activity, routes, this);
        holder.childRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return this.sections.size();
    }

    @Override
    public void onRouteClick(View v, int position) {
        Intent intent = new Intent(this.activity, RoutesMap.class);
        intent.putExtra(Constants.routeExtraTitle, this.routes.get(position).getRoute());
        this.activity.startActivity(intent);
    }

    public class RecyclerRouteHolder extends RecyclerView.ViewHolder {
        private ImageView imageSection;
        private TextView titleSection;
        private TextView numActivitiesSection;
        RecyclerView childRecyclerView;

        public RecyclerRouteHolder(@NonNull View itemView) {
            super(itemView);
            this.imageSection = itemView.findViewById(R.id.imageSectionRoute);
            this.titleSection = itemView.findViewById(R.id.titleSectionRoute);
            this.numActivitiesSection = itemView.findViewById(R.id.numActivitiesSectionRoute);
            this.childRecyclerView = itemView.findViewById(R.id.sectionRecyclerView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

}
