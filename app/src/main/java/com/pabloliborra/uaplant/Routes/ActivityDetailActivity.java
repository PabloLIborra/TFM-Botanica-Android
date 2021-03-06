package com.pabloliborra.uaplant.Routes;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Constants;
import com.pabloliborra.uaplant.Utils.State;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ActivityDetailActivity extends AppCompatActivity {
    private String ACTIVITY_TAG = "Detalle Actividad";

    TextView titleActivity, descriptionActivity, state;
    ImageView locationImage, stateImage;
    Button startButton, questionsButton;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.activity = (Activity) getIntent().getSerializableExtra(Constants.activityExtraTitle);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle("Mapa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.titleActivity = findViewById(R.id.textTitleActivityDetail);
        this.descriptionActivity = findViewById(R.id.textDescriptionActivity);
        this.state = findViewById(R.id.textActivityState);
        this.locationImage = findViewById(R.id.imageLocationActivityDetail);
        this.stateImage = findViewById(R.id.imageStateActivityDetail);
        this.startButton = findViewById(R.id.startActivityButton);
        this.questionsButton = findViewById(R.id.questionsActivityButton);

        this.titleActivity.setText(this.activity.getTitle());
        this.descriptionActivity.setText(this.activity.getInformation());
        this.descriptionActivity.setMovementMethod(new ScrollingMovementMethod());

        File myPath;
        ContextWrapper cw = new ContextWrapper(this);
        String[] nameImageSplit = this.activity.getLocationImage().split("/");
        if(nameImageSplit.length >= 2) {
            String nameImage = nameImageSplit[1];
            nameImageSplit = nameImageSplit[0].split("_");
            File directory = cw.getDir(nameImageSplit[1], Context.MODE_PRIVATE);
            // Create imageDir
            myPath = new File(directory, nameImage);
        } else {
            myPath = new File("", "");
        }
        Picasso.get().load(myPath).placeholder(R.drawable.not_available).into(this.locationImage);

        this.setState();

        this.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });

        this.questionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuestions();
            }
        });

        final ImageButton reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailReport();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.activity = AppDatabase.getDatabaseMain(getApplicationContext()).daoApp().loadActivityById(this.activity.getUid());
        this.setState();
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

    private void startActivity() {
        if(this.activity.getState() == State.AVAILABLE) {
            this.activity.setState(State.IN_PROGRESS);
            this.state.setText("Estado: En Proceso");
            this.stateImage.setImageResource(R.drawable.circle_state_inprocess);
            this.startButton.setEnabled(false);
            this.startButton.setAlpha(0.3f);
            this.questionsButton.setEnabled(true);
            this.questionsButton.setAlpha(1f);

            AppDatabase.getDatabaseMain(this).daoApp().updateActivity(this.activity);
        }
    }

    private void startQuestions() {
        Intent i = new Intent(this, QuestionListActivity.class);
        i.putExtra(Constants.activityExtraTitle, this.activity);
        startActivity(i);
    }

    private void setState() {
        switch (this.activity.getState()) {
            case IN_PROGRESS:
                this.state.setText("Estado: En Proceso");
                this.stateImage.setImageResource(R.drawable.circle_state_inprocess);
                this.startButton.setEnabled(false);
                this.startButton.setAlpha(0.3f);
                this.questionsButton.setEnabled(true);
                this.questionsButton.setAlpha(1f);
                break;
            case AVAILABLE:
                this.state.setText("Estado: Disponible");
                this.stateImage.setImageResource(R.drawable.circle_state_available);
                this.startButton.setEnabled(true);
                this.startButton.setAlpha(1f);
                this.questionsButton.setEnabled(false);
                this.questionsButton.setAlpha(0.3f);
                break;
            case COMPLETE:
                this.state.setText("Estado: Completada");
                this.stateImage.setImageResource(R.drawable.circle_state_complete);
                this.startButton.setEnabled(false);
                this.startButton.setAlpha(0.3f);
                this.questionsButton.setEnabled(true);
                this.questionsButton.setAlpha(1f);
                break;
            case INACTIVE:
                this.state.setText("Estado: Inactiva");
                this.stateImage.setImageResource(R.drawable.circle_state_inactive);
                this.startButton.setEnabled(false);
                this.startButton.setAlpha(0.3f);
                this.questionsButton.setEnabled(false);
                this.questionsButton.setAlpha(0.3f);
                break;
        }
    }
}
