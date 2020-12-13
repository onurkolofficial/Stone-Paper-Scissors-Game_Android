package com.onurkolofficial.spsgame.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onurkolofficial.spsgame.R;

import static com.onurkolofficial.spsgame.classes.GameSound.startBackgroundMusic;
import static com.onurkolofficial.spsgame.classes.GameSound.startButtonClickSound;
import static com.onurkolofficial.spsgame.classes.GameSound.stopBackgroundMusic;
import static com.onurkolofficial.spsgame.data.DataPreference.getGameSetting;
import static com.onurkolofficial.spsgame.data.DataPreference.getSettingPreferences;
import static com.onurkolofficial.spsgame.data.DataPreference.setGameSettings;

public class SettingsActivity extends AppCompatActivity {

    LinearLayout settingBgMusic,settingSndEffect,settingVibration;
    ImageView bgMusicCheckbox,sndEffectCheckbox,vibrationCheckbox;
    TextView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get View Elements
        settingBgMusic=findViewById(R.id.settingBgMusic);
        bgMusicCheckbox=findViewById(R.id.settingBgMusic_Checkbox);
        settingSndEffect=findViewById(R.id.settingSoundEffect);
        sndEffectCheckbox=findViewById(R.id.settingSoundEffect_Checkbox);
        settingVibration=findViewById(R.id.settingVibration);
        vibrationCheckbox=findViewById(R.id.settingVibration_Checkbox);
        backButton=findViewById(R.id.backButton);

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

        settingBgMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Check Setting
                String bgmusicSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Music");
                if(bgmusicSetting.equals("on")){
                    setGameSettings(getSettingPreferences(),"Game.Sound.Music","off");
                    // Stopping loop background music.
                    stopBackgroundMusic();
                }
                else if(bgmusicSetting.equals("off")){
                    setGameSettings(getSettingPreferences(),"Game.Sound.Music","on");
                    // Start background music.
                    startBackgroundMusic();
                }
                checkGameSettings();
            }
        });

        settingSndEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Check Setting
                String sndeffectSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Effects");
                if(sndeffectSetting.equals("on")){
                    setGameSettings(getSettingPreferences(),"Game.Sound.Effects","off");
                }
                else if(sndeffectSetting.equals("off")){
                    setGameSettings(getSettingPreferences(),"Game.Sound.Effects","on");
                }
                checkGameSettings();
            }
        });

        settingVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Check Setting
                String vibrationSetting=getGameSetting(getSettingPreferences(),"Game.Vibration");
                if(vibrationSetting.equals("on")){
                    setGameSettings(getSettingPreferences(),"Game.Vibration","off");
                }
                else if(vibrationSetting.equals("off")){
                    setGameSettings(getSettingPreferences(),"Game.Vibration","on");
                }
                checkGameSettings();
            }
        });

        // Check Game Settings for started activity.
        checkGameSettings();
    }

    public void checkGameSettings(){
        // Background Music
        String bgmusicSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Music");
        if(bgmusicSetting.equals("on")){
            bgMusicCheckbox.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.checkbox_check));
        }
        else if(bgmusicSetting.equals("off")){
            bgMusicCheckbox.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.checkbox));
        }
        // Sound Effects
        String sndeffectSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Effects");
        if(sndeffectSetting.equals("on")){
            sndEffectCheckbox.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.checkbox_check));
        }
        else if(sndeffectSetting.equals("off")){
            sndEffectCheckbox.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.checkbox));
        }
        // Vibration
        String vibrationSetting=getGameSetting(getSettingPreferences(),"Game.Vibration");
        if(vibrationSetting.equals("on")){
            vibrationCheckbox.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.checkbox_check));
        }
        else if(vibrationSetting.equals("off")){
            vibrationCheckbox.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.checkbox));
        }
    }
}