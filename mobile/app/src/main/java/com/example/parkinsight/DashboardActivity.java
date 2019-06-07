package com.example.parkinsight;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DashboardActivity extends AppCompatActivity {

    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        DashboardViewModel model = ViewModelProviders.of(this).get(DashboardViewModel.class);
//        model.getScores().observe(this, users -> {
//            // update UI
//        });

        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 2);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 3);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 7);
        Date d41 = calendar.getTime();
        calendar.add(Calendar.DATE, 11);
        Date d42 = calendar.getTime();
        calendar.add(Calendar.DATE, 12);
        Date d43 = calendar.getTime();
        calendar.add(Calendar.DATE, 23);
        Date d5 = calendar.getTime();

        graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("UPDRS Scores");
//        graph.getGridLabelRenderer().setVerticalAxisTitle("UPDRS");


        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4, 2),
                new DataPoint(d41, 4),
                new DataPoint(d42, 33),
                new DataPoint(d43, 2),
                new DataPoint(d5, 39)
        });
        graph.addSeries(lineSeries);
        lineSeries.setDrawDataPoints(true);
        lineSeries.setAnimated(true);
        lineSeries.setDrawBackground(true);


        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, hh:mmaaa");

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(115);


        graph.getViewport().setMinX(d42.getTime());
        graph.getViewport().setMaxX(d5.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setScalable(true);


        graph.getGridLabelRenderer().setHumanRounding(false);
//        graph.setCursorMode(true);

        // TODO: on tap should display point clicked on the graph.
        lineSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(graph.getContext(),  dataPoint.getY() + ", " + dataPoint.getX(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
