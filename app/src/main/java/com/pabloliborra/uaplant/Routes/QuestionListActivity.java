package com.pabloliborra.uaplant.Routes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pabloliborra.uaplant.R;
import com.pabloliborra.uaplant.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class QuestionListActivity extends AppCompatActivity {

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Toolbar mTopToolbar = findViewById(R.id.toolbar_top);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle("Preguntas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.activity = (Activity) getIntent().getSerializableExtra(Constants.activityExtraTitle);

        List<QuestionListItem> questions = new ArrayList<>();
        for (Question q : this.activity.getQuestions()) {
            questions.add(new QuestionListItem(q));
        }

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerQuestionList);
        QuestionAdapterList adapter = new QuestionAdapterList(this, questions, this.activity.getPlant());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button sendQuestions = findViewById(R.id.sendQuestionButton);
        sendQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.getAdapter().notifyDataSetChanged();
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
}
