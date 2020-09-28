package com.pabloliborra.uaplant.Routes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.Constants;

public class ActivityDetailActivity extends AppCompatActivity {

    TextView titleActivity, descriptionActivity, state;
    ImageView locationImage, stateImage;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.activity = (Activity) getIntent().getSerializableExtra(Constants.activityExtraTitle);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle("Actividad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.titleActivity = findViewById(R.id.textTitleActivityDetail);
        this.descriptionActivity = findViewById(R.id.textDescriptionActivity);
        this.state = findViewById(R.id.textActivityState);
        this.locationImage = findViewById(R.id.imageLocationActivityDetail);
        this.stateImage = findViewById(R.id.imageStateActivityDetail);

        switch (this.activity.getState()) {
            case IN_PROGRESS:
                this.stateImage.setImageResource(R.drawable.circle_state_inprocess);
                break;
            case AVAILABLE:
                this.stateImage.setImageResource(R.drawable.circle_state_available);
                break;
            case COMPLETE:
                this.stateImage.setImageResource(R.drawable.circle_state_complete);
                break;
            case INACTIVE:
                this.stateImage.setImageResource(R.drawable.circle_state_inactive);
                break;
        }


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
