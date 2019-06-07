package com.example.parkinsight;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ScoreListFragment extends Fragment {
    ListView listview;
    DashboardViewModel viewModel;
    Score[] scores;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_score_list, container, false);
        listview = view.findViewById(R.id.scorelistview);
        scores = new Score[0];
       return view;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        viewModel.getScores().observe(this, item -> {
            // TODO : reverse the list so that the dates at the top are most recent
            scores = item.scores;
            Log.e("score fragment: ","why is a graph appearing here. " + item.scores[0].score);
            CustomAdapter customAdapter = new CustomAdapter();
            listview.setAdapter(customAdapter);
        });
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return scores.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.score_item, null);
            TextView dateView = (TextView) convertView.findViewById(R.id.score_date);
            TextView scoreView = (TextView) convertView.findViewById(R.id.score);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d"+ "\n" +"hh:mmaaa");

            String date = dateFormat.format(scores[position].date);
            dateView.setText(date);
            scoreView.setText(scores[position].score);

            return convertView;
        }
    }
}
