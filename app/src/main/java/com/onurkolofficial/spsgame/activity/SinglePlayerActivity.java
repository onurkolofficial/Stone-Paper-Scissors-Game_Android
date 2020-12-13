package com.onurkolofficial.spsgame.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onurkolofficial.spsgame.R;
import com.onurkolofficial.spsgame.classes.SinglePlayerGame;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.onurkolofficial.spsgame.activity.MainActivity.isSignedIn;
import static com.onurkolofficial.spsgame.activity.MainActivity.playerAccount;
import static com.onurkolofficial.spsgame.activity.StoreActivity.playerStoreMoney;
import static com.onurkolofficial.spsgame.classes.GameSound.startButtonClickSound;
import static com.onurkolofficial.spsgame.classes.LoadSaveData.gson;
import static com.onurkolofficial.spsgame.classes.SinglePlayerGame.ITEM_IRON;
import static com.onurkolofficial.spsgame.classes.SinglePlayerGame.ITEM_PAPER;
import static com.onurkolofficial.spsgame.classes.SinglePlayerGame.ITEM_SCISSORS;
import static com.onurkolofficial.spsgame.classes.SinglePlayerGame.ITEM_STONE;
import static com.onurkolofficial.spsgame.classes.SinglePlayerGame.gameStart;
import static com.onurkolofficial.spsgame.classes.SinglePlayerGame.unlockAchievement;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerData;
import static com.onurkolofficial.spsgame.data.GamePlayerData.getPlayerDataPreferences;
import static com.onurkolofficial.spsgame.data.GamePlayerData.setPlayerData;
import static com.onurkolofficial.spsgame.data.GamePlayerData.startPlayerDataSession;

public class SinglePlayerActivity extends AppCompatActivity {

    TextView menuBtn,playerName,playerMoney,playerScore,playerWin,playerDraw,playerLose,shopBtn,stoneBtn,paperBtn,scissorsBtn,ironBtn;

    String temp_win,temp_lose,temp_draw,temp_score,temp_money;

    Intent storeIntent;

    public static List<Integer> buyItemList;
    public static List<Integer> buyItemCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Start Preference Session. (If not sign-in to get preference data.)
        startPlayerDataSession();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        // This Activity
        Activity context=this;
        // Get Store Intent
        storeIntent=new Intent(this,StoreActivity.class);

        // Get Player Data
        // If loaded game, saved game data is get and set preference data. 'classes/LoadSaveData.java'
        // Note! This values is only first open game activity, get and write saved data.
        // Change and save new values in look at 'classes/SinglePlayerGame.java'
        temp_win=getPlayerData(getPlayerDataPreferences(),"Game.Player.Winscore");
        temp_draw=getPlayerData(getPlayerDataPreferences(),"Game.Player.Drawscore");
        temp_lose=getPlayerData(getPlayerDataPreferences(),"Game.Player.Losescore");
        temp_score=getPlayerData(getPlayerDataPreferences(),"Game.Player.Totalscore");
        temp_money=getPlayerData(getPlayerDataPreferences(),"Game.Player.Money");

        // Get View Elements
        menuBtn=findViewById(R.id.menuButton);
        playerName=findViewById(R.id.playerNameText);
        playerMoney=findViewById(R.id.gamePlayerMoney);
        playerScore=findViewById(R.id.gamePlayerScore);
        playerWin=findViewById(R.id.winScoreText);
        playerDraw=findViewById(R.id.drawScoreText);
        playerLose=findViewById(R.id.loseScoreText);
        shopBtn=findViewById(R.id.shopButton);
        stoneBtn=findViewById(R.id.stoneButton);
        paperBtn=findViewById(R.id.paperButton);
        scissorsBtn=findViewById(R.id.scissorButton);
        ironBtn=findViewById(R.id.ironButton);

        // Get Store Items (Convert String to List)
        String getStoreItems=getPlayerData(getPlayerDataPreferences(),"Game.Player.Store.Items");
        String getStoreItemsCount=getPlayerData(getPlayerDataPreferences(),"Game.Player.Store.ItemsCount");
        Type type=new TypeToken<List<Integer>>(){}.getType();
        buyItemList=gson.fromJson(getStoreItems,type);
        buyItemCount=gson.fromJson(getStoreItemsCount,type);
        if(buyItemList==null) buyItemList=new ArrayList<>();
        if(buyItemCount==null) buyItemCount=new ArrayList<>();

        // Button Events
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Close This Activity
                finish();
            }
        });
        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Store Activity
                startActivity(storeIntent);
            }
        });
        // Gameplay Buttons
        stoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,ITEM_STONE);
            }
        });
        paperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,ITEM_PAPER);
            }
        });
        scissorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,ITEM_SCISSORS);
            }
        });
        ironBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sound Effect
                startButtonClickSound();
                // Start Game
                gameStart(context,ITEM_IRON);
                // Check And update new item count.
                if(buyItemList.size()!=0){
                    for(int i=0; i<buyItemList.size(); i++){
                        if(buyItemList.get(i)==ITEM_IRON){
                            // If item end is Gone button.
                            if(buyItemCount.get(i)==1){
                                // Remove array
                                buyItemList.remove(i);
                                buyItemCount.remove(i);
                                ironBtn.setVisibility(View.GONE);
                            }
                            else{
                                int newCount=buyItemCount.get(i);
                                newCount-=1;
                                // Update New Count
                                buyItemCount.set(i,newCount);
                                // Print New Item Count
                                ironBtn.setText(String.valueOf(buyItemCount.get(i)));
                            }
                            break;
                        }
                    }
                    // Save Item Data
                    String saveItemList=gson.toJson(buyItemList);
                    String saveItemListCount=gson.toJson(buyItemCount);
                    setPlayerData(getPlayerDataPreferences(),"Game.Player.Store.Items",saveItemList);
                    setPlayerData(getPlayerDataPreferences(),"Game.Player.Store.ItemsCount",saveItemListCount);
                }
                // Check Sign-in Account
                // If sign-in account to save scores and achievements.
                if(isSignedIn()){
                    unlockAchievement(context.getString(R.string.product_achievement));
                }
            }
        });

        // Check Sign-in and Print Player Name
        if(isSignedIn())
            playerName.setText(playerAccount.getDisplayName());
        else
            playerName.setText(getString(R.string.player_text));

        // Print Player Data
        playerMoney.setText(temp_money);
        playerScore.setText(temp_score);
        playerWin.setText(temp_win);
        playerDraw.setText(temp_draw);
        playerLose.setText(temp_lose);
    }

    @Override
    protected void onResume() {
        // Check exist new items.
        if(buyItemList.size()!=0){
            for(int i=0; i<buyItemList.size(); i++) {
                // If new items to add new query.
                if(buyItemList.get(i)==ITEM_IRON){
                    // Get Iron Button
                    ironBtn.setVisibility(View.VISIBLE);
                    // Print Item Count
                    ironBtn.setText(String.valueOf(buyItemCount.get(i)));
                }
            }
            // Update Money
            temp_money=getPlayerData(getPlayerDataPreferences(),"Game.Player.Money");
            playerMoney.setText(temp_money);

            // Check Sign-in Account
            // If sign-in account to save scores and achievements.
            if(isSignedIn()){
                unlockAchievement(this.getString(R.string.shopping_achievement));
            }
        }
        super.onResume();
    }
}