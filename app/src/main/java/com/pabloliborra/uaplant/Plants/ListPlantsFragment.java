package com.pabloliborra.uaplant.Plants;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Routes.Activity;
import com.pabloliborra.uaplant.Routes.Route;
import com.pabloliborra.uaplant.Routes.RouteAdapterList;
import com.pabloliborra.uaplant.Routes.RouteListItem;
import com.pabloliborra.uaplant.Routes.RoutesChildAdapterList;
import com.pabloliborra.uaplant.Routes.RoutesSection;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPlantsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PlantAdapterList adapter;
    private List<PlantListItem> itemPlants;

    private TextView textTotalPlants, textTotalNumPlants;

    LinkedHashMap<String,List<PlantListItem>> sectionsName =  new LinkedHashMap<String,List<PlantListItem>>();
    private List<PlantsSection> sections;

    public ListPlantsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_plants, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initView();
        this.initPlantsList();
    }

    private void initView() {
        this.recyclerView = getView().findViewById(R.id.listPlants);
        this.textTotalPlants = getView().findViewById(R.id.titleTotalPlants);
        this.textTotalNumPlants = getView().findViewById(R.id.totalNumPlants);
        this.swipeRefreshLayout = getView().findViewById(R.id.refreshPlants);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPlantsList();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        this.textTotalPlants.setText("Plantas totales");
    }

    private void initPlantsList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(manager);

        this.sections = new ArrayList<>();
        this.sections = this.createSectionsData(this.getActivity());
        this.itemPlants = new ArrayList<>();
        for(PlantsSection section:this.sections) {
            for (PlantListItem p : section.getPlantsList()) {
                this.itemPlants.add(p);
            }
        }
        this.adapter = new PlantAdapterList(this.getContext(), this.getActivity(), this.sections, this.itemPlants);
        this.recyclerView.setAdapter(this.adapter);

        int unlockPlants = 0;
        for(PlantListItem item:this.itemPlants) {
            if(item.getPlant() != null && item.getPlant().isUnlock()) {
                unlockPlants++;
            }
        }
        this.textTotalNumPlants.setText(unlockPlants + "/" + this.itemPlants.size());
    }

    public List<PlantsSection> createSectionsData(Context context) {
        List<PlantsSection> sections = new ArrayList<>();

        List<Plant> plants = AppDatabase.getDatabaseMain(getContext()).daoApp().getAllPlants();
        Collections.sort(plants);

        this.sectionsName.clear();
        for(Plant p:plants) {
            String firstLetter = String.valueOf(p.getScientific_name().charAt(0)).toUpperCase();
            if(!this.sectionsName.containsKey(firstLetter)) {
                this.sectionsName.put(firstLetter, new ArrayList<PlantListItem>());
                this.sectionsName.get(firstLetter).add(new PlantListItem(p));
            } else {
                this.sectionsName.get(firstLetter).add(new PlantListItem(p));
            }
        }

        for (Map.Entry<String,List<PlantListItem>> entry : this.sectionsName.entrySet()) {
            String key = entry.getKey();
            List<PlantListItem> values = entry.getValue();
            sections.add(new PlantsSection(key, values));
        }

        return sections;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        initPlantsList();
    }
}
