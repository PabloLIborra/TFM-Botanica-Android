package com.pabloliborra.uaplant.Plants;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Routes.Activity;
import com.pabloliborra.uaplant.Routes.Route;
import com.pabloliborra.uaplant.Routes.RouteAdapterList;
import com.pabloliborra.uaplant.Routes.RouteListItem;
import com.pabloliborra.uaplant.Routes.RoutesSection;
import com.pabloliborra.uaplant.Utils.AppDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPlantsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlantAdapterList adapter;
    private List<PlantListItem> itemPlants;

    private List<String> sectionsName = new ArrayList<String>();
    private List<PlantsSection> sections;

    public ListPlantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_plants, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initView();
        this.initPlantsList();
    }

    private void initView() {
        this.recyclerView = getView().findViewById(R.id.listPlants);
    }

    private void initPlantsList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(manager);

        this.sections = this.createSectionsData(this.getActivity());
        this.itemPlants = new ArrayList<>();
        for(PlantsSection section:this.sections) {
            for (PlantListItem p : section.getPlantsList()) {
                this.itemPlants.add(p);
            }
        }
        this.adapter = new PlantAdapterList(this.getActivity(), this.sections, this.itemPlants);
        this.recyclerView.setAdapter(this.adapter);
    }

    private List<PlantsSection> createSectionsData(Context context) {
        List<PlantsSection> sections = new ArrayList<>();

        List<Plant> plants = AppDatabase.getDatabase(getContext()).daoApp().getAllPlants();

        List<PlantListItem> items = new ArrayList<>();
        for(Plant p:plants) {
            char firstLetter = p.getScientific_name().charAt(0);
            if(!this.sectionsName.contains(String.valueOf(firstLetter))) {
                this.sectionsName.add(String.valueOf(firstLetter));
                sections.add(new PlantsSection(String.valueOf(firstLetter), items));
            }
            items.add(new PlantListItem(p));
        }

        return sections;
    }
}
