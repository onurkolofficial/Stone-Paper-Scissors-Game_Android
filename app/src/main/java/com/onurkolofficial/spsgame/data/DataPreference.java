package com.onurkolofficial.spsgame.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.onurkolofficial.spsgame.R;

import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;

public class DataPreference {
    private static final String settingsPrefName="Game.Settings.Data";
    private static SharedPreferences settingsPreferences;

    public static String getGameSetting(SharedPreferences preferences, String setting_id){
        return preferences.getString(setting_id,null);
    }
    public static void setGameSettings(SharedPreferences preferences, String setting_id, String setting_value){
        preferences.edit().putString(setting_id,setting_value).apply();
    }

    public static SharedPreferences getSettingPreferences(){
        return getActivity().getSharedPreferences(settingsPrefName, Context.MODE_PRIVATE);
    }

    public static void startPreferenceSession(){
        // Preference
        settingsPreferences=getSettingPreferences();

        // Default Game Setting Values
        String gameSoundEffect="on";
        String gameBackgroundMusic="on";
        String gameVibration="on";

        // If values not exists to save preference data.
        // Sound Effects
        if(getGameSetting(settingsPreferences,"Game.Sound.Effects")==null)
            setGameSettings(settingsPreferences,"Game.Sound.Effects",gameSoundEffect);
        // Background Music
        if(getGameSetting(settingsPreferences,"Game.Sound.Music")==null)
            setGameSettings(settingsPreferences,"Game.Sound.Music",gameBackgroundMusic);
        // Vibrate Mode
        if(getGameSetting(settingsPreferences,"Game.Vibration")==null)
            setGameSettings(settingsPreferences,"Game.Vibration",gameVibration);
    }

}
