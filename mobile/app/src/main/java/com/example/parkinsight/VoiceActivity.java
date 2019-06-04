package com.example.parkinsight;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.Voice;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VoiceActivity extends AppCompatActivity {

    MediaRecorder recorder;
    Button recordButton;
    String outputfile,extName;
    File directory = new File(Environment.getExternalStorageDirectory()+"/Test Recordings");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String currentDateandTime = sdf.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        outputfile = sdf.format(new Date());
        extName = ".wav";

        recordButton = findViewById(R.id.recordButton);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recordButton.getText() == "RECORD"){
                    recordButton.setText("RECORDING");
                    directory.mkdirs();
                    if(recorder != null){
                        recorder.release();
                    }
                    File newFile = new File(directory+outputfile+extName); //Storage/Audio Records/Records/Recorded Audio955.wav
                    while(newFile.exists()){
                        outputfile = sdf.format(new Date());
                        newFile = new File(directory+outputfile+extName);
                    }
                    recorder = new MediaRecorder();
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    recorder.setOutputFile(directory+outputfile+extName);
                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    recorder.start();
                    Toast.makeText(VoiceActivity.this,"Recording...",Toast.LENGTH_SHORT).show();
                }
                else{
                    recordButton.setText("RECORD");
                    if(recorder != null){
                        recorder.stop();
                        Toast.makeText(VoiceActivity.this,"The audio file is saved to "+directory+outputfile+extName,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
