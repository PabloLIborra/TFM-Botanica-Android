package com.pabloliborra.uaplant.Plants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;

import java.util.List;

public class PlantAdapterList extends RecyclerView.Adapter<PlantAdapterList.RecyclerPlantHolder> implements PlantsChildAdapterList.OnPlantClickListener {
    Activity activity;

    private List<PlantsSection> sections;
    private List<PlantListItem> plants;

    public PlantAdapterList(Activity activity, List<PlantsSection> sections, List<PlantListItem> plants) {
        this.activity = activity;
        this.sections = sections;
        this.plants = plants;
    }

    @NonNull
    @Override
    public RecyclerPlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_plant, parent, false);
        return new RecyclerPlantHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerPlantHolder holder, int position) {
        PlantsSection section = this.sections.get(position);
        List<PlantListItem> plants = section.getPlantsList();

        holder.titleSection.setText(section.getSectionTitle());
        holder.numPlantsSection.setText(String.valueOf(section.getPlantsList().size()));

        PlantsChildAdapterList childAdapter = new PlantsChildAdapterList(this.activity, plants, this);
        holder.childRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return this.sections.size();
    }

    @Override
    public void onPlantClick(View v, int position) {
        /*Intent intent = new Intent(this.activity, RoutesMap.class);
        intent.putExtra(Constants.routeExtraTitle, this.plants.get(position).getRoute());
        this.activity.startActivity(intent);*/
    }

    public class RecyclerPlantHolder extends RecyclerView.ViewHolder {
        private TextView titleSection;
        private TextView numPlantsSection;
        RecyclerView childRecyclerView;

        public RecyclerPlantHolder(@NonNull View itemView) {
            super(itemView);
            this.titleSection = itemView.findViewById(R.id.titleSectionPlant);
            this.numPlantsSection = itemView.findViewById(R.id.numActivitiesSectionPlant);
            this.childRecyclerView = itemView.findViewById(R.id.sectionRecyclerViewPlant);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                }
            });
        }
    }

}
