package com.pabloliborra.uaplant.Plants;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Plants.PlantListItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PlantsChildAdapterList extends RecyclerView.Adapter<PlantsChildAdapterList.RecyclerPlantHolder> {

    private Context context;
    List<PlantListItem> plantsList;
    OnPlantClickListener onPlantClickListener;

    public PlantsChildAdapterList(Context context, List<PlantListItem> plantsList, OnPlantClickListener onPlantClickListener) {
        this.context = context;
        this.plantsList = plantsList;
        this.onPlantClickListener = onPlantClickListener;
    }

    @NonNull
    @Override
    public PlantsChildAdapterList.RecyclerPlantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_plant_view, parent, false);
        return new PlantsChildAdapterList.RecyclerPlantHolder(view, this.onPlantClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPlantHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return this.plantsList.size();
    }

    class RecyclerPlantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView image;
        CardView cardView;

        OnPlantClickListener onPlantClickListener;

        @SuppressLint("ResourceType")
        public RecyclerPlantHolder(@NonNull View itemView, OnPlantClickListener onPlantClickListener) {
            super(itemView);

            this.title = itemView.findViewById(R.id.titlePlants);
            this.image = itemView.findViewById(R.id.imagePlantCell);
            this.cardView = itemView.findViewById(R.id.cardViewPlant);
            this.onPlantClickListener = onPlantClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.onPlantClickListener.onPlantClick(v, getAdapterPosition());
        }
    }

    public interface OnPlantClickListener {
        void onPlantClick(View v, int position);
    }

}
