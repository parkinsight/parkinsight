package com.example.parkinsight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button dashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startButton = (Button) findViewById(R.id.startVoice);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceActivity();
            }
        });


        dashboardButton = (Button) findViewById(R.id.dashboard);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDashboardActivity();
            }
        });

    }


    private void startDashboardActivity() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    private void startVoiceActivity() {
        Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
        startActivity(intent);
    }
}
