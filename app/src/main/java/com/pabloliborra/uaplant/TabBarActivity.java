package com.pabloliborra.uaplant;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class TabBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar);
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.addTab(tabs.newTab().setIcon(R.drawable.routes_button));
        tabs.addTab(tabs.newTab().setIcon(R.drawable.plants_button));
        tabs.addTab(tabs.newTab().setIcon(R.drawable.credit_button));
    }
}