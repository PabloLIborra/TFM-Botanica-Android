package com.pabloliborra.uaplant.Routes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pabloliborra.uaplant.Plants.Plant;
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.State;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListRoutesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RouteAdapterList adapter;
    private List<RouteListItem> itemRoutes;

    List<RouteListItem> inProcessItem = new ArrayList<>();
    List<RouteListItem> availableItem = new ArrayList<>();
    List<RouteListItem> completeItem = new ArrayList<>();


    private List<String> sectionsName = new ArrayList<String>() {{
        add("En Proceso");
        add("Nuevos");
        add("Completados");
    }};
    private List<RoutesSection> sections;

    public ListRoutesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_routes, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkRoutes();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initView();
        this.initRoutesList();
    }

    private void initView() {
        this.recyclerView = getView().findViewById(R.id.listRoutes);
    }

    private void initRoutesList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(manager);

        this.sections = this.createSectionsData(this.getActivity());
        this.itemRoutes = new ArrayList<>();
        for(RoutesSection section:this.sections) {
            for (RouteListItem route : section.getRoutesList()) {
                this.itemRoutes.add(route);
            }
        }
        this.adapter = new RouteAdapterList(this.getActivity(), this.sections, this.itemRoutes);
        this.recyclerView.setAdapter(this.adapter);
    }

    private List<RoutesSection> createSectionsData(Context context) {
        List<RoutesSection> sections = new ArrayList<>();

        List<Route> routes = AppDatabase.getDatabaseMain(getContext()).daoApp().getAllRoutes();

        List<Activity> activities = new ArrayList<>();
        if(routes != null && routes.size() > 0) {
            activities = AppDatabase.getDatabaseMain(getContext()).daoApp().loadActivityByRouteId(routes.get(0).getUid());
        }

        this.availableItem.clear();
        this.inProcessItem.clear();
        this.completeItem.clear();

        for(Route r:routes) {
            switch (r.getState()) {
                case IN_PROGRESS:
                    inProcessItem.add(new RouteListItem(r, this.getActivity(), activities.size()));
                    break;
                case AVAILABLE:
                    availableItem.add(new RouteListItem(r, this.getActivity(), activities.size()));
                    break;
                case COMPLETE:
                    completeItem.add(new RouteListItem(r, this.getActivity(), activities.size()));
                    break;
            }
        }

        sections.add(new RoutesSection(R.drawable.inprocess_route_icon, this.sectionsName.get(0), inProcessItem));
        sections.add(new RoutesSection(R.drawable.new_route_icon, this.sectionsName.get(1), availableItem));
        sections.add(new RoutesSection(R.drawable.completed_route_icon, this.sectionsName.get(2), completeItem));

        return sections;
    }

    private void checkRoutes() {
        for(RouteListItem item:this.completeItem) {
            if(item.getCompleteActivities() == 0) {
                item.getRoute().setState(State.AVAILABLE);
                this.availableItem.add(item);
                this.completeItem.remove(item);
                AppDatabase.getDatabaseMain(this.getContext()).daoApp().updateRoute(item.getRoute());
            } else if(item.getCompleteActivities() > 0 || item.getInProgressActivities() > 0) {
                item.getRoute().setState(State.IN_PROGRESS);
                this.inProcessItem.add(item);
                this.completeItem.remove(item);
                AppDatabase.getDatabaseMain(this.getContext()).daoApp().updateRoute(item.getRoute());
            }
        }

        for(RouteListItem item:this.inProcessItem) {
            if(item.getCompleteActivities() == 0) {
                item.getRoute().setState(State.AVAILABLE);
                this.availableItem.add(item);
                this.inProcessItem.remove(item);
                AppDatabase.getDatabaseMain(this.getContext()).daoApp().updateRoute(item.getRoute());
            } else if(item.getCompleteActivities() == item.getTotalActivities()) {
                item.getRoute().setState(State.COMPLETE);
                this.completeItem.add(item);
                this.inProcessItem.remove(item);
                AppDatabase.getDatabaseMain(this.getContext()).daoApp().updateRoute(item.getRoute());
            }
        }

        for(RouteListItem item:this.availableItem) {
            if(item.getCompleteActivities() > 0 || item.getInProgressActivities() > 0) {
                item.getRoute().setState(State.IN_PROGRESS);
                this.inProcessItem.add(item);
                this.availableItem.remove(item);
                AppDatabase.getDatabaseMain(this.getContext()).daoApp().updateRoute(item.getRoute());
            } else if(item.getCompleteActivities() == item.getTotalActivities()) {
                item.getRoute().setState(State.COMPLETE);
                this.completeItem.add(item);
                this.availableItem.remove(item);
                AppDatabase.getDatabaseMain(this.getContext()).daoApp().updateRoute(item.getRoute());
            }
        }
    }
}
