package com.pabloliborra.uaplant.Routes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.State;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListRoutesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RouteAdapterList adapter;
    private List<RouteListItem> routes;

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

        this.sections = this.createSectionsData();
        this.routes = new ArrayList<>();
        for(RoutesSection section:this.sections) {
            for (RouteListItem route : section.getRoutesList()) {
                this.routes.add(route);
            }
        }
        this.adapter = new RouteAdapterList(this.getActivity(), this.sections, this.routes);
        this.recyclerView.setAdapter(this.adapter);
    }

    private List<RoutesSection> createSectionsData() {
        List<RoutesSection> sections = new ArrayList<>();

        List<Activity> activities = new ArrayList<>();
        Activity activity = new Activity("Prueba", "Subtitulo", State.AVAILABLE, 38.3864202198746, -0.514705561099236, "Informacion", new Date());
        activities.add(activity);
        Activity activity2 = new Activity("Prueba2", "Subtitulo", State.INACTIVE, 38.38642023, -0.52, "Informacion", new Date());
        activities.add(activity2);

        Route route = new Route("Gimnospermas", "Información de prueba", State.AVAILABLE, activities);
        activity.setRoute(route);

        List<RouteListItem> items = new ArrayList<>();
        items.add(new RouteListItem(route));
        items.add(new RouteListItem(route));
        items.add(new RouteListItem(route));
        items.add(new RouteListItem(route));

        sections.add(new RoutesSection(R.drawable.inprocess_route_icon, this.sectionsName.get(0), items));
        sections.add(new RoutesSection(R.drawable.new_route_icon, this.sectionsName.get(1), items));
        sections.add(new RoutesSection(R.drawable.completed_route_icon, this.sectionsName.get(2), items));

        return sections;
    }
}
