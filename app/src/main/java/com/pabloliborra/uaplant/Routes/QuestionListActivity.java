package com.pabloliborra.uaplant.Routes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.AppDatabase;
import com.pabloliborra.uaplant.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {
    private String ACTIVITY_TAG = "Test";

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle("Actividad");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.activity = (Activity) getIntent().getSerializableExtra(Constants.activityExtraTitle);

        List<QuestionListItem> questionsList = new ArrayList<>();
        List<Question> questions = this.activity.getQuestions(this);
        for (Question q : questions) {
            questionsList.add(new QuestionListItem(q));
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerQuestionList);
        final QuestionAdapterList adapter = new QuestionAdapterList(this, questionsList, this.activity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button sendQuestions = findViewById(R.id.sendQuestionButton);
        sendQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.checkAnswers();
                adapter.notifyDataSetChanged();
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
