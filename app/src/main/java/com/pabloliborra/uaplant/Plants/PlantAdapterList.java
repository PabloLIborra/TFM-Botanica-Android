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

public class PlantAdapterList extends RecyclerView.Adapter<PlantAdapterList.RecyclerPlantHolder> implements PlantsChildAdapterList.OnPlantClickListener {
    private Context context;
    private Activity activity;

    private List<PlantsSection> sections;
    private List<PlantListItem> plants;
    private View listItem;

    private AlertDialog.Builder builder;
    private TextView titleAlert, subtitleAlert, routeAlert, activityAlert;
    private Button closeButtonAlert;

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

        PlantsChildAdapterList childAdapter = new PlantsChildAdapterList(this.activity, plants, this);
        holder.childRecyclerView.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return this.sections.size();
    }

    @Override
    public void onPlantClick(View v, int position) {
        Plant plant = this.plants.get(position).getPlant();
        if(plant.isUnlock() == true) {
            Intent intent = new Intent(this.activity, PlantDetailActivity.class);
            intent.putExtra(Constants.plantExtraTitle, this.plants.get(position).getPlant());
            this.activity.startActivity(intent);
        } else {
            createDialogPlantLocked(plant);
        }
    }

    private void createDialogPlantLocked(Plant plant) {
        builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        ViewGroup viewGroup = listItem.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(listItem.getContext()).inflate(R.layout.custom_dialog_plant_locked, viewGroup, false);

        this.titleAlert = dialogView.findViewById(R.id.titleAlertInfo);
        this.subtitleAlert = dialogView.findViewById(R.id.subtitleAlertInfo);
        this.closeButtonAlert = dialogView.findViewById(R.id.buttonCloseAlertInfo);
        this.routeAlert = dialogView.findViewById(R.id.routeAlertInfo);
        this.activityAlert = dialogView.findViewById(R.id.activityAlertInfo);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        this.titleAlert.setText("Planta Bloqueada");
        this.subtitleAlert.setText("Para poder visualizar esta planta necesitas desbloquearla. Para ello debes completar la siguiente actividad.");


        com.pabloliborra.uaplant.Routes.Activity activityPlant = AppDatabase.getDatabaseMain(this.context).daoApp().loadActivityById(plant.getActivityId());
        Route routePlant = AppDatabase.getDatabaseMain(this.context).daoApp().loadRouteById(activityPlant.getRouteId());

        this.routeAlert.setText("Itinerario\n" + '"' + routePlant.getTitle() + '"');
        this.activityAlert.setText("Actividad\n" + '"' + activityPlant.getTitle() + '"');
        this.closeButtonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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
