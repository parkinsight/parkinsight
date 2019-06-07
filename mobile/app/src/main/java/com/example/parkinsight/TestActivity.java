package com.example.parkinsight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private Button tappingButton;
    private Button nextButton;
    private int taps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        taps = 0;
        tappingButton = (Button) findViewById(R.id.tappingbutton);
        nextButton = (Button) findViewById(R.id.next);

        tappingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taps++;
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, DashboardActivity.class);
                intent.putExtra("taps", taps);
                startActivity(intent);
            }
        });
    }
}
