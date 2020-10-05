package com.pabloliborra.uaplant.Routes;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.MessageEvent;
import com.pabloliborra.uaplant.Utils.State;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListRoutesFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RouteAdapterList adapter;
    private List<RouteListItem> itemRoutes;

    private TextView textTotalRoutes, textTotalActivities;

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.initRoutesList();
        this.checkRoutes();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initView();
        this.initRoutesList();
        this.checkRoutes();
    }

    private void initView() {
        this.recyclerView = getView().findViewById(R.id.listRoutes);
        this.textTotalRoutes = getView().findViewById(R.id.titleTotalRoutes);
        this.textTotalActivities = getView().findViewById(R.id.totalNumActivities);
        this.swipeRefreshLayout = getView().findViewById(R.id.refreshRoutes);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRoutesList();
                int totalRoutes = completeItem.size() + inProcessItem.size() + availableItem.size();
                textTotalActivities.setText(completeItem.size() + "/" + totalRoutes);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        this.textTotalRoutes.setText("Itinerarios totales");
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
        Collections.sort(routes);


        this.availableItem.clear();
        this.inProcessItem.clear();
        this.completeItem.clear();

        for(Route r:routes) {
            List<Activity> activities = new ArrayList<>();
            if(routes != null && routes.size() > 0) {
                activities = AppDatabase.getDatabaseMain(getContext()).daoApp().loadActivityByRouteId(r.getUid());
            }
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

        int totalRoutes = this.completeItem.size() + this.inProcessItem.size() + this.availableItem.size();
        this.textTotalActivities.setText(this.completeItem.size() + "/" + totalRoutes);

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

        int totalRoutes = this.completeItem.size() + this.inProcessItem.size() + this.availableItem.size();
        this.textTotalActivities.setText(this.completeItem.size() + "/" + totalRoutes);

        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event.fromDownload == true) {
            initRoutesList();
            event.fromDownload = false;
        } else {
            int totalRoutes = this.completeItem.size() + this.inProcessItem.size() + this.availableItem.size();
            this.textTotalActivities.setText(this.completeItem.size() + "/" + totalRoutes);
        }
    }
}
