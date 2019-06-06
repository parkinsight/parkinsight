package com.example.parkinsight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView view;
    private Button startButton;
    private Button dashboardButton;
    private Button testButton;

    private static final String PREFERENCES = "prefrences";
    private static final String auth = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (TextView) (TextView) findViewById(R.id.message);

        startButton = (Button) findViewById(R.id.startVoice);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceActivity();
            }
        });

        testButton = (Button) findViewById(R.id.startTest);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTestActivity();
            }
        });


        dashboardButton = (Button) findViewById(R.id.dashboard);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDashboardActivity();
            }
        });

        SharedPreferences pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String hopefullyAuth = pref.getString(auth, null);
        view.setText("hopefully auth: " + hopefullyAuth);
    }

    private void startTestActivity(){
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        startActivity(intent);
    }

    private void startDashboardActivity(){
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    private void startVoiceActivity(){
        Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
        startActivity(intent);
    }
}
