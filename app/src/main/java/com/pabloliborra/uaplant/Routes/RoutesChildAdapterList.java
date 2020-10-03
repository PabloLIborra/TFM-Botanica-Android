package com.pabloliborra.uaplant.Routes;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.pabloliborra.uaplant.Plants.ListPlantsFragment;
import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class RoutesChildAdapterList extends RecyclerView.Adapter<RoutesChildAdapterList.RecyclerRouteHolder> {

    private Context context;
    private View listItem;
    List<RouteListItem> routesList;
    RouteAdapterList parentAdapter;
    OnRouteClickListener onRouteClickListener;

    private AlertDialog.Builder builder;
    private TextView titleAlert;
    private TextView subtitleAlert;
    private Button acceptButtonAlert, closeButtonAlert;

    public RoutesChildAdapterList(Context context, List<RouteListItem> routesList, RouteAdapterList parentAdapter, OnRouteClickListener onRouteClickListener) {
        this.context = context;
        this.routesList = routesList;
        this.parentAdapter = parentAdapter;
        this.onRouteClickListener = onRouteClickListener;
    }

    @NonNull
    @Override
    public RoutesChildAdapterList.RecyclerRouteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new RoutesChildAdapterList.RecyclerRouteHolder(listItem, this.onRouteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerRouteHolder holder, final int position) {
        holder.title.setText(this.routesList.get(position).getTitle());
        holder.description.setText(this.routesList.get(position).getDescription());
        holder.numActivities.setText(this.routesList.get(position).getCompleteActivities() + "/" + this.routesList.get(position).getTotalActivities());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteAlert(routesList.get(position).getRoute(), position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.routesList.size();
    }

    private void showDeleteAlert(final Route route, final int position) {
        builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        ViewGroup viewGroup = listItem.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(listItem.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);

        this.titleAlert = dialogView.findViewById(R.id.titleAlert);
        this.subtitleAlert = dialogView.findViewById(R.id.subtitleAlert);
        this.acceptButtonAlert = dialogView.findViewById(R.id.buttonAcceptAlert);
        this.closeButtonAlert = dialogView.findViewById(R.id.buttonCloseAlert);

        this.acceptButtonAlert.setText("Eliminar");

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        this.titleAlert.setText("Eliminar");
        this.subtitleAlert.setText("¿Desea eliminar el itinerario\n" + '"' + route.getTitle() + '"' + "?\n\nSi realiza esta acción perderá todo su progreso, pero podrá volver a descargarla pulsando en el botón de descarga de la barra de navegación.");
        this.closeButtonAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        this.acceptButtonAlert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(route != null) {
                    List<Activity> activitiesTotal = route.getActivities(context);
                    if(activitiesTotal != null) {
                        for(Activity a:activitiesTotal) {
                            List<Question> questions = a.getQuestions(context);
                            if(a != null && questions != null) {
                                for(Question q:questions){
                                    if(q != null) {
                                        AppDatabase.getDatabaseMain(context).daoApp().deleteQuestion(q);
                                    }
                                }
                                Plant p = a.getPlant(context);
                                if(p != null) {
                                    AppDatabase.getDatabaseMain(context).daoApp().deletePlant(p);
                                }
                                AppDatabase.getDatabaseMain(context).daoApp().deleteActivity(a);
                            }
                        }
                    }
                    AppDatabase.getDatabaseMain(context).daoApp().deleteRoute(route);

                    routesList.remove(position);
                    notifyItemRemoved(position);
                    parentAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new MessageEvent());

                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
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
