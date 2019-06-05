package com.example.parkinsight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button loginButton;
    private static final String PREFERENCES = "prefrences";
    private static final String auth = "auth_token";

    private TextView responseView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);

        responseView = (TextView) findViewById(R.id.response);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });
    }

    private void startSignIn() {
//        usernameField.setText("email8@gmail.com"); //TODO: delete this
//        passwordField.setText("password"); // TODO: delete this too
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "enter an email and password" + username, Toast.LENGTH_LONG).show();

        } else {
            // lol refactor this pls
            String url = "http://192.168.0.21:5000/login";
            RequestQueue queue = Volley.newRequestQueue(this);
            Map<String, String>  params = new HashMap<String, String>();
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
//                                String hopefullyAuth = pref.getString(auth, null);
//                                responseView.setText("hopefully auth: " + hopefullyAuth);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }catch(JSONException e) {
                                Toast.makeText(LoginActivity.this, "hmm.. there was an error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            responseView.setText("there was an error");
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                String message = new JSONObject(responseBody).getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                            } catch (UnsupportedEncodingException e) {
                                responseView.setText("there was an error: " + e.getMessage());
                            } catch (JSONException e) {
                                responseView.setText("there was an error: " + e.getMessage());
                            } catch (Exception e) {
                                responseView.setText("there was some other exception: " + e.getMessage());
                            }
                        }
                    });

            queue.add(jsonObjectRequest);

        }
        }

}
