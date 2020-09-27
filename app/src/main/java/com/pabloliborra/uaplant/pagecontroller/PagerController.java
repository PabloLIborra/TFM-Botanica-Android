package com.pabloliborra.uaplant.pagecontroller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.pabloliborra.uaplant.Credits.ListCreditsFragment;
import com.pabloliborra.uaplant.Plants.ListPlantsFragment;
import com.pabloliborra.uaplant.Routes.ListRoutesFragment;

public class PagerController extends FragmentPagerAdapter {
    int numTabs;

    public PagerController(@NonNull FragmentManager fm, int countTabs) {
        super(fm, countTabs);
        numTabs = countTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ListRoutesFragment();
            case 1:
                return new ListPlantsFragment();
            case 2:
                return new ListCreditsFragment();
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
