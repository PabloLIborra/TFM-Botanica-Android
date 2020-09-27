package com.pabloliborra.uaplant.Routes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;

import java.util.List;

public class RoutesChildAdapterList extends RecyclerView.Adapter<RoutesChildAdapterList.RecyclerRouteHolder> {

    List<RouteListItem> routesList;

    public RoutesChildAdapterList(List<RouteListItem> routesList) {
        this.routesList = routesList;
    }

    @NonNull
    @Override
    public RoutesChildAdapterList.RecyclerRouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new RoutesChildAdapterList.RecyclerRouteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerRouteHolder holder, int position) {
        holder.title.setText(this.routesList.get(position).getTitle());
        holder.description.setText(this.routesList.get(position).getDescription());
        holder.numActivities.setText(this.routesList.get(position).getCompleteActivities() + "/" + this.routesList.get(position).getTotalActivities());
    }

    @Override
    public int getItemCount() {
        return this.routesList.size();
    }

    class RecyclerRouteHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        TextView numActivities;

        public RecyclerRouteHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleRoutes);
            description = itemView.findViewById(R.id.descriptionRoutes);
            numActivities = itemView.findViewById(R.id.numActivities);
        }
    }

}
