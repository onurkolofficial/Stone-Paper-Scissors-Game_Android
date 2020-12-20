package com.onurkolofficial.spsgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onurkolofficial.spsgame.R;
import com.startapp.sdk.adsbase.StartAppAd;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;

public class LoadingActivity extends AppCompatActivity {

    private static String mode;

    public Intent singlePlayerIntent, twoPlayerIntent;

    public TextView loadingPercent;
    public ProgressBar loadingBar;

    Random random;
    public int progress=0;

    Timer timer;
    TimerTask task;
    Thread progressThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // <GET> Game Mode.
        mode=MainActivity.GameMode;

        // Get View Elements
        loadingPercent=findViewById(R.id.progressPercent);
        loadingBar=findViewById(R.id.progressBar);

        // Get Mode Intents
        singlePlayerIntent=new Intent(getActivity(),SinglePlayerActivity.class);
        twoPlayerIntent=new Intent(getActivity(),TwoPlayerActivity.class);

        startProgress();
    }


    public void startProgress(){
        // thread is used to change the progress value
        progressThread=new Thread(new Runnable() {
            @Override
            public void run() {
                // set New timeout number
                random=new Random();
                int timeout=random.nextInt(50); // ms (1000=1s)
                // set the progress
                loadingBar.setProgress(progress);
                loadingPercent.setText("% "+progress);
                // Count progress
                progress+=1;
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(progress>=101) {
                    // Stop Progress
                    stopProgress(progressThread);
                    // Start Game Mode.
                    if(mode.equals("Single-Player")) {
                        StartAppAd.showAd(getActivity());
                        startActivity(singlePlayerIntent);
                    }
                    else if(mode.equals("Two-Player")) {
                        StartAppAd.showAd(getActivity());
                        startActivity(twoPlayerIntent);
                    }
                }
                else {
                    startProgress();
                }
            }
        });
        progressThread.start();
    }

    public void stopProgress(Thread thread){
        thread.interrupt();
    }

    @Override
    public void onBackPressed(){}
}