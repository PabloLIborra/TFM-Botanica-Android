package com.pabloliborra.uaplant.Routes;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;

import java.util.List;

public class RouteAdapterList extends RecyclerView.Adapter<RouteAdapterList.RecyclerRouteHolder> {
    private List<RoutesSection> sections;
    private int selectedPosition = -1;

    public RouteAdapterList(List<RoutesSection> sections) {
        this.sections = sections;
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

        RoutesChildAdapterList childAdapter = new RoutesChildAdapterList(routes);
        holder.childRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return this.sections.size();
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
