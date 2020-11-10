package com.pabloliborra.uaplant.Plants;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Plants.PlantListItem;
import com.pabloliborra.uaplant.Routes.Route;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class PlantsChildAdapterList extends RecyclerView.Adapter<PlantsChildAdapterList.RecyclerPlantHolder> {

    private Context context;
    List<PlantListItem> plantsList;

    private AlertDialog.Builder builder;
    private TextView titleAlert, subtitleAlert, routeAlert, activityAlert;
    private Button closeButtonAlert;
    private View listItem;

    public PlantsChildAdapterList(Context context, List<PlantListItem> plantsList) {
        this.context = context;
        this.plantsList = plantsList;
    }

    @NonNull
    @Override
    public PlantsChildAdapterList.RecyclerPlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_plant_view, parent, false);
        return new PlantsChildAdapterList.RecyclerPlantHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPlantHolder holder, final int position) {
        holder.title.setText(this.plantsList.get(position).getTitle());
        if(this.plantsList.get(position).getPlant().isUnlock() == true) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorCeldaPlantas));
        } else {
            holder.cardView.setCardBackgroundColor(Color.GRAY);
        }

        File myPath = null;
        ContextWrapper cw = new ContextWrapper(this.context);
        if(this.plantsList.get(position).getPlant().getImages().size() > 0) {
            for(String image:this.plantsList.get(position).getPlant().getImages()) {
                String[] nameImageSplit = image.split("/");
                if(nameImageSplit.length >= 2) {
                    String nameImage = nameImageSplit[1];
                    nameImageSplit = nameImageSplit[0].split("_");
                    File directory = cw.getDir(nameImageSplit[1], Context.MODE_PRIVATE);
                    // Create imageDir
                    myPath = new File(directory, nameImage);
                    break;
                } else {
                    myPath = new File("", "");
                }
            }
        } else {
            myPath = new File("", "");
        }
        Picasso.get().load(myPath).placeholder(R.drawable.not_available).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plant plant = plantsList.get(position).getPlant();
                if(plant.isUnlock() == true) {
                    Intent intent = new Intent(context, PlantDetailActivity.class);
                    intent.putExtra(Constants.plantExtraTitle, plantsList.get(position).getPlant());
                    context.startActivity(intent);
                } else {
                    createDialogPlantLocked(plant);
                }
            }
        });
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

    @Override
    public int getItemCount() {
        return this.plantsList.size();
    }

    class RecyclerPlantHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView image;
        CardView cardView;

        @SuppressLint("ResourceType")
        public RecyclerPlantHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.titlePlants);
            this.image = itemView.findViewById(R.id.imagePlantCell);
            this.cardView = itemView.findViewById(R.id.cardViewPlant);
        }
    }

}
