package com.pabloliborra.uaplant.Plants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Routes.Route;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Constants;

import java.util.List;

public class PlantAdapterList extends RecyclerView.Adapter<PlantAdapterList.RecyclerPlantHolder> {
    private Context context;
    private Activity activity;

    private List<PlantsSection> sections;
    private List<PlantListItem> plants;
    private View listItem;

    public PlantAdapterList(Context context, Activity activity, List<PlantsSection> sections, List<PlantListItem> plants) {
        this.context = context;
        this.activity = activity;
        this.sections = sections;
        this.plants = plants;
    }

    @NonNull
    @Override
    public RecyclerPlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_plant, parent, false);
        return new RecyclerPlantHolder(listItem);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerPlantHolder holder, int position) {
        PlantsSection section = this.sections.get(position);
        List<PlantListItem> plants = section.getPlantsList();

        holder.titleSection.setText(section.getSectionTitle());
        int unlockPlants = 0;
        for(PlantListItem item:section.getPlantsList()) {
            if(item.getPlant().isUnlock() == true) {
                unlockPlants++;
            }
        }
        holder.numPlantsSection.setText(unlockPlants + "/" + section.getPlantsList().size());

        PlantsChildAdapterList childAdapter = new PlantsChildAdapterList(this.activity, plants);
        holder.childRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return this.sections.size();
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
        }
    }

}
