package com.pabloliborra.uaplant.Routes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.State;

import java.util.ArrayList;
import java.util.List;

public class ListRoutesFragment extends Fragment {
    private RecyclerView recyclerView;
    private RouteAdapterList adapter;
    private List<RouteListItem> routes;

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

        this.routes = getRoutes();
        this.adapter = new RouteAdapterList(this.routes);
        this.recyclerView.setAdapter(this.adapter);
    }

    private List<RouteListItem> getRoutes() {
        List<RouteListItem> items = new ArrayList<>();
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        items.add(new RouteListItem(new Route("Gimnospermas", "Información de prueba", State.AVAILABLE)));
        return items;
    }
}
