package com.onurkolofficial.spsgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onurkolofficial.spsgame.BuildConfig;
import com.onurkolofficial.spsgame.R;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import static com.onurkolofficial.spsgame.classes.GameSound.startButtonClickSound;

public class InfoActivity extends AppCompatActivity {

    public TextView backButton,gameVersion;
    Button webBtn,twitterBtn,instagramBtn,facebookBtn,githubBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Get View Elements
        backButton=findViewById(R.id.backButton);
        webBtn=findViewById(R.id.webButton);
        twitterBtn=findViewById(R.id.twitterButton);
        instagramBtn=findViewById(R.id.instaButton);
        facebookBtn=findViewById(R.id.facebookButton);
        githubBtn=findViewById(R.id.githubButton);
        gameVersion=findViewById(R.id.gameVersion);

        // Get Application Version
        String gameVerInfo=BuildConfig.VERSION_NAME+" ("+BuildConfig.VERSION_CODE+")";

        // On Click Events
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Close Activity
                finish();
            }
        });
        webBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebActivity("https://onurkolofficial.cf");
            }
        });
        twitterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebActivity("https://twitter.com/onurkolofficial");
            }
        });
        instagramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebActivity("https://instagram.com/onurkolofficial");
            }
        });
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebActivity("https://facebook.com/onurkolofficial");
            }
        });
        githubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebActivity("https://github.com/onurkolofficial");
            }
        });

        // Print Game Version
        gameVersion.setText(gameVerInfo);

        // ADS
        // Check Banner
        final Banner appBanner1=findViewById(R.id.startAppBanner1);
        appBanner1.setBannerListener(new BannerListener() {
            @Override
            public void onReceiveAd(View view) {
                appBanner1.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailedToReceiveAd(View view) {
                appBanner1.setVisibility(View.GONE);
            }
            @Override
            public void onImpression(View view) {}
            @Override
            public void onClick(View view) {}
        });
        // Hide Default
        appBanner1.setVisibility(View.GONE);
    }

    public void openWebActivity(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}