package com.example.parkinsight;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

// TODO: should record and then wait for the user to confirm before saving the recording and sending it to the server
// TODO: add button to proceed to the next page.
public class VoiceActivity extends AppCompatActivity {

    private MediaRecorder recorder;
    private Button recordButton;
    private String outputfile, extName, filePath;
    private TextView textView;
    private File directory = new File(Environment.getExternalStorageDirectory() + "/Test Recordings");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private Handler mHandler = new Handler();
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        textView = (TextView) findViewById(R.id.textview);
        outputfile = sdf.format(new Date());
        extName = ".wav";
        queue = RequestHandler.getInstance(this).getRequestQueue();
        filePath = directory + outputfile + extName;

        recordButton = findViewById(R.id.recordButton);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(recordButton.getText().toString());
                if (recordButton.getText().equals("Record")) {
                    recordButton.setText("RECORDING");
                    directory.mkdirs();
                    if (recorder != null) {
                        recorder.release();
                    }
                    File newFile = new File(filePath); //Storage/Audio Records/Records/Recorded Audio955.wav
                    while (newFile.exists()) {
                        outputfile = sdf.format(new Date());
                        newFile = new File(filePath);
                    }
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    recorder.setOutputFile(filePath);
                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    recorder.start();
                    Toast.makeText(VoiceActivity.this, "Recording...", Toast.LENGTH_SHORT).show();

                    new Timer().schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    mHandler.post(new Runnable() {
                                        public void run() {
                                            recordButton.setText("RECORD");
                                            if (recorder != null) {
                                                recorder.stop();
                                                postVoiceRecording();
                                                Toast.makeText(VoiceActivity.this, "The audio file is saved to " + filePath, Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                                }
                            }

                            , 10000);
                }


            }
        });
    }

    private void postVoiceRecording() {
//      {\ /}
//      (•-•)
//      / ~🍪@
//      Take this biscuit and everything is gonna be okay..
        String url = "https://parkinsight.azurewebsites.net/user/voicerecording";
        SimpleMultiPartRequest smr = new
                SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        smr.addFile("file", filePath);
        queue.add(smr);
    }

}
