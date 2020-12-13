package com.onurkolofficial.spsgame.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;

public class GamePlayerData {
    private static final String pldataPrefName="Game.Player.Data";
    private static SharedPreferences playerDataPreferences;

    // Single Player Data
    private static final String temp_player_total_score="0",
            temp_player_win_score="0",
            temp_player_draw_score="0",
            temp_player_lose_score="0",
            temp_player_money="0";

    public static String getPlayerData(SharedPreferences preferences, String data_id){
        return preferences.getString(data_id,null);
    }
    public static void setPlayerData(SharedPreferences preferences, String data_id, String data_value){
        preferences.edit().putString(data_id,data_value).apply();
    }
    public static SharedPreferences getPlayerDataPreferences(){
        return getActivity().getSharedPreferences(pldataPrefName, Context.MODE_PRIVATE);
    }

    public static void startPlayerDataSession(){
        // Preference
        playerDataPreferences=getPlayerDataPreferences();
        // Single Player Data
        if(getPlayerData(playerDataPreferences,"Game.Player.Totalscore")==null)
            setPlayerData(playerDataPreferences,"Game.Player.Totalscore",temp_player_total_score);
        if(getPlayerData(playerDataPreferences,"Game.Player.Winscore")==null)
            setPlayerData(playerDataPreferences,"Game.Player.Winscore",temp_player_win_score);
        if(getPlayerData(playerDataPreferences,"Game.Player.Drawscore")==null)
            setPlayerData(playerDataPreferences,"Game.Player.Drawscore",temp_player_draw_score);
        if(getPlayerData(playerDataPreferences,"Game.Player.Losescore")==null)
            setPlayerData(playerDataPreferences,"Game.Player.Losescore",temp_player_lose_score);
        if(getPlayerData(playerDataPreferences,"Game.Player.Money")==null)
            setPlayerData(playerDataPreferences,"Game.Player.Money",temp_player_money);
        if(getPlayerData(playerDataPreferences,"Game.Player.Store.Items")==null)
            setPlayerData(playerDataPreferences,"Game.Player.Store.Items","");
        if(getPlayerData(playerDataPreferences,"Game.Player.Store.ItemsCount")==null)
            setPlayerData(playerDataPreferences,"Game.Player.Store.ItemsCount","");
    }

}
