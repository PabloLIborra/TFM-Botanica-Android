package com.pabloliborra.uaplant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.pabloliborra.uaplant.Utils.JSONDownload;
import com.pabloliborra.uaplant.pagecontroller.PagerController;

public class TabBarActivity extends AppCompatActivity {
    private String ACTIVITY_TAG = "Tab Bar Principal";
    private Toolbar mTopToolbar;

    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem routesTab, plantsTab, creditsTab;
    ImageButton reportButton;
    PagerController pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_bar);

        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);

        mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.view_pager);

        routesTab = findViewById(R.id.routesTab);
        plantsTab = findViewById(R.id.plantsTab);
        creditsTab = findViewById(R.id.creditsTab);

        final ImageButton reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailReport();
            }
        });
        final ImageButton downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONDownload(TabBarActivity.this, JSONDownload.TypeClass.Class);
            }
        });

        pagerAdapter = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        pagerAdapter.notifyDataSetChanged();
                    case 1:
                        pagerAdapter.notifyDataSetChanged();
                    case 2:
                        pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void sendEmailReport() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:?subject=" + "Reportar error en " + '"' + ACTIVITY_TAG + '"' + "&to=" + "uaplant.app@gmail.com"));

        startActivity(Intent.createChooser(i, "Enviar email"));
    }
}
