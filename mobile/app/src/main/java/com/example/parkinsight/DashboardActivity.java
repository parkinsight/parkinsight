package com.example.parkinsight;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class DashboardActivity extends AppCompatActivity {

    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        startButton = (Button) findViewById(R.id.startVoice);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceActivity();
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.graph_container, new DashboardGraphFragment())
                .add(R.id.list_container, new ScoreListFragment())
                .commit();
    }

    private void startVoiceActivity() {
        Intent intent = new Intent(DashboardActivity.this, VoiceActivity.class);
        startActivity(intent);
    }
}
