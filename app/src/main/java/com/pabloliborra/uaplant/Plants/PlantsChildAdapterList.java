package com.pabloliborra.uaplant.Plants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Plants.PlantListItem;

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
            holder.cardView.setClickable(true);
            holder.cardView.setFocusable(true);
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorCeldaPlantas));
        } else {
            holder.cardView.setClickable(false);
            holder.cardView.setFocusable(false);
            holder.cardView.setCardBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return this.plantsList.size();
    }

    class RecyclerPlantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        CardView cardView;

        OnPlantClickListener onPlantClickListener;

        @SuppressLint("ResourceType")
        public RecyclerPlantHolder(@NonNull View itemView, OnPlantClickListener onPlantClickListener) {
            super(itemView);

            this.title = itemView.findViewById(R.id.titlePlants);
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
