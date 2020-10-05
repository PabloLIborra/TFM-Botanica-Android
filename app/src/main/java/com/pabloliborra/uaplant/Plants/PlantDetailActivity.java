package com.pabloliborra.uaplant.Plants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Routes.Route;
import com.pabloliborra.uaplant.Utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PlantDetailActivity extends AppCompatActivity {
    private String ACTIVITY_TAG = "Detalle Planta";

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
        this.image = findViewById(R.id.imageLocationPlantDetail);

        this.family.setText("Familia: " + this.plant.getFamily());
        this.description.setText(this.plant.getInformation());
        this.description.setMovementMethod(new ScrollingMovementMethod());

        File myPath = null;
        ContextWrapper cw = new ContextWrapper(this);
        if(this.plant.getImages().size() > 0) {
            for(String image:this.plant.getImages()) {
                String[] nameImageSplit = image.split("/");
                if(nameImageSplit.length >= 2) {
                    String nameImage = nameImageSplit[1];
                    nameImageSplit = nameImageSplit[0].split("_");
                    File directory = cw.getDir(nameImageSplit[1], Context.MODE_PRIVATE);
                    // Create imageDir
                    myPath = new File(directory, nameImage);
                    break;
                } else {
                    myPath = new File("", "");
                }
            }
        } else {
            myPath = new File("", "");
        }
        Picasso.get().load(myPath).placeholder(R.drawable.not_available).into(this.image);

        final ImageButton reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailReport();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmailReport() {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:?subject=" + "Reportar error en " + '"' + ACTIVITY_TAG + '"' + "&to=" + "uaplant.app@gmail.com"));

        startActivity(Intent.createChooser(i, "Enviar email"));
    }
}
