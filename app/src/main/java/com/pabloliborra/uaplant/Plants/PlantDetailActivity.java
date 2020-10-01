package com.pabloliborra.uaplant.Plants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Routes.Route;
import com.pabloliborra.uaplant.Utils.Constants;

public class PlantDetailActivity extends AppCompatActivity {

    private Plant plant;

    private TextView family, description;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);

        this.plant = (Plant) getIntent().getSerializableExtra(Constants.plantExtraTitle);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        setTitle(this.plant.getScientific_name());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.family = findViewById(R.id.familyPlant);
        this.description = findViewById(R.id.textDescriptionPlant);

        this.family.setText("Familia: " + this.plant.getFamily());
        this.description.setText(this.plant.getInformation());
        this.description.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
