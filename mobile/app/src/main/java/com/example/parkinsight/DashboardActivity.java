package com.example.parkinsight;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.graph_container, new DashboardGraphFragment())
                .add(R.id.list_container, new ScoreListFragment())
                .commit();
    }
}
