package com.pabloliborra.uaplant.Routes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.State;

import java.util.ArrayList;
import java.util.List;

public class ListRoutesFragment extends Fragment {
    private androidx.appcompat.widget.Toolbar mTopToolbar;

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

        this.sections = createSectionsData();
        this.adapter = new RouteAdapterList(this.sections);
        this.recyclerView.setAdapter(this.adapter);
    }

    private List<RoutesSection> createSectionsData() {
        List<RoutesSection> sections = new ArrayList<>();

        List<RouteListItem> items = new ArrayList<>();
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));

        sections.add(new RoutesSection(R.drawable.inprocess_route_icon, this.sectionsName.get(0), items));
        sections.add(new RoutesSection(R.drawable.new_route_icon, this.sectionsName.get(1), items));
        sections.add(new RoutesSection(R.drawable.completed_route_icon, this.sectionsName.get(2), items));

        return sections;
    }
}
