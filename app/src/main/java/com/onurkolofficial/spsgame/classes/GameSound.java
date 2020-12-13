package com.onurkolofficial.spsgame.classes;

import android.media.MediaPlayer;

import com.onurkolofficial.spsgame.R;

import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;
import static com.onurkolofficial.spsgame.data.DataPreference.getGameSetting;
import static com.onurkolofficial.spsgame.data.DataPreference.getSettingPreferences;

public class GameSound {
    // Media Player List
    static MediaPlayer bgMusicPlayer,btnClickPlayer,winEffectPlayer,drawEffectPlayer,loseEffectPlayer;

    public static void startBackgroundMusic(){
        // Get Media
        bgMusicPlayer=MediaPlayer.create(getActivity(), R.raw.bg_music);
        // Check Settings
        String bgMusicSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Music");
        if(bgMusicSetting.equals("on")){
            bgMusicPlayer.setLooping(true);
            bgMusicPlayer.start();
        }
        else if(bgMusicSetting.equals("off")){
            stopBackgroundMusic();
        }
    }
    public static void stopBackgroundMusic(){
        bgMusicPlayer.stop();
    }

    public static void startButtonClickSound(){
        // Get Media
        btnClickPlayer=MediaPlayer.create(getActivity(), R.raw.button_click);
        // Check Settings
        String soundEffectSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Effects");
        if(soundEffectSetting.equals("on")){
            btnClickPlayer.start();
        }
        else if(soundEffectSetting.equals("off")){
            btnClickPlayer.stop();
        }
    }

    public static void startWinSound(){
        // Get Media
        winEffectPlayer=MediaPlayer.create(getActivity(), R.raw.win);
        // Check Settings
        String soundEffectSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Effects");
        if(soundEffectSetting.equals("on")){
            winEffectPlayer.start();
        }
        else if(soundEffectSetting.equals("off")){
            winEffectPlayer.stop();
        }
    }

    public static void startDrawSound(){
        // Get Media
        drawEffectPlayer=MediaPlayer.create(getActivity(), R.raw.draw);
        // Check Settings
        String soundEffectSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Effects");
        if(soundEffectSetting.equals("on")){
            drawEffectPlayer.start();
        }
        else if(soundEffectSetting.equals("off")){
            drawEffectPlayer.stop();
        }
    }

    public static void startLoseSound(){
        // Get Media
        loseEffectPlayer=MediaPlayer.create(getActivity(), R.raw.lose);
        // Check Settings
        String soundEffectSetting=getGameSetting(getSettingPreferences(),"Game.Sound.Effects");
        if(soundEffectSetting.equals("on")){
            loseEffectPlayer.start();
        }
        else if(soundEffectSetting.equals("off")){
            loseEffectPlayer.stop();
        }
    }

}
