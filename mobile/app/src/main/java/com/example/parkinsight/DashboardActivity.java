package com.example.parkinsight;


import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DashboardActivity extends AppCompatActivity {

    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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



        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4, 2),
                new DataPoint(d41, 4),
                new DataPoint(d42, 33),
                new DataPoint(d43, 2),
                new DataPoint(d5, 39)
        });
        graph.addSeries(series);

        PointsGraphSeries<DataPoint> series3 = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3),
                new DataPoint(d4, 2),
                new DataPoint(d41, 4),
                new DataPoint(d42, 33),
                new DataPoint(d43, 2),
                new DataPoint(d5, 39)
        });
        graph.addSeries(series3);
        series3.setSize(10);
        series3.setColor(Color.RED);

        SimpleDateFormat df = new SimpleDateFormat("MMM d, hh:mmaaa");

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, df));
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(115);

//         set manual x bounds to have nice steps
        graph.getViewport().setMinX(d42.getTime());
        graph.getViewport().setMaxX(d5.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setScalable(true);

        // as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        series3.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(graph.getContext(), "Series1: On Data Point clicked: "+dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
