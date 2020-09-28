package com.pabloliborra.uaplant;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InitActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        Button initButton = findViewById(R.id.initButton);
        initButton.setBackgroundResource(R.drawable.rounded_button_primary);
        initButton.setTextSize(18);
        initButton.setTypeface(null, Typeface.BOLD);
        initButton.setText(R.string.init_button);
        initButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton();
            }
        });
    }

    private void startButton() {
        Intent intent = new Intent(this, TabBarActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
