package com.onurkolofficial.spsgame.classes;

import android.content.Context;
import android.os.Vibrator;

import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;
import static com.onurkolofficial.spsgame.data.DataPreference.getGameSetting;
import static com.onurkolofficial.spsgame.data.DataPreference.getSettingPreferences;

public class GameVibration {
    // Enable Vibration Permissions in AndroidManifest.xml

    // Get Service
    private static final Vibrator vibration=(Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);

    public static void startGameVibration(){
        // Check Settings
        String vibrationSetting=getGameSetting(getSettingPreferences(),"Game.Vibration");
        if(vibrationSetting.equals("on")) {
            vibration.vibrate(200);
        }
    }
    public static void startGameVibration(int milliseconds){
        // Check Settings
        String vibrationSetting=getGameSetting(getSettingPreferences(),"Game.Vibration");
        if(vibrationSetting.equals("on")) {
            vibration.vibrate(milliseconds);
        }
    }
}
