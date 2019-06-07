package com.example.parkinsight;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DashboardViewModel extends ViewModel {
    private MutableLiveData<List<Score>> scores;

    public LiveData<List<Score>> getScores() {
        if (scores == null) {
            scores = new MutableLiveData<List<Score>>();
            loadScores();
        }
        return scores;
    }

    private void loadScores() {
        // Do an asynchronous operation to fetch users.
    }
}
