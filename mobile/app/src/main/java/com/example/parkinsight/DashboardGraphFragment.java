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
    TextView textView;
    private GraphView graph;


    private DashboardViewModel viewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_dashboard_graph, container, false);
//         textView = (TextView) v.findViewById(R.id.graphfrag);

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

        graph = (GraphView) v.findViewById(R.id.graph2);
        graph.setTitle("UPDRS Scores");

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

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getContext(), dateFormat));
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(115);


        graph.getViewport().setMinX(d42.getTime());
        graph.getViewport().setMaxX(d5.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setScalable(true);


        graph.getGridLabelRenderer().setHumanRounding(false);

        // TODO: on tap should display point clicked on the graph.
        lineSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(graph.getContext(),  dataPoint.getY() + ", " + dataPoint.getX(), Toast.LENGTH_SHORT).show();
            }
        });
         return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        viewModel.getScores().observe(this, item -> {
                Log.e("uunhhh data? ", "the score should  be.... " + item.scores[0].score);
//                textView.setText("the score is: " + item.scores[0].score);
        });
    }


}
