package com.example.parkinsight;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class DashboardViewModel extends AndroidViewModel {
    private MutableLiveData<Scores> scores;
    private static final String PREFERENCES = "prefrences";
    private static final String auth = "auth_token";

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

    private String getAccessToken() {
        SharedPreferences pref = getApplication().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return pref.getString(auth, null);
    }

    private void loadScores() {
        String url = "https://parkinsight.azurewebsites.net/user/scores1";
        RequestQueue queue = RequestHandler.getInstance(getApplication()).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, (response) -> {
                    Gson gson = new GsonBuilder()
                            .setDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
                            .create();

                    Log.e("our response: ", response.toString());
                    Scores s = gson.fromJson(response.toString(), Scores.class);
                    scores.setValue(s);
                    Log.e("okk: ", "and the length is: " + s.scores.length);

                }, error -> {
                    Log.e("ERROROROOROROOROOROROOR", "SOMETHING HORRIBLE HAPPENED WHILE LOADING SCORES");

                })
        {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + getAccessToken());
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }
}
