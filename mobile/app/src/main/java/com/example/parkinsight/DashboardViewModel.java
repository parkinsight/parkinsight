package com.example.parkinsight;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardViewModel extends AndroidViewModel {
    private MutableLiveData<Scores> scores;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Scores> getScores() {
        if (scores == null) {
            scores = new MutableLiveData<>();
            loadScores();
        }
        return scores;
    }

    private void loadScores() {
        // TODO: replace someuser..
        String url = "http://192.168.0.21:5000/user/someuser/scores1";
        RequestQueue queue = RequestHandler.getInstance(getApplication()).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, (response) -> {
                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        Scores s = gson.fromJson(response.toString(), Scores.class);
                        scores.setValue(s);
                            Log.e("okk: ", "and the length is: " + s.scores.length);

                }, error ->  {
                    Log.e("ERROROROOROROOROOROROOR", "SOMETHING HORRIBLE HAPPENED WHILE LOADING SCORES");

                });

        queue.add(jsonObjectRequest);
    }
}
