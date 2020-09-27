package com.pabloliborra.uaplant.Routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.R;

import java.util.List;

public class RoutesChildAdapterList extends RecyclerView.Adapter<RoutesChildAdapterList.RecyclerRouteHolder> {

    private Context context;
    List<RouteListItem> routesList;
    OnRouteClickListener onRouteClickListener;

    public RoutesChildAdapterList(Context context, List<RouteListItem> routesList, OnRouteClickListener onRouteClickListener) {
        this.context = context;
        this.routesList = routesList;
        this.onRouteClickListener = onRouteClickListener;
    }

    @NonNull
    @Override
    public RoutesChildAdapterList.RecyclerRouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new RoutesChildAdapterList.RecyclerRouteHolder(view, this.onRouteClickListener);
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

    class RecyclerRouteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView description;
        TextView numActivities;

        OnRouteClickListener onRouteClickListener;

        public RecyclerRouteHolder(@NonNull View itemView, OnRouteClickListener onRouteClickListener) {
            super(itemView);

            this.title = itemView.findViewById(R.id.titleRoutes);
            this.description = itemView.findViewById(R.id.descriptionRoutes);
            this.numActivities = itemView.findViewById(R.id.numActivities);
            this.onRouteClickListener = onRouteClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.onRouteClickListener.onRouteClick(v, getAdapterPosition());
        }
    }

    public interface OnRouteClickListener {
        void onRouteClick(View v, int position);
    }

}
