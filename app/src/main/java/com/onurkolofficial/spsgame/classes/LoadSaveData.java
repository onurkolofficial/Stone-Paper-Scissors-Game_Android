package com.onurkolofficial.spsgame.classes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.SnapshotsClient;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.onurkolofficial.spsgame.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static com.onurkolofficial.spsgame.activity.MainActivity.RC_SAVED_GAMES;
import static com.onurkolofficial.spsgame.activity.MainActivity.getActivity;
import static com.onurkolofficial.spsgame.activity.MainActivity.loadingDialog;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerData;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerDataPreferences;
import static com.onurkolofficial.spsgame.data.GamePlayerData.setPlayerData;

public class LoadSaveData {
    private static String mCurrentSaveName = "snapshotTemp";
    private static ProgressDialog loadDataDialog;

    // Save Data List
    public static String pl_total_score="none",
            pl_win_score="none",
            pl_draw_score="none",
            pl_lose_score="none",
            pl_money="none",
            listBuyItems="",
            listBuyItemsCount;

    public static final Gson gson = new Gson();

    public static void showSaveLoadIntent(){
        SnapshotsClient snapshotsClient=Games.getSnapshotsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()));
        int maxNumberOfSavedGamesToShow=5;

        Task<Intent> intentTask = snapshotsClient.getSelectSnapshotIntent(
                getActivity().getString(R.string.save_list_text), true, true, maxNumberOfSavedGamesToShow);

        intentTask.addOnSuccessListener(new OnSuccessListener<Intent>() {
            @Override
            public void onSuccess(Intent intent) {
                if(loadingDialog.isShowing())
                    loadingDialog.dismiss();
                getActivity().startActivityForResult(intent, RC_SAVED_GAMES);
            }
        });
    }

    public static void loadGameData(Intent intent) {
        // Show Loading Dialog
        loadDataDialog=new ProgressDialog(getActivity());
        loadDataDialog.setMessage(getActivity().getString(R.string.loading_data_text));
        loadDataDialog.show();

        // Load a snapshot.
        SnapshotMetadata snapshotMetadata=intent.getParcelableExtra(SnapshotsClient.EXTRA_SNAPSHOT_METADATA);
        mCurrentSaveName=snapshotMetadata.getUniqueName();

        // Get SnapshotClient
        SnapshotsClient snapshotsClient=Games.getSnapshotsClient(getActivity(),GoogleSignIn.getLastSignedInAccount(getActivity()));

        // Load Game Snapshot.
        snapshotsClient.open(mCurrentSaveName,true).addOnCompleteListener(task -> {
            Snapshot outSnapshot;
            if(task.getResult().isConflict()){
                SnapshotsClient.SnapshotConflict conflict=task.getResult().getConflict();
                // Fixed Conflict snapshots.
                // Get Snapshots
                Snapshot snapshot=conflict.getSnapshot();
                Snapshot conflictSnapshot = conflict.getConflictingSnapshot();
                // Set Latest snapshot.
                if(snapshot.getMetadata().getLastModifiedTimestamp() < conflictSnapshot.getMetadata().getLastModifiedTimestamp())
                    outSnapshot=conflictSnapshot;
                else
                    outSnapshot=snapshot;
            }
            else {
                outSnapshot=task.getResult().getData();
            }

            try {
                // Send Load Game Datas to Set Preference Data.
                loadToSavePreference(outSnapshot.getSnapshotContents().readFully());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void loadToSavePreference(byte[] data){
        if(data!=null){
            String datastr=new String(data);
            try {
                JSONObject obj = new JSONObject(datastr);
                // Get SaveGame Data
                pl_total_score=obj.getString("singleplayer_totalscore");
                pl_win_score=obj.getString("singleplayer_winscore");
                pl_draw_score=obj.getString("singleplayer_drawscore");
                pl_lose_score=obj.getString("singleplayer_losescore");
                pl_money=obj.getString("singleplayer_money");
                listBuyItems=obj.getString("singleplayer_store_items");
                listBuyItemsCount=obj.getString("singleplayer_store_items_count");
                // Set Preference
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Totalscore", pl_total_score);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Winscore", pl_win_score);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Drawscore", pl_draw_score);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Losescore", pl_lose_score);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Money", pl_money);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Store.Items",listBuyItems);
                setPlayerData(getPlayerDataPreferences(),"Game.Player.Store.ItemsCount",listBuyItemsCount);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getActivity(), getActivity().getString(R.string.load_game_data_text), Toast.LENGTH_SHORT).show();
        loadDataDialog.dismiss();
    }

    public static void saveGameData() {
        // Create a new snapshot named with a unique string
        String unique = new BigInteger(281, new Random()).toString(13);
        mCurrentSaveName = "snapshotTemp-" + unique;

        // Get Preference Data
        pl_total_score=getPlayerData(getPlayerDataPreferences(),"Game.Player.Totalscore");
        pl_win_score=getPlayerData(getPlayerDataPreferences(),"Game.Player.Winscore");
        pl_draw_score=getPlayerData(getPlayerDataPreferences(),"Game.Player.Drawscore");
        pl_lose_score=getPlayerData(getPlayerDataPreferences(),"Game.Player.Losescore");
        pl_money=getPlayerData(getPlayerDataPreferences(),"Game.Player.Money");
        listBuyItems=getPlayerData(getPlayerDataPreferences(),"Game.Player.Store.Items");
        listBuyItemsCount=getPlayerData(getPlayerDataPreferences(),"Game.Player.Store.ItemsCount");

        // Save Game Data
        JSONObject saveObject = new JSONObject();
        try {
            saveObject.put("singleplayer_totalscore", pl_total_score);
            saveObject.put("singleplayer_winscore", pl_win_score);
            saveObject.put("singleplayer_drawscore", pl_draw_score);
            saveObject.put("singleplayer_losescore", pl_lose_score);
            saveObject.put("singleplayer_money", pl_money);
            saveObject.put("singleplayer_store_items",listBuyItems);
            saveObject.put("singleplayer_store_items_count",listBuyItemsCount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get SnapshotClient
        SnapshotsClient snapshotsClient=Games.getSnapshotsClient(getActivity(),GoogleSignIn.getLastSignedInAccount(getActivity()));

        // Save Game
        snapshotsClient.open(mCurrentSaveName,true).addOnCompleteListener(task -> {
            Snapshot snapshot=task.getResult().getData();

            if (snapshot!=null){
                //call of the method writeSnapshot params : the snapshot and the data we
                //want to save with a description
                writeSnapshot(snapshot,saveObject.toString().getBytes(),getActivity().getResources().getString(R.string.app_name)).addOnCompleteListener(task1 -> {
                    //...
                });
            }
        });

        // Save Game Toast
        Toast.makeText(getActivity(), getActivity().getString(R.string.saved_game_text), Toast.LENGTH_SHORT).show();
    }

    // Save Snapshot
    private static Task<SnapshotMetadata> writeSnapshot(Snapshot snapshot, byte[] data, String desc) {
        // Set the data payload for the snapshot
        snapshot.getSnapshotContents().writeBytes(data);
        // Create the change operation
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder().setDescription(desc).build();
        // Get Client
        SnapshotsClient snapshotsClient=Games.getSnapshotsClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getActivity()));
        // Commit the operation
        return snapshotsClient.commitAndClose(snapshot, metadataChange);
    }

}
