package com.pabloliborra.uaplant.Plants;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pabloliborra.uaplant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListPlantsFragment extends Fragment {

    public ListPlantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_plants, container, false);
    }
}
