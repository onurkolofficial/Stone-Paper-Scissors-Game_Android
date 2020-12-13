package com.onurkolofficial.spsgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.onurkolofficial.spsgame.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create
        super.onCreate(savedInstanceState);
        // Start Main Menu
        Intent mainActivity=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}