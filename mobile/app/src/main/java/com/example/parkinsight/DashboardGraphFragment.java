package com.example.parkinsight;

import android.content.Context;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_dashboard_graph, container, false);
        graph = (GraphView) v.findViewById(R.id.graph2);

         return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        viewModel.getScores().observe(this, item -> {

            DataPoint[] dataPoints = new DataPoint[item.scores.length];

            for (int x = 0; x < item.scores.length; x ++) {
                Date d = item.scores[x].date;
                // TODO: change the score type to int so you dont have to parse it.
                int s = Integer.parseInt(item.scores[x].score);
                dataPoints[x] = new DataPoint(d, s);
            }
                Log.e("uunhhh data? ", "the score should  be.... " + item.scores[0].score);

            graph.setTitle("UPDRS Scores");

            LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<>(dataPoints);
            graph.addSeries(lineSeries);
            lineSeries.setDrawDataPoints(true);
            lineSeries.setAnimated(true);
            lineSeries.setDrawBackground(true);


            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d"+ "\n" +"hh:mmaaa");

            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext(), dateFormat));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3);

            if(item.scores.length >=3 ) {
                graph.getViewport().setMinX(item.scores[item.scores.length - 3].date.getTime());
                graph.getViewport().setMaxX(item.scores[item.scores.length - 1].date.getTime());
                graph.getViewport().setXAxisBoundsManual(true);
            }

            graph.getViewport().setScalable(true);


            graph.getGridLabelRenderer().setHumanRounding(false);

            // TODO: on tap should display point clicked on the graph.
            lineSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(graph.getContext(),  dataPoint.getY() + ", " + dataPoint.getX(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


}
