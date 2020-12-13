package com.onurkolofficial.spsgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.onurkolofficial.spsgame.R;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;

import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;
import static com.onurkolofficial.spsgame.classes.GameSound.startButtonClickSound;

public class GameModeActivity extends AppCompatActivity {

    TextView backButton, singlePlayButton, twoPlayButton;
    Intent loadingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        // Get View Elements
        backButton=findViewById(R.id.backButton);
        singlePlayButton=findViewById(R.id.gameSinglePlayer);
        twoPlayButton=findViewById(R.id.gameTwoPlayer);

        // On Click Events
        singlePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Set Game Mode
                MainActivity.GameMode="Single-Player";
                // Starting Game
                loadingIntent=new Intent(getActivity(), LoadingActivity.class);
                startActivity(loadingIntent);
            }
        });
        twoPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Set Game Mode
                MainActivity.GameMode="Two-Player";
                // Starting Game
                loadingIntent=new Intent(getActivity(), LoadingActivity.class);
                startActivity(loadingIntent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Close Activity
                finish();
            }
        });

        // ADS
        // Check Banner
        final Banner appBanner2=findViewById(R.id.startAppBanner2);
        appBanner2.setBannerListener(new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                appBanner2.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailedToReceiveAd(View view) {
                appBanner2.setVisibility(View.GONE);
            }
            @Override
            public void onImpression(View view) {}
            @Override
            public void onClick(View view) {}
        });
        // Hide Default
        appBanner2.setVisibility(View.GONE);

    }
}