package com.example.parkinsight;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DashboardGraphFragment extends Fragment {
    private GraphView graph;
    private DashboardViewModel viewModel;
    private String datePattern = "MMM d" + "\n" + "hh:mmaaa";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_graph, container, false);
        graph = v.findViewById(R.id.graph2);


        return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);

        viewModel.getScores().observe(this, item -> {
            Score[] scores = item.scores;
            DataPoint[] dataPoints = getDataPointsFromScores(scores);

            LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(dataPoints);
            graph.addSeries(lineSeries);
            formatLineGraphSeries(lineSeries);
            formatGridLabels(scores);
            graph.getViewport().setScalable(true);



            // TODO: on tap should display point clicked on the graph.
            lineSeries.setOnDataPointTapListener((Series series, DataPointInterface dataPoint) -> {
                    Toast.makeText(graph.getContext(), dataPoint.getY() + ", " + dataPoint.getX(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void formatLineGraphSeries(LineGraphSeries<DataPoint> lineSeries) {
        lineSeries.setDrawDataPoints(true);
        lineSeries.setAnimated(true);
        lineSeries.setDrawBackground(true);
        //lineSeries.setBackgroundColor(Color.parseColor("#673AB7"));
    }

    private void formatGridLabels(Score[] scores){
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext(), dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.setTitleColor(Color.parseColor("#FFFFFF"));
        graph.setTitleTextSize(60);
        graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        graph.getGridLabelRenderer().setHighlightZeroLines(false);

        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);

        if (scores.length >= 3) {
            graph.getViewport().setMinX(scores[scores.length - 3].date.getTime());
            graph.getViewport().setMaxX(scores[scores.length - 1].date.getTime());
            graph.getViewport().setXAxisBoundsManual(true);
        }
        graph.getGridLabelRenderer().reloadStyles();

    }

    private DataPoint[] getDataPointsFromScores(Score[] scores){
        DataPoint[] dataPoints = new DataPoint[scores.length];
        for (int x = 0; x < scores.length; x++) {
            Date d = scores[x].date;
            int s = scores[x].score;
            dataPoints[x] = new DataPoint(d, s);
        }
        return dataPoints;
    }


}
