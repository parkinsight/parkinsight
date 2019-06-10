package com.example.parkinsight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private Button registerButton;

    private static final String PREFERENCES = "prefrences";
    private static final String auth = "auth_token";

    private TextView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        SharedPreferences pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String authToken = pref.getString(auth, null);

//        if(authToken != null) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        }

        loginButton.setOnClickListener(v -> startSignIn("login"));
        registerButton.setOnClickListener(v -> startSignIn("register"));
    }

    private void startSignIn(String endpoint) {
//        usernameField.setText("email8@gmail.com"); //TODO: delete this
//        passwordField.setText("password"); // TODO: delete this too
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "enter an email and password" + username, Toast.LENGTH_LONG).show();

        } else {
            // lol refactor this pls
            String url = "https://parkinsight.azurewebsites.net/"+endpoint;
            RequestQueue queue = RequestHandler.getInstance(this).getRequestQueue();
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", username);
            params.put("password", password);

            JSONObject parameters = new JSONObject(params);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            SharedPreferences pref = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                            try {
                                String auth_token = response.getString(auth);
                                pref.edit().putString(auth, auth_token).commit();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                Toast.makeText(LoginActivity.this, "hmm.. there was an error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                String message = new JSONObject(responseBody).getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                            } catch (UnsupportedEncodingException e) {
                                Log.e("Auth", "there was an error: " + e.getMessage());
                            } catch (JSONException e) {
                                Log.e("Auth", "there was an error: " + e.getMessage());
                            } catch (Exception e) {
                                Log.e("Auth", "there was some other: " + e.getMessage());
                            }
                        }
                    });

            queue.add(jsonObjectRequest);

        }
    }

}
