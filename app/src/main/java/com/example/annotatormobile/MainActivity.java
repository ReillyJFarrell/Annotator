package com.example.annotatormobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    int TIMERDELAY = 10;
    int FULL_PERMISSION_CODE = 1;
    int WRITE_EXTERNAL_PERMISSION_CODE = 1;
    MediaRecorder recorder;
    MediaPlayer player;
    String recordingFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "AudioRecording.3gp";
    Uri sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        sound = Uri.parse("android.resource://"
                + this.getPackageName() + "/" + R.raw.start);


        //Build Notification Channel
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.speak)
                .setSound(sound)
                .setContentTitle("Annotator")
                .setContentText("Please Speak your Activity")
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


        //Push after Start to demonstrate the timer and recording process (10 second delay)
        Button demoButton = findViewById(R.id.buttonPush);
        demoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        notificationManager.notify(1, builder.build());
                        SystemClock.sleep(2500);

                        recorder.resume();
                        SystemClock.sleep(TIMERDELAY * 1000);
                        recorder.stop();
                        SystemClock.sleep(1000);
                        notificationManager.notify(1, builder.build());
                    }
                };
                //Set a schedule to ping user in 10 seconds, record, then stop recording
                //Can be scaled up to be done over a longer period of time or multiple times
                //Unsure on battery life implications
                timer.schedule(task, TIMERDELAY * 1000);
            }
        });

        //Quick implementation to get permissions

        Button permButton = findViewById(R.id.permreq);
        permButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestRecordPermission();
                } else {
                    Toast.makeText(MainActivity.this, "Recording Active", Toast.LENGTH_SHORT).show();
                    //Continue

                }
            }
        });

        //Start MediaRecorder

        Button startButton = findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    resetRecorder();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    recorder.start();
                    recorder.pause();
                } catch (IllegalStateException stateException) {
                    stateException.printStackTrace();
                }

            }
        });

        //Stop Media recorder, No longer used
        Button stopButton = findViewById(R.id.stop);
        stopButton.setVisibility(View.INVISIBLE);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorder.stop();
            }
        });

        //Playback for testing if audio recording is working

        Button playButton = findViewById(R.id.playback);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(recordingFile);
                try {
                    player = new MediaPlayer();
                    player.setDataSource(recordingFile);
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String name = "PLACEHOLDER NAME";
            String description = "PLACEHOLDER DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    //logic for handling permission request
    private void requestRecordPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, FULL_PERMISSION_CODE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Please enable to allow functionality", Toast.LENGTH_LONG).show();
        }

    }

    //initialize / reset recorder
    private void resetRecorder() throws IOException {


        //https://stackoverflow.com/questions/37338606/mediarecorder-not-saving-audio-to-file
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setAudioEncodingBitRate(16);
        recorder.setAudioSamplingRate(44100);
        recorder.setOutputFile(recordingFile);
        recorder.prepare();
    }


}