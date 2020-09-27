package com.pabloliborra.uaplant.Routes;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;

import java.util.List;

public class RouteAdapterList extends RecyclerView.Adapter<RouteAdapterList.RecyclerRouteHolder> {
    private List<RouteListItem> items;
    private int selectedPosition = -1;

    public RouteAdapterList(List<RouteListItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerRouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new RecyclerRouteHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerRouteHolder holder, int position) {
        RouteListItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.activities.setText(item.getCompleteActivities()+"/"+item.getTotalActivities());

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class RecyclerRouteHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;
        private TextView activities;

        public RecyclerRouteHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.titleRoutes);
            this.description = itemView.findViewById(R.id.descriptionRoutes);
            this.activities = itemView.findViewById(R.id.numActivies);

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
